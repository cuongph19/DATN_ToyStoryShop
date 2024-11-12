const mongoose = require('mongoose');

// Khai báo schema cho Voucher
const VoucherSchema = mongoose.Schema({
        //id ngẫu nhiên của mongo không cần khai báo
    price_reduced: {
        type: Number,
        required: true
    },
    discount_code: {
        type: Number,
        required: true
    },
    quantity_voucher : {
        type: String,
        enum: ['giảm giá vận chuyển', 'giảm giá sản phẩm'],
        required: true
    },
});

// Khởi tạo model cho Cart
const VoucherModel = mongoose.model('voucher', VoucherSchema);

module.exports = VoucherModel;
