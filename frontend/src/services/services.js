import axios from 'axios';
import {COUNTRIES, SUPPLIES, HARBORS} from './endpoints';

export const fetchCountries = async function () {
	const url = COUNTRIES;
	return await axios.get(url);
};

export const fetchSupplies = async function () {
	const url = SUPPLIES;
	return await axios.get(url);
};

export const fetchHarbors = async function () {
	const url = HARBORS;
	return await axios.get(url);
};
