import OverViewTable from '../components/OverviewTable';
export default function Approval() {
	return (
		<div>
			<OverViewTable />
			<button
				type='submit'
				value='Submit'
				className='w-full bg-main-100 text-white font-bold mt-6 py-3 px-4 rounded focus:outline-none focus:shadow-outline hover:ease-in hover:scale-105 transition duration-150 ease-out'
			>
				Submit
			</button>
		</div>
	);
}
