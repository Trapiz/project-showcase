<script>
	import { player } from "../stores";
	import { parseSeconds } from "../utils";

	export let points;
	export let time;

	const shareData = {
		title: "Look at my score on this Pika Run!",
		text: `I got ${Math.round(
			points
		)} points and survived for ${parseSeconds(time)}!`,
		url: "http://localhost:8080/",
	};

	async function tryShare() {
		try {
			await window.navigator.share(shareData);
			console.log("MDN shared successfully");
		} catch (err) {
			console.log(err);
		}
	}
</script>

<div class="container">
	<h2>Game over!</h2>
	<div>
		<span class="title">Points: </span><span>{Math.round(points)}</span><br
		/>
		<span class="title">Time: </span><span>{parseSeconds(time)}</span>
	</div>
	<button on:click={() => tryShare()}>Share!</button>
</div>

<style>
	.container {
		display: flex;
		flex-direction: column;
		align-items: center;
	}

	.title {
		margin-right: 10px;
	}

	h2 {
		font-family: pixel-font;
		font-weight: 300;
		font-size: 40px;
		margin: 0;
		color: white;
	}

	span {
		font-size: 35px;
		color: white;
		font-family: pixel-font;
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

	button:active {
		background-color: rgba(150, 150, 150, 0.5);
		transform: scale(1.1);
	}
</style>
