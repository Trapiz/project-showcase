<script>
	import { isLoggedIn, players, socket } from "./stores.js";
	import GameOver from "./GameOver.svelte";
	import Obstacle from "./Obstacle.svelte";
	import Player from "./Player.svelte";
	import Information from "./Information.svelte";
	import { hasStarted, player } from "./stores";
	import { closeModal } from "./Modals/Modal.svelte";
	import { api } from "./utils.js";

	$socket.onmessage = async ({ data }) => {
		if (data != []) {
			data = JSON.parse(data);
			if (data.type === "jump") {
				$players[data.player].player.jump();
			} else if (data.type === "move") {
				$players[data.player].player.moveTo(data.x);
			} else if (data.type === "dead") {
				console.log(data.player + " is dead");
				delete $players[data.player];
				$players = { ...$players };
			} else if (data.type === "start") {
				$players = {
					...$players,
					[data.player]: await api(`/players/${data.player}`),
				};
			}
		}
	};

	let disableObstacles = false;

	let mainPlayer;

	let playerIsJumping;
	let playerX;
	let playerY;

	let obstacles = [];
	let obstacle1, obstacle2, obstacle3, obstacle4;
	$: obstacles = [obstacle1, obstacle2, obstacle3, obstacle4];

	let pointMultiplier;
	let points = 0;
	let timeSpent = 0;

	let isDead = false;

	$: pointMultiplier = 0.3 + playerX / 10000;

	let frameCount = 0;

	async function gameLoop() {
		frameCount += 1;

		if (frameCount % 4 == 0) {
			if ($isLoggedIn) {
				$socket.send(
					JSON.stringify({ type: "move", id: $player.id, x: playerX })
				);
			}
		}

		if (frameCount % 8 == 0) {
			timeSpent += 1;
		}

		for (const obstacle of obstacles) {
			if (obstacle) {
				obstacle.move();
				if (obstacle.collidesWith(playerX, playerY)) {
					hasStarted.set(false);
					isDead = true;
					if ($isLoggedIn) {
						$socket.send(
							JSON.stringify({ type: "dead", player: $player.id })
						);
					}
					setTimeout(() => {
						timeSpent = 0;
						points = 0;
						playerX = 10;
						playerY = 60;
					}, 100);
					return;
				}
			}
		}

		points += 1 * pointMultiplier || 0;
		setTimeout(gameLoop, 1000 / 7.5);
	}

	let self;
	window.onkeydown = handleKeys;

	function handleKeys(e) {
		if ($hasStarted) {
			if ((e.key == " " || e.key == "w") && !playerIsJumping) {
				if ($isLoggedIn) {
					$socket.send(
						JSON.stringify({ type: "jump", player: $player.id })
					);
				}
				mainPlayer.jump();
			}
			if (["a", "d"].includes(e.key)) {
				mainPlayer.move(e.key);
			}
		} else {
			if (e.key == " ") {
				closeModal();
				gameLoop();
				hasStarted.set(true);
				isDead = false;
				if ($isLoggedIn) {
					$socket.send(
						JSON.stringify({ type: "start", player: $player.id })
					);
				}
			}
		}
	}
</script>

{#each Object.entries($players) as [_, p]}
	<Player other={true} bind:this={p.player} />
{/each}

{#if !$hasStarted}
	<h1>Press space to start</h1>
	{#if isDead}
		<GameOver {points} time={timeSpent} />
	{/if}
{:else}
	<!-- svelte-ignore a11y-click-events-have-key-events -->
	<!-- svelte-ignore a11y-no-noninteractive-tabindex -->
	<div class="game" tabindex="0" bind:this={self}>
		<Information bind:points bind:seconds={timeSpent} />
		<Player
			bind:x={playerX}
			bind:y={playerY}
			bind:this={mainPlayer}
			bind:isJumping={playerIsJumping}
			other={false}
		/>

		{#if !disableObstacles}
			<Obstacle xPos={-100} bind:this={obstacle1} />
			<Obstacle xPos={-800} bind:this={obstacle2} />
			<Obstacle xPos={-1300} bind:this={obstacle3} />
			<Obstacle xPos={-2000} bind:this={obstacle4} />
		{/if}
	</div>
{/if}

<style>
	h1 {
		width: 100%;
		text-align: center;
		color: white;
		margin-top: 100px;
		font-family: pixel-font;
		font-size: 50px;
		font-weight: 300;
	}
</style>
