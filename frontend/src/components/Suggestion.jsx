import {useEffect, useState} from 'react';
import fetchSuggestions from '../services/services';
import {useGlobalState} from '../state/index';
import {CREATE_SUGGESTION} from '../services/endpoints';
import {fetchSimulationDay} from '../services/services';

//mock suggestion data for pre-testing
// const suggestions = [
// 	{
// 		id: 360,
// 		supplierStatus: 'PENDING',
// 		demanderStatus: 'PENDING',
// 		supply: {
// 			id: 32,
// 			country: {
// 				id: 2,
// 				name: 'Japan',
// 				population: 12500000,
// 				vaccinationRate: 2.0724800000000023,
// 				dailyVaccineConsumption: 367000,
// 				vaccineStock: 371219000,
// 				dailyVaccineProduction: 9000000,
// 			},
// 			vaccineType: 'PFIZER',
// 			quantity: 1000,
// 			expirationDate: 1675585945000,
// 			unitPrice: 0,
// 		},
// 		demand: {
// 			id: 24,
// 			country: {
// 				id: 1,
// 				name: 'China',
// 				population: 7680000,
// 				vaccinationRate: 1.459895833333331,
// 				dailyVaccineConsumption: 100000,
// 				vaccineStock: 95328241120,
// 				dailyVaccineProduction: 2217035840,
// 			},
// 			vaccineType: 'BIONTECH',
// 			quantity: 100,
// 			urgency: 'URGENT',
// 		},
// 		quantity: 100,
// 	},
// ];
// const suggestions = [];

