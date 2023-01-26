//Legend for explaining the color & percentage relationship, specified in OverviewMap.jsx
const Legend = ({legendItems}) => {
	return (
		<div className='flex flex-col justify-center px-1 w-64 h-16 absolute bottom-1 left-1 z-50 bg-white bg-opacity-70 rounded-md'>
			<div className='flex'>Vaccination Rates</div>
			<div className='flex w-full'>
				{legendItems.map((item) => (
					<div
						key={item.title}
						className='flex flex-1 items-center justify-center h-6 text-[9px]'
						style={{
							backgroundColor: item.color,
						}}
					>
						<span>{item.title}</span>
					</div>
				))}
			</div>
		</div>
	);
};

export default Legend;
