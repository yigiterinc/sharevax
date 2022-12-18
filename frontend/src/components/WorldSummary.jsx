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

	const blockStyle = 'grid grid-rows-2 grid-flow-col bg-[#ecfaff] rounded-lg p-3 h-24 shadow-lg';

	return (
		<div className='grid grid-cols-3 gap-20 w-full'>
			<div className={blockStyle}>
				<div className='font-bold text-main-100'>Current Transport</div>
				<div className='font-bold mt-3 text-main-100'>
					{activeDeliveriesData.length.toLocaleString(undefined, {minimumFractionDigits: 0})}
				</div>
				<div className='row-span-2 grid justify-items-end items-center'>
					<GiShipBow size={45} color={'#008db9'} />
				</div>
			</div>

			<div className={blockStyle}>
				<div className='font-bold text-main-100'>Daily Vaccine Production</div>
				<div className='font-bold mt-3 text-main-100'>{countDailyVaccineProduction(countriesData)}</div>
				<div className='row-span-2 grid justify-items-end items-center'>
					<GiRobotGrab size={45} color={'#008db9'} />
				</div>
			</div>
			<div className={blockStyle}>
				<div className='font-bold text-main-100'>Daily Vaccine Consumption</div>
				<div className='font-bold mt-3 text-main-100'>{countDailyVaccineConsumption(countriesData)}</div>
				<div className='row-span-2 grid justify-items-end items-center'>
					<TbVaccine size={45} color={'#008db9'} />
				</div>
			</div>
		</div>
	);
}
