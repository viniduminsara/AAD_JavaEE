const express = require('express');
const path = require('path');
const app = express();
const port = 5500;

app.set('view engine', 'ejs');
app.set('views', path.join(__dirname, 'views'));
app.use(express.static(path.join(__dirname, 'public')));

app.listen(port, () => {
    console.log('Server started');
});

app.get('/', (req, res) => {
    res.render('index');
});