import {createElement} from 'react';
import {useSnackbar} from 'notistack';
import {finishEvent} from '../services/services';
import {IoClose} from 'react-icons/io5';

const UnblockPopup = ({id, name, type, onUnblock}) => {
	const {enqueueSnackbar, closeSnackbar} = useSnackbar();

	const action = (snackbarId) => (
		<button
			onClick={() => {
				closeSnackbar(snackbarId);
			}}
		>
			{createElement(IoClose, {size: '20'})}
		</button>
	);

	const onClickUnblock = async () => {
		try {
			await finishEvent(id);
			onUnblock();
			enqueueSnackbar('Success!', {
				variant: 'success',
				autoHideDuration: 2500,
				anchorOrigin: {vertical: 'top', horizontal: 'right'},
				action,
			});
		} catch (e) {
			console.log(e);
			enqueueSnackbar('Something went wrong!', {
				variant: 'error',
				autoHideDuration: 2500,
				anchorOrigin: {vertical: 'top', horizontal: 'right'},
				action,
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
