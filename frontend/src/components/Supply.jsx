//Supply button => supply form
import {useState} from 'react';
import {CREATE_SUPPLY} from '../services/endpoints';
import {useGlobalState} from '../state/index';

const defaultValues = {
	countryId: '',
	vaccineType: '',
	quantity: '',
	unitPrice: '',
};

const countries = [
	{name: 'China', id: 1, value: 'China'},
	{name: 'Japan', id: 2, value: 'Japan'},
	{name: 'India', id: 3, value: 'India'},
	{name: 'United Kingdom', id: 4, value: 'United Kingdom'},
	{name: 'Portugal', id: 5, value: 'Portugal'},
	{name: 'USA', id: 6, value: 'USA'},
	{name: 'Brazil', id: 7, value: 'Brazil'},
	{name: 'South Africa', id: 8, value: 'South Africa'},
	{name: 'Nigeria', id: 9, value: 'Nigeria'},
];
function mapCountryId(country) {
	for (let i = 0; i < countries.length; i++) {
		if (countries[i].name == country) {
			return countries[i].id;
		}
	}
	return console.log('Error: CountryId not found');
}

const vaccineType = [
	{name: 'vaccineType', key: 'BIONTECH', value: 'BIONTECH'},
	{name: 'vaccineType', key: 'MODERNA', value: 'MODERNA'},
	{name: 'vaccineType', key: 'ASTRAZENECA', value: 'ASTRAZENECA'},
	{name: 'vaccineType', key: 'PFIZER', value: 'PFIZER'},
	{name: 'vaccineType', key: 'JANSSEN', value: 'JANSSEN'},
	{name: 'vaccineType', key: 'SINOPHARM', value: 'SINOPHARM'},
	{name: 'vaccineType', key: 'SINOVAC', value: 'SINOVAC'},
	{name: 'vaccineType', key: 'SPUTNIK_V', value: 'SPUTNIK_V'},
	{name: 'vaccineType', key: 'COVAXIN', value: 'COVAXIN'},
	{name: 'vaccineType', key: 'COVISHIELD', value: 'COVISHIELD'},
	{name: 'vaccineType', key: 'NOVAVAX', value: 'NOVAVAX'},
	{name: 'vaccineType', key: 'CANSINO', value: 'CANSINO'},
];

function showVaccineType(index) {
	return (
		<option name='vaccineType' key={vaccineType[index].key} value={vaccineType[index].value}>
			{vaccineType[index].value}
		</option>
	);
}

export default function Supply() {
	const [country] = useGlobalState('country');

	const [formValues, setFormValues] = useState(defaultValues);
	const handleInputChange = (e) => {
		const {name, value} = e.target;
		setFormValues({
			...formValues,
			[name]: value,
		});
	};

	const handleSubmit = (event) => {
		event.preventDefault();
		formValues.countryId = parseInt(formValues.countryId);
		formValues.quantity = parseInt(formValues.quantity);
		formValues.unitPrice = parseInt(formValues.unitPrice);
		console.log('forValues:', formValues, 'country:', country, 'mapCountryId', mapCountryId(country));

		fetch(CREATE_SUPPLY, {
			method: 'POST',
			body: JSON.stringify(formValues),
			headers: {
				'Content-Type': 'application/json',
			},
		})
			.then((response) => response.json())
			.then((response) => {
				console.log('Success:', JSON.stringify(response));
				alert('Success! Supply Submitted!');
				document.getElementById('supplyForm').reset();
				document.getElementById('supplyQuantity').value = '';
				document.getElementById('unitPrice').value = '';
				document.getElementById('supplySelectVaccineType').value = document.getElementById('disabledOption').value;
			})
			.catch((error) => {
				console.error('Error:', error);
				alert('Failed! Error submitting Supply!\nDemand form clearing aborted!');
			});
	};

	return (
		<div>
			<form
				id='supplyForm'
				onSubmit={handleSubmit}
				className='bg-white shadow-xl hover:shadow-2xl transition duration-150 rounded px-8 pt-3 pb-4 mb-4'
			>
				<div className='grid grid-cols-1 my-6'>
					<label className='text-main-100 font-bold text-xl grid justify-items-center'>Supply</label>

					<div className='mb-6'>
						<label
							id='country-input'
							name='countryId'
							label='Country'
							value={(formValues.countryId = mapCountryId(country))}
							onChange={handleInputChange}
						/>
					</div>

					<div className='mb-6'>
						<label className='text-main-100 text-l font-bold'>Vaccine Type*</label>
						<br />
						<select
							id='supplySelectVaccineType'
							name='vaccineType'
							value={formValues.vaccineType}
							onChange={handleInputChange}
							required
							className='bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5 mt-2'
						>
							<option id='disabledOption' value='' disabled selected className='text-gray-500'>
								Select Vaccine Type
							</option>
							{showVaccineType(0)}
							{showVaccineType(1)}
							{showVaccineType(2)}
							{showVaccineType(3)}
							{showVaccineType(4)}
							{showVaccineType(5)}
							{showVaccineType(6)}
							{showVaccineType(7)}
							{showVaccineType(8)}
							{showVaccineType(9)}
							{showVaccineType(10)}
							{showVaccineType(11)}
						</select>
					</div>

					<div className='mb-6'>
						<label className='text-main-100 text-l font-bold'>Quantity*</label>
						<input
							className='bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5 mt-2'
							id='supplyQuantity'
							name='quantity'
							label='Quantity'
							type='number'
							required
							min='0'
							value={formValues.quantity}
							placeholder='1000'
							onChange={handleInputChange}
						/>
					</div>

					<div className='mb-6'>
						<label className='text-main-100 text-l font-bold'>Unit Price*</label>
						<input
							className='bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5 mt-2'
							id='unitPrice'
							name='unitPrice'
							label='unitPrice'
							type='number'
							required
							min='0'
							value={formValues.unitPrice}
							placeholder='1'
							onChange={handleInputChange}
						/>
					</div>

					<button
						type='submit'
						value='Submit'
						className='bg-main-100 text-white font-bold py-2 px-4 rounded focus:outline-none focus:shadow-outline hover:ease-in hover:scale-105 transition duration-150 ease-out'
					>
						Submit
					</button>
				</div>
			</form>
		</div>
	);
}
