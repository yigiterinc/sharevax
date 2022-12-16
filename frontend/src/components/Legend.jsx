//Legend for explaining the color & percentage relationship, specified in OverviewMap.jsx
const Legend = ({legendItems}) => {
	return (
		<div className='flex justify-end w-1/4 absolute bottom-0 left-0'>
			{legendItems.map((item) => (
				<div
					key={item.title}
					className='flex flex-1 items-center justify-center h-8 text-[10px]'
					style={{
						backgroundColor: item.color,
					}}
				>
					<span>{item.title}</span>
				</div>
			))}
		</div>
	);
};

export default Legend;
