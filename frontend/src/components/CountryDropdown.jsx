import {setGlobalState, useGlobalState} from '../state';

const countries = [
	{name: 'China', value: 'China', flag: '🇨🇳'},
	{name: 'Japan', value: 'Japan', flag: '🇯🇵'},
	{name: 'India', value: 'India', flag: '🇮🇳'},
	{name: 'United Kingdom', value: 'United Kingdom', flag: '🇬🇧'},
	{name: 'Portugal', value: 'Portugal', flag: '🇵🇹'},
	{name: 'USA', value: 'USA', flag: '🇺🇸'},
	{name: 'Brazil', value: 'Brazil', flag: '🇧🇷'},
	{name: 'South Africa', value: 'South Africa', flag: '🇿🇦'},
	{name: 'Nigeria', value: 'Nigeria', flag: '🇳🇬'},
];

function CountryDropdown() {
	const [country] = useGlobalState('country');
	const handleCountryChange = (e) => {
		setGlobalState('country', e.target.value);
		setGlobalState('flag', countries.find((c) => c.value === e.target.value).flag);
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
