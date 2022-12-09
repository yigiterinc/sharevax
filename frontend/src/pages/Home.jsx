//Home Page
import OverviewMap from '../components/OverviewMap';
import OverviewTable from '../components/OverviewTable';
import CountryDropdown from '../components/CountryDropdown';

function Home() {
	return (
		<div className='flex flex-col items-center gap-12 m-6 grow'>
			<div className='flex justify-end w-full -mb-8'>
				<CountryDropdown />
			</div>
			<OverviewMap />
			<OverviewTable />
		</div>
	);
}

export default Home;
