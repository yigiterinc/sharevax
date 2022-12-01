//Home Page
import OverviewMap from '../components/OverviewMap';
import OverviewTable from '../components/OverviewTable';
function Home() {
	return (
		<div className='flex flex-col items-center gap-12 m-6 grow'>
			<OverviewMap />
			<OverviewTable />
		</div>
	);
}

export default Home;
