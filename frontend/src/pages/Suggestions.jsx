//Suggestion Page
import Suggestion from '../components/Suggestion';
import {useGlobalState} from '../state/index';

export default function SuggestionPage() {
	const [country] = useGlobalState('country');

	return (
		<div className='flex flex-col gap-6 m-6 grow'>
			<div className='font-semibold text-xl align-left'>Suggestions for {country}</div>
			<Suggestion />
		</div>
	);
}
