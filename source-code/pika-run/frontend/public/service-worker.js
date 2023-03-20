const cacheName = "pikache"

self.addEventListener('install', (event) => {
	console.log("Installed sw!")
})

self.addEventListener('fetch', (event) => {
	if (event.request.url.startsWith("http://localhost:8080/")) {
		event.respondWith(
			fetch(event.request)
				.then(async response => {
					const cache = await caches.open(cacheName)
					cache.add(event.request)
					return response
				})
				.catch(async () => {
					const cache = await caches.open(cacheName)
					return cache.match(event.request);
				})
		);
	}

});