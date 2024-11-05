package com.example.datn_toystoryshop;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.datn_toystoryshop.Adapter.Favorite_Adapter;
import com.example.datn_toystoryshop.Model.Favorite_Model;
import com.example.datn_toystoryshop.Server.APIService;
import com.example.datn_toystoryshop.Server.RetrofitClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Favorite_screen extends AppCompatActivity {
    private RecyclerView recyclerViewFavorites;
    private Favorite_Adapter favoriteAdapter;
    private ImageView imgBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_products);

        recyclerViewFavorites = findViewById(R.id.recyclerViewFavorites);
        recyclerViewFavorites.setLayoutManager(new LinearLayoutManager(this));

        loadFavoriteProducts();
        imgBack = findViewById(R.id.btnBack);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Favorite_screen.this, Home_screen.class);
                startActivity(intent);
                finish();
            }});
    }
    private void loadFavoriteProducts() {
        APIService apiService = RetrofitClient.getAPIService();
        apiService.getFavorites().enqueue(new Callback<List<Favorite_Model>>() {
            @Override
            public void onResponse(Call<List<Favorite_Model>> call, Response<List<Favorite_Model>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d("FavoriteScreen", "Favorites retrieved: " + response.body().size());
                    favoriteAdapter = new Favorite_Adapter(Favorite_screen.this, response.body(), apiService);
                    recyclerViewFavorites.setAdapter(favoriteAdapter);
                }
            }

            @Override
            public void onFailure(Call<List<Favorite_Model>> call, Throwable t) {
                // Xử lý lỗi khi gọi API thất bại
            }
        });
    }
}