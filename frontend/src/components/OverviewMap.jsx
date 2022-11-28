import {MapContainer, Polyline, GeoJSON} from 'react-leaflet';
import countries from '../data/custom.geo.json';
import 'leaflet/dist/leaflet.css';
import Legend from './Legend';

const OverviewMap = () => {
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
		{title: '< 40%', color: colors[1], textColor: 'black'},
		{title: '< 60%', color: colors[2], textColor: 'black'},
		{title: '< 80%', color: colors[3], textColor: 'black'},
		{title: '< 100%', color: colors[4], textColor: 'black'},
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
		const countryName = country.properties.name_en;
		//placeHolders for dropdown info of every country on the map
		var doseInStock = 1000;
		var doseUsedPerDay = 100;
		var infectionRate = 0.3;
		var recoveryRate = 0.4;
		var vaccinationRate = 0.3;
		layer.bindPopup(countryName, doseInStock, doseUsedPerDay, infectionRate, recoveryRate, vaccinationRate);
		layer.options.fillColor = getColor(Math.random(0, 1));
	};

	return (
		<MapContainer
			className='w-[80vw] h-[60vh] relative'
			style={{backgroundColor: '#e8f4f6'}} //let's make it floating to the right?
			center={[30.0, 20.0]}
			zoom={2}
			maxZoom={5}
			minZoom={1}
			scrollWheelZoom={true}
		>
			<GeoJSON data={countries} style={countryStyle} onEachFeature={onEachCountry} />
			<Polyline pathOptions={{color: '#136bf7'}} positions={coordinates} />
			<Legend legendItems={legendItems} />
		</MapContainer>
	);
};

export default OverviewMap;
