import OverviewMap from './OverviewMap';
import OverviewTable from './OverviewTable';

function Home() {
	return (
		<div style={{display: 'flex', flexDirection: 'column', alignItems: 'center', rowGap: 50}}>
			<OverviewMap />
			<OverviewTable />
		</div>
	);
}

export default Home;
