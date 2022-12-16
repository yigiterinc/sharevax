//World Summary table on the right of the map on Home page
import {GiShipBow, GiRobotGrab} from 'react-icons/gi';
import {TbVaccine} from 'react-icons/tb';
import {fetchActiveDeliveries, fetchCountries} from '../services/services';
import {useEffect, useState} from 'react';

export default function WorldSummary() {
	//fetch activeDeliveries number
	const [activeDeliveriesData, setActiveDeliveriesData] = useState([]);

	useEffect(() => {
		fetchActiveDeliveriesData();
	}, []);

	const fetchActiveDeliveriesData = async () => {
		const result = await fetchActiveDeliveries();
		setActiveDeliveriesData(result.data);
	};
	console.log(activeDeliveriesData);
	console.log('active Deliveries number: \n', activeDeliveriesData.length);

	//fetch country dailyVaccineProduction & dailyVaccineConsumption
	const [countriesData, setCountriesData] = useState([]);

	useEffect(() => {
		fetchCountriesData();
	}, []);

	const fetchCountriesData = async () => {
		const result = await fetchCountries();
		setCountriesData(result.data);
	};

	function countDailyVaccineProduction(countriesData) {
		let count = 0;
		for (let i = 0; i < countriesData.length; i++) {
			count += countriesData[i].dailyVaccineProduction;
		}
		return count.toLocaleString(undefined, {minimumFractionDigits: 0});
	}

	function countDailyVaccineConsumption(countriesData) {
		let count = 0;
		for (let i = 0; i < countriesData.length; i++) {
			count += countriesData[i].dailyVaccineConsumption;
		}
		return count.toLocaleString(undefined, {minimumFractionDigits: 0});
	}

	console.log('number of countries:', countriesData.length);
	console.log('country data:', countriesData);

	const blockStyle =
		'grid grid-rows-2 grid-flow-col bg-main-100 rounded-lg p-3 hover:drop-shadow-xl hover:ease-in hover:scale-105 transition duration-150 ease-out ';

	return (
		<div>
			<div className='grid grid-cols-3 gap-11 scale-10 w-full'>
				<div className={blockStyle}>
					<div className='font-bold text-white'>Current Transport</div>
					<div className='font-bold mt-5 text-white'>
						{activeDeliveriesData.length.toLocaleString(undefined, {minimumFractionDigits: 0})}
					</div>
					<div className='row-span-2 grid justify-items-end items-center'>
						<GiShipBow size={70} color={'white'} />
					</div>
				</div>

				<div className={blockStyle}>
					<div className='font-bold text-white'>Daily Vaccine Production</div>
					<div className='font-bold mt-5 text-white'>{countDailyVaccineProduction(countriesData)}</div>
					<div className='row-span-2 grid justify-items-end items-center'>
						<GiRobotGrab size={70} color={'white'} />
					</div>
				</div>
				<div className={blockStyle}>
					<div className='font-bold text-white'>Daily Vaccine Consumption</div>
					<div className='font-bold mt-5 text-white'>{countDailyVaccineConsumption(countriesData)}</div>
					<div className='row-span-2 grid justify-items-end items-center'>
						<TbVaccine size={70} color={'white'} />
					</div>
				</div>
			</div>
		</div>
	);
}
