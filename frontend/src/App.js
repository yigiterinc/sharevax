import Home from './pages/Home';
import './App.css';
import Sidebar from './components/Sidebar';
import {Routes, Route} from 'react-router-dom';

function App() {
	return (
		<div className='flex'>
			<Sidebar />
			<Routes>
				<Route path='/' element={<Home />} />
			</Routes>
		</div>
	);
}

export default App;