export default function Suggestion({onNextDay, setUpdated}) {
	const [country] = useGlobalState('country');
	const [id] = useGlobalState('id');

	//handle submit
	const defaultValues = {
		suggestionId: 0,
		countryId: id,
		approvalStatus: '',
		currentDate: '2023-01-26T19:06:36.364Z',
	};
	const [formValues] = useState(defaultValues);
	const handleSubmit = (event, sID, status) => {
		event.preventDefault();
		console.log('sID:', sID);
		console.log('FV', formValues);
		formValues.suggestionId = sID;
		formValues.approvalStatus = status;
		formValues.currentDate = new Date(currentDate).toISOString();

		fetch(CREATE_SUGGESTION, {
			method: 'POST',
			body: JSON.stringify(formValues),
			headers: {
				'Content-Type': 'application/json',
			},
		})
			.then((response) => response.json())
			.then((response) => {
				console.log('Suggestion submitted:', formValues);
				console.log('Success:', JSON.stringify(response));
			})
			.catch((error) => {
				console.error('Error:', error);
			});
		//fetch suggestion data on submit
		fetchSuggestionData();
		showStatus(status, sID);
	};

	//fetch suggestions
	const [suggestionData, setSuggestionData] = useState([]);
	const [suggestionLength, setSuggestionLength] = useState([]);

	useEffect(() => {
		if (onNextDay) {
			setSuggestionLength([]);
			setSuggestionData([]);
			fetchSuggestionData();
			setUpdated(true);
		}
	}, [onNextDay]);

	useEffect(() => {
		fetchSuggestionData();
	}, []);

	const fetchSuggestionData = async () => {
		const result = await fetchSuggestions(id);
		setSuggestionData(result.data);
		setSuggestionLength(result.data.length);
		console.log('Country selected:', country, '\nCountry ID:', id);
		console.log('\nSuggestion data got:\n', suggestionData);
	};

	//display status of suggestion piece
	function showStatus(status, sID) {
		//refetch suggestion data, display the length of the new suggestion data in {suggestionLength} of function `showNotification(data)`
		fetchSuggestionData();
		// document.getElementById('msg').value = showNotification(suggestionData);
		if (status == 'DENIED') {
			document.getElementById(sID).remove();
		} else if (status == 'APPROVED') {
			// document.getElementById('msg').contentWindow.location.reload(true);
			// document.getElementById(sID + 'approveButton').hidden = true;
			// document.getElementById(sID + 'denyButton').hidden = true;
			// document.getElementById(sID + 'pending').classList.remove('hidden');
			console.log('af');
			// document.getElementById(sID + 'approveButton').setAttribute('disabled', 'disabled');
			// document.getElementById(sID + 'denyButton').setAttribute('disabled', 'disabled');
		}
	}

	//formate expiration date
	function formatDate(d) {
		let date = new Date(d);
		return date.toLocaleDateString('en-US');
	}

	//fetch current date
	const [currentDate, setCurrentDate] = useState([]);

	useEffect(() => {
		if (onNextDay) {
			setCurrentDate([]);
			fetchCurrentDateData([]);
			setUpdated(true);
		}
	}, [onNextDay]);

	useEffect(() => {
		fetchCurrentDateData();
	}, []);

	const fetchCurrentDateData = async () => {
		const result = await fetchSimulationDay();
		setCurrentDate(result.data);
	};
	// console.log('DDDD', currentDate);

	//If the current selected country is `demander`, display the `supplier` suggestion, vice versa.
	function supplyOrDemand(data, i) {
		let role = '';
		for (let i = 0; i < data.length; i++) {
			if (data[i].supply.country.id == id) {
				role = 'supplier';
			}
		}
		role = role == 'supplier' ? 'supplier' : 'demander';
		// console.log('role in supplyOrDemand:', role);
		if (role == 'demander') {
			return (
				<div className='grid grid-rows-5 ml-[10%] mr-[-15%]'>
					<div className='grid grid-cols-2'>
						<div>
							<b>Supplier:</b>
						</div>
						<div>{data[i].supply.country.name}</div>
					</div>
					<div className='grid grid-cols-2'>
						<b>Vaccine Type:</b> {data[i].supply.vaccineType}
					</div>
					<div className='grid grid-cols-2'>
						<b>Unit Price:</b> {data[i].supply.unitPrice}
					</div>
					<div className='grid grid-cols-2'>
						<b>Quantity:</b> {data[i].quantity}
					</div>
					<div className='grid grid-cols-2'>
						<b>Expiration Date:</b> {formatDate(data[i].supply.expirationDate)}
					</div>
				</div>
			);
		} else {
			return (
				<div className='grid grid-rows-4 ml-[4%] mr-[-15%]'>
					<div className='grid grid-cols-2'>
						<b>Demander:</b> {data[i].demand.country.name}
					</div>
					<div className='grid grid-cols-2'>
						<b>Vaccine Requested:</b> {data[i].demand.vaccineType}
					</div>
					<div className='grid grid-cols-2'>
						<b>Quantity Requested:</b> {data[i].quantity}
					</div>
					<div className='grid grid-cols-2'>
						<b>Urgency:</b> {data[i].demand.urgency}
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
			if (data[i].supply.country.id == id) {
				role = 'supplier';
			}
		}

		role = role == 'supplier' ? 'supplier' : 'demander';

		if (role == 'demander') {
			for (let i = 0; i < data.length; i++) {
				if (data[i].demand.country.id == id) {
					caseIndex.push(i);
				}
			}
		} else if (role == 'supplier') {
			for (let i = 0; i < data.length; i++) {
				if (data[i].supply.country.id == id) {
					caseIndex.push(i);
				}
			}
		}

		let caseNotEmpty = (
			<div className='text-center py-4 lg:px-4'>
				<div className='p-2 bg-main-50/50 items-center leading-none lg:rounded-full flex lg:inline-flex'>
					<span className='flex rounded-full bg-main-100/75 uppercase px-2 py-1 text-xs font-bold mr-3'>
						New Suggestion!
					</span>
					<span className='font-semibold mr-2 text-left flex-auto'>
						Check it out! <b>{suggestionLength}</b> suggestions!
					</span>
					<svg className='fill-current opacity-75 h-4 w-4' xmlns='http://www.w3.org/2000/svg' viewBox='0 0 20 20'>
						<path d='M12.95 10.707l.707-.707L8 4.343 6.586 5.757 10.828 10l-4.242 4.243L8 15.657l4.95-4.95z' />
					</svg>
				</div>
			</div>
		);

		let caseEmpty = (
			<div className='grid grid-rows-1 ml-[210%] text-center py-4 lg:px-4'>
				<div className='grid p-2  bg-main-100/50 items-center leading-none lg:rounded-full flex lg:inline-flex'>
					<span className='grid rounded-full bg-yellow-100 uppercase px-2 py-1 text-xs font-bold mr-3'>
						No Suggestion Yet
					</span>
					<span className='grid font-semibold mr-2 text-left flex-auto'>Chill~</span>
					<svg className='fill-current opacity-75 h-4 w-4' xmlns='http://www.w3.org/2000/svg' viewBox='0 0 20 20'></svg>
				</div>
			</div>
		);

		return caseIndex.length == 0 ? caseEmpty : caseNotEmpty;
	}

	//button options: Pending or not
	function deniedOrApproved(data, i, irole, status) {
		//get roleCountryName
		function roleCountryName(irole) {
			return irole == 'supplier' ? <>{data[i].supply.country.name}</> : <>{data[i].demand.country.name}</>;
		}

		let buttons1 = (
			<div className='grid float-right'>
				<div className='grid text-center place-content-center p-2'>
					<button
						id={data[i].id + 'approveButton'}
						type='submit'
						className='text-green-500 font-bold p-1 rounded-sm hover:bg-green-500 hover:text-white hover:ease-in transition duration-500 ease-out'
						name='approvalStatus'
						value={'APPROVED'}
						onClick={(e) => handleSubmit(e, data[i].id, 'APPROVED')}
					>
						✅ Accept
					</button>
				</div>
				<div className='grid text-center place-content-center p-2'>
					<button
						id={data[i].id + 'denyButton'}
						type='submit'
						className='text-red-500 font-bold  p-1 rounded-sm hover:bg-red-500 hover:text-white hover:ease-in transition duration-500 ease-out'
						name='approvalStatus'
						value={'DENIED'}
						onClick={(e) => handleSubmit(e, data[i].id, 'DENIED')}
					>
						❌ Decline
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
				⏳⏳⏳ <br />
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
			if (data[i].supply.country.id == id) {
				role = 'demander';
			} else if (data[i].demand.country.id == id) {
				role = 'supplier';
			}
		}

		function roleStatus() {
			return role == 'demander' ? data[i].supplierStatus : data[i].demanderStatus;
		}

		// let a = roleStatus();
		let suggestion = (
			<div className='grid'>
				<form id={data[i].id}>
					<div className='grid grid-cols-2 border-0 border-main-100 bg-main-50/50 rounded-xl p-5 m-5'>
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

	//show all suggestion pieces, ps. the map function below does not work, need a fix to use map
	function showSuggestion(data) {
		return (
			<div className='grid'>
				<div className='grid grid-cols-2'>
					{(() => {
						const arr = [];
						for (let i = 0; i < caseIndex.length; i++) {
							arr.push(<div className='grid'>{suggestionPiece(data, caseIndex[i])}</div>);
						}
						return arr;
					})()}
				</div>
			</div>
		);
	}

	return (
		<div>
			<div id='msg'>{showNotification(suggestionData)}</div>
			<div>{showSuggestion(suggestionData)}</div>
		</div>
	);
}
