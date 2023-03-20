<script>
	import { player } from "./stores";

	export let trophy;
	let hasUnlocked = false;
	let trophyImg = trophy.lockedImage;
	let playerTrophy = $player.trophies.filter(
		(playerTrophy) => playerTrophy.id === trophy.id
	);
	if (playerTrophy.length == 1) {
		hasUnlocked = true;
		trophyImg = trophy.image;
		trophy = playerTrophy[0];
	}
</script>

<div>
	<h3>{trophy.title}</h3>
	<p>{trophy.description}</p>
	<img src={trophyImg} alt="{trophy.title} trophy" />
	{#if hasUnlocked}
		<span>{new Date(trophy.received_at).toLocaleDateString()}</span>
	{:else}
		<span>Not unlocked yet</span>
	{/if}
</div>

<style>
	div {
		display: flex;
		flex-direction: column;
		align-items: center;
	}

	h3 {
		font-size: 30px;
		font-weight: 300;
		margin-bottom: 0;
	}

	p {
		margin-top: 5px;
	}

	img {
		width: 64px;
		height: 64px;
	}

	span {
		font-size: 20px;
	}
</style>
