const mongoose = require('mongoose');

const FavoriteSchema = mongoose.Schema({
    favId: {
        type: Number,
        required: true
    },
    cusId: {
        type: Number,
        required: true
    },
    prodId: {
        type: Number,
        required: true
    },
    imgFavPl:{
        type: [String],
        maxlength: 255
    },
});

const FavoriteModel = mongoose.model('favorite', FavoriteSchema);

module.exports = FavoriteModel;
