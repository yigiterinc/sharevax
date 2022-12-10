import axios from 'axios';
import {COUNTRIES, SUPPLIES, HARBORS, ACTIVE_DELIVERIES, PROGRESS_SIMULATION} from './endpoints';

export const fetchCountries = async function () {
	return await axios.get(COUNTRIES);
};

export const fetchSupplies = async function () {
	return await axios.get(SUPPLIES);
};

export const fetchHarbors = async function () {
	return await axios.get(HARBORS);
};

export const fetchActiveDeliveries = async function () {
	const url = ACTIVE_DELIVERIES;
	return await axios.get(url);
};

export const progressSimulation = async function () {
	return await axios.patch(PROGRESS_SIMULATION);
};
