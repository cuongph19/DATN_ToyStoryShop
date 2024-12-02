package com.example.datn_toystoryshop.history;

import static java.security.AccessController.getContext;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.datn_toystoryshop.Adapter.OrderHistoryAdapter;
import com.example.datn_toystoryshop.Adapter.Order_History_Purchase_Adapter;
import com.example.datn_toystoryshop.Model.Order_Model;
import com.example.datn_toystoryshop.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


import com.example.datn_toystoryshop.R;
import com.example.datn_toystoryshop.Server.APIService;
import com.example.datn_toystoryshop.Server.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class History_purchase_screen extends AppCompatActivity {
    private SwipeRefreshLayout swipeRefreshLayout;
    private SharedPreferences sharedPreferences;
    private boolean nightMode;
    private Spinner spinnerMonth, spinnerYear;
    private RecyclerView rvOrderHistory;
    private Order_History_Purchase_Adapter adapter;
    private List<Order_Model> orderList = new ArrayList<>();
    private List<Order_Model> filteredOrderList = new ArrayList<>();
    private String documentId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_purchase_screen);
        sharedPreferences = getSharedPreferences("Settings", MODE_PRIVATE);
        nightMode = sharedPreferences.getBoolean("night", false);
        // Initialize UI components
        spinnerMonth = findViewById(R.id.spinnerMonth);
        spinnerYear = findViewById(R.id.spinnerYear);
        rvOrderHistory = findViewById(R.id.rvOrderHistory);
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);

        Intent intent = getIntent();
        documentId = intent.getStringExtra("documentId");
        Log.e("API_ERROR", "bttttttttttttttttttttt" + documentId);
        // Setup spinners
      //  setupSpinners();

        APIService apiService = RetrofitClient.getAPIService();
        adapter = new Order_History_Purchase_Adapter(this, filteredOrderList, apiService);
        rvOrderHistory.setAdapter(adapter);
        rvOrderHistory.setLayoutManager(new LinearLayoutManager(this));
        fetchOrders();
        swipeRefreshLayout.setOnRefreshListener(() -> {
            fetchOrders(); // Gọi lại API để làm mới danh sách
        });

    }
    private void setupSpinners() {
        // Populate month spinner
        List<String> months = new ArrayList<>();
        months.add("January");
        months.add("February");
        months.add("March");
        months.add("April");
        months.add("May");
        months.add("June");
        months.add("July");
        months.add("August");
        months.add("September");
        months.add("October");
        months.add("November");
        months.add("December");
        ArrayAdapter<String> monthAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, months);
        monthAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMonth.setAdapter(monthAdapter);

        // Populate year spinner
        List<String> years = new ArrayList<>();
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        for (int i = currentYear; i >= currentYear - 10; i--) {
            years.add(String.valueOf(i));
        }
        ArrayAdapter<String> yearAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, years);
        yearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerYear.setAdapter(yearAdapter);
    }

    private List<String> getSampleData() {
        // Replace with actual data from the server or database
        List<String> data = new ArrayList<>();
        data.add("Order #1");
        data.add("Order #2");
        data.add("Order #3");
        return data;
    }
    private void fetchOrders() {
        String cusId = documentId;
        Log.e("FavoriteScreen", "cusId không được để trống " + cusId);
        if (cusId == null || cusId.isEmpty()) {
            Log.e("FavoriteScreen", "cusId không được để trống");
            return;
        }
        APIService apiService = RetrofitClient.getAPIService();
        Call<List<Order_Model>> call = apiService.getOrders_successful(cusId);
        call.enqueue(new Callback<List<Order_Model>>() {
            @Override
            public void onResponse(Call<List<Order_Model>> call, Response<List<Order_Model>> response) {
                if (getContext() != null) {
                    if (response.isSuccessful() && response.body() != null) {
                        swipeRefreshLayout.setRefreshing(false);
                        orderList.clear();
                        orderList.addAll(response.body());
                        filteredOrderList.clear();
                        filteredOrderList.addAll(orderList);
                        adapter.notifyDataSetChanged();
                        Log.d("API Response", "Số lượng đơn hàngggggggggggg: " + response.body().size());
                        for (Order_Model order : response.body()) {
                            Log.d("Số lượng đơn hàngggggggggggg API Response", order.toString());
                        }
                    } else {
                        Toast.makeText(History_purchase_screen.this, "Không có dữ liệu đơn hàng", Toast.LENGTH_SHORT).show();
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Order_Model>> call, Throwable t) {
                if (getContext() != null) {
                    Toast.makeText(History_purchase_screen.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    swipeRefreshLayout.setRefreshing(false);
                }
            }
        });
    }
}