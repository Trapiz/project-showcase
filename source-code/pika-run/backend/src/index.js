import express from 'express';
import cors from 'cors';
import data from './data.js';
import { WebSocketServer } from 'ws';


import { players, games, trophies } from '../routes/index.js'

const app = express();

// Allow browsers that are pointed at different URLs (such as our frontend) to access this API.blank
// In an actual production app, you'll want to tighten what origin you allow.
app.use(cors());

// Configure express to automatically decode JSON bodies
app.use(express.json());

// Make sure tables and initial data exist in the database
data.applySchema();

app.use('/players', players)
app.use('/games', games)
app.use('/trophies', trophies)

// Reset database
app.put('/reset_database', function (req, rsp) {
	data.dropAllTables();
	data.applySchema();
	rsp.json({});
});

// Return a 404 if none of the above routes matched.
app.use(function (request, response, next) {
	response.status(404).json({ error: "Invalid resource" })
});

// Set the default error handler
app.use(function (err, req, res, next) {
	console.error(err.stack)
	res.status(500).send('Internal server error')
});

// Start accepting requests
const listener = app.listen(process.env.PORT || 3000, "0.0.0.0", function () {
	console.log('REST API listening on port ' + listener.address().port);
});



const server = new WebSocketServer({ port: '9000' })
console.log('Websocket listening on port ' + server.address().port);

const sockets = []

server.on('connection', socket => {

	socket.on('message', message => {
		message = JSON.parse(message.toString())
		if (message.type === 'connect') {
			socket.player = message.id
			sockets.push(socket)
			for (let s of sockets) {
				if (s != socket) {
					socket.send(JSON.stringify({ type: 'connect', player: s.player }))
					s.send(JSON.stringify({ type: 'connect', player: socket.player }))
				}
			}
		}
		if (message.type === 'jump') {
			for (let s of sockets) {
				if (s != socket) {
					s.send(JSON.stringify({ type: 'jump', player: socket.player }))
				}
			}
		}

		if (message.type === 'move') {
			for (let s of sockets) {
				if (s != socket) {
					s.send(JSON.stringify({ type: 'move', player: socket.player, x: message.x }))
				}
			}
		}

		if (message.type === 'dead') {
			for (let s of sockets) {
				if (s != socket) {
					s.send(JSON.stringify({ type: 'dead', player: socket.player }))
				}
			}
		}

		if (message.type === 'start') {
			for (let s of sockets) {
				if (s != socket) {
					s.send(JSON.stringify({ type: 'start', player: socket.player }))
				}
			}
		}
	})

	socket.on('close', () => {
		if ('player' in socket) {
			for (let s of sockets) {
				if (s != socket) {
					s.send(JSON.stringify({ type: 'dead', player: socket.player }))
				}
			}
		}
	})

})