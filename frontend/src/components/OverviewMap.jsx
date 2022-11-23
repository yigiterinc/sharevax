import {MapContainer, Polyline, GeoJSON} from 'react-leaflet';
import countries from '../data/custom.geo.json';
import 'leaflet/dist/leaflet.css';

const OverviewMap = () => {
	const coordinates = [
		[40.43, -74.0],
		[40, -70],
		[39, -60],
		[40, -50],
		[47.14, -1.34],
	];

	const countryStyle = {
		fillColor: 'white',
		fillOpacity: 0.8,
		color: 'gray',
		opacity: 0.5,
		weight: 1,
	};

	const onEachCountry = (country, layer) => {
		const countryName = country.properties.name_en;
		layer.bindPopup(countryName);
	};

	return (
		<MapContainer
			className='w-[90vw] h-[90vh]'
			style={{backgroundColor: '#b1d2dd'}}
			center={[30.0, 20.0]}
			zoom={2}
			maxZoom={5}
			minZoom={1}
			scrollWheelZoom={false}
		>
			<GeoJSON data={countries} style={countryStyle} onEachFeature={onEachCountry} />
			<Polyline pathOptions={{color: 'red'}} positions={coordinates} />
		</MapContainer>
	);
};

export default OverviewMap;
