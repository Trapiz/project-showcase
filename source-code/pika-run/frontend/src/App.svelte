<script>
	import Modal from "./Modals/Modal.svelte";
	import Background from "./Background.svelte";
	import Game from "./Game.svelte";
	import Header from "./Header.svelte";
	import { isLoggedIn, player } from "./stores";
	import { api } from "./utils";

	if ("id" in localStorage && window.navigator.onLine) {
		isLoggedIn.set(true);
		api(`/players/${localStorage.id}/full`).then((response) => {
			player.set(response);
		});
	}
</script>

<main>
	<Header />
	<Game />
	<Background />
</main>

<Modal />
