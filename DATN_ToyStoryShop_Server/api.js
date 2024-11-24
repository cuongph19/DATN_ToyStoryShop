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
const Address = require('./model/AddressModel');
const Chat = require('./model/ChatModel');


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
router.get('/sale', async (req, res) => {
    try {
        await mongoose.connect(server.uri);
        const newProducts = await server.productModel.find({ listPro: { $regex: "^SALE$", $options: "i" } });

        if (newProducts.length === 0) {
            return res.status(404).json({ error: 'Không tìm thấy sản phẩm giảm nhập.' });
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


// Lấy sản phẩm yêu thích
router.get('/favorites', async (req, res) => {
    try {
        const { cusId } = req.query;

        if (!cusId) {
            return res.status(400).json({ error: 'cusId không được để trống.' });
        }

        // Kiểm tra kết nối MongoDB đã thành công trước khi thực hiện query
        if (mongoose.connection.readyState !== 1) {
            return res.status(500).json({ error: 'Kết nối MongoDB chưa thành công.' });
        }

        // Tìm tất cả các sản phẩm trong collection 'favorites'
        const favorites = await FavoriteModel.find({ cusId }, '_id prodId cusId');

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
        const { cusId } = req.query;

        if (!cusId) {
            return res.status(400).json({ error: 'cusId không được để trống.' });
        }
        console.log('cusId truyền vào:', cusId);
        await mongoose.connect(server.uri);

        // Tìm tất cả các sản phẩm trong collection 'carts'
        const carts = await CartModel.find({ cusId }, '_id prodId quantity cusId prodSpecification ');
        console.log('Kết quả truy vấn:', carts);
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
router.get('/orders/confirm', async (req, res) => {
    try {
        const { cusId } = req.query;

        if (!cusId) {
            return res.status(400).json({ error: 'cusId không được để trống.' });
        }

        await mongoose.connect(server.uri);

        // Tìm tất cả các sản phẩm trong collection 'orders'
        const orders = await OrderModel.find({ cusId, orderStatus: 'Chờ xác nhận' }, '_id cusId revenue_all prodDetails content orderStatus orderDate ');

        if (orders.length === 0) {
            return res.status(404).json({ error: 'Không có sản phẩm nào trong đơn hàng.' });
        }

        res.json(orders);
    } catch (error) {
        console.error('Lỗi khi lấy sản phẩm trong đơn hàng.', error);
        res.status(500).json({ error: 'Có lỗi xảy ra khi lấy sản phẩm trong đơn hàng.' });
    }
});
router.get('/orders/getgoods', async (req, res) => {
    try {
        const { cusId } = req.query;

        if (!cusId) {
            return res.status(400).json({ error: 'cusId không được để trống.' });
        }

        await mongoose.connect(server.uri);

        // Tìm tất cả các sản phẩm trong collection 'orders'
        const orders = await OrderModel.find({ cusId, orderStatus: 'Chờ lấy hàng' }, '_id cusId revenue_all prodDetails content orderStatus orderDate ');

        if (orders.length === 0) {
            return res.status(404).json({ error: 'Không có sản phẩm nào trong đơn hàng.' });
        }

        res.json(orders);
    } catch (error) {
        console.error('Lỗi khi lấy sản phẩm trong đơn hàng.', error);
        res.status(500).json({ error: 'Có lỗi xảy ra khi lấy sản phẩm trong đơn hàng.' });
    }
});
router.get('/orders/delivery', async (req, res) => {
    try {
        const { cusId } = req.query;

        if (!cusId) {
            return res.status(400).json({ error: 'cusId không được để trống.' });
        }

        await mongoose.connect(server.uri);

        // Tìm tất cả các sản phẩm trong collection 'orders'
        const orders = await OrderModel.find({ cusId, orderStatus: 'Chờ giao hàng' }, '_id cusId revenue_all prodDetails content orderStatus orderDate ');

        if (orders.length === 0) {
            return res.status(404).json({ error: 'Không có sản phẩm nào trong đơn hàng.' });
        }

        res.json(orders);
    } catch (error) {
        console.error('Lỗi khi lấy sản phẩm trong đơn hàng.', error);
        res.status(500).json({ error: 'Có lỗi xảy ra khi lấy sản phẩm trong đơn hàng.' });
    }
});
router.get('/orders/successful', async (req, res) => {
    try {
        const { cusId } = req.query;

        if (!cusId) {
            return res.status(400).json({ error: 'cusId không được để trống.' });
        }

        await mongoose.connect(server.uri);

        // Tìm tất cả các sản phẩm trong collection 'orders'
        const orders = await OrderModel.find({ cusId, orderStatus: 'Đã giao' }, '_id cusId revenue_all prodDetails content orderStatus orderDate ');

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
        const { cusId } = req.query;

        if (!cusId) {
            return res.status(400).json({ error: 'cusId không được để trống.' });
        }

        await mongoose.connect(server.uri);

        // Tìm tất cả các sản phẩm trong collection 'feeback'
        const feebacks = await FeebackModel.find({ cusId }, '_id cusId prodId start content dateFeed ');

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
// Xóa sản phẩm khỏi favorites
router.delete('/deleteFavorite/:id', async (req, res) => {
    const { id } = req.params;
    try {
        // Kiểm tra kết nối MongoDB đã thành công
        if (mongoose.connection.readyState !== 1) {
            return res.status(500).json({ error: 'Kết nối MongoDB chưa thành công.' });
        }

        const result = await FavoriteModel.deleteOne({ prodId: id });
        if (result.deletedCount === 0) {
            return res.status(404).json({ error: 'Không tìm thấy sản phẩm yêu thích với ID đã cho.' });
        }

        res.status(200).json({ message: 'Sản phẩm yêu thích đã được xóa thành công.' });
    } catch (error) {
        console.error('Lỗi khi xóa sản phẩm yêu thích:', error);
        res.status(500).json({ error: 'Có lỗi xảy ra khi xóa sản phẩm yêu thích.' });
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
// Thêm sản phẩm vào favorites
router.post('/add/add-to-favorites', async (req, res) => {
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
        const { prodId, quantity, cusId, prodSpecification } = req.body;
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
        const cartItem = await CartModel.findById({ _id: cartId });
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


router.get('/addresses', async (req, res) => {
    try {
        // Lấy tất cả các địa chỉ trong database
        const addresses = await Address.find();
        res.json(addresses);
    } catch (error) {
        console.error('Lỗi khi lấy tất cả địa chỉ:', error);
        res.status(500).json({ error: 'Có lỗi xảy ra khi lấy tất cả địa chỉ.' });
    }
});

router.get('/addresses/:id', async (req, res) => {
    try {
      const addressId = req.params.id;
  
      // Tìm địa chỉ theo ID
      const address = await Address.findById(addressId);
  
      // Kiểm tra nếu không tìm thấy địa chỉ
      if (!address) {
        return res.status(404).json({ message: "Address not found" });
      }
  
      // Trả về địa chỉ tìm thấy
      res.status(200).json(address);
    } catch (error) {
      console.error(error);
      res.status(500).json({ message: "Server error" });
    }
  });

  
router.post('/addresses', async (req, res) => {
    try {
        const { name, phone, address, addressDetail, isDefault } = req.body;

        // Tạo đối tượng Address mới
        const newAddress = new Address({
            userId: "defaultUserId", // Giá trị mặc định tạm thời
            name,
            phone,
            address,
            addressDetail,
            isDefault: isDefault || false, // Mặc định là `false` nếu không được truyền
        });

        // Lưu địa chỉ vào cơ sở dữ liệu
        const savedAddress = await newAddress.save();
        res.status(201).json({
            success: true,
            message: "Địa chỉ đã được thêm thành công",
            data: savedAddress,
        });
    } catch (error) {
        console.error("Lỗi khi thêm địa chỉ:", error);
        res.status(500).json({
            success: false,
            message: "Không thể thêm địa chỉ",
            error: error.message,
        });
    }
});

router.patch('/addresses/:id', async (req, res) => {
    try {
      const addressId = req.params.id;
      const { name, phone, address, addressDetail } = req.body;
  
      // Kiểm tra xem ít nhất một trong các trường cần cập nhật có được cung cấp không
      if (!name && !phone && !address && !addressDetail) {
        return res.status(400).json({
          success: false,
          message: 'Cần cung cấp ít nhất một trường để cập nhật',
        });
      }
  
      // Tạo đối tượng cập nhật chỉ với các trường cần thay đổi
      const updateData = {};
      if (name) updateData.name = name;
      if (phone) updateData.phone = phone;
      if (address) updateData.address = address;
      if (addressDetail) updateData.addressDetail = addressDetail;
  
      // Cập nhật địa chỉ trong cơ sở dữ liệu
      const updatedAddress = await Address.findByIdAndUpdate(addressId, updateData, { new: true });
  
      // Kiểm tra nếu không tìm thấy địa chỉ
      if (!updatedAddress) {
        return res.status(404).json({ message: 'Địa chỉ không tìm thấy' });
      }
  
      // Trả về địa chỉ đã được cập nhật
      res.status(200).json({
        success: true,
        message: 'Cập nhật địa chỉ thành công',
        data: updatedAddress,
      });
    } catch (error) {
      console.error('Lỗi khi cập nhật địa chỉ:', error);
      res.status(500).json({
        success: false,
        message: 'Không thể cập nhật địa chỉ',
        error: error.message,
      });
    }
  });
  

router.get('/cart/check-product', async (req, res) => {
    const { prodId, cusId } = req.query; // Lấy productId và customerId từ query

    try {
        // Kết nối đến MongoDB
        await mongoose.connect(server.uri);

        // Tìm sản phẩm trong giỏ hàng
        const cartItem = await CartModel.findOne({ prodId, cusId });

        if (!cartItem) {
            return res.json({ exists: false }); // Sản phẩm không tồn tại trong giỏ hàng
        }

        res.json({ exists: true }); // Sản phẩm đã tồn tại trong giỏ hàng
    } catch (error) {
        console.error('Lỗi khi kiểm tra sản phẩm:', error);
        res.status(500).json({ error: 'Có lỗi xảy ra khi kiểm tra sản phẩm.', details: error.message });
    }
});




const supportIds = ['support1', 'support2', 'support3']; // Danh sách ID hỗ trợ

router.get('/chat/history', async (req, res) => {
    try {
        const { user1, user2 } = req.query;

        if (!user1) {
            return res.status(400).json({ error: 'Thiếu thông tin người dùng!' });
        }

        // Nếu user2 không được truyền, giả định đây là chat với bộ phận hỗ trợ
        const realUser2 = user2 || supportIds.find(id => id !== user1);

        if (!realUser2) {
            return res.status(404).json({ error: 'Không tìm thấy bộ phận hỗ trợ!' });
        }

        const chatHistory = await Chat.find({
            $or: [
                { senderId: user1, receiverId: realUser2 },
                { senderId: realUser2, receiverId: user1 },
            ],
        }).sort({ timestamp: 1 });

        res.status(200).json({ data: chatHistory });
    } catch (error) {
        res.status(500).json({ error: 'Lỗi khi lấy lịch sử tin nhắn', details: error.message });
    }
});
router.post('/chat/send', async (req, res) => {
    try {
        const { senderId, receiverId, message, chatType } = req.body;

        if (!senderId || !message || !chatType) {
            return res.status(400).json({ error: 'Thiếu thông tin cần thiết!' });
        }

        let realReceiverId = receiverId;

        // Nếu không truyền receiverId, giả định người gửi là khách hàng và gửi tới bộ phận hỗ trợ
        if (!receiverId) {
            const supportIds = ['support1', 'support2', 'support3'];
            realReceiverId = supportIds.find(id => id !== senderId);

            if (!realReceiverId) {
                return res.status(404).json({ error: 'Không tìm thấy bộ phận hỗ trợ!' });
            }
        }

        const newMessage = new Chat({
            senderId,
            receiverId: realReceiverId,
            message,
            chatType,
            timestamp: Date.now(),
        });

        const savedMessage = await newMessage.save();
        res.status(200).json({ message: 'Gửi tin nhắn thành công!', data: savedMessage });
    } catch (error) {
        res.status(500).json({ error: 'Lỗi khi gửi tin nhắn', details: error.message });
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
router.get('/cart/get-cart-id', async (req, res) => {
    const { prodId, cusId } = req.query;

    try {
        // Kết nối đến MongoDB
        await mongoose.connect(server.uri);

        // Tìm sản phẩm trong giỏ hàng
        const cartItem = await CartModel.findOne({ prodId, cusId }).select('_id quantity prodSpecification'); // Chỉ lấy trường _id

        if (!cartItem) {
            return res.status(404).json({ message: 'Không tìm thấy sản phẩm trong giỏ hàng.' });
        }

        res.json({
            cartId: cartItem._id,
            prodSpecification: cartItem.prodSpecification,
            quantity: cartItem.quantity,
        }); // Trả về _id của cart
    } catch (error) {
        console.error('Lỗi khi lấy _id và prodSpecification  của sản phẩm:', error);
        res.status(500).json({ error: 'Có lỗi xảy ra khi truy vấn _id của sản phẩm.', details: error.message });
    }
});

module.exports = router;

