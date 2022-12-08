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
const defaultValues = {
	country: '',
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
		console.log(formValues);
	};
	return (
		<div>
			<form onSubmit={handleSubmit}>
				<Grid container alignItems='left' justify='center' direction='column' className='my-3'>
					<Grid item>
						<TextField
							id='country-input'
							name='country'
							label='Country'
							type='text'
							value={formValues.country}
							onChange={handleInputChange}
						/>
					</Grid>

					<Grid item>
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

					<Grid item>
						<TextField
							id='quantity-input'
							name='quantity'
							label='Quantity'
							type='number'
							value={formValues.quantity}
							onChange={handleInputChange}
						/>
					</Grid>

					<Grid item>
						<TextField
							id='unitPrice-input'
							name='unitPrice'
							label='Unit Price'
							type='number'
							value={formValues.unitPrice}
							onChange={handleInputChange}
						/>
					</Grid>

					<Button variant='contained' color='black' type='submit'>
						Submit
					</Button>
				</Grid>
			</form>
		</div>
	);
}
