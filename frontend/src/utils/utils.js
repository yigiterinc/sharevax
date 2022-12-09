const colors = ['#ff0000', '#ffa700', '#fff400', '#a3ff00', '#2cba00'];

const scale = [0, 0.2, 0.4, 0.6, 0.8];

export const legendItems = [
	{title: '< 20%', color: colors[0], textColor: 'black'},
	{title: '20%-40%', color: colors[1], textColor: 'black'},
	{title: '40%-60%', color: colors[2], textColor: 'black'},
	{title: '60%-80%', color: colors[3], textColor: 'black'},
	{title: '> 80%', color: colors[4], textColor: 'black'},
];

export const getColor = (val) => {
	let color = '';
	for (let i = 1; i < scale.length; i++) {
		if (val < scale[i]) {
			color = colors[i - 1];
			return colors[i - 1];
		}
	}
	color = colors[colors.length - 1];
	console.log(color);
	return colors[colors.length - 1];
};
