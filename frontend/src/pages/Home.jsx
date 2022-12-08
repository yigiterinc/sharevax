//Home Page
import OverviewMap from '../components/OverviewMap';
import OverviewTable from '../components/OverviewTable';
import Demand from '../components/Demand';
import Supply from '../components/Supply';
import WorldSummary from '../components/WorldSummary';
function Home() {
	return (
		<div className='flex flex-col items-center gap-12 m-6 grow'>
			<OverviewMap />
			<OverviewTable />
			<Demand />
			<Supply />
			<WorldSummary />
		</div>
	);
}

export default Home;
