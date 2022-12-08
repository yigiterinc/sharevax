//Supply button => supply form
import {useState} from 'react';
import Grid from '@material-ui/core/Grid';
import TextField from '@material-ui/core/TextField';
import Button from '@material-ui/core/Button';
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
