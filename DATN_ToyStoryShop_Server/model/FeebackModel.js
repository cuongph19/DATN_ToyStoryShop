const mongoose = require('mongoose');

const FeebackSchema = mongoose.Schema({
    //id ngẫu nhiên của mongo không cần khai báo
    cusId: {
        type: String,
        maxlength: 255
    },
    orderId: {
        type: mongoose.Schema.Types.ObjectId, // Dùng ObjectId để liên kết đến sản phẩm
        ref: 'order', // Tham chiếu đến collection 'product'
        required: true
    },

    start: {
        type: Number,
        required: true
    },
    content: {
        type: String,
        maxlength: 255
    },
    dateFeed: {
        type: Date
    },

});

const FeebackModel = mongoose.model('feeback', FeebackSchema);
module.exports = FeebackModel;
