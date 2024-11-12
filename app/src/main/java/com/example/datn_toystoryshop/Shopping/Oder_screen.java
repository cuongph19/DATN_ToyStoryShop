package com.example.datn_toystoryshop.Shopping;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.datn_toystoryshop.Profile.Terms_Conditions_screen;
import com.example.datn_toystoryshop.R;
import com.example.datn_toystoryshop.Server.APIService;

import java.util.ArrayList;

public class Oder_screen extends AppCompatActivity {

    private ImageView imgBack, product_image;
    private TextView product_name, product_price, product_quantity, btnOrder, product_type, tvTotalAmount, total_amount, shipping_money, money_pay1, money_pay, tvPaymentDetail,clause;
    private LinearLayout addressLayout, ship, pay, productDiscounttv, shipDiscounttv;
    private RelativeLayout voucher;
    private EditText tvLeaveMessage;
    private TextView shipDiscount, shipDiscountPrice, productDiscount, productDiscountPrice;

    private double productPrice;
    private double totalProductDiscount = 0;
    private double totalShipDiscount = 0;
    private boolean statusPro;
    private int owerId, quantity, cateId;
    private String productId, productName, desPro, creatDatePro, listPro, brand, selectedColor, customerId;
    private ArrayList<String> productImg; // Danh sách URL ảnh của sản phẩm
    private int currentImageIndex = 0; // Vị trí ảnh hiện tại
    private Handler handler = new Handler(); // Tạo Handler để cập nhật ảnh
    private String favoriteId;
    private boolean isFavorite = false;
    private APIService apiService;
    private int currentQuantity; // Số lượng sản phẩm ban đầu là 1
    private double totalAmount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oder_screen);

        // Khởi tạo các view trong layout
        imgBack = findViewById(R.id.imgBack);
        product_image = findViewById(R.id.product_image);
        product_name = findViewById(R.id.product_name);
        product_type = findViewById(R.id.product_type);
        product_price = findViewById(R.id.product_price);
        product_quantity = findViewById(R.id.product_quantity);
        btnOrder = findViewById(R.id.btnOrder);
        addressLayout = findViewById(R.id.addressLayout);
        ship = findViewById(R.id.ship);
        pay = findViewById(R.id.pay);
        voucher = findViewById(R.id.voucher);
        tvLeaveMessage = findViewById(R.id.tvLeaveMessage);
        tvTotalAmount = findViewById(R.id.tvTotalAmount);
        total_amount = findViewById(R.id.total_amount);
        shipping_money = findViewById(R.id.shipping_money);
        money_pay1 = findViewById(R.id.money_pay1);
        money_pay = findViewById(R.id.money_pay);
        tvPaymentDetail = findViewById(R.id.tvPaymentDetail);
        productDiscounttv = findViewById(R.id.productDiscounttv);
        shipDiscounttv = findViewById(R.id.shipDiscounttv);
        clause = findViewById(R.id.clause);

        // Các TextView để hiển thị thông tin giảm giá
        shipDiscountPrice = findViewById(R.id.shipDiscountPrice);
        productDiscountPrice = findViewById(R.id.productDiscountPrice);

        // Nhận dữ liệu từ Intent
        Intent intent = getIntent();
        productId = intent.getStringExtra("productId");
        owerId = intent.getIntExtra("owerId", -1);
        statusPro = intent.getBooleanExtra("statusPro", false);
        productPrice = intent.getDoubleExtra("productPrice", 0.0);
        desPro = intent.getStringExtra("desPro");
        creatDatePro = intent.getStringExtra("creatDatePro");
        quantity = intent.getIntExtra("quantity", 0);
        listPro = intent.getStringExtra("listPro");
        productImg = intent.getStringArrayListExtra("productImg");
        productName = intent.getStringExtra("productName");
        cateId = intent.getIntExtra("cateId", -1);
        brand = intent.getStringExtra("brand");
        favoriteId = intent.getStringExtra("favoriteId");

        // Thêm các thông tin vào giao diện
        currentQuantity = intent.getIntExtra("currentQuantity", 0);
        customerId = intent.getStringExtra("customerId");
        selectedColor = intent.getStringExtra("selectedColor");


        product_name.setText(productName);
        product_price.setText(String.format("%,.0fđ", productPrice));
        product_quantity.setText(String.format("x%d", currentQuantity));
        product_type.setText(selectedColor);

        totalAmount = productPrice * currentQuantity;
