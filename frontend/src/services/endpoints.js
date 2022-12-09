export const BASE_URL = 'http://localhost:8080';
export const COUNTRIES = `${BASE_URL}/countries`;
export const SUPPLIES = `${BASE_URL}/supplies`;
export const HARBORS = `${BASE_URL}/harbors`;
export const COUNTRY_SUMMARY = (countryName) => `${BASE_URL}/countries/${countryName}/summary`;
export const DELIVERIES = `${BASE_URL}/deliveries`;
export const CREATE_SUPPLY = `${BASE_URL}/supplies`;
export const CREATE_DEMAND = `${BASE_URL}/demands`;
export const PROGRESS_SIMULATION = `${BASE_URL}/simulation/increment-day`;
