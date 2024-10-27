
const express = require('express');

const app = express();

const port = 3000;

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

// app.post('/add_sp', async (req, res) => {
//     await mongoose.connect(uri);

//     // let product = {
//     //     ten: 'Sanpham 4',
//     //     gia: 500,
//     //     soluong: 10,
//     //     tonkho: false
//     // }

//     let product = req.body;

//     let kq = await productModel.create(product);

//     console.log(kq);

//     let products = await productModel.find();

//     res.send(products);
// })

app.get('/xoa/:id', async(req, res) => {
    await mongoose.connect(uri);

    let id = req.params.id;
    let kq = await productModel.deleteOne({_id: id});

    console.log(kq);

    res.redirect('../')
})

// app.get('/update/:id', async (req, res) => {

//     await mongoose.connect(uri);

//     console.log('Ket noi DB thanh cong');

//     let id = req.params.id;

//     let tenSPMoi = 'San pham phien ban moi 2024';

//     await productModel.updateOne({_id: id}, {ten: tenSPMoi});

//     let products = await productModel.find({});

//     res.send(products);
// }) 

// module.exports = {
//     uri: uri,
// }

exports.uri = uri;
exports.mongoose = mongoose;
exports.productModel = productModel;




