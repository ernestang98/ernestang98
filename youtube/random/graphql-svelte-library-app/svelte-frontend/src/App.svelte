<script>
	import Library from './components/Library.svelte';
	import { ApolloClient, InMemoryCache } from "@apollo/client";
	import { setClient } from "svelte-apollo";
	import Add from './components/Add.svelte';
	const client = new ApolloClient({
		uri: "http://localhost:3000/graphql",
		cache: new InMemoryCache()
	});
	setClient(client);
</script>

<main>
	<h1>Hello World</h1>
	<!-- <Library/> -->
	<Add/>
</main>

<style>
	main {
		text-align: center;
		padding: 1em;
		max-width: 240px;
		margin: 0 auto;
	}

	h1 {
		color: #ff3e00;
		text-transform: uppercase;
		font-size: 4em;
		font-weight: 100;
	}

	@media (min-width: 640px) {
		main {
			max-width: none;
		}
	}
</style>