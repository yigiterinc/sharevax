import {MapContainer, GeoJSON, Marker, Popup} from 'react-leaflet';
import L from 'leaflet';
import {useEffect, useState} from 'react';
import countries from '../data/custom.geo.json';
import 'leaflet/dist/leaflet.css';
import Legend from './Legend';
import ReportPopup from './ReportPopup';
import {fetchCountries, fetchHarbors, fetchEvents} from '../services/services';
import {getColor, legendItems} from '../utils/utils';
import '../styles/Map.css';
import {useGlobalState} from '../state';
import straits from '../data/straits.json';
import channels from '../data/channels.json';
import UnblockPopup from './UnblockPopup';
import blocked from '../assets/blocked.png';

const blockedIcon = (iconSize) => {
	return L.icon({
		iconUrl: blocked,
		iconSize: [iconSize, iconSize],
	});
};

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
	const [eventsData, setEventsData] = useState([]);
	const [countriesLoading, setCountriesLoading] = useState(true);
	const [harborsLoading, setHarborsLoading] = useState(true);
	const [eventsLoading, setEventsLoading] = useState(true);
	const [countryState] = useGlobalState('country');

	const [blockedStraits, setBlockedStraits] = useState([]);
	const [blockedChannels, setBlockedChannels] = useState([]);
	const [blockedHarbors, setBlockedHarbors] = useState([]);

	useEffect(() => {
		fetchCountryData();
		fetchHarborsData();
		fetchEventsData();
	}, []);

	useEffect(() => {
		console.log('countryState', countryState);
	}, [countryState]);

	useEffect(() => {
		if (!eventsLoading) {
			eventsData.forEach((event) => {
				if (event.type === 'STRAIT' && event.eventStatus === 'ACTIVE') {
					setBlockedStraits((blockedStraits) => [...blockedStraits, event]);
				} else if (event.type === 'CHANNEL' && event.eventStatus === 'ACTIVE') {
					setBlockedChannels((blockedChannels) => [...blockedChannels, event]);
				} else if (event.type === 'HARBOR' && event.eventStatus === 'ACTIVE') {
					setBlockedHarbors((blockedHarbors) => [...blockedHarbors, event]);
				}
			});
		}
	}, [eventsData]);

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

	const fetchEventsData = async () => {
		const result = await fetchEvents();
		setEventsData(result.data);
		setEventsLoading(false);
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
						return blockedStraits.some((bs) => bs.subject === strait.name.toUpperCase().replace(/-/g, '_')) ? (
							<Marker key={strait.name} position={strait.coordinate} icon={blockedIcon(20)}>
								<Popup>
									<UnblockPopup
										id={blockedStraits.find((bs) => bs.subject === strait.name.toUpperCase().replace(/-/g, '_')).id}
										name={strait.name}
										type='Strait'
									/>
								</Popup>
							</Marker>
						) : (
							<Marker key={strait.name} position={strait.coordinate} icon={greenIcon()}>
								<Popup>
									<ReportPopup name={strait.name} type='Strait' />
								</Popup>
							</Marker>
						);
					})}
					{/* Channels */}
					{channels.map((channel) => {
						return blockedChannels.some((bc) => bc.subject === channel.name.toUpperCase()) ? (
							<Marker key={channel.name} position={channel.coordinate} icon={blockedIcon(20)}>
								<Popup>
									<UnblockPopup
										id={blockedChannels.find((bc) => bc.subject === channel.name.toUpperCase()).id}
										name={channel.name}
										type='Channel'
									/>
								</Popup>
							</Marker>
						) : (
							<Marker key={channel.name} position={channel.coordinate} icon={orangeIcon()}>
								<Popup>
									<ReportPopup name={channel.name} type='Channel' />
								</Popup>
							</Marker>
						);
					})}
					{/* Harbors */}
					{!harborsLoading &&
						harborsData.map((harbor) => {
							return blockedHarbors.some((bh) => bh.subject === harbor.name) ? (
								<Marker
									key={harbor.name}
									position={[harbor.coordinate[1], harbor.coordinate[0]]}
									icon={blockedIcon(20)}
								>
									<Popup>
										<UnblockPopup
											id={blockedHarbors.find((bh) => bh.subject === harbor.name).id}
											name={harbor.name}
											type='Harbor'
										/>
									</Popup>
								</Marker>
							) : (
								<Marker key={harbor.name} position={[harbor.coordinate[1], harbor.coordinate[0]]} icon={blueIcon()}>
									<Popup>
										<ReportPopup name={harbor.name} type='Harbor' />
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
