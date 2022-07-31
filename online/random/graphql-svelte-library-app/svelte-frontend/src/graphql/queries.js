import { gql } from "@apollo/client";

const GET_ALL_BOOKS_IN_LIBRARY = gql`
query {
    bookStore {
        id
        name
        writtenBy {
            name
        }
    }
}`;

const GET_ALL_AUTHORS_IN_LIBRARY = gql`
query {
    shakespeareFavorites {
        name
        id
        booksWritten {
            name
        }
    }
}`;

const ADD_BOOK_TO_LIBRARY = (name, author, genre) => {
    return gql`
    mutation {
        addBook(name: "${name}", writtenBy: ${author}, genre: "${genre}") {
            name
            genre
        }
    }`;
}

const GET_BOOK_FROM_LIBRARY = (id) => {
    return gql`
    query {
        book(id: ${id}) {
            name,
            writtenBy {
                name
                booksWritten {
                    name
                }
            },
            id
        }
    }
    `;
}

export {
    GET_ALL_BOOKS_IN_LIBRARY,
    GET_ALL_AUTHORS_IN_LIBRARY,
    GET_BOOK_FROM_LIBRARY,
    ADD_BOOK_TO_LIBRARY
}