const express = require('express');

const router = express.Router();
const mongoose = require('mongoose'); // Đảm bảo rằng dòng này có ở đây
const FavoriteModel = require('./model/FavoriteModel');
const FeebackAppModel = require('./model/FeebackAppModel');
const FeebackModel = require('./model/FeebackModel');
const CartModel = require('./model/CartModel');
const OrderModel = require('./model/OrderModel');
const VoucherModel = require('./model/VoucherModel');
const ArtStory = require('./model/ArtStoryModel');

const server = require('./server');


router.get('/', (req, res) => {
    res.send('URI:' + app.uri);
});

// Tạo bài viết (Create)
router.post('/artstories', async (req, res) => {
    try {
        const artStory = new ArtStory(req.body);
        await artStory.save();
        res.status(201).json(artStory);
    } catch (error) {
        res.status(400).json({ message: error.message });
    }
});

// Lấy danh sách bài viết (Read All)
router.get('/artstories', async (req, res) => {
    try {
        const artStories = await ArtStory.find();
        res.json(artStories);
    } catch (error) {
        res.status(500).json({ message: error.message });
    }
});

// Lấy bài viết theo ID (Read One)
router.get('/artstories/:id', async (req, res) => {
    try {
        const artStory = await ArtStory.findById(req.params.id);
        if (!artStory) return res.status(404).json({ message: 'ArtStory not found' });
        res.json(artStory);
    } catch (error) {
        res.status(500).json({ message: error.message });
    }
});

// Cập nhật bài viết theo ID (Update)
router.put('/artstories/:id', async (req, res) => {
    try {
        const artStory = await ArtStory.findByIdAndUpdate(req.params.id, req.body, { new: true });
        if (!artStory) return res.status(404).json({ message: 'ArtStory not found' });
        res.json(artStory);
    } catch (error) {
        res.status(400).json({ message: error.message });
    }
});

