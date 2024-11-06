const mongoose = require('mongoose');

// Khai báo schema cho Cart
const CartSchema = mongoose.Schema({
        //id ngẫu nhiên của mongo không cần khai báo

    prodId: {
        type: mongoose.Schema.Types.ObjectId, // Dùng ObjectId để liên kết đến sản phẩm
        ref: 'product', // Tham chiếu đến collection 'product'
        required: true
    },
    quantity: {
        type: Number,
        required: true
    },
    cusId: {
        type: String, // cusId ở đây là ID của khách hàng trong Firestore (ví dụ '8iPTPiB47jBO0EKMkn7K')
        required: true
    }
});

// Khởi tạo model cho Cart
const CartModel = mongoose.model('cart', CartSchema);

module.exports = CartModel;