// Định dạng chuỗi để hiển thị với dấu phân cách hàng nghìn và đơn vị "đ"
        String formattedTotalAmount = String.format("%,.0fđ", totalAmount);
// Gán giá trị cho TextView tvTotalAmount
        tvTotalAmount.setText(formattedTotalAmount);
        total_amount.setText(formattedTotalAmount);
        // Chi phí vận chuyển cố định
        double shippingCost = 30000;
        String shippingmoney = String.format("%,.0fđ", shippingCost);
        shipping_money.setText(shippingmoney);
// Kiểm tra null và tính toán moneyPay theo các trường hợp
        double moneyPay;
        if (totalProductDiscount == 0 && totalShipDiscount == 0) {
            // Nếu cả hai đều là null, chỉ cộng thêm phí vận chuyển
            moneyPay = totalAmount + shippingCost;
        } else if (totalShipDiscount == 0) {
            // Nếu chỉ totalShipDiscount là null
            moneyPay = (totalAmount + shippingCost) - totalProductDiscount;
        } else if (totalProductDiscount == 0) {
            // Nếu chỉ totalProductDiscount là null
            moneyPay = (totalAmount + shippingCost) - totalShipDiscount;
        } else {
            // Nếu cả hai đều không null
            moneyPay = (totalAmount + shippingCost) - totalProductDiscount - totalShipDiscount;
        }

// Định dạng chuỗi để hiển thị với dấu phân cách hàng nghìn và đơn vị "đ"
        String formattedMoneyPay = String.format("%,.0fđ", moneyPay);

// Gán giá trị cho TextView money_pay
        money_pay.setText(formattedMoneyPay);
        money_pay1.setText(formattedMoneyPay);
        if (!productImg.isEmpty()) {
            String firstImage = productImg.get(0);
            Glide.with(this)
                    .load(firstImage)
                    .into(product_image);
        }

        // Cài đặt hành động cho các nút
        btnOrder.setOnClickListener(v -> onBackPressed());


        voucher.setOnClickListener(v -> {
            Intent intent1 = new Intent(Oder_screen.this, Voucher_screen.class);
            startActivityForResult(intent1, 100);  // Sử dụng mã requestCode để nhận kết quả
        });

        pay.setOnClickListener(v -> {
            Intent intent1 = new Intent(Oder_screen.this, payment_method_screen.class);
            startActivityForResult(intent1, 100);
        });

        ship.setOnClickListener(v -> {
            Intent intent1 = new Intent(Oder_screen.this, Add_address_screen.class);
            startActivity(intent1);
            finish();
        });
        clause.setOnClickListener(v -> {
            Intent intent1 = new Intent(Oder_screen.this, Terms_Conditions_screen.class);
            startActivity(intent1);
            finish();
        });

        addressLayout.setOnClickListener(v -> {
            Intent intent1 = new Intent(Oder_screen.this, Add_address_screen.class);
            startActivity(intent1);
            finish();
        });

        imgBack.setOnClickListener(v -> onBackPressed());
    }

    // Hàm nhận kết quả từ màn hình Voucher
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK && data != null) {
            // Nhận dữ liệu từ màn hình Voucher
             totalProductDiscount = data.getDoubleExtra("totalProductDiscount", 0.0);
             totalShipDiscount = data.getDoubleExtra("totalShipDiscount", 0.0);
            String paytext = data.getStringExtra("paytext");
            tvPaymentDetail.setText(paytext);
            Log.d("OrderScreen", "paytextpaytextpaytext: " + paytext);
            // Log dữ liệu voucher nhận được
            Log.d("OrderScreen", "Received totalProductDiscount: " + totalProductDiscount);
            Log.d("OrderScreen", "Received totalShipDiscount: " + totalShipDiscount);
            productDiscountPrice.setText(String.format("-₫%,.0f", totalProductDiscount));
            shipDiscounttv.setVisibility(View.VISIBLE);
            productDiscounttv.setVisibility(View.VISIBLE);
            shipDiscountPrice.setText(String.format("-₫%,.0f", totalShipDiscount));
// Tính toán lại moneyPay sau khi nhận được giá trị mới
            calculateMoneyPay();
        }
    }
    private void calculateMoneyPay() {
        double shippingCost = 30000;
        double moneyPay;

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

}
