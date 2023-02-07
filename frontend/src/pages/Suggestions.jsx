//Suggestion Page
import SuggestionList from '../components/SuggestionList';
import {useGlobalState} from '../state/index';
import CountryDropdown from '../components/CountryDropdown';

export default function SuggestionPage() {
	const [country] = useGlobalState('country');

	return (
		<div className='flex flex-col gap-6 m-6 grow'>
			<div className='flex justify-between items-center w-full'>
				<div className='font-semibold text-xl align-left'>Suggestions for {country}</div>
				<CountryDropdown />
			</div>
			<SuggestionList />
		</div>
	);
}
