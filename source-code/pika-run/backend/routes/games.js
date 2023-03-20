import express from 'express';

import data from '../src/data.js';


const router = express.Router();
router.use(express.json());

router.get('/', function (request, response) {

	let { order_by, order_on } = request.query

	order_by ??= 'desc'
	order_on ??= 'points'

	order_by = order_by.toLowerCase();
	order_on = order_on.toLowerCase();


	if (!["asc", "desc"].includes(order_by) || !["points", "time"].includes(order_on)) {
		return response.status(404).json({ error: "Invalid query parameters" })
	}

	return response.status(200).json({ games: data.getOrderedGames({ order_on, order_by }) })
})

router.post('/', function (request, response) {
	const { player_id, points, time } = request.body;
	if (!player_id || !points || !time) return response.status(400).json({ error: 'Must provide a player id, points and time' })

	return response.status(201).json({ id: data.insertGame(player_id, points, time) })
})

export default router
