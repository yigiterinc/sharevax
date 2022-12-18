import {useSnackbar} from 'notistack';
import {useEffect, useState} from 'react';
import {fetchSimulationDay} from '../services/services';
import {millisecondsToDate} from '../utils/utils';

function NextDaySnackbar({onNextDay}) {
	const [currentDay, setCurrentDay] = useState();
	const [loading, setLoading] = useState(true);
	const {enqueueSnackbar} = useSnackbar();

	useEffect(() => {
		if (onNextDay) {
			fetchCurrentDay();
			console.log('fetched current day');
		}
	}, [onNextDay]);

	useEffect(() => {
		if (!loading) {
			console.log('if not loading');
			handleOnNextDay();
		}
	}, [loading]);

	const fetchCurrentDay = async () => {
		setLoading(true);
		const result = await fetchSimulationDay();
		setCurrentDay(result.data);
		setLoading(false);
	};

	const handleOnNextDay = () => {
		enqueueSnackbar(`Success! Today is ${millisecondsToDate(currentDay)}`, {
			variant: 'success',
			autoHideDuration: 2500,
			anchorOrigin: {vertical: 'top', horizontal: 'right'},
			style: {marginTop: '60px'},
		});
	};
}

export default NextDaySnackbar;
