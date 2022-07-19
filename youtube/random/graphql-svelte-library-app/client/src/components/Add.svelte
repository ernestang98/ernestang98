<script>
	import { getClient, query } from "svelte-apollo";
	import { GET_ALL_BOOKS_IN_LIBRARY, GET_ALL_AUTHORS_IN_LIBRARY, ADD_BOOK_TO_LIBRARY, GET_BOOK_FROM_LIBRARY } from "../graphql/queries";
	import { writable } from 'svelte/store';
	const client = getClient()
	const getAllAuthors = client.query({
		query: GET_ALL_AUTHORS_IN_LIBRARY
	})
	let selectedGenre;
	let bookToAdd;
	let listofGenres = [
		{ id: 1, text: `Sci-Fi` },
		{ id: 2, text: `Romance` },
		{ id: 3, text: `Fantasy` }
	];
	export const bookStore = writable({
		name: "Change me pls...",
		author: "YOU SHOULD NOT SEE ME",
		genre: selectedGenre,
	})
	bookStore.subscribe(value => {
		bookToAdd = value;
	});

	const hotfix = (args) => {
		const hothotfix = bookToAdd
		hothotfix.author = args[0].id
	}

	let library;
	const libraryStore = query(GET_ALL_BOOKS_IN_LIBRARY);

	libraryStore.subscribe(dataFromLibrary => {
		library = dataFromLibrary
	})

	async function handleClick() {
		try {
			await client.mutate({
				mutation: ADD_BOOK_TO_LIBRARY(bookToAdd.name, parseFloat(bookToAdd.author), bookToAdd.genre),
			})
			libraryStore.refetch()
		} catch (e) {
			alert("ERROR: " + JSON.stringify(e.graphQLErrors[0].message))
		}
	}

	let viewBook;

	async function handleViewBookClick(args) {
		try {
			const res = await client.query({
				query: GET_BOOK_FROM_LIBRARY(args.id),
			})
			setTimeout(() => {
				viewBook = res;
			}, 10)
		} catch (e) {
			alert("ERROR: " + JSON.stringify(e.graphQLErrors[0].message))
		}
	}
</script>

<main>
	<h1>I am the library that has refetch!</h1>
	{#if library === undefined}
		Loading...
	
	{:else if library.loading}
		Loading...
	
	{:else if library.data}
		<ul>
			{#each library.data.bookStore as data_point, i}
			<li on:click={handleViewBookClick(data_point)} class="ignore">
				{
					JSON.stringify(data_point)
				}
			</li>
			{/each}
		</ul>
		
	{:else if library.error}
		{JSON.stringify(library.error)}
	
	{/if}
</main>

{#if viewBook !== undefined}
	<main>
		<h1>VIEW A BOOK</h1>
		<p>Name: {viewBook.data.book.name}</p>
		<p>Author: {viewBook.data.book.writtenBy.name}</p>
		<p>Books also written by the same author:</p>
		<ul>
			{#each viewBook.data.book.writtenBy.booksWritten as data_point, i}
			<li>
				{
					data_point.name
				}
			</li>
			{/each}
		</ul>
	</main>
{/if}

<main>
	<h1>ADD BOOK TO LIBRARY RIGHT NOW</h1>
	<form class="content">
		<label for="name">Name</label>
		<input id="name" type="text" bind:value={$bookStore.name} />
		<label for="author">Author</label>
		<select id="author" bind:value={$bookStore.author}>
			{#await getAllAuthors}
			Loading...
		
			{:then result}	
					{#each result.data.shakespeareFavorites as data_point, i}
					<option value={data_point.id}>
						{data_point.name}
					</option>
					{/each}
					{
						hotfix(result.data.shakespeareFavorites)
					}
				
			{:catch error}
				{JSON.stringify(error)}
			
			{/await}
		</select>
		<label for="genre">Genre</label>
		<select id="genre" bind:value={$bookStore.genre}>
			{#each listofGenres as genre}
				<option value={genre.text}>
					{genre.text}
				</option>
			{/each}
		</select>
	</form>
	<button on:click={handleClick}>Click me</button>
</main>

<style>
	.content {
		display: grid;
		grid-template-columns: 20% 80%;
		grid-column-gap: 10px;
	}
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

	li.ignore {
		cursor: pointer;
	}

	li.ignore:hover {
		color: blue;
	}
</style>