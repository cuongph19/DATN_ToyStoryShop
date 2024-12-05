package com.example.datn_toystoryshop.Shopping;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.datn_toystoryshop.Adapter.Favorite_Adapter;
import com.example.datn_toystoryshop.Home_screen;
import com.example.datn_toystoryshop.Model.Favorite_Model;
import com.example.datn_toystoryshop.R;
import com.example.datn_toystoryshop.Server.APIService;
import com.example.datn_toystoryshop.Server.RetrofitClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Favorite_screen extends AppCompatActivity {
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerViewFavorites;
    private Favorite_Adapter favoriteAdapter;
    private ImageView imgBack;
    private String documentId;
    private SharedPreferences sharedPreferences;
    private boolean nightMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_products);

        sharedPreferences = getSharedPreferences("Settings", MODE_PRIVATE);
        nightMode = sharedPreferences.getBoolean("night", false);
        Intent intent = getIntent();
        documentId = intent.getStringExtra("documentId");


        recyclerViewFavorites = findViewById(R.id.recyclerViewFavorites);
        recyclerViewFavorites.setLayoutManager(new LinearLayoutManager(this));
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(() -> {
            loadFavoriteProducts(); // Gọi lại API để làm mới danh sách
        });
        loadFavoriteProducts();
        imgBack = findViewById(R.id.btnBack);
        if (nightMode) {
            imgBack.setImageResource(R.drawable.back_icon);
        } else {
            imgBack.setImageResource(R.drawable.back_icon_1);
        }
        imgBack.setOnClickListener(v -> onBackPressed());
    }

    private void loadFavoriteProducts() {
        String cusId = documentId;

        if (cusId == null || cusId.isEmpty()) {
            Log.e("FavoriteScreen", "cusId không được để trống");
            return;
        }

        APIService apiService = RetrofitClient.getAPIService();
        apiService.getFavorites(cusId).enqueue(new Callback<List<Favorite_Model>>() {
            @Override
            public void onResponse(Call<List<Favorite_Model>> call, Response<List<Favorite_Model>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    swipeRefreshLayout.setRefreshing(false);
                    Log.d("FavoriteScreen", "Favorites retrieved: " + response.body().size());
                    favoriteAdapter = new Favorite_Adapter(Favorite_screen.this, response.body(), apiService, documentId);
                    recyclerViewFavorites.setAdapter(favoriteAdapter);
                }
            }

            @Override
            public void onFailure(Call<List<Favorite_Model>> call, Throwable t) {
                swipeRefreshLayout.setRefreshing(false);
                // Xử lý lỗi khi gọi API thất bại
            }
        });
    }
}