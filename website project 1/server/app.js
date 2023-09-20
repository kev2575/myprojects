const express = require('express');
//const connectDB = require('./config/db');
const mongoose = require('mongoose');
//const router = require('./routes/routes');
const cors = require('cors');
const app = express();
const items = require('./routes/api/items');
const users = require('./routes/api/users');

// Connect Database
//connectDB();

app.use(cors({ origin: true, credentials: true }));

app.use(express.json({ extended: false }));

//
app.use('/api/items', items);
app.use('/api/users', users);

app.get('/', (req, res) => res.send('<h2>Hello Web Programmers!</h2>'));

const port = process.env.PORT || 8082;
const conn_str = "mongodb+srv://bcj33435:kQKlwN1PuWnJIsya@cluster0.fmafctw.mongodb.net/?retryWrites=true&w=majority";

mongoose.set('strictQuery', false);

mongoose.connect(conn_str,{
    useUnifiedTopology : true,
    useNewUrlParser : true
})
.then(() => {
    app.listen(port)
    console.log(`MongoDB Connection Suceeded...`)
 })
 .catch(err => {
    console.log(`Error in DB Connection ${err}`);
});


// const connectDB = async () => {
//     try {
//         mongoose.set('strictQuery', true);
//         await mongoose.connect(conn_str, {
//             useNewUrlParser: true,
//         });
//         console.log('MongoDB is connected...');
//     } catch (err) {
//         console.error(err.message);
//         process.exit(1);
//     }
// }

//app.listen(port, () => console.log(`Server running on port ${port}`));


