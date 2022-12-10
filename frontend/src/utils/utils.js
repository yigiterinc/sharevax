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

export const separateNumberWithCommas = (val) => {
	// remove sign if negative
	var sign = 1;
	if (val < 0) {
		sign = -1;
		val = -val;
	}
	// trim the number decimal point if it exists
	let num = val.toString().includes('.') ? val.toString().split('.')[0] : val.toString();
	let len = num.toString().length;
	let result = '';
	let count = 1;

	for (let i = len - 1; i >= 0; i--) {
		result = num.toString()[i] + result;
		if (count % 3 === 0 && count !== 0 && i !== 0) {
			result = ',' + result;
		}
		count++;
	}

	// add number after decimal point
	if (val.toString().includes('.')) {
		result = result + '.' + val.toString().split('.')[1];
	}
	// return result with - sign if negative
	return sign < 0 ? '-' + result : result;
};

export const swapLatLng = (coordinates) => {
	coordinates[0] = coordinates.splice(1, 1, coordinates[0])[0];
	return coordinates;
};
