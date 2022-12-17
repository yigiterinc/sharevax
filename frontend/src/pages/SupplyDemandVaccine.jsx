import Demand from '../components/Demand';
import Supply from '../components/Supply';

function SupplyDemandVaccine() {
	return (
		<div className='flex flex-col items-center gap-12 m-6 grow'>
			<Demand />
			<Supply />
		</div>
	);
}

export default SupplyDemandVaccine;
