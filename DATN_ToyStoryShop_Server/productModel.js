const mongoose = require('mongoose');

const ProductSchema = mongoose.Schema({

    ten: {
        type: String,
        required: true
    },

    gia: {
        type: Number,
        required: true
    },

    soluong: {
        type: Number,
        required: true
    },

    tonkho: {
        type: Boolean
    },

});

const ProductModel = mongoose.model('product', ProductSchema);

module.exports = ProductModel;

