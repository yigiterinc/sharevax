import {useEffect, useState} from 'react';
import {useSnackbar} from 'notistack';
import {fetchSimulationDay, createEvent} from '../services/services';
import {millisecondsToYYYYMMDD, daysBetween} from '../utils/utils';

const ReportPopup = ({name, type, onBlock}) => {
	const [currentDay, setCurrentDay] = useState({todaysDate: '', formattedDate: ''});
	const [selectedDate, setSelectedDate] = useState(new Date());
	const {enqueueSnackbar} = useSnackbar();

	useEffect(() => {
		fetchCurrentDay();
	}, []);

	const fetchCurrentDay = async () => {
		const result = await fetchSimulationDay();
		setCurrentDay({todaysDate: new Date(result.data), formattedDate: millisecondsToYYYYMMDD(result.data)});
		setSelectedDate(new Date(result.data));
	};

	const onClickReport = async () => {
		const formattedName = type === 'Harbor' ? name : name.toUpperCase().replace(/-/g, '_');
		try {
			await createEvent('Blocked' + type, formattedName, daysBetween(currentDay.todaysDate, selectedDate));
			onBlock();
			enqueueSnackbar('Success!', {
				variant: 'success',
				autoHideDuration: 2500,
				anchorOrigin: {vertical: 'top', horizontal: 'right'},
			});
		} catch (e) {
			console.log(e);
			enqueueSnackbar('Something went wrong!', {
				variant: 'error',
				autoHideDuration: 2500,
				anchorOrigin: {vertical: 'top', horizontal: 'right'},
			});
		}
	};

	return (
		<div className='flex flex-col items-center gap-5 mt-8 mb-2'>
			<div className='font-semibold text-base'>
				{name} {type}
			</div>
			<div>
				<div className='font-semibold mb-2'>Start day</div>
				<input
					className='rounded border h-[40px] px-3 focus:outline-none'
					type='date'
					value={selectedDate.toISOString().split('T')[0]}
					onChange={(e) => setSelectedDate(new Date(e.target.value))}
				/>
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
