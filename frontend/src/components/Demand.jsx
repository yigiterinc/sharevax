//Request button => request form
import {useState} from 'react';
import {CREATE_DEMAND} from '../services/endpoints';
import {useGlobalState} from '../state/index';

const defaultValues = {
	countryId: '',
	vaccineType: '',
	quantity: '',
	urgency: '',
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

export default function Demand() {
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
		console.log('forValues:', formValues, '\ncountry', country);

		fetch(CREATE_DEMAND, {
			method: 'POST',
			body: JSON.stringify(formValues),
			headers: {
				'Content-Type': 'application/json',
			},
		})
			.then((response) => response.json())
			.then((response) => {
				console.log('Success:', JSON.stringify(response));
				alert('Success! Demand Submitted!');
				document.getElementById('demandForm').reset();
				document.getElementById('quantity').value = '';
				document.getElementById('selectVaccineType').value = document.getElementById('disabledOption').value;
				document.getElementById('selectedDemandUrgency').value = document.getElementById('disabledUrgencyOption').value;
			})
			.catch((error) => {
				console.error('Error:', error);
				alert('Failed! Error submitting Demand!\nDemand form clearing aborted!');
			});
	};

	return (
		<div className='w-[30vw]'>
			<form id='demandForm' onSubmit={handleSubmit}>
				<div className='grid grid-cols-1 my-6 mx-6'>
					<label className='text-main-100 font-bold text-l grid justify-items-center mb-3'>
						Request vaccines for {country}
					</label>

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
						{/* -------------- vaccineType ratio group---------------- */}
						{/* <div name='vaccineType' value={formValues.vaccineType} onChange={handleInputChange}>
							<div className='grid grid-cols-2'>
								<div>{showVaccineType(0)}</div>
								<div>{showVaccineType(1)}</div>
								<div>{showVaccineType(2)}</div>
								<div>{showVaccineType(3)}</div>
								<div>{showVaccineType(4)}</div>
								<div>{showVaccineType(5)}</div>
								<div>{showVaccineType(6)}</div>
								<div>{showVaccineType(7)}</div>
								<div>{showVaccineType(8)}</div>
								<div>{showVaccineType(9)}</div>
								<div>{showVaccineType(10)}</div>
								<div>{showVaccineType(11)}</div>
							</div>
						</div> */}

						<br />
						<select
							id='selectVaccineType'
							name='vaccineType'
							value={formValues.vaccineType}
							onChange={handleInputChange}
							required
							className='bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5 mt-2'
						>
							<option id='disabledOption' value='' disabled selected className='text-gray-100'>
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
							id='quantity'
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
						<label className='text-main-100 text-l font-bold'>Urgency*</label>
						{/* -------------- urgency ratio group---------------- */}
						{/* <div name='urgency' value={formValues.urgency} onChange={handleInputChange} className='grid grid-cols-3'>
							<div>
								<input type='radio' name='urgency' key='NORMAL' value='NORMAL' required />
								<label className='ml-1 mr-4'>Normal</label>
							</div>
							<div>
								<input type='radio' name='urgency' key='URGENT' value='URGENT' required />
								<label className='ml-1 mr-4'>Urgent</label>
							</div>
							<div>
								<input type='radio' name='urgency' key='CRITICAL' value='CRITICAL' required />
								<label className='ml-1 mr-4'>Critical</label>
							</div>
						</div> */}

						<select
							id='selectedDemandUrgency'
							name='urgency'
							value={formValues.urgency}
							onChange={handleInputChange}
							className='bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5 mt-2'
							required
						>
							<option id='disabledUrgencyOption' value='' disabled selected className='text-gray-100'>
								Select Urgency
							</option>
							<option name='urgency' key='NORMAL' value='NORMAL'>
								Normal
							</option>
							<option name='urgency' key='URGENT' value='URGENT'>
								Urgent
							</option>
							<option name='urgency' key='CRITICAL' value='CRITICAL'>
								Critical
							</option>
						</select>
					</div>

					<button
						type='submit'
						value='Submit'
						className='bg-main-100 text-white font-bold mt-6 py-3 px-4 rounded focus:outline-none focus:shadow-outline hover:ease-in hover:scale-105 transition duration-150 ease-out'
					>
						Submit
					</button>
				</div>
			</form>
		</div>
	);
}
