import fs from 'fs';
import Database from 'better-sqlite3';

const db = new Database(process.env.DB || './src/sqlite3.db');

const functions = {}



// ----------------------- General --------------------------------

functions.applySchema = () => {
	// Make sure tables and initial data exist in the database
	return db.exec(fs.readFileSync('./src/schema.sql').toString());
}

functions.dropAllTables = () => {
	for (let row of db.prepare("SELECT name FROM sqlite_master WHERE type = 'table'").all()) {
		return db.prepare(`drop table "${row['name']}"`).run();
	}
}



// ----------------------- Players --------------------------------
functions.insertPlayer = (name, full) => {
	db.prepare('INSERT OR IGNORE INTO players (name) VALUES (?) RETURNING *').run(name)
	return functions.getPlayer({ name, full })
}

functions.getPlayer = (data) => {
	let player = 'id' in data
		? db.prepare("SELECT * FROM players WHERE id = ?").get(data.id)
		: db.prepare("SELECT * FROM players WHERE name = ? COLLATE NOCASE").get(data.name)

	if (data.full) {
		player.games = functions.getPlayerGames(player.id)
		player.trophies = functions.getPlayerTrophies(player.id)
	}
	return player
}

functions.getPlayers = () => {
	const players = db.prepare("SELECT * FROM players").all();

	return players;
}

functions.getPlayerGames = (id) => {
	return db.prepare("SELECT id, points, time FROM games WHERE playerId = ? ORDER BY points DESC").all(id);
}

functions.getPlayerTrophies = (id) => {
	return db.prepare(`
	SELECT trophies.*, pt.received_at FROM trophies
	JOIN playerTrophies AS pt ON trophies.id = pt.trophyId
	JOIN players ON players.id = pt.playerId
	WHERE players.id = ?
`).all(id)
}

functions.insertPlayerTrophy = (playerId, trophyId) => {
	return db.prepare("INSERT OR IGNORE INTO playerTrophies (playerId, trophyId) VALUES (?, ?)").run(playerId, trophyId).lastInsertRowid
}

functions.deletePlayer = (id) => {
	return db.prepare("DELETE FROM players WHERE id = ?").run(id).changes
}



// ----------------------- Games --------------------------------
functions.getOrderedGames = (data) => {
	const other = data.order_on == 'time' ? 'points' : 'time'
	const order_query = `ORDER BY ${data.order_on} ${data.order_by}, ${other} ${data.order_by}`
	return db.prepare(`
		SELECT games.*, players.name as playerName FROM games
		JOIN players ON games.playerId = players.id
		${order_query}
		`).all()
}

functions.insertGame = (id, points, time) => {
	return db.prepare('INSERT INTO games (playerId, points, time) VALUES (?, ?, ?)').run(id, points, time).lastInsertRowid
}





// ----------------------- Trophies --------------------------------
functions.getTrophies = () => {
	return db.prepare("SELECT * FROM trophies").all()
}

functions.insertTrophy = (title, description, image, lockedImage) => {
	return db.prepare('INSERT INTO trophies (title, description, image, lockedImage) VALUES (?, ?, ?, ?)').run(title, description, image, lockedImage).lastInsertRowid
}

export default functions
