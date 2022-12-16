import Home from './pages/Home';
import './App.css';
import Sidebar from './components/Sidebar';
import {Routes, Route} from 'react-router-dom';
import CountryInfo from './pages/CountryInfo';
import OrderDetail from './pages/OrderDetail';
import Report from './pages/Report';
import OfferVaccine from './pages/OfferVaccine';

function App() {
	return (
		<div className='flex'>
			<Sidebar />
			<Routes>
				<Route path='/' element={<Home />} />
				<Route path='/country-info' element={<CountryInfo />} />
				<Route path='/order-detail' element={<OrderDetail />} />
				<Route path='/report' element={<Report />} />
				<Route path='/offer-vaccine' element={<OfferVaccine />} />
			</Routes>
		</div>
	);
}

export default App;
