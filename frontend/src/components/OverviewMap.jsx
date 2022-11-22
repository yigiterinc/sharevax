import {MapContainer, TileLayer, Polyline} from 'react-leaflet';

const OverviewMap = () => {
	const coordinates = [
		[40.43, -74.0],
		[40, -70],
		[39, -60],
		[40, -50],
		[47.14, -1.34],
	];

	return (
		<MapContainer
			className='w-[90vw] h-[90vh]'
			center={[30.0, 20.0]}
			zoom={2}
			maxZoom={5}
			minZoom={1}
			scrollWheelZoom={false}
		>
			<TileLayer url='https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png' />
			<Polyline pathOptions={{color: 'red'}} positions={coordinates} />
		</MapContainer>
	);
};

export default OverviewMap;
