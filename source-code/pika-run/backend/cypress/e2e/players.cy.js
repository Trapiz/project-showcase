import { expect } from "chai"

describe('Get list of all users', () => {
	it('Lists all users', () => {
		cy.request('/players').then((response) => {
			expect(response).property('status').to.equal(200)
			expect(response).property('body').property('players').to.have.lengthOf(1)
		})
	})
})

describe('Get a player', () => {
	it('Gets a player', () => {
		cy.request('/players/1').then((response) => {
			expect(response).property('status').to.equal(200)
			expect(response.body.id).to.equal(1)
			expect(response.body.name).to.equal('Sander')
		})
	})

	it('Gets a player with their trophies', () => {
		cy.request('/players/1/trophies').then((response) => {
			expect(response).property('status').to.equal(200)
			let player = response.body
			expect(player).to.have.keys(['id', 'name', 'trophies'])
			expect(player).to.not.have.key('games')

		})
	})

	it('Gets a player with their games', () => {
		cy.request('/players/1/games').then((response) => {
			expect(response).property('status').to.equal(200)
			let player = response.body
			expect(player).to.have.keys(['id', 'name', 'games'])
			expect(player).to.not.have.key('trophies')
		})
	})

	it('Gets a player with their trophies and games', () => {
		cy.request('/players/1/full').then((response) => {
			expect(response).property('status').to.equal(200)
			let player = response.body
			expect(player).to.have.keys(['id', 'name', 'trophies', 'games'])
		})
	})
})

describe('Add a trophy to a player', () => {
	it('Gives a player a trophy', () => {
		cy.request('/players/1/trophies').then((response) => {
			expect(response.body.trophies).to.be.lengthOf(2)
		})
		cy.request({
			url: '/players',
			method: 'put',
			failOnStatusCode: false,
			body: {
				player_id: 1,
				trophy_id: 3
			}
		}).then((response) => {
			expect(response).property('status').to.equal(201)
		})
		cy.request('/players/1/trophies').then((response) => {
			expect(response.body.trophies).to.be.lengthOf(3)
		})
	})
})

describe('Create a new user', () => {
	it('Create a new user without providing name', () => {
		cy.request({
			url: '/players',
			method: 'post',
			failOnStatusCode: false,
			body: {}
		}).then((response) => {
			expect(response.status).to.be.equal(400)
			expect(response.body.error).to.be.equal('Must provide a name')
		})
	})

	it('Creates a new user', () => {
		cy.request({
			url: '/players',
			method: 'post',
			body: { name: 'Stephen' }
		}).then((response) => {
			expect(response.status).to.be.equal(201)
			expect(response.body.id).to.be.equal(2)
		})
	})
})

describe('Deleting a user', () => {
	it('Delete a user with invalid id', () => {
		cy.request({
			url: '/players/3',
			method: 'delete',
			failOnStatusCode: false
		}).then((response) => {
			expect(response.status).to.be.equal(404)
			expect(response.body.error).to.be.equal("Invalid player id")
		})
	})

	it('Delete a user', () => {
		cy.request({
			url: '/players/1',
			method: 'delete',
		}).then((response) => {
			expect(response.status).to.be.equal(200)
			expect(response.body).to.be.empty
		})
	})
})
