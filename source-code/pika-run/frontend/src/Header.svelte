<script>
	import Leaderboard from "./Modals/Leaderboard.svelte";
	import { openModal } from "./Modals/Modal.svelte";
	import Profile from "./Modals/Profile.svelte";
	import { isLoggedIn, socket } from "./stores";

	function showContent(modal) {
		if ($socket.readyState == WebSocket.OPEN) {
			openModal(modal);
		}
	}
</script>

<header>
	<button on:click={() => showContent(Profile)}
		>{$isLoggedIn ? "Profile" : "Login"}</button
	>
	<h1>Pika Run</h1>
	<button on:click={() => showContent(Leaderboard)}>Leaderboard</button>
</header>

<style>
	header {
		height: 50px;
		background: url("../bricks.png");
		background-size: contain;
		display: flex;
		justify-content: center;
		justify-content: space-between;
		align-items: center;
		box-shadow: 0 5px 10px black;
		padding: 0 15px;
	}

	h1 {
		margin: 0;
		color: white;
		font-family: pixel-font;
		font-size: 60px;
		font-weight: 100;
		text-shadow: 5px 5px black;
	}

	button {
		outline: none;
		border: none;
		border-radius: 3px;
		background-color: rgba(0, 0, 0, 0.5);
		color: white;
		font-size: 30px;
		font-family: pixel-font;
	}

	button:active {
		background-color: rgba(150, 150, 150, 0.5);
		transform: scale(1.1);
	}

	@media screen and (max-width: 550px) {
		h1 {
			display: none;
		}
	}
</style>
