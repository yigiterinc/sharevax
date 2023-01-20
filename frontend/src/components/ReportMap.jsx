import {MapContainer, GeoJSON, Marker, Popup} from 'react-leaflet';
import L from 'leaflet';
import {useEffect, useState} from 'react';
import countries from '../data/custom.geo.json';
import 'leaflet/dist/leaflet.css';
import Legend from './Legend';
import ReportPopup from './ReportPopup';
import {fetchCountries, fetchHarbors} from '../services/services';
import {getColor, legendItems} from '../utils/utils';
import '../styles/Map.css';
import {useGlobalState} from '../state';
import straits from '../data/straits.json';
import channels from '../data/channels.json';

const greenIcon = () => {
	return L.icon({
		iconUrl: 'https://raw.githubusercontent.com/pointhi/leaflet-color-markers/master/img/marker-icon-2x-green.png',
		iconSize: [20, 30],
		iconAnchor: [10, 30],
		popupAnchor: [1, -34],
	});
};

const blueIcon = () => {
	return L.icon({
		iconUrl: 'https://raw.githubusercontent.com/pointhi/leaflet-color-markers/master/img/marker-icon-2x-blue.png',
		iconSize: [20, 30],
		iconAnchor: [10, 30],
		popupAnchor: [1, -34],
	});
};

const orangeIcon = () => {
	return L.icon({
		iconUrl: 'https://raw.githubusercontent.com/pointhi/leaflet-color-markers/master/img/marker-icon-2x-orange.png',
		iconSize: [20, 30],
		iconAnchor: [10, 30],
		popupAnchor: [1, -34],
	});
};

const ReportMap = () => {
	const [countriesData, setCountriesData] = useState([]);
	const [harborsData, setHarborsData] = useState([]);
	const [countriesLoading, setCountriesLoading] = useState(true);
	const [harborsLoading, setHarborsLoading] = useState(true);
	const [countryState] = useGlobalState('country');

	useEffect(() => {
		fetchCountryData();
		fetchHarborsData();
	}, []);

	useEffect(() => {
		console.log('countryState', countryState);
	}, [countryState]);

	const fetchCountryData = async () => {
		const result = await fetchCountries();
		setCountriesData(result.data);
		setCountriesLoading(false);
	};

	const fetchHarborsData = async () => {
		const result = await fetchHarbors();
		setHarborsData(result.data);
		setHarborsLoading(false);
	};

	const countryStyle = {
		fillOpacity: 1,
		opacity: 0.6,
	};

	const onEachCountry = (country, layer) => {
		if (countriesData.find((c) => c.name === country.properties.name || c.name === country.properties.adm0_a3)) {
			const currentCountry = countriesData.find(
				(c) => c.name === country.properties.name || c.name === country.properties.adm0_a3,
			);

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
			{!countriesLoading && (
				<MapContainer
					className='overview-map-container w-full h-[80vh] relative z-0'
					style={{backgroundColor: '#ecfaff'}}
					center={[30.0, 0.0]}
					zoom={2}
					maxZoom={5}
					minZoom={1}
					scrollWheelZoom={false}
				>
					<GeoJSON key={countryState} data={countries} style={countryStyle} onEachFeature={onEachCountry} />
					{/* Straits */}
					{straits.map((strait) => {
						return (
							<Marker key={strait.name} position={strait.coordinate} icon={greenIcon()}>
								<Popup>
									<ReportPopup name={strait.name} />
								</Popup>
							</Marker>
						);
					})}
					{/* Channels */}
					{channels.map((channel) => {
						return (
							<Marker key={channel.name} position={channel.coordinate} icon={orangeIcon()}>
								<Popup>
									<ReportPopup name={channel.name} />
								</Popup>
							</Marker>
						);
					})}
					{/* Harbors */}
					{!harborsLoading &&
						harborsData.map((harbor) => {
							return (
								<Marker key={harbor.name} position={[harbor.coordinate[1], harbor.coordinate[0]]} icon={blueIcon()}>
									<Popup>
										<ReportPopup name={harbor.name} />
									</Popup>
								</Marker>
							);
						})}
					<Legend legendItems={legendItems} />
				</MapContainer>
			)}
		</>
	);
};

export default ReportMap;