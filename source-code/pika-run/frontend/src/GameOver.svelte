<script>
	import Dead from "./Modals/Dead.svelte";
	import { openModal } from "./Modals/Modal.svelte";
	import { isLoggedIn, player } from "./stores";
	import { api } from "./utils";

	export let points = 0;
	export let time = 0;

	if ($isLoggedIn) {
		updatePlayer();
	}

	async function updatePlayer() {
		await api("/games", "POST", {
			player_id: $player.id,
			points: Math.round(points),
			time,
		});

		if ($player.games.length >= 5) {
			await api("/players", "PUT", {
				player_id: $player.id,
				trophy_id: 3,
			});
		}

		if (points >= 100) {
			await api("/players", "PUT", {
				player_id: $player.id,
				trophy_id: 1,
			});
		}

		if (time >= 60) {
			await api("/players", "PUT", {
				player_id: $player.id,
				trophy_id: 2,
			});
		}

		await api(`/players/${$player.id}/full`).then((response) => {
			player.set(response);
		});
	}

	openModal(Dead, {
		points,
		time,
	});
</script>
