import {useEffect, useState} from 'react';
import {useGlobalState} from '../state/index';
import {fetchSimulationDay, fetchSuggestions, postSuggestion} from '../services/services';
import {millisecondsToDate} from '../utils/utils';
import {useSnackbar} from 'notistack';

export default function Suggestion() {
	const [countryId] = useGlobalState('id');
	const [suggestionData, setSuggestionData] = useState([]);
	const [currentDate, setCurrentDate] = useState([]);

	const {enqueueSnackbar} = useSnackbar();

	useEffect(() => {
		fetchCurrentDate();
		fetchSuggestionData();
	}, []);

	const fetchSuggestionData = async () => {
		const result = await fetchSuggestions(countryId);
		setSuggestionData(result.data);
	};

	const fetchCurrentDate = async () => {
		const result = await fetchSimulationDay();
		setCurrentDate(result.data);
	};

	const handleSubmit = async (event, suggestionId, status) => {
		event.preventDefault();
		try {
			await postSuggestion(suggestionId, countryId, status, new Date(currentDate).toISOString());
			fetchSuggestionData();
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

	//If the current selected country is `demander`, display the `supplier` suggestion, vice versa.
	function supplyOrDemand(data, i) {
		let role = '';
		for (let i = 0; i < data.length; i++) {
			if (data[i].supply.country.id == countryId) {
				role = 'supplier';
			}
		}
		role = role == 'supplier' ? 'supplier' : 'demander';
		if (role == 'demander') {
			return (
				<div className='grid grid-rows-5 ml-[10%] mr-[-15%] gap-2'>
					<div className='grid grid-cols-2'>
						<span className='font-semibold'>Supplier:</span>
						{data[i].supply.country.name}
					</div>
					<div className='grid grid-cols-2'>
						<span className='font-semibold'>Vaccine Type:</span>
						{data[i].supply.vaccineType}
					</div>
					<div className='grid grid-cols-2'>
						<span className='font-semibold'>Unit Price:</span>
						{data[i].supply.unitPrice}
					</div>
					<div className='grid grid-cols-2'>
						<span className='font-semibold'>Quantity:</span>
						{data[i].quantity}
					</div>
					<div className='grid grid-cols-2'>
						<span className='font-semibold'>Expiration Date:</span>
						{millisecondsToDate(data[i].supply.expirationDate)}
					</div>
				</div>
			);
		} else {
			return (
				<div className='grid grid-rows-4 ml-[10%] mr-[-15%] gap-2'>
					<div className='grid grid-cols-2'>
						<span className='font-semibold'>Demander:</span>
						{data[i].demand.country.name}
					</div>
					<div className='grid grid-cols-2'>
						<span className='font-semibold'>Vaccine Type:</span>
						{data[i].demand.vaccineType}
					</div>
					<div className='grid grid-cols-2'>
						<span className='font-semibold'>Quantity:</span>
						{data[i].quantity}
					</div>
					<div className='grid grid-cols-2'>
						<span className='font-semibold'>Urgency:</span>
						{data[i].demand.urgency}
					</div>
				</div>
			);
		}
	}

	//display suggestion number msg
	let caseIndex = []; //store all index of supply/demand
	function showNotification(data) {
		let role = '';

		for (let i = 0; i < data.length; i++) {
			if (data[i].supply.country.id == countryId) {
				role = 'supplier';
			}
		}

		role = role == 'supplier' ? 'supplier' : 'demander';

		if (role == 'demander') {
			for (let i = 0; i < data.length; i++) {
				if (data[i].demand.country.id == countryId) {
					caseIndex.push(i);
				}
			}
		} else if (role == 'supplier') {
			for (let i = 0; i < data.length; i++) {
				if (data[i].supply.country.id == countryId) {
					caseIndex.push(i);
				}
			}
		}

		let caseNotEmpty = (
			<div className='italic'>
				You have got <span className='font-bold'>{suggestionData.length}</span>{' '}
				{suggestionData.length === 1 ? 'suggestion' : 'suggestions'}. Please check below
			</div>
		);

		let caseEmpty = <div className='italic'>You do not have any suggestions yet. Please check back later</div>;

		return caseIndex.length == 0 ? caseEmpty : caseNotEmpty;
	}

	//button options: Pending or not
	function deniedOrApproved(data, i, irole, status) {
		//get roleCountryName
		function roleCountryName(irole) {
			return irole == 'supplier' ? <>{data[i].supply.country.name}</> : <>{data[i].demand.country.name}</>;
		}

		let buttons1 = (
			<div className='grid grid-rows-2 items-center justify-end mr-6'>
				<div className='grid-rows-1 text-center'>
					<button
						id={data[i].id + 'approveButton'}
						type='submit'
						className='text-white bg-green-500 hover:bg-green-600 active:ring-4 active:ring-green-300 font-medium rounded-lg text-sm px-5 py-2.5 mr-2 mb-2'
						name='approvalStatus'
						value={'APPROVED'}
						onClick={(e) => handleSubmit(e, data[i].id, 'APPROVED')}
					>
						Accept
					</button>
				</div>
				<div className='grid-rows-1 text-center'>
					<button
						id={data[i].id + 'denyButton'}
						type='submit'
						className='text-white bg-red-500 hover:bg-red-600 active:ring-4 active:ring-red-300 font-medium rounded-lg text-sm px-5 py-2.5 mr-2 mb-2'
						name='approvalStatus'
						value={'DENIED'}
						onClick={(e) => handleSubmit(e, data[i].id, 'DENIED')}
					>
						Decline
					</button>
				</div>
			</div>
		);

		let buttons2 = (
			<div
				className='grid text-center place-content-center text-orange-500 font-bold  p-1 rounded-sm disabled cursor-not-allowed'
				id={data[i].id + 'pending'}
				value={'PENDING'}
			>
				⏳<br />
				Waiting for approval
				<br />
				from {roleCountryName(irole)}
			</div>
		);

		return status == 'PENDING' ? buttons1 : buttons2;
	}

	//Styling Suggestion piece
	function suggestionPiece(data, i) {
		let role = '';

		for (let i = 0; i < data.length; i++) {
			if (data[i].supply.country.id == countryId) {
				role = 'demander';
			} else if (data[i].demand.country.id == countryId) {
				role = 'supplier';
			}
		}

		function roleStatus() {
			return role == 'demander' ? data[i].supplierStatus : data[i].demanderStatus;
		}

		let suggestion = (
			<div className='grid'>
				<form id={data[i].id}>
					<div className='grid grid-cols-2 bg-[#ecfaff] rounded-lg border drop-shadow-lg border-main-100 p-5 mt-6'>
						<div className='grid grid-rows-flex grid-cols-1'>
							<div>{supplyOrDemand(data, i)}</div>
						</div>
						<div className='grid grid-rows-flex grid-cols-1'>{deniedOrApproved(data, i, role, roleStatus())}</div>
					</div>
				</form>
			</div>
		);
		return suggestion;
	}

	function showSuggestion(data) {
		return (
			<div className='grid'>
				<div className='grid grid-cols-2 gap-4'>
					{caseIndex.map((i) => (
						<div key={i} className='grid'>
							{suggestionPiece(data, i)}
						</div>
					))}
				</div>
			</div>
		);
	}

	return (
		<div>
			<div>{showNotification(suggestionData)}</div>
			<div>{showSuggestion(suggestionData)}</div>
		</div>
	);
}
