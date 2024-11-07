const mongoose = require('mongoose');

const OrderSchema = mongoose.Schema({
    //id ngẫu nhiên của mongo không cần khai báo
    cusId: {
        type: String,
        maxlength: 255
    },
    prodId: {
        type: mongoose.Schema.Types.ObjectId, // Dùng ObjectId để liên kết đến sản phẩm
        ref: 'product', // Tham chiếu đến collection 'product'
        required: true
    },
    revenue: {
        type: Number,
        required: true
    },
    content: {
        type: String,
        maxlength: 255
    },
    orderStatus: {
        type: String,
        maxlength: 255
    },
    orderDate: {
        type: Date
    },

});

const OrderModel = mongoose.model('order', OrderSchema);
module.exports = OrderModel;
