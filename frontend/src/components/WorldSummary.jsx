//World Summary table on the right of the map on Home page
import {GiShipBow, GiRobotGrab} from 'react-icons/gi';
import {TbVaccine} from 'react-icons/tb';
export default function WorldSummary() {
	return (
		<div>
			<div className='mx-5 mt-3 grid grid-col-1 gap-2'>
				<div className='grid grid-rows-2 grid-flow-col bg-main-100/100 rounded-lg mb-6 p-3'>
					<div className='font-bold font-mono'>Current Transport</div>
					<div className='font-bold mt-5'>324</div>
					<div className='row-span-2'>
						<GiShipBow size={70} />
					</div>
				</div>
				<div className='grid grid-rows-2 grid-flow-col bg-main-100/75 rounded-lg mb-6 p-3'>
					<div className='font-bold font-mono'>Daily Vaccine Production</div>
					<div className='font-bold mt-5'>12345</div>
					<div className='row-span-2'>
						<GiRobotGrab size={70} />
					</div>
				</div>
				<div className='grid grid-rows-2 grid-flow-col bg-main-100/50 rounded-lg p-3'>
					<div className='font-bold font-mono'>Daily Vaccine Consumption</div>
					<div className='font-bold mt-5'>12314</div>
					<div className='row-span-2'>
						<TbVaccine size={70} />
					</div>
				</div>
			</div>
		</div>
	);
}
