import {useEffect, useState} from 'react';
import fetchSuggestions from '../services/services';
import {useGlobalState} from '../state/index';

// const data = [
// 	{id: 'supplier', label: 'USA', amount: 4000, expirationDate: '22.4.2023'},
// 	{id: 'supplier', label: 'China', amount: 3000, expirationDate: '23.7.2023'},
// 	{id: 'supplier', label: 'Germany', amount: 6000, expirationDate: '1.5.2023'},
// ];
const suggestions = [
	{
		id: 360,
		supplierStatus: 'PENDING',
		demanderStatus: 'PENDING',
		supply: {
			id: 32,
			country: {
				id: 2,
				name: 'Japan',
				population: 12500000,
				vaccinationRate: 2.0724800000000023,
				dailyVaccineConsumption: 367000,
				vaccineStock: 371219000,
				dailyVaccineProduction: 9000000,
			},
			vaccineType: 'PFIZER',
			quantity: 1000,
			expirationDate: 1674584945000,
			unitPrice: 0,
		},
		demand: {
			id: 24,
			country: {
				id: 1,
				name: 'China',
				population: 7680000,
				vaccinationRate: 1.459895833333331,
				dailyVaccineConsumption: 100000,
				vaccineStock: 95328241120,
				dailyVaccineProduction: 2217035840,
			},
			vaccineType: 'BIONTECH',
			quantity: 100,
			urgency: 'URGENT',
		},
		quantity: 100,
	},
	{
		id: 356,
		supplierStatus: 'PENDING',
		demanderStatus: 'PENDING',
		supply: {
			id: 32,
			country: {
				id: 2,
				name: 'Japan',
				population: 12500000,
				vaccinationRate: 2.0724800000000023,
				dailyVaccineConsumption: 367000,
				vaccineStock: 371219000,
				dailyVaccineProduction: 9000000,
			},
			vaccineType: 'PFIZER',
			quantity: 1000,
			expirationDate: 1674584945000,
			unitPrice: 0,
		},
		demand: {
			id: 25,
			country: {
				id: 1,
				name: 'China',
				population: 7680000,
				vaccinationRate: 1.459895833333331,
				dailyVaccineConsumption: 100000,
				vaccineStock: 95328241120,
				dailyVaccineProduction: 2217035840,
			},
			vaccineType: 'BIONTECH',
			quantity: 100,
			urgency: 'URGENT',
		},
		quantity: 100,
	},
	{
		id: 352,
		supplierStatus: 'PENDING',
		demanderStatus: 'PENDING',
		supply: {
			id: 32,
			country: {
				id: 2,
				name: 'Japan',
				population: 12500000,
				vaccinationRate: 2.0724800000000023,
				dailyVaccineConsumption: 367000,
				vaccineStock: 371219000,
				dailyVaccineProduction: 9000000,
			},
			vaccineType: 'PFIZER',
			quantity: 1000,
			expirationDate: 1674584945000,
			unitPrice: 0,
		},
		demand: {
			id: 163,
			country: {
				id: 1,
				name: 'China',
				population: 7680000,
				vaccinationRate: 1.459895833333331,
				dailyVaccineConsumption: 100000,
				vaccineStock: 95328241120,
				dailyVaccineProduction: 2217035840,
			},
			vaccineType: 'PFIZER',
			quantity: 10,
			urgency: 'NORMAL',
		},
		quantity: 10,
	},
];
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

	function numberOfSuggestions(data) {
		let l = data.length;
		return l > 0 ? (
			<div className='grid justify-items-center items-center'>You have {l} suggestions:</div>
		) : (
			<div className='grid justify-items-center items-center'>You have no suggestion.</div>
		);
	}

	function formatDate(d) {
		let date = new Date(d);
		return date.toLocaleDateString('en-US');
	}

	function supplyOrDemand(data) {
		let role = '';
		let supplyIndex = 0;

		for (let i = 0; i < data.length; i++) {
			if (data[i].supply.country.id == id) {
				role = 'demander';
			} else if (data[i].demand.country.id == id) {
				role = 'supplier';
				supplyIndex = i;
			}
		}

		if (role == 'supplier') {
			return (
				<div className='grid grid-rows-flex'>
					<div>
						<b>Expiration Date:</b>
					</div>
					<div>{formatDate(data[supplyIndex].supply.expirationDate)}</div>
				</div>
			);
		} else {
			return <div></div>;
		}
	}

	function suggestionPiece(data, i) {
		let role = '';
		let roleName, sExpirationDate, supplyIndex;

		for (let i = 0; i < data.length; i++) {
			if (data[i].supply.country.id == id) {
				role = 'demander';
				roleName = <div>{data[i].demand.country.name}</div>;
			} else if (data[i].demand.country.id == id) {
				role = 'supplier';
				roleName = <div>{data[i].supply.country.name}</div>;
			}
		}
		console.log(sExpirationDate, supplyIndex);
		let suggestion = (
			<div className='grid grid-cols-2 border-2 border-main-100 rounded-xl p-5 m-5'>
				<div className='grid grid-rows-flex grid-cols-1'>
					<div className='capitalize grid grid-rows-flex'>
						<b>{role}:</b> {roleName}
					</div>
					<div>
						<b>Amount: </b> {data[i].quantity}
					</div>
					<div>{supplyOrDemand(data)}</div>
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

	function showSuggestion(data) {
		if (data.length == 1) {
			return <div>{suggestionPiece(data, 0)}</div>;
		} else if (data.length == 2) {
			return (
				<div>
					<div>{suggestionPiece(data, 0)}</div>
					<div>{suggestionPiece(data, 1)}</div>
				</div>
			);
		} else if (data.length == 3) {
			return (
				<div>
					<div>{suggestionPiece(data, 0)}</div>
					<div>{suggestionPiece(data, 1)}</div>
					<div>{suggestionPiece(data, 2)}</div>
				</div>
			);
		}
	}

	return (
		<div>
			<div>{numberOfSuggestions(suggestions)}</div>
			<div>{showSuggestion(suggestions)}</div>
		</div>
	);
}
