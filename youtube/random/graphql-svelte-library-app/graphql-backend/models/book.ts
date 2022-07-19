import mongoose, { Schema, Document } from 'mongoose';

export enum Genre {
    fantansy = 'Fantasy',
    romance = 'Romance',
    scifi = 'Sci-Fi'
}  

const BookSchema: Schema = new Schema({
    name: { type: String, required: true, unique: true },
    id: { type: Number, required: true },
    authorId: { type: String, required: true },
    genre: { type: String, enum: Object.values(Genre), required: true },
});
  
export interface IBook extends Document {
    name: string;
    id: string;
    authorId: string;
    genre: Genre;
}
export default mongoose.model<IBook>('Book', BookSchema);