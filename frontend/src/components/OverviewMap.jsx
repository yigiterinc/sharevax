import {MapContainer, Polyline, GeoJSON} from 'react-leaflet';
import {useEffect, useState} from 'react';
import ReactDOMServer from 'react-dom/server';
import countries from '../data/custom.geo.json';
import 'leaflet/dist/leaflet.css';
import Legend from './Legend';
import Popup from './Popup';
import {fetchCountries} from '../services/services';
import {getColor, legendItems} from '../utils/utils';

const OverviewMap = () => {
	const [countriesData, setCountriesData] = useState([]);
	const [loading, setLoading] = useState(true);

	useEffect(() => {
		fetchCountryData();
	}, []);

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
		} else {
			const countryName = country.properties.name;
			layer.bindPopup(countryName);
			layer.options.fillColor = 'white';
		}
	};

	return (
		<>
			{!loading && (
				<MapContainer
					className='w-[80vw] h-[65vh] relative z-0'
					style={{backgroundColor: '#e8f4f6'}}
					center={[30.0, 20.0]}
					zoom={2}
					maxZoom={5}
					minZoom={1}
					scrollWheelZoom={false}
				>
					<GeoJSON data={countries} style={countryStyle} onEachFeature={onEachCountry} />
					<Polyline pathOptions={{color: '#136bf7'}} positions={coordinates} />
					<Legend legendItems={legendItems} />
				</MapContainer>
			)}
		</>
	);
};

export default OverviewMap;
