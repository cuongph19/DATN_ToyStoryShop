package com.example.datn_toystoryshop.history;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.datn_toystoryshop.Adapter.Order_History_Purchase_Adapter;
import com.example.datn_toystoryshop.Model.Order_Model;
import com.example.datn_toystoryshop.R;
import com.example.datn_toystoryshop.Server.APIService;
import com.example.datn_toystoryshop.Server.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class history_cancel_screen extends AppCompatActivity {
    private SwipeRefreshLayout swipeRefreshLayout;
    private SharedPreferences sharedPreferences;
    private boolean nightMode;
    private RecyclerView rvOrderHistory;
    private Order_History_Purchase_Adapter adapter;
    private List<Order_Model> orderList = new ArrayList<>();
    private List<Order_Model> filteredOrderList = new ArrayList<>();
    private String documentId;
    private ImageView imgBack;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_cancel_screen);

        // Khởi tạo các thành phần UI
        rvOrderHistory = findViewById(R.id.rvOrderHistory);
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        imgBack = findViewById(R.id.ivBack);

        sharedPreferences = getSharedPreferences("Settings", MODE_PRIVATE);
        nightMode = sharedPreferences.getBoolean("night", false);
        if (nightMode) {
            imgBack.setImageResource(R.drawable.back_icon);
        } else {
            imgBack.setImageResource(R.drawable.back_icon_1);
        }

        Intent intent = getIntent();
        documentId = intent.getStringExtra("documentId");

        imgBack.setOnClickListener(v -> onBackPressed());


        // Cấu hình RecyclerView và Adapter
        APIService apiService = RetrofitClient.getAPIService();
        adapter = new Order_History_Purchase_Adapter(this, filteredOrderList, apiService);
        rvOrderHistory.setAdapter(adapter);
        rvOrderHistory.setLayoutManager(new LinearLayoutManager(this));

        // Lấy dữ liệu từ API
        fetchOrders();

        swipeRefreshLayout.setOnRefreshListener(() -> fetchOrders());

    }

    private void fetchOrders() {
        if (documentId == null || documentId.isEmpty()) {
            Log.e("HistoryPurchase", "documentId không được để trống");
            return;
        }

        APIService apiService = RetrofitClient.getAPIService();
        Call<List<Order_Model>> call = apiService.getOrders_canceled(documentId);
        call.enqueue(new Callback<List<Order_Model>>() {
            @Override
            public void onResponse(Call<List<Order_Model>> call, Response<List<Order_Model>> response) {
                swipeRefreshLayout.setRefreshing(false);
                if (response.isSuccessful() && response.body() != null) {
                    orderList.clear();
                    orderList.addAll(response.body());
                    filteredOrderList.clear();
                    filteredOrderList.addAll(orderList);
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(history_cancel_screen.this, "Không có dữ liệu đơn hàng", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Order_Model>> call, Throwable t) {
                swipeRefreshLayout.setRefreshing(false);
                Toast.makeText(history_cancel_screen.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}
