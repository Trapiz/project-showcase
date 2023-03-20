<script>
	import Trophy from "./../Trophy.svelte";

	import { isLoggedIn, player, socket } from "../stores.js";

	import { api } from "../utils.js";

	let name;
	async function onFormSubmit() {
		let response = await api("/players", "POST", {
			name,
			full: true,
		});
		player.set(response);
		isLoggedIn.set(true);

		localStorage.id = response.id;
		$socket.send(JSON.stringify({ type: "connect", id: $player.id }));
	}

	let trophies = undefined;
	async function getTrophies() {
		let response = await api("/trophies", "GET");
		trophies = response.trophies;
	}

	function logOut() {
		player.set({});
		isLoggedIn.set(false);
		delete localStorage.id;
	}

	getTrophies();
</script>

<div class="container">
	{#if $isLoggedIn}
		<h2>Profile</h2>
		<div class="info">
			<span class="title">Name: </span><span>{$player.name}</span><br />
			<span class="title">Highscore: </span><span
				>{$player.games[0] ? $player.games[0].points : "None"}</span
			><br />
			<span class="title">Games played: </span><span
				>{$player.games.length}</span
			><br />
		</div>
		<button class="log-out" on:click={logOut}>Log out</button>
		<h2>Trophies:</h2>
		<div class="trophies">
			{#if trophies}
				{#each trophies as trophy}
					<Trophy bind:trophy />
				{/each}
			{/if}
		</div>
	{:else}
		<h2>Sign in</h2>
		<form on:submit|preventDefault={() => onFormSubmit()}>
			<label for="name">Name: </label>
			<input type="text" bind:value={name} />
			<button type="submit">Log in</button>
		</form>
	{/if}
</div>

<style>
	.container {
		display: flex;
		flex-direction: column;
		align-items: center;
		color: white;
		font-family: pixel-font;
	}

	.trophies {
		display: flex;
		flex-wrap: wrap;
		justify-content: center;
		gap: 20px;
	}

	.title {
		margin-right: 10px;
	}

	.log-out {
		margin-top: 10px;
		margin-bottom: 30px;
	}

	span {
		font-size: 35px;
	}

	form {
		display: flex;
		flex-direction: row;
		align-items: center;
		justify-content: center;
		flex-wrap: wrap;
	}

	input {
		height: 30px;
		border: 2px solid black;
		border-radius: 5px;
		background-color: rgba(0, 0, 0, 0.6);
		color: white;
		font-family: pixel-font;
		font-size: 30px;
	}

	label {
		font-size: 40px;
		margin-right: 10px;
	}

	button {
		font-size: 30px;
		margin-left: 10px;
		border: 2px solid black;
		border-radius: 5px;
		background-color: rgba(0, 0, 0, 0.6);
		color: white;
		font-family: pixel-font;
	}

	h2 {
		font-size: 60px;
		margin: 0;
		font-weight: 300;
	}
</style>
