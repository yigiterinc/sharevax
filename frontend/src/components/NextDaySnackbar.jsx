import {useSnackbar} from 'notistack';
import {useEffect} from 'react';

function NextDaySnackbar({onNextDay}) {
	const {enqueueSnackbar} = useSnackbar();

	useEffect(() => {
		if (onNextDay) {
			handleOnNextDay();
		}
	}, [onNextDay]);

	const handleOnNextDay = () => {
		enqueueSnackbar('Success! It is the next day', {
			variant: 'success',
			autoHideDuration: 3000,
			anchorOrigin: {vertical: 'top', horizontal: 'right'},
			style: {marginTop: '60px'},
		});
	};
}

export default NextDaySnackbar;
