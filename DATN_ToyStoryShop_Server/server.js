const express = require('express');
const mongoose = require('mongoose');
const WebSocket = require('ws');
const bodyParser = require("body-parser");
const api = require('./api');

const app = express();
const port = 3000;
const WS_PORT = 8080; // Cổng cho WebSocket server

app.use(bodyParser.json());
app.use(bodyParser.urlencoded({ extended: true }));

app.use ('/api', api);
app.use(express.json());

app.listen(port, () => {
    console.log(`Server dang chay cong ${port}`)
})

const uri = 'mongodb+srv://hoalacanh2508:FnXN4Z9PhHQdRbcv@cluster0.x6cjq.mongodb.net/DATN_ToyStoryShop';

mongoose.connect(uri, {
    useNewUrlParser: true,
    useUnifiedTopology: true,
    connectTimeoutMS: 30000 // Thời gian chờ kết nối là 30 giây
})
.then(() => {
    console.log('Kết nối MongoDB thành công');
    

// Theo dõi thay đổi trong bộ sưu tập MongoDB
const orderCollection = mongoose.connection.collection('orders');
const changeStream = orderCollection.watch();

// Lắng nghe sự kiện thay đổi dữ liệu trong MongoDB
changeStream.on('change', (change) => {
    if (change.operationType === 'update') {
        const updatedFields = change.updateDescription.updatedFields;
        const orderId = change.documentKey._id;
        const orderStatus = updatedFields.orderStatus;

        if (orderStatus) {
            console.log(`Order ID: ${orderId} có trạng thái mới: ${orderStatus}`);

            // Gửi thông báo qua WebSocket
            wss.clients.forEach((client) => {
                if (client.readyState === WebSocket.OPEN) {
                    client.send(JSON.stringify({ orderId, orderStatus }));
                }
            });
        }
    }
});

})
.catch(err => {
    console.error('Lỗi kết nối MongoDB:', err);
});


// Xử lý sự kiện kết nối WebSocket
const wss = new WebSocket.Server({ port: WS_PORT });
// Khởi chạy WebSocket server
console.log(`WebSocket server đang chạy trên cổng ${WS_PORT}`);
wss.on('connection', (ws) => {
    console.log('Client đã kết nối WebSocket');
});

const productModel = require('./model/productModel');



app.get('/', async (req, res)=>{
    await mongoose.connect(uri);

    let products = await productModel.find();

    console.log(products);

    res.send(products);
});

exports.uri = uri;
exports.mongoose = mongoose;
exports.productModel = productModel;