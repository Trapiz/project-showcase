<script>
	import { parseSeconds } from "../utils";

	export let game;
	export let position;
</script>

<div class="entry">
	<div class="player">
		<span>{position}.</span>
		<span>{game.playerName}</span>
	</div>
	<div class="perf">
		<span>{game.points}</span>
		<span>{parseSeconds(game.time)}</span>
	</div>
</div>

<style>
	.entry {
		display: flex;
		justify-content: space-between;
	}

	span {
		display: inline-block;
		color: white;
		font-size: 35px;
		font-family: pixel-font;
	}

	.player > span:nth-child(1) {
	}

	.perf > span {
		width: 100px;
		text-align: right;
	}
</style>
