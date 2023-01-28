import {useSnackbar} from 'notistack';
import {finishEvent} from '../services/services';

const UnblockPopup = ({id, name, type, onUnblock}) => {
	const {enqueueSnackbar} = useSnackbar();

	const onClickUnblock = async () => {
		try {
			await finishEvent(id);
			onUnblock();
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
			<button
				type='button'
				className='text-white bg-green-500 hover:bg-green-600 active:ring-4 active:ring-green-300 font-medium rounded-lg text-sm px-4 py-2'
				onClick={onClickUnblock}
			>
				Unblock
			</button>
		</div>
	);
};

export default UnblockPopup;
