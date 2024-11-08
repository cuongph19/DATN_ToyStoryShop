package com.example.datn_toystoryshop.Home;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.datn_toystoryshop.Adapter.ArtStoryAdapter;
import com.example.datn_toystoryshop.Adapter.Product_Adapter;
import com.example.datn_toystoryshop.Model.ArtStoryModel;
import com.example.datn_toystoryshop.Model.Product_Model;
import com.example.datn_toystoryshop.R;
import com.example.datn_toystoryshop.Server.APIService;
import com.example.datn_toystoryshop.Server.RetrofitClient;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ArtStory_screen extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ArtStoryAdapter adapter;
    private TextView headerTitle;
    private ImageView backIcon;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artstory);
        ImageView ivBack = findViewById(R.id.ivBack); // Lấy đối tượng ImageView
        recyclerView = findViewById(R.id.product_list);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 1));
        headerTitle = findViewById(R.id.header_title);
        headerTitle.setText("Art Story"); // Đặt tiêu đề là "Blind Box"
        fetchArtStories();


        // Xử lý nút back
        backIcon = findViewById(R.id.back_icon);
        backIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void fetchArtStories() {
        APIService apiService = RetrofitClient.getAPIService();

        apiService.getArtStories().enqueue(new Callback<List<ArtStoryModel>>() {
            @Override
            public void onResponse(Call<List<ArtStoryModel>> call, Response<List<ArtStoryModel>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    adapter = new ArtStoryAdapter(ArtStory_screen.this, response.body());
                    recyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<List<ArtStoryModel>> call, Throwable t) {
                Toast.makeText(ArtStory_screen.this, "Failed to load data", Toast.LENGTH_SHORT).show();
            }
        });


    }
    @Override
    public void onBackPressed () {
        super.onBackPressed(); // Quay lại activity trước đó
    }


}