//Home Page
import OverviewMap from '../components/OverviewMap';
import OverviewTable from '../components/OverviewTable';
import CountryDropdown from '../components/CountryDropdown';
import NextDayButton from '../components/NextDayButton';

function Home() {
	return (
		<div className='flex flex-col items-center gap-12 m-6 grow'>
			<div className='flex justify-end w-full gap-3 -mb-8'>
				<NextDayButton />
				<CountryDropdown />
			</div>
			<OverviewMap />
			<OverviewTable />
		</div>
	);
}

export default Home;
