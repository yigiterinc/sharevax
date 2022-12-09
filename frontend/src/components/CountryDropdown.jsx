import {setGlobalState, useGlobalState} from '../state';

function CountryDropdown() {
	const [country] = useGlobalState('country');
	const handleCountryChange = (e) => {
		setGlobalState('country', e.target.value);
	};

	return (
		<div>
			<select
				id='countries'
				value={country}
				onChange={handleCountryChange}
				className='bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5'
			>
				<option value='' selected>
					Choose a country
				</option>
				<option value='China'>China</option>
				<option value='Japan'>Japan</option>
				<option value='India'>India</option>
				<option value='United Kingdom'>United Kingdom</option>
				<option value='Portugal'>Portugal</option>
				<option value='USA'>USA</option>
				<option value='Brazil'>Brazil</option>
				<option value='South Africa'>South Africa</option>
				<option value='Nigeria'>Nigeria</option>
			</select>
		</div>
	);
}

export default CountryDropdown;
