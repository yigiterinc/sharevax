//Supply button => supply form
import {useState} from 'react';
import Grid from '@material-ui/core/Grid';
import TextField from '@material-ui/core/TextField';
import Button from '@material-ui/core/Button';
import FormControlLabel from '@material-ui/core/FormControlLabel';
import Radio from '@material-ui/core/Radio';
import FormControl from '@material-ui/core/FormControl';
import FormLabel from '@material-ui/core/FormLabel';
import RadioGroup from '@material-ui/core/RadioGroup';
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
		console.log('forValues:', formValues);
		console.log('country', country);
		console.log(mapCountryId(country));

		fetch(CREATE_SUPPLY, {
			method: 'POST',
			body: JSON.stringify(formValues),
			headers: {
				'Content-Type': 'application/json',
			},
		})
			.then((response) => response.json())
			.then((response) => console.log('Success:', JSON.stringify(response)))
			.catch((error) => console.error('Error:', error));
	};

	return (
		<div>
			<form onSubmit={handleSubmit}>
				<Grid container alignItems='left' justify='center' direction='column' className='my-6'>
					<Grid className='text-main-100 font-bold text-xl'>Supply</Grid>
					<Grid item className='mb-6'>
						<div className='font-bold'>Current Country</div>
						<FormLabel
							id='country-input'
							name='countryId'
							label='Country'
							value={(formValues.countryId = mapCountryId(country))}
							onChange={handleInputChange}
						/>
						<div>{country}</div>
					</Grid>

					<Grid item className='mb-6'>
						<FormControl>
							<FormLabel className='mt-3'>Vaccine Type</FormLabel>
							<RadioGroup
								name='vaccineType'
								value={formValues.vaccineType}
								onChange={handleInputChange}
								column
								className='my-2'
							>
								<Grid item>
									<FormControlLabel key='BIONTECH' value='BIONTECH' control={<Radio size='small' />} label='BIONTECH' />
									<FormControlLabel key='MODERNA' value='MODERNA' control={<Radio size='small' />} label='MODERNA' />
									<FormControlLabel
										key='ASTRAZENECA'
										value='ASTRAZENECA'
										control={<Radio size='small' />}
										label='ASTRAZENECA'
									/>
								</Grid>
								<Grid>
									<FormControlLabel key='PFIZERL' value='PFIZER' control={<Radio size='small' />} label='PFIZER' />
									<FormControlLabel key='JANSSEN' value='JANSSEN' control={<Radio size='small' />} label='JANSSEN' />
									<FormControlLabel
										key='SINOPHARM'
										value='SINOPHARM'
										control={<Radio size='small' />}
										label='SINOPHARM'
									/>
								</Grid>
								<Grid>
									<FormControlLabel key='SINOVAC' value='SINOVAC' control={<Radio size='small' />} label='SINOVAC' />
									<FormControlLabel
										key='SPUTNIK_V'
										value='SPUTNIK_V'
										control={<Radio size='small' />}
										label='SPUTNIK_V'
									/>
									<FormControlLabel key='COVAXIN' value='COVAXIN' control={<Radio size='small' />} label='COVAXIN' />
								</Grid>
								<Grid>
									<FormControlLabel
										key='COVISHIELD'
										value='COVISHIELD'
										control={<Radio size='small' />}
										label='COVISHIELD'
									/>
									<FormControlLabel key='NOVAVAX' value='NOVAVAX' control={<Radio size='small' />} label='NOVAVAX' />
									<FormControlLabel key='CANSINO' value='CANSINO' control={<Radio size='small' />} label='CANSINO' />
								</Grid>
							</RadioGroup>
						</FormControl>
					</Grid>

					<Grid item className='mb-6'>
						<TextField
							id='quantity-input'
							name='quantity'
							label='Quantity'
							type='number'
							value={formValues.quantity}
							onChange={handleInputChange}
						/>
					</Grid>

					<Grid item className='mb-6'>
						<TextField
							id='unitPrice-input'
							name='unitPrice'
							label='Unit Price'
							type='number'
							value={formValues.unitPrice}
							onChange={handleInputChange}
						/>
					</Grid>

					<Button variant='contained' type='submit' className='bg-main-100 text-white'>
						Submit
					</Button>
				</Grid>
			</form>
		</div>
	);
}
