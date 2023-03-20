import express from 'express';

import data from '../src/data.js';

const router = express.Router();
router.use(express.json());

router.get('/', function (request, response) {
	return response.status(200).json({ trophies: data.getTrophies() })
})

router.post('/', function (request, response) {
	let { title, description, image, lockedImage } = request.body;

	if (!title || !description || !image || !lockedImage) return response.status(400).json({ error: 'Must provide a title, description, image and locked image' })

	return response.status(200).json({ id: data.insertTrophy(title, description, image, lockedImage) })

})

export default router
