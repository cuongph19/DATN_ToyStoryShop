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

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.datn_toystoryshop.Adapter.Order_History_Purchase_Adapter;
import com.example.datn_toystoryshop.Home_screen;
import com.example.datn_toystoryshop.Model.Order_Model;
import com.example.datn_toystoryshop.R;
import com.example.datn_toystoryshop.Register_login.Forgot_pass;
import com.example.datn_toystoryshop.Register_login.SignIn_screen;
import com.example.datn_toystoryshop.Server.APIService;
import com.example.datn_toystoryshop.Server.RetrofitClient;
import com.example.datn_toystoryshop.Shopping.Favorite_screen;

import java.util.ArrayList;
import java.util.List;

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
    private ImageView imgBack;
    private LinearLayout history_cancel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_purchase_screen);

        spinnerMonth = findViewById(R.id.spinnerMonth);
        spinnerYear = findViewById(R.id.spinnerYear);
        rvOrderHistory = findViewById(R.id.rvOrderHistory);
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        imgBack = findViewById(R.id.ivBack);
        history_cancel = findViewById(R.id.history_cancel);

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
        history_cancel.setOnClickListener(view -> {
            Intent intent1 = new Intent(History_purchase_screen.this, history_cancel_screen.class);
            intent1.putExtra("documentId", documentId);
            startActivity(intent1);
        });

        // Thiết lập Spinner
        setUpSpinners();

        // Cấu hình RecyclerView và Adapter
        APIService apiService = RetrofitClient.getAPIService();
        adapter = new Order_History_Purchase_Adapter(this, filteredOrderList, apiService,documentId);
        rvOrderHistory.setAdapter(adapter);
        rvOrderHistory.setLayoutManager(new LinearLayoutManager(this));

        // Lấy dữ liệu từ API
        fetchOrders();

        // Xử lý sự kiện chọn Spinner
        spinnerMonth.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                filterOrders();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        spinnerYear.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                filterOrders();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        swipeRefreshLayout.setOnRefreshListener(() -> fetchOrders());
    }

    private void setUpSpinners() {
        // Thiết lập Adapter cho Spinner tháng
        ArrayAdapter<CharSequence> monthAdapter = ArrayAdapter.createFromResource(
                this,
                R.array.months_array,
                android.R.layout.simple_spinner_item
        );
        monthAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMonth.setAdapter(monthAdapter);

        // Thiết lập Adapter cho Spinner năm
        ArrayList<String> years = new ArrayList<>();
        for (int i = 2024; i <= 2030; i++) {
            years.add(String.valueOf(i));
        }
        ArrayAdapter<String> yearAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                years
        );
        yearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerYear.setAdapter(yearAdapter);
    }

    private void fetchOrders() {
        if (documentId == null || documentId.isEmpty()) {
            Log.e("HistoryPurchase", "documentId không được để trống");
            return;
        }

        APIService apiService = RetrofitClient.getAPIService();
        Call<List<Order_Model>> call = apiService.getOrders_successful(documentId);
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
                    Toast.makeText(History_purchase_screen.this, "Không có dữ liệu đơn hàng", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Order_Model>> call, Throwable t) {
                swipeRefreshLayout.setRefreshing(false);
                Toast.makeText(History_purchase_screen.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void filterOrders() {
        String selectedMonth = spinnerMonth.getSelectedItem().toString();
        String selectedYear = spinnerYear.getSelectedItem().toString();

        if (selectedMonth.equals("...")) {
            filteredOrderList.clear();
            filteredOrderList.addAll(orderList);
        } else {
            String monthNumber = convertMonthNameToNumber(selectedMonth);
            filteredOrderList.clear();

            for (Order_Model order : orderList) {
                String orderDate = order.getOrderDate();
                String orderMonth = orderDate.substring(5, 7);
                String orderYear = orderDate.substring(0, 4);

                if (orderMonth.equals(monthNumber) && orderYear.equals(selectedYear)) {
                    filteredOrderList.add(order);
                }
            }
        }

        adapter.notifyDataSetChanged();
    }

    private String convertMonthNameToNumber(String monthName) {
        switch (monthName) {
            case "Tháng 1": return "01";
            case "Tháng 2": return "02";
            case "Tháng 3": return "03";
            case "Tháng 4": return "04";
            case "Tháng 5": return "05";
            case "Tháng 6": return "06";
            case "Tháng 7": return "07";
            case "Tháng 8": return "08";
            case "Tháng 9": return "09";
            case "Tháng 10": return "10";
            case "Tháng 11": return "11";
            case "Tháng 12": return "12";
            default: return "01";
        }
    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(History_purchase_screen.this, Home_screen.class);
        intent.putExtra("documentId", documentId);
        startActivity(intent);
        finish();
        super.onBackPressed();
    }

}
