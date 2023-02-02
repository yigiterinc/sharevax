import axios from 'axios';
import {
	COUNTRIES,
	SUPPLIES,
	HARBORS,
	ACTIVE_DELIVERIES,
	PROGRESS_SIMULATION,
	SIMULATION_DAY,
	CREATE_EVENT,
	GET_EVENTS,
	FINISH_EVENT,
	RESET_SIMULATION,
	SUGGESTION,
} from './endpoints';

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

export default async function fetchSuggestions(id) {
	const url = SUGGESTION + id;
	return await axios.get(url);
}

export const progressSimulation = async function () {
	return await axios.patch(PROGRESS_SIMULATION);
};

export const fetchSimulationDay = async function () {
	return await axios.get(SIMULATION_DAY);
};

export const resetSimulation = async function () {
	return await axios.patch(RESET_SIMULATION);
};

export const createEvent = async function (type, subject, remainingDaysToStart) {
	return await axios.post(CREATE_EVENT, {type, subject, remainingDaysToStart});
};

export const fetchEvents = async function () {
	return await axios.get(GET_EVENTS);
};

export const finishEvent = async function (eventId) {
	return await axios.patch(FINISH_EVENT(eventId));
};
