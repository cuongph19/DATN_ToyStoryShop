package com.example.datn_toystoryshop.Shopping;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.StrikethroughSpan;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.datn_toystoryshop.Adapter.Order_Adapter_Cart;
import com.example.datn_toystoryshop.Adapter.Order_Adapter_Detail;
import com.example.datn_toystoryshop.Model.OderProductDetail_Model;
import com.example.datn_toystoryshop.Model.Order_Detail_Model;
import com.example.datn_toystoryshop.Model.Order_Model;
import com.example.datn_toystoryshop.Profile.Terms_Conditions_screen;
import com.example.datn_toystoryshop.R;
import com.example.datn_toystoryshop.Server.APIService;
import com.example.datn_toystoryshop.Server.RetrofitClient;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Order_screen extends AppCompatActivity implements Order_Adapter_Detail.TotalAmountCallback {

    private ImageView imgBack;
    private TextView btnOrder, tvTotalAmount, total_amount, shipping_money, money_pay1, money_pay, tvPaymentDetail,clause,tvTotalAmountLabel;
    private LinearLayout addressLayout, ship, pay, productDiscounttv, shipDiscounttv;
    private RelativeLayout voucher;
    private EditText tvLeaveMessage;
    private RecyclerView recycler_view_oder;
    private TextView shipDiscountPrice, productDiscountPrice, estimated_delivery, voucher_info, old_price, new_price,shipping_method_name,show_more_oder;

    private double totalProductDiscount = 0;
    private double totalShipDiscount = 0;
    private String productId, selectedColor, customerId;
    private String productImg; // Danh sách URL ảnh của sản phẩm
    private int currentImageIndex = 0; // Vị trí ảnh hiện tại
    private Handler handler = new Handler(); // Tạo Handler để cập nhật ảnh
    private String content;
    private boolean isFavorite = false;
    private int currentQuantity; // Số lượng sản phẩm ban đầu là 1
    private double totalAmount;
    private int quantity;
    private String productType;
    private String documentId;
    private double moneyPay;
    private ArrayList<String> productIds;
    private double shippingCost = 40000;
    private static final String CHANNEL_ID = "home_notification_channel";
    private static final String PREFS_NAME = "NotificationPrefs";
    private static final String NOTIFICATION_BLOCKED_KEY = "isNotificationBlocked";
    private NotificationManager notificationManager;


    private String getFormattedDate(int daysToAdd, String format) {
        // Khởi tạo Calendar với ngày hiện tại
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, daysToAdd);

        // Định dạng ngày theo yêu cầu
        SimpleDateFormat dateFormat = new SimpleDateFormat(format, new Locale("vi", "VN"));
        return dateFormat.format(calendar.getTime());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oder_screen);

        // Khởi tạo các view trong layout
        imgBack = findViewById(R.id.imgBack);
        btnOrder = findViewById(R.id.btnOrder);
        addressLayout = findViewById(R.id.addressLayout);
        ship = findViewById(R.id.ship);
        pay = findViewById(R.id.pay);
        voucher = findViewById(R.id.voucher);
        tvLeaveMessage = findViewById(R.id.tvLeaveMessage1);
        tvTotalAmount = findViewById(R.id.tvTotalAmount);
        total_amount = findViewById(R.id.total_amount);
        shipping_money = findViewById(R.id.shipping_money);
        money_pay1 = findViewById(R.id.money_pay1);
        money_pay = findViewById(R.id.money_pay);
        tvPaymentDetail = findViewById(R.id.tvPaymentDetail);
        productDiscounttv = findViewById(R.id.productDiscounttv);
        shipDiscounttv = findViewById(R.id.shipDiscounttv);
        clause = findViewById(R.id.clause);
        estimated_delivery = findViewById(R.id.estimated_delivery);
        voucher_info = findViewById(R.id.voucher_info);
        old_price = findViewById(R.id.old_price);
        new_price = findViewById(R.id.new_price);
        shipping_method_name = findViewById(R.id.shipping_method_name);
        // Các TextView để hiển thị thông tin giảm giá
        shipDiscountPrice = findViewById(R.id.shipDiscountPrice);
        productDiscountPrice = findViewById(R.id.productDiscountPrice);
        show_more_oder = findViewById(R.id.show_more_oder);
        recycler_view_oder = findViewById(R.id.recycler_view_oder);
        tvTotalAmountLabel = findViewById(R.id.tvTotalAmountLabel);

        TextView addressName = findViewById(R.id.address_name);
        TextView addressPhone = findViewById(R.id.address_phone);
        TextView addressDetail = findViewById(R.id.address_detail);


        // Nhận dữ liệu từ Intent
        Intent intent = getIntent();

        String name = intent.getStringExtra("selectedAddressName");
        String phone = intent.getStringExtra("selectedAddressPhone");
        String address = intent.getStringExtra("selectedAddress");
        String detail = intent.getStringExtra("selectedAddressDetail");

        // Hiển thị dữ liệu lên giao diện
        String fullAddress = (address != null ? address : "") +
                (detail != null ? ", " + detail : "");
        addressName.setText(name != null ? name : "");
        addressPhone.setText(phone != null ? phone : "");
        addressDetail.setText(!fullAddress.trim().isEmpty() ? fullAddress : "Chưa chọn thông tin người nhận!");

        documentId = intent.getStringExtra("documentId");
        Log.e("OrderHistoryAdapter", "j66666666666666666Order_screen" + documentId);
        ///dulieunhanCart
        productIds = intent.getStringArrayListExtra("productIds");
         totalShipDiscount = intent.getDoubleExtra("totalShipDiscount", 0);
         totalProductDiscount = intent.getDoubleExtra("totalProductDiscount", 0);
        Log.e("API_ERROR", "bttttttttttttttttttttt" + totalShipDiscount);
        Log.e("API_ERROR", "btttttttttttttttttttttff " + totalProductDiscount);
        if (totalShipDiscount != 0 && totalProductDiscount != 0) {
            String text = String.format("%,.0fđ", shippingCost);
        SpannableString spannableString = new SpannableString(text);
        spannableString.setSpan(new StrikethroughSpan(), 0, text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#888888")), 0, text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        old_price.setText(spannableString);
        // Hiển thị các thông tin giảm giá
        shipDiscounttv.setVisibility(View.VISIBLE);
        productDiscounttv.setVisibility(View.VISIBLE);
        new_price.setVisibility(View.VISIBLE);
        shipDiscountPrice.setText(String.format("-₫%,.0f", totalShipDiscount));
        // Tính giá mới và set cho new_price
        double newPriceValue = shippingCost - totalShipDiscount;
        new_price.setText(String.format("₫%,.0f", newPriceValue));

        productDiscountPrice.setText(String.format("-₫%,.0f", totalProductDiscount));
        // Tính toán lại moneyPay sau khi nhận được giá trị mới
        calculateMoneyPay();
    }
        ///dulieunhanDetail
        productId = intent.getStringExtra("productId");
        currentQuantity = intent.getIntExtra("currentQuantity", 0);
        selectedColor = intent.getStringExtra("selectedColor");
        productImg = intent.getStringExtra("productImg");
        RecyclerView recyclerView = findViewById(R.id.recycler_view_oder);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        APIService apiService = RetrofitClient.getInstance().create(APIService.class);

        if (productIds != null && !productIds.isEmpty()) {

            // Sử dụng Oder_Adapter_Cart
            Order_Adapter_Cart adapter = new Order_Adapter_Cart(productIds, apiService, new Order_Adapter_Cart.OnTotalAmountChangeListener() {
                @Override
                public void onTotalAmountChanged(double totalAmount1, List<OderProductDetail_Model> productSummaries) {
                    totalAmount = totalAmount1;
                    // Định dạng chuỗi để hiển thị
                    String formattedTotalAmount = String.format("%,.0fđ", totalAmount1);
                    List<Order_Model.ProductDetail> productDetails = new ArrayList<>();
                    for (OderProductDetail_Model oderProductDetailModel : productSummaries) {
                        String productId = oderProductDetailModel.getId();
                        double totalAmount = oderProductDetailModel.getTotalPrice();
                        int quantity = oderProductDetailModel.getQuantity();
                        String productType = oderProductDetailModel.getProdSpecification();
                        int count = productIds.size();
                        tvTotalAmountLabel.setText("Tổng số tiền ("+ count+ " sản phẩm):");
                        Log.d("ProductIDCount", "Số lượng ID trong danh sách: " + count);

                        // Thêm sản phẩm vào danh sách productDetails
                          productDetails.add(new Order_Model.ProductDetail(productId, totalAmount, quantity, productType));

                        Log.d("MainActivity", "aaaaaaaaaaaaaaaaaaaaaaaaaaaahhhhh " + productId + ", Total Price: " + moneyPay);
                    }
                    // Gán vào TextView
                    Log.e("API_ERROR", "Thêm oder thất bại, mã phản hồi: 2 " + totalAmount1);
                    tvTotalAmount.setText(formattedTotalAmount);
                    total_amount.setText(formattedTotalAmount);
                    calculateMoneyPay();

                    btnOrder.setOnClickListener(v -> {
                        submitOrder_Cart(productDetails, totalAmount1);
                        // Khởi tạo NotificationManager
                        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

                        // Tạo Notification Channel nếu cần (dành cho Android 8.0 trở lên)
                        createNotificationChannel();

                        // Hiển thị thông báo chào mừng nếu thông báo đang được bật
                        showWelcomeNotification();

                    });
                }
            });

            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));

            // Hiển thị nút "Xem thêm" nếu danh sách có hơn 2 sản phẩm
            if (productIds.size() > 2) {
                show_more_oder.setVisibility(View.VISIBLE);
            }

            // Xử lý sự kiện nhấn "Xem thêm"
            show_more_oder.setOnClickListener(v -> {
                adapter.toggleShowAll(); // Gọi phương thức trong adapter để hiển thị tất cả
                show_more_oder.setVisibility(View.GONE); // Ẩn nút "Xem thêm" sau khi nhấn

            });
        } else {
            List<Order_Detail_Model> productList = new ArrayList<>();
            productList.add(new Order_Detail_Model(productId, currentQuantity, customerId, selectedColor, productImg));
            // Sử dụng Oder_Adapter_Detail
            Order_Adapter_Detail adapter = new Order_Adapter_Detail(productList, apiService, this);
            recyclerView.setAdapter(adapter);


        }
        // Đặt ngày giao hàng dự kiến
        String estimatedDeliveryDate = getFormattedDate(5, "'ngày' dd 'tháng' MM");
        estimated_delivery.setText("Đảm bảo nhận hàng vào "+ estimatedDeliveryDate);
        String voucherExpiryDate = getFormattedDate(7, "'ngày' dd 'tháng' MM 'năm' yyyy");
        voucher_info.setText("Nhận Voucher trị giá ₫15.000 nếu đơn hàng được giao đến bạn sau " + voucherExpiryDate);

                String shippingmoney = String.format("%,.0fđ", shippingCost);
        shipping_money.setText(shippingmoney);
