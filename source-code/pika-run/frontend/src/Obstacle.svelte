<script>
	export let self = undefined;
	export let xPos = -100;

	export function move() {
		if (xPos < window.innerWidth + 20) {
			xPos += 28;
			self.style.right = `${xPos}px`;
		} else {
			xPos = Math.floor(Math.random() * -100 - 300);
		}
	}

	export function collidesWith(playerX, playerY) {
		let obstacle = {
			x: window.innerWidth - xPos - 100,
			y: 60,
			w: 100,
			h: 150,
		};
		let player = { x: playerX, y: playerY, w: 110, h: 80 };

		return (
			obstacle.x < player.x + player.w &&
			obstacle.x + obstacle.w > player.x &&
			obstacle.y < player.y + player.h &&
			obstacle.h + obstacle.y > player.y
		);
	}
</script>

<div bind:this={self} />

<style>
	div {
		width: 100px;
		height: 150px;
		background: url("../obstacle.png") no-repeat;
		background-position: center;
		background-size: 100px 150px;
		position: absolute;
		bottom: 60px;
		right: -100px;

		transition: right 133ms linear;
	}

	@keyframes slide-floor {
		0% {
			transform: translate(1920px);
		}

		100% {
			transform: translate(-200px);
		}
	}
</style>
