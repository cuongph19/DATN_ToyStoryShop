const mongoose = require('mongoose');

const refundSchema = new mongoose.Schema({

    orderId: {
        type: mongoose.Schema.Types.ObjectId,
        required: true,
        ref: 'order' // Tham chiếu đến bảng Order (nếu có)
    },
    cusId: {
        type: String,
        required: true,
        maxlength: 1000
    },
    content: {
        type: String,
        required: true,
        maxlength: 1000
    },
    orderRefundDate: {
        type: Date
    },
    refundStatus: {
        type: String,
        enum: [
            'Chờ xác nhận',
            'Hủy hoàn hàng',
            'Đã xác nhận',
            'Đã nhận hàng hoàn'
        ],
        required: true
    }
});

const RefunModel = mongoose.model('refund', refundSchema);

module.exports = RefunModel;
