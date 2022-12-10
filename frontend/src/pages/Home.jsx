//Home Page
import OverviewMap from '../components/OverviewMap';
import OverviewTable from '../components/OverviewTable';
import WorldSummary from '../components/WorldSummary';

function Home() {
	return (
		<div className='flex flex-col items-center gap-12 m-6 grow'>
			<OverviewMap />
			<OverviewTable />
			<WorldSummary />
		</div>
	);
}

export default Home;
