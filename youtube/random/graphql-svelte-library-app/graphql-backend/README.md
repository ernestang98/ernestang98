{
    book(id: 1) {
        name,
        writtenBy {
            name
        },
        id
    }
}
{
    author(id: 1) {
        name,
        age,
        id
    }
}
{
    author(id: 4) {
        name,
        age,
        id,
        booksWritten {
            name
        }
    }
}
{
    bookStore {
        name
        writtenBy {
            name
        }
    }
}
{
    shakespeareFavorites {
        name
        booksWritten {
            name
        }
    }
}
mutation {
  addAuthor(name: "Boelyn Choo", age: 20) {
    name
  }
}
mutation {
  addBook(name: "Lord of the rings", writtenBy: "1658111219438", genre: "Fantasy") {
    name
    genre
  }
}
{
    book(id: 1658111630742) {
        name,
        writtenBy {
            name
        }
    }
}
{
    author(id: 1658111219438) {
        name,
        age,
        id,
        booksWritten {
            name
        }
    }
}