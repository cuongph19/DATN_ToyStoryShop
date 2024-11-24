package com.example.datn_toystoryshop.Home;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
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

import com.example.datn_toystoryshop.Home_screen;
import com.example.datn_toystoryshop.Model.Product_Model;
import com.example.datn_toystoryshop.R;

import java.util.List;

public class Sale_screen extends AppCompatActivity {
    private RecyclerView recyclerViewSaleProducts;
 //   private Sale_Adapter saleAdapter;
    private List<Product_Model> productList;
    private String documentId;
    private  ImageView imgBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sale_screen);
        Intent intent = getIntent();
        documentId = intent.getStringExtra("documentId");
        Log.e("OrderHistoryAdapter", "j8888888888888888All_new_screen" + documentId);

//        recyclerViewSaleProducts = findViewById(R.id.recyclerViewSaleProducts);
//        recyclerViewSaleProducts.setLayoutManager(new LinearLayoutManager(this));
//        imgBack = findViewById(R.id.ivBack);
//        productList = (List<Product_Model>) getIntent().getSerializableExtra("productList");
//
//        // Thiết lập Adapter
//        saleAdapter = new Sale_Adapter(this, productList, false, documentId);
//        recyclerViewSaleProducts.setAdapter(saleAdapter);
//
//        // Thêm ItemDecoration để tạo khoảng cách dưới mỗi item
//        recyclerViewSaleProducts.addItemDecoration(new RecyclerView.ItemDecoration() {
//            @Override
//            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
//                super.getItemOffsets(outRect, view, parent, state);
//                // Thêm khoảng cách dưới mỗi item (16px)
//                outRect.bottom = 16;
//            }
//        });

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Sale_screen.this, Home_screen.class));
            }
        });
    }
}