import {useEffect, useState} from 'react';
import {fetchSimulationDay} from '../services/services';
import {millisecondsToYYYYMMDD} from '../utils/utils';

const ReportPopup = ({name, type}) => {
	const [currentDay, setCurrentDay] = useState();

	useEffect(() => {
		fetchCurrentDay();
	}, []);

	const fetchCurrentDay = async () => {
		const result = await fetchSimulationDay();
		setCurrentDay(millisecondsToYYYYMMDD(result.data));
	};

	const onClickReport = () => {
		console.log('Reported');
	};

	return (
		<div className='flex flex-col items-center gap-5 mt-8 mb-2'>
			<div className='font-semibold text-base'>
				{name} {type}
			</div>
			<div>
				<div className='font-semibold mb-2'>Start day</div>
				<input className='rounded border h-[40px] px-3 focus:outline-none' type='date' defaultValue={currentDay} />
			</div>
			<button
				type='button'
				className='text-white bg-red-500 hover:bg-red-600 active:ring-4 active:ring-red-300 font-medium rounded-lg text-sm px-4 py-2'
				onClick={onClickReport}
			>
				Block
			</button>
		</div>
	);
};

export default ReportPopup;
