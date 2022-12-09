//Home Page
import OverviewMap from '../components/OverviewMap';
import OverviewTable from '../components/OverviewTable';
import CountryDropdown from '../components/CountryDropdown';
import NextDayButton from '../components/NextDayButton';
import WorldSummary from '../components/WorldSummary';

function Home() {
	return (
		<div className='flex flex-col items-center gap-12 m-6 grow'>
			<div className='flex justify-end w-full gap-3 -mb-8'>
				<NextDayButton />
				<CountryDropdown />
			</div>
			<div className='flex flex-row w-full'>
				<OverviewMap className='w-9/10' />
				<WorldSummary className='w-1/10' />
			</div>

			<OverviewTable />
		</div>
	);
}

export default Home;
