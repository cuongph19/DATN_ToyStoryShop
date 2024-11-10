package com.example.datn_toystoryshop;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.datn_toystoryshop.Adapter.Cart_Adapter;
import com.example.datn_toystoryshop.Model.Product_Model;
import com.example.datn_toystoryshop.Profile.Setting_screen;
import com.example.datn_toystoryshop.Setting.UpdateInfo_screen;

import java.util.ArrayList;
import java.util.List;

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
    private List<Product_Model> productList;
    private int currentQuantity;
    private String customerId;
    private String selectedColor;
    private RecyclerView recyclerViewCart;
    private Cart_Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_cart_screen);

        imgBack = findViewById(R.id.imgBack_cart);
        recyclerViewCart = findViewById(R.id.recyclerViewCart);
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
        selectedColor = intent.getStringExtra("selectedColor");

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(My_cart_screen.this, Home_screen.class);
                startActivity(intent);
                finish();
            }});
        // Initialize product list and add sample products
        productList = new ArrayList<>();
        productList.add(new Product_Model("Ghế văn phòng ergonomic", "759.000", "1.300.000"));

        // Set up RecyclerView with LinearLayoutManager and Adapter
        recyclerViewCart.setLayoutManager(new LinearLayoutManager(this));
        adapter = new Cart_Adapter(this, productList);
        recyclerViewCart.setAdapter(adapter);
    }
    }

