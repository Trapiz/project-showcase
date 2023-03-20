describe('Gets list of trophies', () => {
	it('Gets list of trophies', () => {
		cy.request('/trophies').then(response => {
			expect(response.status).to.be.equal(200)
			expect(response.body.trophies).to.be.lengthOf(3)
		})
	})
})

describe('Add a new trophy', () => {
	it('Fails when adding a trophy without providing required fields', () => {
		cy.request({
			url: '/trophies',
			method: 'post',
			failOnStatusCode: false,
			body: {}
		}).then(response => {
			expect(response.status).to.be.equal(400)
			expect(response.body.error).to.be.equal('Must provide a title, description, image and locked image')
		})

		cy.request({
			url: '/trophies',
			method: 'post',
			failOnStatusCode: false,
			body: {
				title: 'Around the world',
				description: 'Get 40075 points',
			}
		}).then(response => {
			expect(response.status).to.be.equal(400)
			expect(response.body.error).to.be.equal('Must provide a title, description, image and locked image')
		})

	})

	it('Creates a new trophy', () => {
		cy.request('/trophies').then(response => {
			expect(response.status).to.be.equal(200)
			expect(response.body.trophies).to.be.lengthOf(3)
		})

		cy.request({
			url: '/trophies',
			method: 'post',
			body: {
				title: 'Around the world',
				description: 'Get 40075 points',
				image: 'https://url.to.img1',
				lockedImage: 'https://url.to.img2'

			}
		}).then(response => {
			expect(response.status).to.be.equal(200)
			expect(response.body.id).to.be.equal(4)
		})

		cy.request('/trophies').then(response => {
			expect(response.status).to.be.equal(200)
			expect(response.body.trophies).to.be.lengthOf(4)
		})
	})
})