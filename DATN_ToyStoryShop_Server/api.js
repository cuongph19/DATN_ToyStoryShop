const express = require('express');

const router = express.Router();
const mongoose = require('mongoose'); // Đảm bảo rằng dòng này có ở đây
module.exports = router;

const server = require('./server');

router.get('/', (req, res) => {
    res.send('URI:' + app.uri);
});

router.get('/list', async (req, res) => {
    await server.mongoose.connect(server.uri);

    try {
        let products = await server.productModel.find(); // Lấy tất cả sản phẩm
        console.log(products);
        res.send(products);
    } catch (error) {
        console.error("Lỗi khi lấy sản phẩm:", error);
        res.status(500).send({ error: "Có lỗi xảy ra khi lấy sản phẩm." });
    }
});
router.get('/brand-counts', async (req, res) => {
    try {
        await mongoose.connect(server.uri);

        // Các thương hiệu bạn muốn đếm
        const brands = ["BANPRESTO", "POP MART", "FUNISM"];

        const counts = await Promise.all(
            brands.map(async (brand) => {
                // Sử dụng $regex để tìm kiếm không phân biệt dấu cách và dấu câu
                const count = await server.productModel.countDocuments({
                    brand: { $regex: brand.trim(), $options: 'i' } // Sử dụng brand.trim() để loại bỏ khoảng trắng
                });

                return { brand, count };
            })
        );

        res.json(counts);
    } catch (error) {
        console.error("Lỗi khi lấy số lượng sản phẩm theo thương hiệu:", error);
        res.status(500).json({ error: "Có lỗi xảy ra khi lấy số lượng sản phẩm." });
    }
});




router.get('/new-arrivals', async (req, res) => {
    try {
        await mongoose.connect(server.uri);
        const newProducts = await server.productModel.find({ listPro: { $regex: "^NEW_ARRIVALS$", $options: "i" } });

        if (newProducts.length === 0) {
            return res.status(404).json({ error: 'Không tìm thấy sản phẩm mới nhập.' });
        }

        // Xử lý tên sản phẩm không dấu
        newProducts.forEach(product => {
            product.namePro = removeDiacritics(product.namePro);
        });

        res.json(newProducts);
    } catch (error) {
        console.error('Lỗi khi lấy sản phẩm:', error);
        res.status(500).json({ error: 'Có lỗi xảy ra khi lấy sản phẩm.', details: error.message });
    }
});

router.get('/limited', async (req, res) => {
    try {
        await mongoose.connect(server.uri);
        const newProducts = await server.productModel.find({ listPro: { $regex: "^LIMITED_FIGURE$", $options: "i" } });

        if (newProducts.length === 0) {
            return res.status(404).json({ error: 'Không tìm thấy sản phẩm mới nhập.' });
        }

        newProducts.forEach(product => {
            product.namePro = removeDiacritics(product.namePro);
        });

        res.json(newProducts);
    } catch (error) {
        console.error('Lỗi khi lấy sản phẩm:', error);
        res.status(500).json({ error: 'Có lỗi xảy ra khi lấy sản phẩm.', details: error.message });
    }
});

router.get('/figuring', async (req, res) => {
    try {
        await mongoose.connect(server.uri);
        const newProducts = await server.productModel.find({ listPro: { $regex: "^FIGURING$", $options: "i" } });

        if (newProducts.length === 0) {
            return res.status(404).json({ error: 'Không tìm thấy sản phẩm mới nhập.' });
        }

        newProducts.forEach(product => {
            product.namePro = removeDiacritics(product.namePro);
        });

        res.json(newProducts);
    } catch (error) {
        console.error('Lỗi khi lấy sản phẩm:', error);
        res.status(500).json({ error: 'Có lỗi xảy ra khi lấy sản phẩm.', details: error.message });
    }
});

router.get('/other', async (req, res) => {
    try {
        await mongoose.connect(server.uri);
        const newProducts = await server.productModel.find({ listPro: { $regex: "^OTHER_PRODUCTS$", $options: "i" } });

        if (newProducts.length === 0) {
            return res.status(404).json({ error: 'Không tìm thấy sản phẩm mới nhập.' });
        }

        newProducts.forEach(product => {
            product.namePro = removeDiacritics(product.namePro);
        });

        res.json(newProducts);
    } catch (error) {
        console.error('Lỗi khi lấy sản phẩm:', error);
        res.status(500).json({ error: 'Có lỗi xảy ra khi lấy sản phẩm.', details: error.message });
    }
});

router.get('/blind_box', async (req, res) => {
    try {
        await mongoose.connect(server.uri);
        const newProducts = await server.productModel.find({ listPro: { $regex: "^BLIND_BOX$", $options: "i" } });

        if (newProducts.length === 0) {
            return res.status(404).json({ error: 'Không tìm thấy sản phẩm mới nhập.' });
        }

        newProducts.forEach(product => {
            product.namePro = removeDiacritics(product.namePro);
        });

        res.json(newProducts);
    } catch (error) {
        console.error('Lỗi khi lấy sản phẩm:', error);
        res.status(500).json({ error: 'Có lỗi xảy ra khi lấy sản phẩm.', details: error.message });
    }
});

// Hàm chuyển đổi chuỗi có dấu thành không dấu
function removeDiacritics(input) {
    const normalized = input.normalize("NFD");
    return normalized.replace(/[\u0300-\u036f]/g, "");
}