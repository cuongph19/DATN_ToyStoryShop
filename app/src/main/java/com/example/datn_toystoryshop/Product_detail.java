package com.example.datn_toystoryshop;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;

public class Product_detail extends AppCompatActivity {
    private TextView tvProductName, tvProductPrice;
    private ImageView imgProduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        // Ánh xạ các view
        tvProductName = findViewById(R.id.productTitle);
        tvProductPrice = findViewById(R.id.productPrice);
        imgProduct = findViewById(R.id.productImage);

        // Nhận dữ liệu từ Intent
        Intent intent = getIntent();
        String productId = intent.getStringExtra("productId");
        String productName = intent.getStringExtra("productName");
        double productPrice = intent.getDoubleExtra("productPrice", 0.0);
        String productImg = intent.getStringExtra("productImg");
        Log.e("Product_detail", "aaaaaaaaaaaaaaaa: " + productId);
        // Hiển thị dữ liệu
        tvProductName.setText(productName);
        tvProductPrice.setText(String.format("%,.0fđ", productPrice));
        Glide.with(this)
                .load(productImg)
                .placeholder(R.drawable.product1)
                .into(imgProduct);
    }
}