import {MapContainer, Polyline, GeoJSON, Marker, Popup} from 'react-leaflet';
import L from 'leaflet';
import {useEffect, useState} from 'react';
import ReactDOMServer from 'react-dom/server';
import countries from '../data/custom.geo.json';
import 'leaflet/dist/leaflet.css';
import Legend from './Legend';
import CountryPopup from './CountryPopup';
import {fetchCountries, fetchActiveDeliveries} from '../services/services';
import {getColor, legendItems, swapLatLng} from '../utils/utils';
import ship from '../assets/ship.png';
import '../styles/Map.css';

const getIcon = (iconSize) => {
	return L.icon({
		iconUrl: ship,
		iconSize: [iconSize],
	});
};

const OverviewMap = ({onNextDay, setOnNextDay}) => {
	const [countriesData, setCountriesData] = useState([]);
	const [activeDeliveriesData, setActiveDeliveriesData] = useState([]);
	const [countriesLoading, setCountriesLoading] = useState(true);
	const [activeDeliveriesLoading, setActiveDeliveriesLoading] = useState(true);
	const [deliveryCoordinates, setDeliveryCoordinates] = useState([]);
	const [shipCoordinates, setShipCoordinates] = useState([]);

	useEffect(() => {
		fetchCountryData();
		fetchActiveDeliveriesData();
	}, []);

	useEffect(() => {
		if (!activeDeliveriesLoading) {
			activeDeliveriesData.forEach((delivery) => {
				const fullRoute = delivery.routeHistory.concat(delivery.futureRoute);
				fullRoute.forEach((coordinate) => {
					swapLatLng(coordinate);
				});
				setDeliveryCoordinates((prev) => [...prev, fullRoute]);
				const shipLocation = delivery.routeHistory[delivery.routeHistory.length - 1];
				setShipCoordinates((prev) => [...prev, shipLocation]);
			});
		}
	}, [activeDeliveriesLoading]);

	useEffect(() => {
		if (onNextDay) {
			console.log('Next day');
			setActiveDeliveriesLoading(true);
			fetchActiveDeliveriesData();
			setOnNextDay(false);
		}
	}, [onNextDay]);

	const fetchCountryData = async () => {
		const result = await fetchCountries();
		setCountriesData(result.data);
		setCountriesLoading(false);
	};

	const fetchActiveDeliveriesData = async () => {
		const result = await fetchActiveDeliveries();
		setActiveDeliveriesData(result.data);
		setActiveDeliveriesLoading(false);
	};

	const countryStyle = {
		fillOpacity: 1,
		color: 'gray',
		opacity: 0.5,
		weight: 1,
	};

	const onEachCountry = (country, layer) => {
		if (countriesData.find((c) => c.name === country.properties.name || c.name === country.properties.adm0_a3)) {
			const currentCountry = countriesData.find(
				(c) => c.name === country.properties.name || c.name === country.properties.adm0_a3,
			);

			const popupContent = ReactDOMServer.renderToString(
				<CountryPopup
					countryName={currentCountry.name}
					vaccinationRate={currentCountry.vaccinationRate}
					vaccineConsumption={currentCountry.dailyVaccineConsumption}
					vaccineProduction={currentCountry.dailyVaccineProduction}
					vaccineStock={currentCountry.vaccineStock}
				/>,
			);

			layer.bindPopup(popupContent);
			layer.options.fillColor = getColor(currentCountry.vaccinationRate);
		} else {
			const countryName = country.properties.name;
			layer.bindPopup(countryName);
			layer.options.fillColor = 'white';
		}
	};

	return (
		<>
			{!countriesLoading && (
				<MapContainer
					className='overview-map-container w-full h-[65vh] relative z-0'
					style={{backgroundColor: '#e8f4f6'}}
					center={[30.0, 0.0]}
					zoom={2}
					maxZoom={5}
					minZoom={1}
					scrollWheelZoom={false}
				>
					<GeoJSON data={countries} style={countryStyle} onEachFeature={onEachCountry} />
					{!activeDeliveriesLoading &&
						deliveryCoordinates.map((coordinates, index) => (
							<Polyline key={index} pathOptions={{color: '#006686'}} positions={coordinates} />
						))}
					{!activeDeliveriesLoading &&
						shipCoordinates.map((coordinate, index) => (
							<Marker key={index} position={coordinate} icon={getIcon(26)}>
								<Popup>
									Coordinates: ({coordinate[0]}, {coordinate[1]})
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
