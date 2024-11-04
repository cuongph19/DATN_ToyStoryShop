package com.example.datn_toystoryshop;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.GestureDetector;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import java.util.ArrayList;


public class Product_detail extends AppCompatActivity {
    private TextView tvProductName, tvProductPrice, tvproductSkuValue, tvproductDescription, tvproductDetails,  quantityText;
    private ImageView imgProduct, btnBack, shareButton, heartIcon ;
    private double productPrice;
    private boolean statusPro;
    private int prodId, owerId, quantity, cateId;
    private String productId, productName, desPro, creatDatePro, listPro;
    private Button decreaseButton , increaseButton;
    private RadioGroup radioGroup;
    private ArrayList<String> productImg; // Danh sách URL ảnh của sản phẩm
    private int currentImageIndex = 0; // Vị trí ảnh hiện tại
    private Handler handler = new Handler(); // Tạo Handler để cập nhật ảnh
    private Runnable imageSwitcherRunnable;
    private View viewDetail1, viewDetail2, viewDetail3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        // Ánh xạ các view
        tvProductName = findViewById(R.id.productTitle);
        tvProductPrice = findViewById(R.id.productPrice);
        tvproductSkuValue = findViewById(R.id.productSkuValue);
        tvproductDescription = findViewById(R.id.productDescription);
        tvproductDetails = findViewById(R.id.productDetails);
        imgProduct = findViewById(R.id.productImage);
        btnBack = findViewById(R.id.btnBack);
        shareButton = findViewById(R.id.shareButton);
        heartIcon = findViewById(R.id.heart_icon);
        decreaseButton  = findViewById(R.id.decreaseQuantity);
        increaseButton  = findViewById(R.id.increaseQuantity);
        quantityText  = findViewById(R.id.quantityText);
        radioGroup = findViewById(R.id.radioGroup);
        viewDetail1 = findViewById(R.id.view_detail_1);
        viewDetail2 = findViewById(R.id.view_detail_2);
        viewDetail3 = findViewById(R.id.view_detail_3);

        // Nhận dữ liệu từ Intent
        Intent intent = getIntent();
         productId = intent.getStringExtra("productId");
         prodId = intent.getIntExtra("prodId", -1);
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
        Log.e("Product_detail", "aaaaaaaaaaaaaaaa: " + productId);
        // Hiển thị dữ liệu

        tvProductName.setText(productName);
        tvProductPrice.setText(String.format("%,.0fđ", productPrice));
/// thiếu hãng đồ chơi
        tvproductSkuValue.setText(String.valueOf(quantity));
        tvproductDescription.setText(desPro);
        tvproductDetails.setText(creatDatePro);

        // Tạo Runnable để tự động thay đổi ảnh sau 3 giây
        imageSwitcherRunnable = new Runnable() {
            @Override
            public void run() {
                if (productImg != null && !productImg.isEmpty()) {
                    // Cập nhật ảnh trên imgProduct
                    Glide.with(Product_detail.this)
                            .load(productImg.get(currentImageIndex))
                            .placeholder(R.drawable.product1)
                            .into(imgProduct);
                    updateDotIndicator();
                    // Tăng chỉ số ảnh, nếu đạt cuối danh sách thì quay lại đầu
                    currentImageIndex = (currentImageIndex + 1) % productImg.size();
                }

                // Lặp lại sau 3 giây
                handler.postDelayed(this, 3000);
            }
        };
        // Bắt đầu tự động chuyển ảnh
        handler.postDelayed(imageSwitcherRunnable, 3000);



        // Sự kiện quay lại
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onBackPressed();
            }
        });
        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareApp(productName);
            }
        });
        heartIcon.setOnClickListener(new View.OnClickListener() {
            private boolean isRed = false; // trạng thái mặc định là màu xám

            @Override
            public void onClick(View v) {
                if (isRed) {
                    heartIcon.setColorFilter(Color.parseColor("#A09595")); // Màu xám ban đầu
                } else {
                    heartIcon.setColorFilter(Color.RED); // Đổi sang màu đỏ
                }
                isRed = !isRed; // Đảo trạng thái
            }
        });
        int[] currentQuantity = {1}; // Khởi tạo giá trị tối thiểu là 1
        quantityText.setText(String.valueOf(currentQuantity[0])); // Cập nhật TextView ban đầu

        decreaseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentQuantity[0] > 1) { // Kiểm tra để không giảm dưới 1
                    currentQuantity[0]--;
                    quantityText.setText(String.valueOf(currentQuantity[0]));
                    Toast.makeText(getApplicationContext(), "Số lượng: " + currentQuantity[0], Toast.LENGTH_SHORT).show();
                }
            }
        });

        increaseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentQuantity[0]++;
                quantityText.setText(String.valueOf(currentQuantity[0]));
                Toast.makeText(getApplicationContext(), "Số lượng: " + currentQuantity[0], Toast.LENGTH_SHORT).show();
            }
        });
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rbIndividual) {
                    Toast.makeText(getApplicationContext(), "Bạn đã chọn: Mô hình riêng lẻ", Toast.LENGTH_SHORT).show();
                } else if (checkedId == R.id.rbSet) {
                    Toast.makeText(getApplicationContext(), "Bạn đã chọn: Nguyên set 12 bộ", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Hủy Handler khi Activity bị hủy để tránh rò rỉ bộ nhớ
        handler.removeCallbacks(imageSwitcherRunnable);
    }
    private void updateDotIndicator() {
        // Đặt tất cả các view thành màu xám
        viewDetail1.setBackgroundResource(R.drawable.dot_inactive);
        viewDetail2.setBackgroundResource(R.drawable.dot_inactive);
        viewDetail3.setBackgroundResource(R.drawable.dot_inactive);

        // Đặt màu xanh cho dot indicator hiện tại
        if (currentImageIndex == 0) {
            viewDetail1.setBackgroundResource(R.drawable.dot_active);
        } else if (currentImageIndex == 1) {
            viewDetail2.setBackgroundResource(R.drawable.dot_active);
        } else if (currentImageIndex == 2) {
            viewDetail3.setBackgroundResource(R.drawable.dot_active);
        }
    }
    private void shareApp(String productName) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.intro_title_detail) + productName);
        sendIntent.setType("text/plain");

        Intent shareIntent = Intent.createChooser(sendIntent, getString(R.string.share_with_friends_int));
        startActivity(shareIntent);
    }
}