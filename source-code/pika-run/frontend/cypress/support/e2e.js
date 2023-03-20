// ***********************************************************
// This example support/e2e.js is processed and
// loaded automatically before your test files.
//
// This is a great place to put global configuration and
// behavior that modifies Cypress.
//
// You can change the location of this file or turn off
// automatically serving support files with the
// 'supportFile' configuration option.
//
// You can read more here:
// https://on.cypress.io/configuration
// ***********************************************************

// Import commands.js using ES2015 syntax:
import './commands'

beforeEach(function () {
	// Ask the server to reset the database to what's in schema.sql
	cy.request({ method: 'PUT', url: 'http://localhost:3000/reset_database' });

	if (window.navigator && navigator.serviceWorker) {
		navigator.serviceWorker.getRegistrations()
			.then((registrations) => {
				registrations.forEach((registration) => {
					registration.unregister();
				});
			});
	}
});
