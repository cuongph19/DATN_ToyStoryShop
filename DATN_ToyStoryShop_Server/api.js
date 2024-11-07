const express = require('express');

const router = express.Router();
const mongoose = require('mongoose'); // Đảm bảo rằng dòng này có ở đây
const FavoriteModel = require('./model/FavoriteModel');
const FeebackModel = require('./model/FeebackModel');

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

router.get('/list-popular', async (req, res) => {
    try {
        await mongoose.connect(server.uri);

        // Lọc sản phẩm có listPro là BLIND_BOX hoặc FIGURING
        const popularProducts = await server.productModel.find({
            listPro: { $in: ["BLIND_BOX", "FIGURING"] }
        });

        if (popularProducts.length === 0) {
            return res.status(404).json({ error: 'Không tìm thấy sản phẩm phổ biến.' });
        }

        // Xử lý tên sản phẩm không dấu
        popularProducts.forEach(product => {
            product.namePro = removeDiacritics(product.namePro);
        });

        res.json(popularProducts);
    } catch (error) {
        console.error('Lỗi khi lấy sản phẩm phổ biến:', error);
        res.status(500).json({ error: 'Có lỗi xảy ra khi lấy sản phẩm phổ biến.', details: error.message });
    }
});

// Hàm chuyển đổi chuỗi có dấu thành không dấu
function removeDiacritics(input) {
    const normalized = input.normalize("NFD");
    return normalized.replace(/[\u0300-\u036f]/g, "");
};

// API thêm sản phẩm vào favorites
router.post('/add/add-to-favorites', async (req, res) => {
    console.log(req.body); // Xem dữ liệu nhận được trong body

    try {
        const { prodId, cusId } = req.body;
        const newFavorite = new FavoriteModel({ prodId, cusId });
        await newFavorite.save();
        res.status(201).json({ message: 'Thêm vào yêu thích thành công!', data: newFavorite });
    } catch (error) {
        console.error('Lỗi chi tiết:', error);
        res.status(500).json({ message: 'Lỗi khi thêm vào yêu thích', error });
    }
});
// API xóa sản phẩm vào favorites
router.delete('/delete/:id', async (req, res) => {
    const { id } = req.params; // Nhận _id từ đường dẫn

    try {
        await mongoose.connect(server.uri);

        // Xóa sản phẩm trong collection 'favorites' dựa trên _id
        const result = await FavoriteModel.deleteOne({ prodId: id });
        if (result.deletedCount === 0) {
            return res.status(404).json({ error: 'Không tìm thấy sản phẩm yêu thích với ID đã cho.' });
        }

        res.status(200).json({ message: 'Sản phẩm yêu thích đã được xóa thành công.' });
    } catch (error) {
        console.error('Lỗi khi xóa sản phẩm yêu thích:', error);
        res.status(500).json({ error: 'Có lỗi xảy ra khi xóa sản phẩm yêu thích.' });
    } finally {
        mongoose.connection.close(); // Đảm bảo kết nối được đóng
    }
});


router.get('/favorites', async (req, res) => {
    try {
        await mongoose.connect(server.uri);

        // Tìm tất cả các sản phẩm trong collection 'favorites'
        const favorites = await FavoriteModel.find({}, '_id prodId cusId ');

        if (favorites.length === 0) {
            return res.status(404).json({ error: 'Không có sản phẩm yêu thích nào.' });
        }

        res.json(favorites);
    } catch (error) {
        console.error('Lỗi khi lấy sản phẩm yêu thích:', error);
        res.status(500).json({ error: 'Có lỗi xảy ra khi lấy sản phẩm yêu thích.' });
    }
});
router.get('/:prodId', async (req, res) => {
    const { prodId } = req.params; // Lấy prodId từ tham số URL

    try {
        // Kết nối đến MongoDB
        await mongoose.connect(server.uri);
        const product = await server.productModel.findOne({ _id: prodId });

        if (!product) {
            return res.status(404).json({ error: 'Không tìm thấy sản phẩm.' });
        }

        // Xử lý tên sản phẩm không dấu
        product.namePro = removeDiacritics(product.namePro);

        res.json(product);
    } catch (error) {
        console.error('Lỗi khi lấy sản phẩm:', error);
        res.status(500).json({ error: 'Có lỗi xảy ra khi lấy sản phẩm.', details: error.message });
    }
});

router.get('/check-favorite/:prodId', async (req, res) => {
    try {
        const { prodId } = req.params;

        // Kiểm tra xem prodId có tồn tại trong collection 'favorites' không
        const favorite = await FavoriteModel.findOne({ prodId: prodId });

        if (favorite) {
            res.json({ exists: true });
        } else {
            res.json({ exists: false });
        }
    } catch (error) {
        console.error('Lỗi khi kiểm tra sản phẩm yêu thích:', error);
        res.status(500).json({ error: 'Có lỗi xảy ra khi kiểm tra sản phẩm yêu thích.' });
    }
});


// API thêm đáng giá
router.post('/add/add-to-feeback', async (req, res) => {
    console.log(req.body); // Xem dữ liệu nhận được trong body

    try {
        const { cusId, start, content, dateFeed } = req.body;
        const newFeeback = new FeebackModel({ cusId, start, content, dateFeed });
        await newFeeback.save();
        res.status(201).json({ message: 'Thêm vào đáng giá thành công!', data: newFeeback });
    } catch (error) {
        console.error('Lỗi chi tiết:', error);
        res.status(500).json({ message: 'Lỗi khi thêm vào đáng giá', error });
    }
});



  
module.exports = router;

