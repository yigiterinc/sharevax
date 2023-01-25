import {useEffect, useState} from 'react';
import fetchSuggestions from '../services/services';
import {useGlobalState} from '../state/index';

// const defaultValues = {
// 	countryId: 0,
// 	suggestionId: 0,
// 	status: '',
// };

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
// 	{
// 		id: 356,
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
// 			expirationDate: 1674584945000,
// 			unitPrice: 0,
// 		},
// 		demand: {
// 			id: 25,
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
// 	{
// 		id: 352,
// 		supplierStatus: 'PENDING',
// 		demanderStatus: 'PENDING',
// 		supply: {
// 			id: 32,
// 			country: {
// 				id: 5,
// 				name: 'Japan',
// 				population: 12500000,
// 				vaccinationRate: 2.0724800000000023,
// 				dailyVaccineConsumption: 367000,
// 				vaccineStock: 371219000,
// 				dailyVaccineProduction: 9000000,
// 			},
// 			vaccineType: 'PFIZER',
// 			quantity: 1000,
// 			expirationDate: 1774584945000,
// 			unitPrice: 0,
// 		},
// 		demand: {
// 			id: 163,
// 			country: {
// 				id: 4,
// 				name: 'China',
// 				population: 7680000,
// 				vaccinationRate: 1.459895833333331,
// 				dailyVaccineConsumption: 100000,
// 				vaccineStock: 95328241120,
// 				dailyVaccineProduction: 2217035840,
// 			},
// 			vaccineType: 'PFIZER',
// 			quantity: 10,
// 			urgency: 'NORMAL',
// 		},
// 		quantity: 10,
// 	},
// ];
// const suggestions = [];

export default function Suggestion({onNextDay, setUpdated}) {
	const [country] = useGlobalState('country');
	const [id] = useGlobalState('id');

	//fetch suggestions
	const [suggestionData, setSuggestionData] = useState([]);

	useEffect(() => {
		if (onNextDay) {
			setSuggestionData([]);
			fetchSuggestionData();
			setUpdated(true);
		}
	}, [onNextDay]);

	useEffect(() => {
		fetchSuggestionData();
	}, []);

	const fetchSuggestionData = async () => {
		const result = await fetchSuggestions();
		setSuggestionData(result.data);
	};
	console.log('Country selected:', country, '\nCountry ID:', id);
	console.log('\nSuggestion data got:\n', suggestionData);

	//formate expiration date
	function formatDate(d) {
		let date = new Date(d);
		return date.toLocaleDateString('en-US');
	}

	//determine role(supplier or demander) by matching global id with id in incoming data
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
			// let i = 0;
			return (
				<div>
					<div className='grid grid-rows-1'>
						<div>
							<b>Expiration Date:</b>
						</div>
						<div>{formatDate(data[i].supply.expirationDate)}</div>
					</div>
				</div>
			);
		} else {
			return <div></div>;
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
			<div>
				<div className='grid justify-items-center items-center'>You have {caseIndex.length} suggestions:</div>
			</div>
		);
		let caseEmpty = <div className='grid justify-items-center items-center'>You have no suggestion.</div>;
		// console.log('Role:', role, '\nCaseLength:', caseLength);
		return caseIndex.length == 0 ? caseEmpty : caseNotEmpty;
	}

	//Styling Suggestion piece
	function suggestionPiece(data, i) {
		let role = '';
		let roleName;

		for (let i = 0; i < data.length; i++) {
			if (data[i].supply.country.id == id) {
				role = 'demander';
				roleName = <div>{data[i].demand.country.name}</div>;
			} else if (data[i].demand.country.id == id) {
				role = 'supplier';
				roleName = <div>{data[i].supply.country.name}</div>;
			}
		}
		let suggestion = (
			<div className='grid grid-cols-2 border-2 border-main-100 rounded-xl p-5 m-5'>
				<div className='grid grid-rows-flex grid-cols-1'>
					<div className='capitalize grid grid-rows-flex'>
						<b>{role}:</b> {roleName}
					</div>
					<div>
						<b>Amount: </b> {data[i].quantity}
					</div>
					<div>{supplyOrDemand(data, i)}</div>
				</div>
				<div className='grid float-right'>
					<div className='grid justify-items-center items-center mb-1'>
						<button
							type='submit'
							value='Submit'
							className='text-green-500 font-bold p-1 rounded-sm hover:bg-green-500 hover:text-white hover:ease-in transition duration-500 ease-out'
						>
							Accept
						</button>
					</div>
					<div className='grid justify-items-center items-center mt-1'>
						<button
							type='submit'
							value='Submit'
							className='text-red-500 font-bold  p-1 rounded-sm hover:bg-red-500 hover:text-white hover:ease-in transition duration-500 ease-out'
						>
							Decline
						</button>
					</div>
				</div>
			</div>
		);
		return suggestion;
	}

	//show all suggestion pieces, ps. the map function below does not work, need a fix to use map
	function showSuggestion(data) {
		return (
			<>
				{(() => {
					const arr = [];
					for (let i = 0; i < caseIndex.length; i++) {
						arr.push(<div>{suggestionPiece(data, caseIndex[i])}</div>);
					}
					return arr;
				})()}
			</>
		);
	}

	return (
		<div>
			<div>{showNotification(suggestionData)}</div>
			<div>{showSuggestion(suggestionData)}</div>
		</div>
	);
}
