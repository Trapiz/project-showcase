<script context="module">
	// Note the `context="module"`, meaning this script runs only once for this component,
	// instead of for each instance. The functions we create will be static.
	import { writable } from "svelte/store";

	// We'll use a global store to contain the details for the one modal that can be open at
	// any time. It either contains `undefined` (there's currently no modal) or an objects
	// containing a `component` (the class for a component that will be used to render the
	// inside of the modal) and `props` (the properties to pass to that component).
	const store = writable();

	export function openModal(component, props = {}) {
		store.set({ component, props });
	}

	export function closeModal() {
		store.set();
	}
</script>

{#if $store}
	<!-- The modal window to create a new list item: -->
	<!-- svelte-ignore a11y-click-events-have-key-events -->
	<div class="modalBg" on:click|self={closeModal}>
		<div class="modal">
			<svelte:component this={$store.component} {...$store.props} />
		</div>
	</div>
{/if}

<style>
	.modalBg {
		position: fixed;
		left: 0;
		top: 0;
		width: 100%;
		height: 100%;
		background-color: rgba(0, 0, 0, 0.5);
		display: flex;
		justify-content: center;
		align-items: center;
	}

	.modal {
		max-width: 800px;
		border-radius: 5px;
		background: linear-gradient(rgba(0, 0, 0, 0.25), rgba(0, 0, 0, 0.25)),
			url("../bricks.png") repeat;
		background-size: contain;
		border-radius: 5px;
		border: 3px solid white;
		padding: 15px;
		width: 80%;
		overflow-y: auto;
		max-height: 600px;
	}
</style>
