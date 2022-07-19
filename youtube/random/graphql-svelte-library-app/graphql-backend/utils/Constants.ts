export const BOOKS: Object[] = [
    { name: "Lord of The Rings", genre: "Fantasy", id: 1, authorId: "1" },
    { name: "Harry Potter and the Deathly Hallows Part 1", genre: "Fantasy", id: 2, authorId: "2" },
    { name: "Beauty and the Beast", genre: "Romance", id: 3, authorId: "2" },
    { name: "Avengers 3: End Game", genre: "Sci-Fi", id: 4, authorId: "3" }
];

// For fields that are GraphQLID types, the actual type of the field of every data point must be in strings (i.e. id)
export const AUTHORS: Object[] = [
    { name: "John Doe", age: 44, id: "1" }, 
    { name: "Lim Bo Seng", age: 35, id: "2" },
    { name: "Shinigami Ryuk", age: 100, id: "3" },
    { name: "Dara Lew", age: 20, id: "4" }
];