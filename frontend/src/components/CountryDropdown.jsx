import {setGlobalState, useGlobalState} from '../state';

const countries = [
	{name: 'China', value: 'China'},
	{name: 'Japan', value: 'Japan'},
	{name: 'India', value: 'India'},
	{name: 'United Kingdom', value: 'United Kingdom'},
	{name: 'Portugal', value: 'Portugal'},
	{name: 'USA', value: 'USA'},
	{name: 'Brazil', value: 'Brazil'},
	{name: 'South Africa', value: 'South Africa'},
	{name: 'Nigeria', value: 'Nigeria'},
];

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
				{countries.map((country) => (
					<option key={country.value} value={country.value}>
						{country.name}
					</option>
				))}
			</select>
		</div>
	);
}

export default CountryDropdown;
