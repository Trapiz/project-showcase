<script>
	import { hasStarted } from "./stores";

	let sky;
	let floor;
</script>

<div class="background">
	{#if $hasStarted}
		<div bind:this={floor} class="floor" />
		<div bind:this={sky} class="sky" />
	{:else}
		<div bind:this={floor} class="floor paused" />
		<div bind:this={sky} class="sky paused" />
	{/if}
</div>

<style>
	.floor {
		position: absolute;
		background: url("../floor.png");
		bottom: 0;
		background-size: 200px 200px;
		background-repeat: repeat-x;
		height: 150px;
		width: 4000px;
		animation: slide-floor 1s linear infinite;
		z-index: -1;
	}

	.sky {
		position: absolute;
		top: 0;
		background: linear-gradient(rgba(0, 0, 0, 0) 60%, rgba(0, 0, 0, 0.7)),
			url("../sky.png") repeat-x;
		background-position: top;
		height: 100vh;
		width: 7680px;
		animation: slide-sky 10s linear infinite;
		z-index: -2;
	}

	.paused {
		animation: none;
	}

	@keyframes slide-floor {
		0% {
			transform: translate(0);
		}

		100% {
			transform: translate(-200px);
		}
	}

	@keyframes slide-sky {
		0% {
			transform: translate(0);
		}

		100% {
			transform: translate(-1920px);
		}
	}
</style>
