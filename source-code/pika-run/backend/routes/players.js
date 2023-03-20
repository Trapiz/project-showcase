import express from 'express';

import data from '../src/data.js';

const router = express.Router();
router.use(express.json());

router.get('/', function (request, response) {
	return response.status(200).json({ players: data.getPlayers() })
})

router.get('/:id', function (request, response) {
	const { id } = request.params
	return response.status(200).json({ ...data.getPlayer({ id }) })
})

router.get('/:id/full', function (request, response) {
	const { id } = request.params
	return response.status(200).json({ ...data.getPlayer({ id, full: true }) })
})

router.get('/:id/trophies', function (request, response) {
	const { id } = request.params
	return response.status(200).json({ ...data.getPlayer({ id }), trophies: data.getPlayerTrophies(id) })
})

router.get('/:id/games', function (request, response) {
	const { id } = request.params
	return response.status(200).json({ ...data.getPlayer({ id }), games: data.getPlayerGames(id) })
})

router.put('/', function (request, response) {
	const { player_id, trophy_id } = request.body

	if (!player_id || !trophy_id) return response.status(404).json({ error: 'Must provide player_id and trophy_id' })


	return response.status(201).json({ id: data.insertPlayerTrophy(player_id, trophy_id) })
})

router.post('/', function (request, response) {
	const { name, full = false } = request.body;
	if (!name) return response.status(400).json({ error: 'Must provide a name' })

	return response.status(201).json({ ...data.insertPlayer(name, full) })
})

router.delete("/:id", function (request, response) {
	const { id } = request.params

	if (!data.deletePlayer(id)) return response.status(404).json({ error: 'Invalid player id' })
	return response.status(200).json({})
})

export default router