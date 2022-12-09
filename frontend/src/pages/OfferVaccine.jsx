import Demand from '../components/Demand';
import Supply from '../components/Supply';

function OfferVaccine() {
	return (
		<div className='flex flex-col-1 items-center gap-12 m-6 grow relative justify-center'>
			<Demand />
			<Supply />
		</div>
	);
}

export default OfferVaccine;
