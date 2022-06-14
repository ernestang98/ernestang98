#!/usr/local/bin/node
var express = require('express');
var app = express();

app.listen(process.env.PORT || 12345);

app.get('/person', function (req, res) { res.json({name:'Mario',surname:'Ferraro'})});
