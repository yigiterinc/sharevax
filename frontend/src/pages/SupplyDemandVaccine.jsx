import {useState, createElement} from 'react';
import {FaParachuteBox} from 'react-icons/fa';
import {TbVaccine} from 'react-icons/tb';
import {BsCheckCircleFill} from 'react-icons/bs';
import Demand from '../components/Demand';
import Supply from '../components/Supply';
import Approval from '../components/Approval';
import CountryDropdown from '../components/CountryDropdown';

function SupplyDemandVaccine() {
	const [openTab, setOpenTab] = useState(1);
	return (
		<div className='flex flex-col items-center gap-12 m-6 grow'>
			<div className='flex justify-end w-full gap-3 -mb-8'>
				<CountryDropdown />
			</div>
			<div className='flex flex-wrap'>
				<div className='w-full'>
					<ul className='flex mb-0 list-none flex-wrap pt-3 pb-4 flex-row' role='tablist'>
						<li className='-mb-px mr-2 last:mr-0 flex-auto text-center focus:outline-none focus:shadow-outline hover:ease-in hover:scale-105 transition duration-150 ease-out'>
							<a
								className={
									'text-xs font-bold uppercase px-5 py-3 shadow-lg rounded block leading-normal ' +
									(openTab === 1 ? 'text-white bg-main-100' : 'text-main-100 bg-white')
								}
								onClick={(e) => {
									e.preventDefault();
									setOpenTab(1);
								}}
								data-toggle='tab'
								href='#link1'
								role='tablist'
							>
								<div className='flex justify-center items-center gap-3 '>
									<div>{createElement(FaParachuteBox, {size: '20'})}</div>
									<h2>Supply</h2>
								</div>
							</a>
						</li>
						<li className='-mb-px mr-2 last:mr-0 flex-auto text-center focus:outline-none focus:shadow-outline hover:ease-in hover:scale-105 transition duration-150 ease-out'>
							<a
								className={
									'text-xs font-bold uppercase px-5 py-3 shadow-lg rounded block leading-normal ' +
									(openTab === 2 ? 'text-white bg-main-100' : 'text-main-100 bg-white')
								}
								onClick={(e) => {
									e.preventDefault();
									setOpenTab(2);
								}}
								data-toggle='tab'
								href='#link2'
								role='tablist'
							>
								<div className='flex justify-center items-center gap-3 '>
									<div>{createElement(TbVaccine, {size: '20'})}</div>
									<h2>Demand</h2>
								</div>
							</a>
						</li>
						<li className='-mb-px mr-2 last:mr-0 flex-auto text-center focus:outline-none focus:shadow-outline hover:ease-in hover:scale-105 transition duration-150 ease-out'>
							<a
								className={
									'text-xs font-bold uppercase px-5 py-3 shadow-lg rounded block leading-normal ' +
									(openTab === 3 ? 'text-white bg-main-100' : 'text-main-100 bg-white')
								}
								onClick={(e) => {
									e.preventDefault();
									setOpenTab(3);
								}}
								data-toggle='tab'
								href='#link3'
								role='tablist'
							>
								<div className='flex justify-center items-center gap-3 '>
									<div>{createElement(BsCheckCircleFill, {size: '20'})}</div>
									<h2>Approval</h2>
								</div>
							</a>
						</li>
					</ul>
					<div className='relative flex flex-col min-w-0 bg-white w-full mb-6 shadow-lg rounded'>
						<div className='px-4 py-5'>
							<div className='tab-content tab-space'>
								<div className={openTab === 1 ? 'block' : 'hidden'} id='link1'>
									<Supply />
								</div>
								<div className={openTab === 2 ? 'block' : 'hidden'} id='link2'>
									<Demand />
								</div>
								<div className={openTab === 3 ? 'block' : 'hidden'} id='link3'>
									<Approval />
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	);
}

export default SupplyDemandVaccine;
