describe('Pika Run', () => {
	it('Login as a new user', () => {
		cy.login('Sander')

		cy.contains('Profile')
		cy.contains('Sander')

		cy.reload()
		cy.contains('Profile')
	})

	it('Logout user', () => {
		cy.login('Sander')
		cy.contains('Log out').click()
		cy.contains('Sign in')
	})

	it('View leaderboard', () => {
		cy.visit('http://localhost:8080')

		cy.contains('Leaderboard').click()

		cy.get('h2').contains('Leaderboard')

	})

	it('Play game', () => {
		cy.visit('http://localhost:8080')

		cy.get('body').trigger('keydown', { key: ' ' })
		cy.contains('Guest')
		cy.get('.container', { timeout: 10000 }).contains('Game over')
		cy.contains('Share').click()
	})


})