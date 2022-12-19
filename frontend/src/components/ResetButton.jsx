import {resetSimulation} from '../services/services';

function ResetButton() {
	const onClickReset = async () => {
		let result = await resetSimulation();

		if (result.status === 200) {
			console.log('Simulation reset');
			window.location.reload();
		} else {
			console.log('Error resetting simulation');
		}
	};

	return (
		<button
			type='button'
			className='text-white bg-red-500 hover:bg-red-600 active:ring-4 active:ring-red-300 font-medium rounded-lg text-sm px-5 py-2.5 mr-2 mb-2'
			onClick={onClickReset}
		>
			Reset
		</button>
	);
}

export default ResetButton;
