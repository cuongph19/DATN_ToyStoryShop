package com.example.datn_toystoryshop.Home;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.datn_toystoryshop.Adapter.ProductNewAdapter;
import com.example.datn_toystoryshop.Home_screen;
import com.example.datn_toystoryshop.Model.Product_Model;
import com.example.datn_toystoryshop.R;

import java.util.List;

public class All_new_screen extends AppCompatActivity {
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerViewAllNewProducts;
    private ProductNewAdapter productNewAdapter;
    private List<Product_Model> productList;
    private String documentId;
    private SharedPreferences sharedPreferences;
    private boolean nightMode;
    private ImageView imgBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_new_screen);

        recyclerViewAllNewProducts = findViewById(R.id.recyclerViewAllNewProducts);
        imgBack = findViewById(R.id.ivBack);
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);

        Intent intent = getIntent();
        documentId = intent.getStringExtra("documentId");
        sharedPreferences = getSharedPreferences("Settings", MODE_PRIVATE);
        nightMode = sharedPreferences.getBoolean("night", false);
        productList = (List<Product_Model>) getIntent().getSerializableExtra("productList");
        recyclerViewAllNewProducts.setLayoutManager(new LinearLayoutManager(this));
        // Thiết lập Adapter
        productNewAdapter = new ProductNewAdapter(this, productList, false, documentId);
        recyclerViewAllNewProducts.setAdapter(productNewAdapter);

        // Thêm ItemDecoration để tạo khoảng cách dưới mỗi item
        recyclerViewAllNewProducts.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                // Thêm khoảng cách dưới mỗi item (16px)
                outRect.bottom = 16;
            }
        });
        if (nightMode) {
            imgBack.setImageResource(R.drawable.back_icon);
        } else {
            imgBack.setImageResource(R.drawable.back_icon_1);
        }
        swipeRefreshLayout.setOnRefreshListener(() -> {
            productNewAdapter.notifyDataSetChanged();
            // Tắt hiệu ứng tải lại
            swipeRefreshLayout.setRefreshing(false);
        });
        imgBack.setOnClickListener(v -> onBackPressed());
    }
}