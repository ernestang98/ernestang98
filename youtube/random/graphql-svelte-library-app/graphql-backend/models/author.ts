import mongoose, { Schema, Document } from 'mongoose';

const AuthorSchema: Schema = new Schema({
    name: { type: String, required: true, unique: true },
    age: { type: Number, required: true },
    id: { type: String, required: true },
});
  
export interface IAuthor extends Document {
    name: string;
    age: number;
    id: string;
}

export default mongoose.model<IAuthor>('Author', AuthorSchema);