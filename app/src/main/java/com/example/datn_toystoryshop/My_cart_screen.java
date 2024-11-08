package com.example.datn_toystoryshop;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.datn_toystoryshop.Profile.Setting_screen;
import com.example.datn_toystoryshop.Setting.UpdateInfo_screen;

import java.util.ArrayList;

public class My_cart_screen extends AppCompatActivity {

    private TextView btnIncrease, btnDecrease;
    private TextView tvQuantity, add_address,aaa;
    private ImageView imgBack;
    private String productId;
    private int owerId;
    private boolean statusPro;
    private double productPrice;
    private String desPro;
    private String creatDatePro;
    private int quantity;
    private String listPro;
    private ArrayList<String> productImg;
    private String productName;
    private int cateId;
    private String brand;
    private String favoriteId;

    private int currentQuantity;
    private String customerId;
    private String productSpecification;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_cart_screen);

        // Tìm các view theo ID
        btnIncrease = findViewById(R.id.btnIncrease);
        btnDecrease = findViewById(R.id.btnDecrease);
        tvQuantity = findViewById(R.id.tvQuantity);
        add_address = findViewById(R.id.add_address);
        imgBack = findViewById(R.id.imgBack_cart);
        ////test xem nhận được dữ liệu sang không
        aaa = findViewById(R.id.aaa);

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

        // Nhận thêm thuộc tính currentQuantity, customerId, và productSpecification
        currentQuantity = intent.getIntExtra("currentQuantity", 1);
        customerId = intent.getStringExtra("customerId");
        productSpecification = intent.getStringExtra("productSpecification");
        aaa.setText(productName);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(My_cart_screen.this, Home_screen.class);
                startActivity(intent);
                finish();
            }});

        add_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Chuyển sang UpdateInfo_screen
                Intent intent = new Intent(My_cart_screen.this, Add_address_screen.class);

                startActivity(intent);
            }
        });
        // Thiết lập hành động khi bấm nút tăng
        btnIncrease.setOnClickListener(v -> {
            quantity = Integer.parseInt(tvQuantity.getText().toString());
            tvQuantity.setText(String.valueOf(quantity + 1));
        });

        // Thiết lập hành động khi bấm nút giảm
        btnDecrease.setOnClickListener(v -> {
            quantity = Integer.parseInt(tvQuantity.getText().toString());
            if (quantity > 1) {
                tvQuantity.setText(String.valueOf(quantity - 1));
            }
        });
    }
}
