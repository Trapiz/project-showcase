import { expect } from "chai"

describe('Get list of games', () => {

	it('Gets list of games without additional filter option (default filters by points descending)', () => {
		cy.request('/games').then((response) => {
			expect(response.status).to.be.equal(200)
			expect(response.body.games).to.be.lengthOf(5)
			let points = response.body.games.map(game => game.points)
			expect(points).to.be.equal(points.sort())
		})
	})
	it('Gets list of games filtered by time ascending', () => {
		cy.request('/games?order_on=time&order_by=asc').then((response) => {
			let time = response.body.games.map(game => game.time)
			expect(time).to.be.equal(time.sort().reverse())
		})
	})

	it('Gets list of games filtered by time descending', () => {
		cy.request('/games?order_on=time&order_by=desc').then((response) => {
			let time = response.body.games.map(game => game.time)
			expect(time).to.be.equal(time.sort())
		})
	})
})

describe('Add a new game', () => {
	it('Fails when not providing all required fields', () => {
		cy.request({
			url: '/games',
			method: 'post',
			failOnStatusCode: false,
			body: {}
		}).then((response) => {
			expect(response.status).to.be.equal(400)
			expect(response.body.error).to.be.equal("Must provide a player id, points and time")
		})

		cy.request({
			url: '/games',
			method: 'post',
			failOnStatusCode: false,
			body: {
				player_id: 1,
				points: 500
			}
		}).then((response) => {
			expect(response.status).to.be.equal(400)
			expect(response.body.error).to.be.equal("Must provide a player id, points and time")
		})
	})

	it('Inserts a new game', () => {
		cy.request('/players/1/games').then((response) => {
			expect(response.body.games).to.be.lengthOf(5)
		})
		cy.request({
			url: '/games',
			method: 'post',
			body: {
				player_id: 1,
				points: 500,
				time: 5,
			}
		}).then((response) => {
			expect(response).property('status').to.be.equal(201)
			expect(response.body.id).to.be.equal(6)
			cy.request('/players/1/games', (response) => {
				expect(response.body.games).to.be.lengthOf(7)
			})
		})
	})
})