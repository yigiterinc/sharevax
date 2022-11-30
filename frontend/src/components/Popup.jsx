const Popup = ({countryName, vaccinationRate, infectionRate, recoveryRate, doseInStock, doseUsedPerDay}) => {
	return (
		<div>
			<div className='font-semibold'>
				Country: <span className='font-normal'>{countryName}</span>
			</div>
			<div className='font-semibold'>
				Vaccination rate: <span className='font-normal'>{vaccinationRate}</span>
			</div>
			<div className='font-semibold'>
				Infection rate: <span className='font-normal'>{infectionRate}</span>
			</div>
			<div className='font-semibold'>
				Recovery rate: <span className='font-normal'>{recoveryRate}</span>
			</div>
			<div className='font-semibold'>
				Dose in stock: <span className='font-normal'>{doseInStock}</span>
			</div>
			<div className='font-semibold'>
				Dose used per day: <span className='font-normal'>{doseUsedPerDay}</span>
			</div>
		</div>
	);
};

export default Popup;
