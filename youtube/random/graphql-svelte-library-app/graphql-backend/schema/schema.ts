import { GraphQLObjectType, GraphQLString, GraphQLInt, GraphQLID, GraphQLSchema, GraphQLList, GraphQLFloat, GraphQLNonNull } from "graphql";
import _ from "lodash";
import author from "../models/author";
import book from "../models/book";
// import {BOOKS, AUTHORS} from "../utils/Constants"; // DATA FROM CONSTANTS.TS

const BookType: GraphQLObjectType = new GraphQLObjectType<any, any>({
    name: 'Book',
    fields: () => ({
        // id: { type : GraphQLInt  }, // DATA FROM CONSTANTS.TS
        id: { type : GraphQLFloat  },
        name: { type : GraphQLString },
        genre: { type : GraphQLString },
        writtenBy: {
            type: AuthorType,
            resolve: (eachBook, args) => {
                // return _.find(AUTHORS, {id: parent.authorId}) // DATA FROM CONSTANTS.TS
                return author.find({id: eachBook.authorId}).then((res)=> {
                    return res[0]
                })
            }
        }
    })
});

const AuthorType: GraphQLObjectType = new GraphQLObjectType<any, any>({
    name: 'Author',
    fields: () => ({
        id: { type : GraphQLID  },
        name: { type : GraphQLString },
        age: { type : GraphQLInt },
        booksWritten: {
            type : new GraphQLList(BookType),
            resolve: (eachAuthor, args) => {
                // return _.filter(BOOKS, { authorId: parent.id }) // DATA FROM CONSTANTS.TS
                return book.find({authorId: eachAuthor.id}).then((res)=> {
                    return res
                })
            }
        }
    })
});

const RootQueryType: GraphQLObjectType = new GraphQLObjectType<any, any>({
   name: "RootQuery",
   fields: {
       book: {
           type: BookType,
           args: {
                // id: { type : GraphQLInt  }, // DATA FROM CONSTANTS.TS
                id: { type : GraphQLFloat  }
           },
           resolve(parent, args) {
               // return _.find(BOOKS, {id: args.id}) // DATA FROM CONSTANTS.TS
                return book.find({id: args.id}).then(res=> {
                    return res[0]
                })
           }
       },
       author: {
            type: AuthorType,
            args: {
                id: {type: GraphQLID}
            },
            resolve(parent, args) {
                //return _.find(AUTHORS, {id: args.id}) // DATA FROM CONSTANTS.TS
                return author.find({id: args.id}).then(res=> {
                    return res[0]
                })
            }
       },
       bookStore: {
            type: new GraphQLList(BookType),
            resolve(parent, args) {
                // return BOOKS
                return book.find({})
            }
       },
       shakespeareFavorites: {
            type: new GraphQLList(AuthorType),
            resolve(parent, args) {
                //return AUTHORS
                return author.find({})
            }
       }
   }
});

const Mutation: GraphQLObjectType = new GraphQLObjectType<any, any>({
    name: 'Mutation',
    fields: {
        addAuthor: {
            type: AuthorType,
            args: {
                name: { type: new GraphQLNonNull(GraphQLString) },
                age: { type: new GraphQLNonNull(GraphQLInt) }
            },
            resolve(parent, args) {
                let id = Date.now() // dirty way of randomly generating id https://dev.to/rahmanfadhil/how-to-generate-unique-id-in-javascript-1b13
                let newAuthorToCreate = new author({
                    name: args.name, 
                    age: args.age, 
                    id
                })
                return newAuthorToCreate.save()    
            }
        },
        addBook: {
            type: BookType,
            args: {
                name: { type: new GraphQLNonNull(GraphQLString) },
                writtenBy: { type: new GraphQLNonNull(GraphQLID) },
                genre: { type: new GraphQLNonNull(GraphQLString) },
            },
            resolve(parent, args) {
                let id = Date.now() // dirty way of randomly generating id https://dev.to/rahmanfadhil/how-to-generate-unique-id-in-javascript-1b13
                let newBookToCreate = new book({
                    name: args.name, 
                    authorId: args.writtenBy, 
                    genre: args.genre,
                    id
                })
                return newBookToCreate.save()    
            }
        }
    }
 });

export = new GraphQLSchema({
    query: RootQueryType,
    mutation: Mutation
})