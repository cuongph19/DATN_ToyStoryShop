const mongoose = require('mongoose');

const ProductSchema = mongoose.Schema({
    prodId: {
        type: Number,
        required: true
    },
    owerId: {
        type: Number,
        required: true
    },
    statusPro: {
        type: Boolean
    },
    price: {
        type: Number,
        required: true
    },
    desPro: {
        type: String,
        maxlength: 255
    },
    creatDatePro: {
        type: Date
    },
    quantity: {
        type: Number,
        required: true
    },
    listPro: {
        type: String,
        maxlength: 255
    },
    imgPro: {
        type: [String],
        maxlength: 255
    },
    namePro: {
        type: String,
        maxlength: 255
    },
    cateId: {
        type: Number,
        required: true
    }
});

const ProductModel = mongoose.model('product', ProductSchema);

module.exports = ProductModel;
