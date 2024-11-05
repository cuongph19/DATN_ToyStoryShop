const express = require('express');

const app = express();

const port = 3000;

app.use(express.json());
//////////
const bodyParser = require("body-parser");
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({ extended: true }));


app.listen(port, () => {
    console.log(`Server dang chay cong ${port}`)
})

const api = require('./api');
app.use ('/api', api);

const uri = 'mongodb://localhost:27017/DATN_ToyStoryShop';

const productModel = require('./productModel');
const mongoose = require('mongoose');

app.get('/', async (req, res)=>{
    await mongoose.connect(uri);

    let products = await productModel.find();

    console.log(products);

    res.send(products);
})
exports.uri = uri;
exports.mongoose = mongoose;
exports.productModel = productModel;