import {MapContainer, Polyline, GeoJSON} from 'react-leaflet';
import {useEffect, useState} from 'react';
import ReactDOMServer from 'react-dom/server';
import countries from '../data/custom.geo.json';
import 'leaflet/dist/leaflet.css';
import Legend from './Legend';
import Popup from './Popup';
import {fetchCountries} from '../services/services';

const OverviewMap = () => {
	const [countriesData, setCountriesData] = useState([]);
	const [loading, setLoading] = useState(true);

	useEffect(() => {
		fetch();
	}, []);

	const fetch = async () => {
		const result = await fetchCountries();
		setCountriesData(result.data);
		setLoading(false);
	};

	useEffect(() => {
		if (!loading) {
			console.log('countries: ', countriesData);
		}
	}, [countriesData]);

	const coordinates = [
		[40.43, -74.0],
		[40, -70],
		[39, -60],
		[40, -50],
		[47.14, -1.34],
	];

	const colors = ['#fffddd', '#f4dd9f', '#f2b866', '#f38e38', '#f6571d'];
	const scale = [0, 0.2, 0.4, 0.6, 0.8];
	const legendItems = [
		{title: '< 20%', color: colors[0], textColor: 'black'},
		{title: '20%-40%', color: colors[1], textColor: 'black'},
		{title: '40%-60%', color: colors[2], textColor: 'black'},
		{title: '60%-80%', color: colors[3], textColor: 'black'},
		{title: '> 80%', color: colors[4], textColor: 'black'},
	];

	const getColor = (val) => {
		let color = '';
		for (let i = 1; i < scale.length; i++) {
			if (val < scale[i]) {
				color = colors[i - 1];
				return colors[i - 1];
			}
		}
		color = colors[colors.length - 1];
		console.log(color);
		return colors[colors.length - 1];
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
				<Popup
					countryName={currentCountry.name}
					vaccinationRate={currentCountry.vaccinationRate}
					vaccineConsumption={currentCountry.dailyVaccineConsumption}
					vaccineProduction={currentCountry.dailyVaccineProduction}
					vaccineStock={currentCountry.vaccineStock}
				/>,
			);

			layer.bindPopup(popupContent);
			layer.options.fillColor = getColor(Math.random(0, 1));
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
