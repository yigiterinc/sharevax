import {progressSimulation} from '../services/services';

function NextDayButton({onNextDay}) {
	const onClickNextDay = async () => {
		let result = await progressSimulation();

		if (result.status === 200) {
			console.log('Simulation progressed');
			onNextDay();
		} else {
			console.log('Error progressing simulation');
		}
	};

	return (
		<button
			type='button'
			className='text-white bg-main-100 hover:bg-main-200 active:ring-4 active:ring-main-50 font-medium rounded-lg text-sm px-5 py-2.5 mr-2 mb-2'
			onClick={onClickNextDay}
		>
			Next Day
		</button>
	);
}

export default NextDayButton;
