<script>
	export let points = 0;
	export let seconds = 0;

	import { isLoggedIn, player } from "./stores.js";
	import { parseSeconds } from "./utils.js";
</script>

<div class="information">
	<section class="name">
		<span
			>{#if $isLoggedIn}
				{$player.name}
			{:else}
				Guest
			{/if}</span
		>
	</section>
	<section class="time">
		<span>{parseSeconds(seconds)}</span>
	</section>
	<section class="score">
		<span>{points.toFixed(0)}</span>
	</section>
</div>

<style>
	.information {
		position: absolute;
		bottom: 0;
		height: 60px;
		width: 100%;
		z-index: 3;
		display: flex;
		justify-content: space-between;
		align-items: center;
		padding: 0 10px;
		box-sizing: border-box;
	}

	section {
		font-family: pixel-font;
		color: white;
		font-size: 50px;
	}
	span {
		margin: 0 10px;
		text-shadow: 4px 4px black;
	}
</style>
