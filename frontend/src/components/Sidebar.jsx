//Sidebar on ALL Pages
//Universal Sidebar component appears on multiple pages
const Sidebar = ({Home, Report, OrderDetail, CountryInfo}) => {
	return (
		<div className='flex justify-end w-1/4 absolute bottom-0 left-0'>
			<a href='#'>Home</a>
			<a href='#'>Report</a>
			<a href='#'>Order Detail</a>
			<a href='#'>Country Info</a>
		</div>
	);
};

export default Sidebar;
