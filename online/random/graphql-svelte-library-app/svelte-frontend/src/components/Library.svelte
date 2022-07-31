<script>
	import { getClient } from "svelte-apollo";
	import { GET_ALL_BOOKS_IN_LIBRARY } from "../graphql/queries";
	const client = getClient()
	const getAllAuthors = client.query({
		query: GET_ALL_BOOKS_IN_LIBRARY
	})
</script>

<main>
	<h1>I am the library!</h1>
	{#await getAllAuthors}
		Loading...
	
	{:then result}	
		<ul>
			{#each result.data.bookStore as data_point, i}
			<li>
				{
					JSON.stringify(data_point)
				}
			</li>
			{/each}
		</ul>
		
	{:catch error}
		{JSON.stringify(error)}
	
	{/await}
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