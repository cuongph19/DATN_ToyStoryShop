package com.example.datn_toystoryshop.Home;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.datn_toystoryshop.Adapter.ProductNewAdapter;
import com.example.datn_toystoryshop.Adapter.Product_Adapter;
import com.example.datn_toystoryshop.Home_screen;
import com.example.datn_toystoryshop.Model.Product_Model;
import com.example.datn_toystoryshop.R;

import java.util.List;

public class Popular_screen extends AppCompatActivity {
    private RecyclerView recyclerViewPopular;
    private Product_Adapter productAdapter;
    private List<Product_Model> productList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_popular_screen);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        // Khởi tạo RecyclerView
        recyclerViewPopular = findViewById(R.id.recyPopuPro);
        recyclerViewPopular.setLayoutManager(new LinearLayoutManager(this));
        ImageView imgBack = findViewById(R.id.ivBack);
        productList = (List<Product_Model>) getIntent().getSerializableExtra("productListPopu");


        // Thiết lập Adapter
        productAdapter = new Product_Adapter(this, productList);
        recyclerViewPopular.setAdapter(productAdapter);

        // Thêm ItemDecoration để tạo khoảng cách dưới mỗi item
        recyclerViewPopular.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                // Thêm khoảng cách dưới mỗi item (16px)
                outRect.bottom = 16;
            }
        });
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Popular_screen.this, Home_screen.class));
            }
        });
    }
}