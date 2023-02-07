import {setGlobalState, useGlobalState} from '../state';
import {useNavigate} from 'react-router-dom';

const countries = [
	{name: 'China', id: 1, value: 'China', flag: '🇨🇳'},
	{name: 'Japan', id: 2, value: 'Japan', flag: '🇯🇵'},
	{name: 'India', id: 3, value: 'India', flag: '🇮🇳'},
	{name: 'United Kingdom', id: 4, value: 'United Kingdom', flag: '🇬🇧'},
	{name: 'Portugal', id: 5, value: 'Portugal', flag: '🇵🇹'},
	{name: 'USA', id: 6, value: 'USA', flag: '🇺🇸'},
	{name: 'Brazil', id: 7, value: 'Brazil', flag: '🇧🇷'},
	{name: 'South Africa', id: 8, value: 'South Africa', flag: '🇿🇦'},
	{name: 'Nigeria', id: 9, value: 'Nigeria', flag: '🇳🇬'},
];

function CountryDropdown() {
	const [country] = useGlobalState('country');
	const navigate = useNavigate();

	const handleCountryChange = (e) => {
		if (e.target.value === '') {
			setGlobalState('country', '');
			setGlobalState('flag', '');
			setGlobalState('id', 0);
			navigate('/');
			return;
		} else {
			setGlobalState('country', e.target.value);
			setGlobalState('flag', countries.find((c) => c.value === e.target.value).flag);
			setGlobalState('id', countries.find((c) => c.value === e.target.value).id);
		}
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
