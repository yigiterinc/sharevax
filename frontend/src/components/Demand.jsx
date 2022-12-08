//Request button => request form
import {useState} from 'react';
import Grid from '@material-ui/core/Grid';
import TextField from '@material-ui/core/TextField';
import FormControlLabel from '@material-ui/core/FormControlLabel';
import FormControl from '@material-ui/core/FormControl';
import FormLabel from '@material-ui/core/FormLabel';
import RadioGroup from '@material-ui/core/RadioGroup';
import Radio from '@material-ui/core/Radio';
import Button from '@material-ui/core/Button';
const defaultValues = {
	country: '',
	vaccineType: '',
	quantity: '',
	urgency: '',
};

export default function Demand() {
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
						<TextField
							id='vaccineType-input'
							name='vaccineType'
							label='Vaccine Type'
							type='text'
							value={formValues.vaccineType}
							onChange={handleInputChange}
						/>
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
						<FormControl>
							<FormLabel className='mt-3'>Urgency</FormLabel>
							<RadioGroup
								name='urgency'
								value={formValues.urgency}
								onChange={handleInputChange}
								column
								className='my-2'
							>
								<FormControlLabel key='NORMAL' value='normal' control={<Radio size='small' />} label='Normal' />
								<FormControlLabel key='URGENT' value='urgent' control={<Radio size='small' />} label='Urgent' />
								<FormControlLabel key='CRITICAL' value='critical' control={<Radio size='small' />} label='Critical' />
							</RadioGroup>
						</FormControl>
					</Grid>
					<Button variant='contained' color='black' type='submit'>
						Submit
					</Button>
				</Grid>
			</form>
		</div>
	);
}
