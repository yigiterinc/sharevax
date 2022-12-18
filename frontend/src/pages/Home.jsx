//Home Page
import {useEffect, useState} from 'react';
import OverviewMap from '../components/OverviewMap';
import OverviewTable from '../components/OverviewTable';
import CountryDropdown from '../components/CountryDropdown';
import NextDayButton from '../components/NextDayButton';
import NextDaySnackbar from '../components/NextDaySnackbar';
import WorldSummary from '../components/WorldSummary';

function Home() {
	const [onNextDay, setOnNextDay] = useState(false);
	const [mapUpdated, setMapUpdated] = useState(false);
	const [tableUpdated, setTableUpdated] = useState(false);

	useEffect(() => {
		if (mapUpdated && tableUpdated) {
			setOnNextDay(false);
			setMapUpdated(false);
			setTableUpdated(false);
		}
	}, [mapUpdated, tableUpdated]);

	return (
		<div className='flex flex-col items-center gap-12 m-6 grow'>
			<div className='flex justify-end w-full gap-3 -mb-8'>
				<NextDayButton onNextDay={() => setOnNextDay(true)} />
				<CountryDropdown />
			</div>
			<NextDaySnackbar onNextDay={onNextDay} />
			<OverviewMap onNextDay={onNextDay} setUpdated={setMapUpdated} />
			<WorldSummary />
			<OverviewTable onNextDay={onNextDay} setUpdated={setTableUpdated} />
		</div>
	);
}

export default Home;