// Xóa bài viết theo ID (Delete)
router.delete('/artstories/:id', async (req, res) => {
    try {
        const artStory = await ArtStory.findByIdAndDelete(req.params.id);
        if (!artStory) return res.status(404).json({ message: 'ArtStory not found' });
        res.json({ message: 'ArtStory deleted' });
    } catch (error) {
        res.status(500).json({ message: error.message });
    }
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
///////////////////////////////////
router.get('/carts', async (req, res) => {
    try {
        await mongoose.connect(server.uri);

        // Tìm tất cả các sản phẩm trong collection 'carts'
        const carts = await CartModel.find({}, '_id prodId quantity cusId prodSpecification ');

        if (carts.length === 0) {
            return res.status(404).json({ error: 'Không có sản phẩm nào trong giỏ hàng.' });
        }

        res.json(carts);
    } catch (error) {
        console.error('Lỗi khi lấy sản phẩm trong giỏ hàng.', error);
        res.status(500).json({ error: 'Có lỗi xảy ra khi lấy sản phẩm trong giỏ hàng.' });
    }
});
///////////////////////////////////
router.get('/orders', async (req, res) => {
    try {
        await mongoose.connect(server.uri);

        // Tìm tất cả các sản phẩm trong collection 'orders'
        const orders = await OrderModel.find({}, '_id cusId revenue_all prodDetails content orderStatus orderDate ');

        if (orders.length === 0) {
            return res.status(404).json({ error: 'Không có sản phẩm nào trong đơn hàng.' });
        }

        res.json(orders);
    } catch (error) {
        console.error('Lỗi khi lấy sản phẩm trong đơn hàng.', error);
        res.status(500).json({ error: 'Có lỗi xảy ra khi lấy sản phẩm trong đơn hàng.' });
    }
});
router.get('/feebacks', async (req, res) => {
    try {
        await mongoose.connect(server.uri);

        // Tìm tất cả các sản phẩm trong collection 'feeback'
        const feebacks = await FeebackModel.find({}, '_id cusId prodId start content dateFeed ');

        if (feebacks.length === 0) {
            return res.status(404).json({ error: 'Không có  đánh giá.' });
        }

        res.json(feebacks);
    } catch (error) {
        console.error('Lỗi khi lấy đánh giá.', error);
        res.status(500).json({ error: 'Có lỗi xảy ra khi lấy đánh giá.' });
    }
});
router.get('/vouchers', async (req, res) => {
    try {
        await mongoose.connect(server.uri);

        // Lấy tất cả các voucher từ database và loại bỏ khoảng trắng ở đầu và cuối của trường `quantity_voucher`
        const vouchers = await VoucherModel.find({}, '_id price_reduced quantity_voucher discount_code');

        // Loại bỏ khoảng trắng thừa trong trường `quantity_voucher` trước khi trả về
        const cleanedVouchers = vouchers.map(voucher => {
            // Trim khoảng trắng ở đầu và cuối của `quantity_voucher`
            voucher.quantity_voucher = voucher.quantity_voucher.trim();
            return voucher;
        });

        if (cleanedVouchers.length === 0) {
            return res.status(404).json({ error: 'Không có mã giảm giá.' });
        }

        res.json(cleanedVouchers);
    } catch (error) {
        console.error('Lỗi khi lấy mã giảm giá.', error);
        res.status(500).json({ error: 'Có lỗi xảy ra khi lấy mã giảm giá.' });
    }
});


// Hàm chuyển đổi chuỗi có dấu thành không dấu
function removeDiacritics(input) {
    const normalized = input.normalize("NFD");
    return normalized.replace(/[\u0300-\u036f]/g, "");
};

// API xóa sản phẩm vào Cart
router.delete('/deleteCart/:id', async (req, res) => {
    const { id } = req.params; // Nhận _id từ đường dẫn

    try {
        await mongoose.connect(server.uri);

        // Xóa sản phẩm trong collection 'Carts' dựa trên _id
        const result = await CartModel.deleteOne({ prodId: id });
        if (result.deletedCount === 0) {
            return res.status(404).json({ error: 'Không tìm thấy sản phẩm trong giỏ hàng với ID đã cho.' });
        }

        res.status(200).json({ message: 'Sản phẩm trong giỏ hàng đã được xóa thành công.' });
    } catch (error) {
        console.error('Lỗi khi xóa sản phẩm trong giỏ hàng:', error);
        res.status(500).json({ error: 'Có lỗi xảy ra khi xóa sản phẩm trong giỏ hàng.' });
    } finally {
        mongoose.connection.close(); // Đảm bảo kết nối được đóng
    }
});
// API xóa sản phẩm vào favorites
router.delete('/deleteFavorite/:id', async (req, res) => {
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

///////////////////////////////////
// API thêm đáng giá
router.post('/add/add-to-app-feeback', async (req, res) => {
    console.log(req.body); // Xem dữ liệu nhận được trong body

    try {
        const { cusId, start, content, dateFeed } = req.body;
        const newFeebackApp = new FeebackAppModel({ cusId, start, content, dateFeed });
        await newFeebackApp.save();
        res.status(201).json({ message: 'Thêm vào đáng giá thành công!', data: newFeebackApp });
    } catch (error) {
        console.error('Lỗi chi tiết:', error);
        res.status(500).json({ message: 'Lỗi khi thêm vào đáng giá', error });
    }
});
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
///////////////////////////////////
// API thêm đáng giá
router.post('/add/add-to-feeback', async (req, res) => {
    console.log(req.body); // Xem dữ liệu nhận được trong body

    try {
        const { cusId, prodId, start, content, dateFeed } = req.body;
        const newFeeback = new FeebackModel({ cusId, prodId, start, content, dateFeed });
        await newFeeback.save();
        res.status(201).json({ message: 'Thêm vào đáng giá thành công!', data: newFeeback });
    } catch (error) {
        console.error('Lỗi chi tiết:', error);
        res.status(500).json({ message: 'Lỗi khi thêm vào đáng giá', error });
    }
});

router.post('/add/add-to-cart', async (req, res) => {
    console.log(req.body); // Xem dữ liệu nhận được trong body

    try {
        const { prodId, quantity, cusId, prodSpecification  } = req.body;
        const newCart = new CartModel({ prodId, quantity, cusId, prodSpecification });
        await newCart.save();
        res.status(201).json({ message: 'Thêm vào giỏ hàng thành công!', data: newCart });
    } catch (error) {
        console.error('Lỗi chi tiết:', error);
        res.status(500).json({ message: 'Lỗi khi thêm vào giỏ hàng', error });
    }
});
router.put('/update/cart/:cartId', async (req, res) => {
    console.log(req.body); // Xem dữ liệu nhận được trong body

    try {
        const { cartId } = req.params; // Lấy id từ URL
        const { quantity, prodSpecification } = req.body; // Lấy các thông tin cập nhật từ body

        // Tìm sản phẩm theo id
        const cartItem = await CartModel.findById({ _id: cartId});
        if (!cartItem) {
            return res.status(404).json({ message: 'Sản phẩm không tồn tại trong giỏ hàng!' });
        }

        // Cập nhật các trường
        if (quantity !== undefined) cartItem.quantity = quantity;
        if (prodSpecification !== undefined) cartItem.prodSpecification = prodSpecification;

        await cartItem.save(); // Lưu cập nhật

        res.status(200).json({ message: 'Cập nhật sản phẩm trong giỏ hàng thành công!', data: cartItem });
    } catch (error) {
        console.error('Lỗi chi tiết:', error);
        res.status(500).json({ message: 'Lỗi khi cập nhật sản phẩm trong giỏ hàng', error });
    }
});


router.post('/add/add-to-order', async (req, res) => {
    console.log(req.body); // Xem dữ liệu nhận được trong body

    try {
        const { cusId, revenue_all, prodDetails, content, orderStatus, orderDate } = req.body;

        // Kiểm tra prodDetails có phải là mảng không
        if (!Array.isArray(prodDetails)) {
            return res.status(400).json({ message: 'prodDetails phải là một mảng!' });
        }

        // Kiểm tra từng phần tử trong prodDetails
        for (let item of prodDetails) {
            if (!item.prodId || !item.revenue || !item.quantity || !item.prodSpecification) {
                return res.status(400).json({ message: 'Mỗi sản phẩm phải có prodId và revenue!' });
            }
        }

        const newOrder = new OrderModel({ cusId, revenue_all, prodDetails, content, orderStatus, orderDate });
        await newOrder.save();
        res.status(201).json({ message: 'Thêm vào lịch sử mua thành công!', data: newOrder });
    } catch (error) {
        console.error('Lỗi chi tiết:', error);
        res.status(500).json({ message: 'Lỗi khi thêm vào lịch sử mua', error });
    }
});

// bắt buộc nó phải ở cuối
// hiển thị thông tin dựa vào id sản phẩm 
router.get('/product-by/:prodId', async (req, res) => {
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
router.get('/cart-by/:cartId', async (req, res) => {
    const { cartId } = req.params; // Lấy prodId từ tham số URL

    try {
        // Kết nối đến MongoDB
        await mongoose.connect(server.uri);

        const cart = await CartModel.findOne({ _id: cartId });
        if (!cart) {
            return res.status(404).json({ error: 'Không tìm thấy sản phẩm.' });
        }

        res.json(cart);
    } catch (error) {
        console.error('Lỗi khi lấy sản phẩm:', error);
        res.status(500).json({ error: 'Có lỗi xảy ra khi lấy sản phẩm.', details: error.message });
    }
});
module.exports = router;