// Kiểm tra null và tính toán moneyPay theo các trường hợp


        // Cài đặt hành động cho các nút

        voucher.setOnClickListener(v -> {
            Intent intent1 = new Intent(Order_screen.this, Voucher_screen.class);
            if (totalShipDiscount != 0) {
                intent1.putExtra("totalShipDiscount", totalShipDiscount);
            }
            if (totalProductDiscount != 0) {
                intent1.putExtra("totalProductDiscount", totalProductDiscount);
            }
            startActivityForResult(intent1, 100);  // Sử dụng mã requestCode để nhận kết quả
        });

        pay.setOnClickListener(v -> {
            Intent intent1 = new Intent(Order_screen.this, Payment_method_screen.class);
            String currentPayment = tvPaymentDetail.getText().toString();
            if (!currentPayment.isEmpty()) {
                intent1.putExtra("currentPayment", currentPayment);  // Chỉ truyền khi đã có lựa chọn
            }
            startActivityForResult(intent1, 101);
        });

        ship.setOnClickListener(v -> {
            Intent intent1 = new Intent(Order_screen.this, ShippingUnit_screen.class);

            if (totalShipDiscount != 0) {
                intent1.putExtra("totalShipDiscount", totalShipDiscount);
            }
            String ShipDiscount = shipping_method_name.getText().toString();
            if (!ShipDiscount.isEmpty()) {
                intent1.putExtra("ShipDiscount", ShipDiscount);  // Chỉ truyền khi đã có lựa chọn
            }

            startActivityForResult(intent1, 102);
        });
        clause.setOnClickListener(v -> {
            Intent intent1 = new Intent(Order_screen.this, Terms_Conditions_screen.class);
            startActivity(intent1);
            finish();
        });

        addressLayout.setOnClickListener(v -> {
            Intent intent1 = new Intent(Order_screen.this, AddressList_Screen.class);
            startActivity(intent1);
            finish();
        });

        imgBack.setOnClickListener(v -> onBackPressed());
    }
    @Override
    public void onTotalAmountCalculated(double totalAmount2, int quantity2, String productType2) {
        totalAmount = totalAmount2;
        quantity = quantity2;
        productType = productType2;

        String formattedTotalAmount = String.format("%,.0fđ", totalAmount2);
        Log.e("API_ERROR", "hhhhhhhhhhhhhhhhhhhhhhh " + totalAmount);
        Log.e("API_ERROR", "hhhhhhhhhhhhhhhhhhhhhhh" + quantity);
        Log.e("API_ERROR", "hhhhhhhhhhhhhhhhhhhhhhh" + productType);
        tvTotalAmount.setText(formattedTotalAmount);
        total_amount.setText(formattedTotalAmount);
         tvTotalAmountLabel.setText("Tổng số tiền (1 sản phẩm):");
        calculateMoneyPay();
        btnOrder.setOnClickListener(v -> {
            sumitOrder_Detail();
            finish();
        });
    }
    // Hàm nhận kết quả từ màn hình Voucher
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            switch (requestCode) {
                case 100:
                    // Nhận dữ liệu từ màn hình Voucher
                    totalProductDiscount = data.getDoubleExtra("totalProductDiscount", 0.0);
                    totalShipDiscount = data.getDoubleExtra("totalShipDiscount", 0.0);
                    //set gạch ngang cho giá cũ
                    String text = String.format("%,.0fđ", shippingCost);
                    SpannableString spannableString = new SpannableString(text);
                    spannableString.setSpan(new StrikethroughSpan(), 0, text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#888888")), 0, text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    old_price.setText(spannableString);
                    // Hiển thị các thông tin giảm giá
                    shipDiscounttv.setVisibility(View.VISIBLE);
                    productDiscounttv.setVisibility(View.VISIBLE);
                    new_price.setVisibility(View.VISIBLE);
                    shipDiscountPrice.setText(String.format("-₫%,.0f", totalShipDiscount));
                    // Tính giá mới và set cho new_price
                    double newPriceValue = shippingCost - totalShipDiscount;
                    new_price.setText(String.format("₫%,.0f", newPriceValue));

                    productDiscountPrice.setText(String.format("-₫%,.0f", totalProductDiscount));
                    // Tính toán lại moneyPay sau khi nhận được giá trị mới
                    calculateMoneyPay();
                    break;

                case 101:
                    // Nhận dữ liệu từ màn hình phương thức thanh toán
                    String paytext = data.getStringExtra("paytext");
                    tvPaymentDetail.setText(paytext);
                    break;

                case 102:
                    // Xử lý dữ liệu từ Shipping_method_screen
                    // Nhận dữ liệu từ màn hình phương thức vận chuyển
                    String shipping_price = data.getStringExtra("shipping_price");
                    String shipping_method = data.getStringExtra("shipping_method");

                    if (totalShipDiscount != 0) {
                    new_price.setVisibility(View.VISIBLE);
                    // Tính giá mới và set cho new_price
                    double shippingPriceValue = Double.parseDouble(shipping_price.replaceAll("[^\\d]", ""));
                    double newPriceValue1 = shippingPriceValue - totalShipDiscount;
                    new_price.setText(String.format("₫%,.0f", newPriceValue1));
                    //set gạch ngang cho giá cũ
                    String text1 = String.format("%,.0fđ", shippingCost);
                    SpannableString spannableString1 = new SpannableString(text1);
                    spannableString1.setSpan(new StrikethroughSpan(), 0, text1.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    spannableString1.setSpan(new ForegroundColorSpan(Color.parseColor("#888888")), 0, text1.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    old_price.setText(spannableString1);
                    }
                    shipping_method_name.setText(shipping_method);
//                    String shippingmoney = String.format("%,.0fđ", shippingCost);
//                    shipping_money.setText(shippingmoney);

                    calculateMoneyPay();
                    // Kiểm tra phương thức giao hàng và xử lý tương ứng
                    if ("Nhanh".equals(shipping_method)) {
                        shippingCost= 40000;
                        String shippingmoney = String.format("%,.0fđ", shippingCost);
                        shipping_money.setText(shippingmoney);
                        calculateMoneyPay();
                        String text1 = String.format("%,.0fđ", shippingCost);
                        SpannableString spannableString1 = new SpannableString(text1);
                        spannableString1.setSpan(new StrikethroughSpan(), 0, text1.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        spannableString1.setSpan(new ForegroundColorSpan(Color.parseColor("#888888")), 0, text1.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        old_price.setText(spannableString1);
//                        String oldprice = String.format("%,.0fđ", shippingCost);
//                        old_price.setText(oldprice);
                        String estimatedDeliveryDate = getFormattedDate(5, "'ngày' dd 'tháng' MM");
                        estimated_delivery.setText("Đảm bảo nhận hàng vào " + estimatedDeliveryDate);
                        String voucherExpiryDate = getFormattedDate(7, "'ngày' dd 'tháng' MM 'năm' yyyy");
                        voucher_info.setText("Nhận Voucher trị giá ₫15.000 nếu đơn hàng được giao đến bạn sau " + voucherExpiryDate);

                    } else if ("Hỏa Tốc".equals(shipping_method)) {
                        shippingCost= 80000;
                                String shippingmoney = String.format("%,.0fđ", shippingCost);
                                 shipping_money.setText(shippingmoney);
                        calculateMoneyPay();
                        String oldprice = String.format("%,.0fđ", shippingCost);
                        old_price.setText(oldprice);
                        // Xử lý cho phương thức Hỏa Tốc
                        String estimatedDeliveryDate = getFormattedDate(2, "'ngày' dd 'tháng' MM");
                        estimated_delivery.setText("Đảm bảo nhận hàng vào " + estimatedDeliveryDate);

                        String voucherExpiryDate = getFormattedDate(3, "'ngày' dd 'tháng' MM 'năm' yyyy");
                        voucher_info.setText("Nhận Voucher trị giá ₫15.000 nếu đơn hàng được giao đến bạn sau " + voucherExpiryDate);
                    }
                    break;
            }}
    }
    private void calculateMoneyPay() {

        // Kiểm tra null và tính toán moneyPay theo các trường hợp
        if (totalProductDiscount == 0 && totalShipDiscount == 0) {
            moneyPay = totalAmount + shippingCost;
        } else if (totalShipDiscount == 0) {
            moneyPay = (totalAmount + shippingCost) - totalProductDiscount;
        } else if (totalProductDiscount == 0) {
            moneyPay = (totalAmount + shippingCost) - totalShipDiscount;
        } else {
            moneyPay = (totalAmount + shippingCost) - totalProductDiscount - totalShipDiscount;
        }

        // Định dạng chuỗi để hiển thị với dấu phân cách hàng nghìn và đơn vị "đ"
        String formattedMoneyPay = String.format("%,.0fđ", moneyPay);

        // Gán giá trị cho TextView money_pay và money_pay1
        money_pay.setText(formattedMoneyPay);
        money_pay1.setText(formattedMoneyPay);
    }
    private void sumitOrder_Detail() {
        content = tvLeaveMessage.getText().toString();
        Log.e("API_ERROR", "AAAAAAAAAAAAAAA mã phản hồi:1 " + content);
        Log.e("API_ERROR", "AAAAAAAAAAAAAAA mã phản hồi:2 " + productId);
        Log.e("API_ERROR", "AAAAAAAAAAAAAAA mã phản hồi:3 " + totalAmount);
        Log.e("API_ERROR", "AAAAAAAAAAAAAAA mã phản hồi:4 " + quantity);
        Log.e("API_ERROR", "AAAAAAAAAAAAAAA mã phản hồi:5 " + productType);
        // Tạo một danh sách các sản phẩm trong đơn hàng
        List<Order_Model.ProductDetail> productDetails = new ArrayList<>();
        productDetails.add(new Order_Model.ProductDetail(productId, totalAmount, quantity, productType));

        // Tạo đối tượng Order_Model với danh sách sản phẩm
        Order_Model orderModel = new Order_Model(
                null,                // _id
                documentId,             // cusId
                (int) moneyPay,
                productDetails,      // prodDetails (danh sách sản phẩm)
                content,             // content (nội dung đơn hàng)
                "Đang chờ xác nhận", // orderStatus
                new Date().toString()// orderDate
        );

        APIService apiService = RetrofitClient.getInstance().create(APIService.class);
        Call<Order_Model> call = apiService.addToOrder(orderModel);

        call.enqueue(new Callback<Order_Model>() {
            @Override
            public void onResponse(Call<Order_Model> call, Response<Order_Model> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "Cảm ơn đã mua", Toast.LENGTH_SHORT).show();
                } else {
                    Log.e("API_ERROR", "Thêm order thất bại, mã phản hồi: " + response.code());
                    Toast.makeText(getApplicationContext(), "Thêm order thất bại", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Order_Model> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Không thể kết nối tới API", Toast.LENGTH_SHORT).show();
                Log.e("API_ERROR", "Lỗi kết nối API khi thêm đánh giá", t);
            }
        });
    }
private void submitOrder_Cart(List<Order_Model.ProductDetail> productDetails, double totalAmount) {
    String content = tvLeaveMessage.getText().toString();

    Log.e("API_ERROR", "Content: " + content);
    Log.e("API_ERROR", "Product Details: " + productDetails.toString());
    Log.e("API_ERROR", "Total Amount: " + moneyPay);

    // Tạo đối tượng Order_Model với danh sách sản phẩm
    Order_Model orderModel = new Order_Model(
            null,                // _id
            documentId,             // cusId
            (int) moneyPay,
            productDetails,      // prodDetails (danh sách sản phẩm)
            content,             // content (nội dung đơn hàng)
            "Đang chờ xác nhận", // orderStatus
            new Date().toString()// orderDate
    );

    APIService apiService = RetrofitClient.getInstance().create(APIService.class);
    Call<Order_Model> call = apiService.addToOrder(orderModel);

    call.enqueue(new Callback<Order_Model>() {
        @Override
        public void onResponse(Call<Order_Model> call, Response<Order_Model> response) {
            if (response.isSuccessful()) {
                Toast.makeText(getApplicationContext(), "Cảm ơn đã mua", Toast.LENGTH_SHORT).show();
            } else {
                Log.e("API_ERROR", "Thêm order thất bại, mã phản hồi: " + response.code());
                Toast.makeText(getApplicationContext(), "Thêm order thất bại", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onFailure(Call<Order_Model> call, Throwable t) {
            Toast.makeText(getApplicationContext(), "Không thể kết nối tới API", Toast.LENGTH_SHORT).show();
            Log.e("API_ERROR", "Lỗi kết nối API khi thêm order", t);
        }
    });
}

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Home Notification Channel";
            String description = "Channel for home screen notifications";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);

            // Đăng ký channel với hệ thống
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }
    }

    private void showWelcomeNotification() {
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        boolean isNotificationBlocked = sharedPreferences.getBoolean(NOTIFICATION_BLOCKED_KEY, false);

        if (!isNotificationBlocked) {
            // Tạo thông báo chào mừng
            Notification.Builder builder = new Notification.Builder(this, CHANNEL_ID)
                    .setSmallIcon(R.drawable.ic_logo) // Thay bằng icon của ứng dụng
                    .setContentTitle("Đơn hàng của bạn đang đợi xác nhận")
                    .setContentText("Cảm ơn bạn đã mua sắm tại ToyStory Shop. Chúng tôi đang xử lý đơn hàng của bạn")
                    .setPriority(Notification.PRIORITY_DEFAULT);

            if (notificationManager != null) {
                notificationManager.notify(2, builder.build());
            }

        } else {
            // Thông báo bị tắt
            Toast.makeText(this, "Thông báo đang bị tắt.", Toast.LENGTH_SHORT).show();
        }
    }

}
