const express = require('express');
const path = require('path');
const cors = require('cors');
const app = express();
const port = 5500;

app.set('view engine', 'ejs');
app.set('views', path.join(__dirname, 'views'));
app.use(express.static(path.join(__dirname, 'public')));
// app.use(cors());

// // Middleware for parsing incoming requests
// app.use(express.json());
// app.use(express.urlencoded({ extended: true }));

app.listen(port, () => {
    console.log('Server started');
});

app.get('/', (req, res) => {
    res.render('index');
});