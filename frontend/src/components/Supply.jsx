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

const defaultValues = {
	countryId: '',
	vaccineType: '',
	quantity: '',
	unitPrice: '',
};

export default function Supply() {
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
		console.log(formValues);

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
						<TextField
							id='country-input'
							name='countryId'
							label='Country'
							type='text'
							value={formValues.countryId}
							onChange={handleInputChange}
						/>
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
