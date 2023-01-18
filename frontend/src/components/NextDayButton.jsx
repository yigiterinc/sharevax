import {progressSimulation} from '../services/services';
import LoadingSpinner from './LoadingSpinner';
import {useState} from 'react';

function NextDayButton({onNextDay}) {
	const [nextDayLoading, setNextDayLoading] = useState(false);

	const onClickNextDay = async () => {
		setNextDayLoading(true);
		let result = await progressSimulation();

		if (result.status === 200) {
			console.log('Simulation progressed');
			onNextDay();
			setNextDayLoading(false);
		} else {
			console.log('Error progressing simulation');
		}
	};

	return (
		<>
			{nextDayLoading ? (
				<div className='mr-12 mt-2 mb-4'>
					<LoadingSpinner />
				</div>
			) : (
				<button
					type='button'
					className='text-white bg-main-100 hover:bg-main-200 active:ring-4 active:ring-main-50 font-medium rounded-lg text-sm px-5 py-2.5 mr-2 mb-2'
					onClick={onClickNextDay}
				>
					Next Day
				</button>
			)}
		</>
	);
}

export default NextDayButton;
