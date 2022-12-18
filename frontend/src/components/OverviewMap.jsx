import {MapContainer, Polyline, GeoJSON} from 'react-leaflet';
import {useEffect, useState} from 'react';
import ReactDOMServer from 'react-dom/server';
import countries from '../data/custom.geo.json';
import 'leaflet/dist/leaflet.css';
import Legend from './Legend';
import CountryPopup from './CountryPopup';
import ShipPopup from './ShipPopup';
import {fetchCountries, fetchActiveDeliveries} from '../services/services';
import {getColor, legendItems, swapLatLng} from '../utils/utils';
import ship from '../assets/ship.png';
import '../styles/Map.css';
import {useGlobalState} from '../state';

const OverviewMap = () => {
	const [countriesData, setCountriesData] = useState([]);
	const [activeDeliveriesData, setActiveDeliveriesData] = useState([]);
	const [countriesLoading, setCountriesLoading] = useState(true);
	const [activeDeliveriesLoading, setActiveDeliveriesLoading] = useState(true);
	const [shipInfo, setShipInfo] = useState([]);
	const [routeHistoryCoordinates, setRouteHistoryCoordinates] = useState([]);
	const [futureRouteCoordinates, setFutureRouteCoordinates] = useState([]);
	const [countryState] = useGlobalState('country');

	useEffect(() => {
		fetchCountryData();
	}, []);

	useEffect(() => {
		if (!activeDeliveriesLoading) {
			activeDeliveriesData.forEach((delivery) => {
				const routeHistory = delivery.routeHistory;
				const futureRoute = delivery.futureRoute;
				const shipLocation = delivery.routeHistory[delivery.routeHistory.length - 1];

				routeHistory.forEach((coordinate) => {
					swapLatLng(coordinate);
				});
				futureRoute.forEach((coordinate) => {
					swapLatLng(coordinate);
				});
				setRouteHistoryCoordinates((prev) => [...prev, routeHistory]);
				setFutureRouteCoordinates((prev) => [...prev, futureRoute]);
				setShipInfo((prev) => [
					...prev,
					{
						startHarbor: delivery.startHarbor.name,
						startCountry: delivery.startHarbor.countryName,
						endHarbor: delivery.destinationHarbor.name,
						endCountry: delivery.destinationHarbor.countryName,
						status: delivery.deliveryStatus,
						coordinates: shipLocation,
					},
				]);
			});
		}
	}, [activeDeliveriesLoading]);

	useEffect(() => {
		if (onNextDay) {
			setShipInfo([]);
			setRouteHistoryCoordinates([]);
			setFutureRouteCoordinates([]);
			setActiveDeliveriesLoading(true);
			fetchActiveDeliveriesData();
			setOnNextDay(false);
		}
	}, [onNextDay]);

	useEffect(() => {
		console.log('countryState', countryState);
	}, [countryState]);

	console.log(activeDeliveriesData);

	const fetchCountryData = async () => {
		const result = await fetchCountries();
		setCountriesData(result.data);
		setLoading(false);
	};

	const coordinates = [
		[40.43, -74.0],
		[40, -70],
		[39, -60],
		[40, -50],
		[47.14, -1.34],
	];

	const countryStyle = {
		fillOpacity: 1,
		opacity: 0.6,
	};

	const onEachCountry = (country, layer) => {
		if (countriesData.find((c) => c.name === country.properties.name || c.name === country.properties.adm0_a3)) {
			const currentCountry = countriesData.find(
				(c) => c.name === country.properties.name || c.name === country.properties.adm0_a3,
			);

			const popupContent = ReactDOMServer.renderToString(
				<Popup
					countryName={currentCountry.name}
					vaccinationRate={currentCountry.vaccinationRate}
					vaccineConsumption={currentCountry.dailyVaccineConsumption}
					vaccineProduction={currentCountry.dailyVaccineProduction}
					vaccineStock={currentCountry.vaccineStock}
				/>,
			);

			layer.bindPopup(popupContent);
			layer.options.fillColor = getColor(currentCountry.vaccinationRate);

			if (currentCountry.name === countryState) {
				layer.setStyle({color: 'black', weight: 3});
			} else {
				layer.setStyle({color: 'gray', weight: 1});
			}
		} else {
			const countryName = country.properties.name;
			layer.bindPopup(countryName);
			layer.options.fillColor = 'white';
			layer.setStyle({color: 'gray', weight: 1});
		}
	};

	return (
		<>
			{!loading && (
				<MapContainer
					className='w-full h-[65vh] relative z-0'
					style={{backgroundColor: '#e8f4f6'}}
					center={[30.0, 0.0]}
					zoom={2}
					maxZoom={5}
					minZoom={1}
					scrollWheelZoom={false}
				>
					<GeoJSON key={countryState} data={countries} style={countryStyle} onEachFeature={onEachCountry} />
					{!activeDeliveriesLoading &&
						routeHistoryCoordinates.map((coordinates, index) => (
							<Polyline key={index} pathOptions={{color: '#6283D5'}} positions={coordinates} />
						))}
					{!activeDeliveriesLoading &&
						futureRouteCoordinates.map((coordinates, index) => (
							<Polyline key={index} pathOptions={{color: '#6283D5', dashArray: '5 8'}} positions={coordinates} />
						))}
					{!activeDeliveriesLoading &&
						shipInfo.map((info, index) => (
							<Marker key={index} position={info.coordinates} icon={getIcon(26)}>
								<Popup>
									<ShipPopup {...info} />
								</Popup>
							</Marker>
						))}
					<Legend legendItems={legendItems} />
				</MapContainer>
			)}
		</>
	);
};

export default OverviewMap;
