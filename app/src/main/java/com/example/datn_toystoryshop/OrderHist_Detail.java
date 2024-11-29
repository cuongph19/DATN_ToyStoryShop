package com.example.datn_toystoryshop;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.datn_toystoryshop.Contact_support.Chat_contact;
import com.example.datn_toystoryshop.Model.Product_Model;
import com.example.datn_toystoryshop.Server.APIService;
import com.example.datn_toystoryshop.Server.RetrofitClient;
import com.example.datn_toystoryshop.Profile.ContactSupport_screen;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderHist_Detail extends AppCompatActivity {
    private TextView tvProductName, tvOrderStatus, tvOrderRevenue;
    private ImageView ivProductImage, imgBack;
    private LinearLayout ivContactShop,ivSupportCenter;
    private APIService apiService;
    private Button btnRateProduct;
    private SharedPreferences sharedPreferences;
    private boolean nightMode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = getSharedPreferences("Settings", MODE_PRIVATE);
        nightMode = sharedPreferences.getBoolean("night", false);

        setContentView(R.layout.activity_order_hist_detail);
        apiService = RetrofitClient.getAPIService();

        Intent intent = getIntent();
        String prodId = intent.getStringExtra("prodId");
        String orderStatus = intent.getStringExtra("orderStatus");
        double orderRevenue = intent.getDoubleExtra("orderRevenue", 0);
        String orderContent = intent.getStringExtra("orderContent");
        Log.e("OrderHistoryAdapter", "j66666666666666666" + prodId);
        Log.e("OrderHistoryAdapter", "j66666666666666666" + orderStatus);
        Log.e("OrderHistoryAdapter", "j66666666666666666" + orderRevenue);
        Log.e("OrderHistoryAdapter", "j66666666666666666" + orderContent);



        tvProductName = findViewById(R.id.tvProductName);
        tvOrderStatus = findViewById(R.id.tvOrderStatus);
        tvOrderRevenue = findViewById(R.id.tvProductPrice);
//        tvOrderContent = findViewById(R.id.tvProductContent);
        ivProductImage = findViewById(R.id.tvProductImage);
        ivContactShop = findViewById(R.id.ivContactShop);
        ivSupportCenter = findViewById(R.id.ivSupportCenter);
        imgBack = findViewById(R.id.btnBack);
        btnRateProduct = findViewById(R.id.btnRateProduct);

        tvOrderStatus.setText(orderStatus);
        tvOrderRevenue.setText(String.format("%,.0fĐ", orderRevenue));
//        tvOrderContent.setText(orderContent);
        loadProductDetails(prodId);
        ivSupportCenter.setOnClickListener(v -> {
            Intent intent1 = new Intent(OrderHist_Detail.this, Chat_contact.class);
            startActivity(intent1);
            finish();
        });
        ivContactShop.setOnClickListener(v -> {
            Intent intent1 = new Intent(OrderHist_Detail.this, ContactSupport_screen.class);
            startActivity(intent1);
            finish();
        });
        btnRateProduct.setOnClickListener(v -> {
            /// đánh giá chưa làm
            Intent intent1 = new Intent(OrderHist_Detail.this, ContactSupport_screen.class);
            startActivity(intent1);
            finish();
        });

        imgBack.setOnClickListener(v -> onBackPressed());
        if (nightMode) {
            imgBack.setImageResource(R.drawable.back_icon);
        } else {
            imgBack.setImageResource(R.drawable.back_icon_1);
        }
    }

    private void loadProductDetails(String prodId) {
        // Gọi API để lấy thông tin sản phẩm
        apiService.getProductById(prodId).enqueue(new Callback<Product_Model>() {
            @Override
            public void onResponse(Call<Product_Model> call, Response<Product_Model> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Product_Model product = response.body();

                    // Hiển thị tên sản phẩm
                    tvProductName.setText(product.getNamePro());

                    // Hiển thị ảnh sản phẩm (dùng Glide để tải ảnh)
                    if (product.getImgPro() != null && !product.getImgPro().isEmpty()) {
                        Glide.with(OrderHist_Detail.this)
                                .load(product.getImgPro().get(0))  // Chọn ảnh đầu tiên trong danh sách ảnh
                                .into(ivProductImage);
                    }
                }
            }

            @Override
            public void onFailure(Call<Product_Model> call, Throwable t) {
                // Xử lý khi gọi API thất bại
            }
        });
    }
}