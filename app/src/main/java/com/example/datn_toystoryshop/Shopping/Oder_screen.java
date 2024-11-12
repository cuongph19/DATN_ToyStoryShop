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
import com.example.datn_toystoryshop.R;
import com.example.datn_toystoryshop.Server.APIService;

import java.util.ArrayList;

public class Oder_screen extends AppCompatActivity {

    private ImageView imgBack, product_image;
    private TextView product_name, product_price, product_quantity, btnOrder, product_type;
    private LinearLayout addressLayout, ship, pay;
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

        // Các TextView để hiển thị thông tin giảm giá
        shipDiscount = findViewById(R.id.shipDiscount);
        shipDiscountPrice = findViewById(R.id.shipDiscountPrice);
        productDiscount = findViewById(R.id.productDiscount);
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
        product_quantity.setText(String.valueOf(quantity));
        product_type.setText(selectedColor);

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
            Intent intent1 = new Intent(Oder_screen.this, Add_address_screen.class);
            startActivity(intent1);
            finish();
        });

        ship.setOnClickListener(v -> {
            Intent intent1 = new Intent(Oder_screen.this, Add_address_screen.class);
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
            double totalProductDiscount = data.getDoubleExtra("totalProductDiscount", 0.0);
            double totalShipDiscount = data.getDoubleExtra("totalShipDiscount", 0.0);

            // Log dữ liệu voucher nhận được
            Log.d("OrderScreen", "Received totalProductDiscount: " + totalProductDiscount);
            Log.d("OrderScreen", "Received totalShipDiscount: " + totalShipDiscount);

            // Cập nhật giao diện với các giá trị voucher nhận được
            updateDiscounts(totalProductDiscount, totalShipDiscount);
        }
    }
    private void updateDiscounts(double totalProductDiscount, double totalShipDiscount) {
        // Cập nhật giao diện với giá trị giảm giá sản phẩm và vận chuyển
        productDiscountPrice.setText(String.format("-₫%,.0f", totalProductDiscount));
        shipDiscountPrice.setText(String.format("-₫%,.0f", totalShipDiscount));
        updateTotalPrice(totalProductDiscount, totalShipDiscount);
    }

    // Hàm cập nhật giá trị tổng sau khi áp dụng voucher
    private void updateTotalPrice(double totalProductDiscount, double totalShipDiscount) {
        double finalProductPrice = productPrice * quantity - totalProductDiscount;
        double finalTotalPrice = finalProductPrice - totalShipDiscount;
        product_price.setText(String.format("%,.0fđ", finalTotalPrice));
    }
}
