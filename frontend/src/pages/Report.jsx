//Report Page
import ReportMap from '../components/ReportMap';

function Report() {
	return (
		<div className='flex flex-col gap-6 m-6 grow'>
			<div className='font-semibold text-xl align-left'>Report a Blockage</div>
			<div>
				Here, you can block <span className='text-blue-500'>harbors</span>,{' '}
				<span className='text-green-500'>straits</span>, and <span className='text-orange-500'>channels</span> for a
				specified period of time. To do this, simply click on the marker on the map representing the location you wish
				to block, enter the starting day, and then click the <span className='text-red-500'>Block</span> button.
			</div>
			<ReportMap />
		</div>
	);
}

export default Report;
