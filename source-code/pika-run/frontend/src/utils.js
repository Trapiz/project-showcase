/* TODO: This would be a great place to define and `export` a function
 * (or multiple functions) that allows the rest of the code to easily
 * interact with the REST API.
 */

import { isLoggedIn, player, players, socket } from "./stores";

/**
 * Helper function to help with fetching the api
 *
 * @param {string} path The path to fetch.
 * @param {string} method The fetch method used.
 * @param {object} body The body for the fetch.
 * @return {promise} The fetched result
 */
export async function api(path, method = "GET", body = {}) {
	let options = {
		method
	}
	if (method != "GET") {
		options = {
			...options,
			headers: {
				'Content-Type': 'application/json',
				'Access-Control-Allow-Origin': '*'
			},
			body: JSON.stringify(body)
		}
	}

	let response = await fetch('http://localhost:3000' + path, options)
	return await response.json()
}

export function parseSeconds(seconds) {
	let minutes = Math.floor(seconds / 60);
	let remainingSeconds = seconds % 60;
	return `${minutes > 0 ? `${minutes}m` : ""} ${remainingSeconds}s`;
}

