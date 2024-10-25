const mongoose = require('mongoose');

const SanphamSchema = mongoose.Schema({
    MaSP: {
        type: Number,
        required: true
    },
    MaCCH: {
        type: Number,
        required: true
    },
    TrangThai: {
        type: Boolean
    },
    GiaSP: {
        type: Number,
        required: true
    },
    MoTaSP: {
        type: String,
        maxlength: 255
    },
    NgayTaoSP: {
        type: Date
    },
    SoLuongTon: {
        type: Number
    },
    DanhMuc: {
        type: String,
        maxlength: 255
    },
    HinhAnh: {
        type: String,
        maxlength: 255
    },
    TenSP: {
        type: String,
        maxlength: 255,
        required: true
    },
    MaTL: {
        type: Number,
        required: true
    }
});

const SanphamModel = mongoose.model('SanPham', SanphamSchema);

module.exports = SanphamModel;
