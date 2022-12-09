//Country Information Page
import {useGlobalState} from '../state';

function CountryInfo() {
	const [country] = useGlobalState('country');

	return <div className='flex flex-col items-center gap-12'>Country Info page of {country}</div>;
}

export default CountryInfo;
