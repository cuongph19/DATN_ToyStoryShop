package com.example.datn_toystoryshop.Home;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.datn_toystoryshop.Adapter.Product_Adapter;
import com.example.datn_toystoryshop.Model.Product_Model;
import com.example.datn_toystoryshop.R;
import com.example.datn_toystoryshop.Server.APIService;
import com.example.datn_toystoryshop.Server.RetrofitClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Figuring_screen extends AppCompatActivity {
    private RecyclerView recyclerView;
    private Product_Adapter adapter;
    private TextView headerTitle;
    private ImageView backIcon;
    private String documentId;
    private SharedPreferences sharedPreferences;
    private boolean nightMode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_figuring);
        sharedPreferences = getSharedPreferences("Settings", MODE_PRIVATE);
        nightMode = sharedPreferences.getBoolean("night", false);
//        if (nightMode) {
//            imgBack.setImageResource(R.drawable.back_icon);
//        } else {
//            imgBack.setImageResource(R.drawable.back_icon_1);
//        }
        recyclerView = findViewById(R.id.product_list);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        headerTitle = findViewById(R.id.header_title);
        headerTitle.setText("Figuring"); // Đặt tiêu đề là "Figuring"
        Intent intent = getIntent();
        documentId = intent.getStringExtra("documentId");
        Log.e("OrderHistoryAdapter", "j8888888888888888Figuring_screen" + documentId);
        // Gọi API và xử lý dữ liệu
        APIService apiService = RetrofitClient.getAPIService();
        apiService.getFiguring().enqueue(new Callback<List<Product_Model>>() {
            @Override
            public void onResponse(Call<List<Product_Model>> call, Response<List<Product_Model>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Product_Model> products = response.body();
                    adapter = new Product_Adapter(Figuring_screen.this, products, documentId);
                    recyclerView.setAdapter(adapter);
                } else {
                    Toast.makeText(Figuring_screen.this, "Không có dữ liệu", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Product_Model>> call, Throwable t) {
                Toast.makeText(Figuring_screen.this, "Lỗi kết nối API", Toast.LENGTH_SHORT).show();
            }
        });

        // Xử lý nút back
        backIcon = findViewById(R.id.back_icon);
        backIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}