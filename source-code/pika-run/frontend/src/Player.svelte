<script>
	import { hasStarted } from "./stores";

	export let self;
	export let isJumping = false;
	export let other;

	const sleep = (ms) => new Promise((r) => setTimeout(r, ms));

	export let x = 10;
	export let y = 60;

	export function moveTo(newX) {
		x = newX;
		self.style.left = `${x}px`;
	}

	export async function jump() {
		isJumping = true;
		let goingUp = true;
		while (true) {
			if (goingUp) {
				y += (400 - y) / 100 + 5;
				if (y > 400) {
					goingUp = false;
				}
			} else {
				y -= (400 - y) / 100 + 5;
				if (y < 60) {
					break;
				}
			}
			await sleep(15);
			if (!$hasStarted && !other) {
				break;
			}
			self.style.bottom = `${y}px`;
		}
		isJumping = false;
	}

	export function move(key) {
		if (key == "a" && x > 10) {
			x -= 5;
		} else if (key == "d" && x + 120 < window.innerWidth) {
			x += 5;
		}

		self.style.left = `${x}px`;
	}
</script>

<div class="player {other ? 'other' : ''}" bind:this={self} />

<style>
	.player {
		background: url("../player.gif") no-repeat;
		background-size: contain;
		height: 80px;
		width: 110px;
		position: absolute;
		bottom: 60px;
		left: 10px;
		margin: 10px;
		z-index: 3;
		transition: left 66ms linear;
	}

	.other {
		z-index: 1;
		transition: left 0.5s linear;
		opacity: 0.4;
	}
</style>
