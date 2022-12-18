import {deliveryStatus} from '../utils/utils';

const ShipPopup = ({coordinates, startHarbor, startCountry, endHarbor, endCountry, status}) => {
	return (
		<>
			<div className='font-semibold'>
				Coordinates:{' '}
				<span className='font-normal'>
					({coordinates[0]}, {coordinates[1]})
				</span>
			</div>
			<div className='font-semibold'>
				Start:{' '}
				<span className='font-normal'>
					{startHarbor}, {startCountry}
				</span>
			</div>
			<div className='font-semibold'>
				Destination:{' '}
				<span className='font-normal'>
					{endHarbor}, {endCountry}
				</span>
			</div>
			<div className='font-semibold'>
				Status: <span className={`font-normal ${deliveryStatus[status].color}`}>{deliveryStatus[status].text}</span>
			</div>
		</>
	);
};

export default ShipPopup;
