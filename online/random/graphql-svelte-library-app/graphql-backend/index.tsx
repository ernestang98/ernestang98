import 'dotenv/config'
import express, { Express, Request, Response } from "express";
import { graphqlHTTP } from "express-graphql";
import schema from "./schema/schema";
import mongoose, { ConnectOptions } from 'mongoose';
import cors from "cors";

const app: Express = express();
const port = process.env.PORT || 3000;

mongoose.connect(process.env.MONGO_URI || "mongodb://localhost:27017/test?authSource=admin&ssl=false", {
    useNewUrlParser: true,
    useUnifiedTopology: true
} as ConnectOptions, (err) => {
    if (err) {
        console.log('Unable to connect to the MongoDB. Error:', err);
    } else {
        console.log('Connected to MongoDB successfully!');
    }
})

app.use(cors<Request>());

app.get('/', (req: Request, res: Response) => {
    res.send('Express + TypeScript Server');
});

app.use('/graphql', graphqlHTTP({
    schema: schema,
    graphiql: true
}));

app.listen(port, () => {
    console.log(`⚡️ Server is running at https://localhost:${port}`);
});