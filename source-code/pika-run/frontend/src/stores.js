import { derived, writable } from "svelte/store";

export const isLoggedIn = writable(false);
export const player = writable({});
export const hasStarted = writable(false);
export const socket = writable(new WebSocket("ws://localhost:9000"))
export const players = writable({})

