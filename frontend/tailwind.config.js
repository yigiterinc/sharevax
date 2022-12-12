/* eslint-env node */
/** @type {import('tailwindcss').Config} */

const themeColors = {
	main: {
		50: '#64daff',
		100: '#008db9',
		200: '#0080a8',
		300: '#00b4ec',
		400: '#006686',
	},
};

module.exports = {
	content: ['./src/**/*.{html,js,jsx}'],
	important: '#root',
	theme: {
		extend: {
			colors: {...themeColors},
		},
	},
	plugins: [],
};
