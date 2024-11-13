package com.example.datn_toystoryshop;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.example.datn_toystoryshop.Fragment.History_Fragment;
import com.example.datn_toystoryshop.Model.Product_Model;
import com.example.datn_toystoryshop.Server.APIService;
import com.example.datn_toystoryshop.Server.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderHist_Detail extends AppCompatActivity {
    private TextView tvProductName, tvOrderStatus, tvOrderRevenue, tvOrderContent;
    private ImageView ivProductImage, imgBack;

    private APIService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_order_hist_detail);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        apiService = RetrofitClient.getAPIService();

        Intent intent = getIntent();
        String prodId = intent.getStringExtra("prodId");
        String orderStatus = intent.getStringExtra("orderStatus");
        double orderRevenue = intent.getDoubleExtra("orderRevenue", 0);
        String orderContent = intent.getStringExtra("orderContent");

        tvProductName = findViewById(R.id.tvProductName);
        tvOrderStatus = findViewById(R.id.tvOrderStatus);
        tvOrderRevenue = findViewById(R.id.tvProductPrice);
        tvOrderContent = findViewById(R.id.tvProductContent);
        ivProductImage = findViewById(R.id.tvProductImage);


        tvOrderStatus.setText(orderStatus);
        tvOrderRevenue.setText(String.format("%,.0fĐ", orderRevenue));
        tvOrderContent.setText(orderContent);
        loadProductDetails(prodId);


        imgBack = findViewById(R.id.btnBack);

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
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