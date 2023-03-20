<script>
	import LeaderboardEntry from "./LeaderboardEntry.svelte";
	import { api } from "../utils";

	let games = undefined;
	async function getGames() {
		let response = await api("/games", "GET");
		games = response.games.slice(0, 10);
	}

	getGames();
</script>

<div>
	<h2>Leaderboard</h2>
	{#if games}
		{#each games as game, i}
			<LeaderboardEntry {game} position={i + 1} />
		{/each}
	{/if}
</div>

<style>
	h2 {
		width: 100%;
		text-align: center;
		font-size: 60px;
		margin: 0;
		color: white;
		font-family: pixel-font;
		font-weight: 300;
	}
</style>
