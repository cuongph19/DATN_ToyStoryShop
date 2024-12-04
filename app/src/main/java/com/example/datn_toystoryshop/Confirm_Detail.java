package com.example.datn_toystoryshop;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.datn_toystoryshop.Adapter.OrderHist_Detail_Adapter;
import com.example.datn_toystoryshop.Contact_support.Chat_contact;
import com.example.datn_toystoryshop.Model.Order_Model;
import com.example.datn_toystoryshop.Profile.ContactSupport_screen;
import com.example.datn_toystoryshop.Server.APIService;
import com.example.datn_toystoryshop.Server.RetrofitClient;

import java.text.NumberFormat;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Confirm_Detail extends AppCompatActivity {

    private SwipeRefreshLayout swipeRefreshLayout;
    private TextView tvTotalPrice, tvOrderStatus, tvPaymentMethod, address_name, address_phone, address_detail,btncancelOrder;
    private ImageView imgBack;
    private LinearLayout ivContactShop, ivSupportCenter;
    private APIService apiService;
    private SharedPreferences sharedPreferences;
    private boolean nightMode;
    private RecyclerView rvProductList;
    private OrderHist_Detail_Adapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_detail);

        tvOrderStatus = findViewById(R.id.tvOrderStatus);
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        tvTotalPrice = findViewById(R.id.tvTotalPrice);
        tvPaymentMethod = findViewById(R.id.tvPaymentMethod);
        address_name = findViewById(R.id.address_name);
        address_phone = findViewById(R.id.address_phone);
        address_detail = findViewById(R.id.address_detail);
        ivContactShop = findViewById(R.id.ivContactShop);
        rvProductList = findViewById(R.id.rvProductList);
        ivSupportCenter = findViewById(R.id.ivSupportCenter);
        imgBack = findViewById(R.id.btnBack);
        btncancelOrder = findViewById(R.id.btncancelOrder);

        sharedPreferences = getSharedPreferences("Settings", MODE_PRIVATE);
        nightMode = sharedPreferences.getBoolean("night", false);
        apiService = RetrofitClient.getAPIService();

        Intent intent = getIntent();
        String orderId = intent.getStringExtra("orderId");
        Log.e("OrderHistoryAdapter", "j66666666666666666gggghhhhConfirm_Detail " );
        loadOrderDetails(orderId);
        rvProductList.setLayoutManager(new LinearLayoutManager(this));

        imgBack.setOnClickListener(v -> onBackPressed());
        ivSupportCenter.setOnClickListener(v -> {
            Intent intent1 = new Intent(Confirm_Detail.this, Chat_contact.class);
            startActivity(intent1);
            finish();
        });
        ivContactShop.setOnClickListener(v -> {
            Intent intent1 = new Intent(Confirm_Detail.this, ContactSupport_screen.class);
            startActivity(intent1);
            finish();
        });
        btncancelOrder.setOnClickListener(v -> {
            // Tạo dialog xác nhận
            new AlertDialog.Builder(Confirm_Detail.this)
                    .setTitle("Xác nhận hủy đơn hàng")
                    .setMessage("Bạn có chắc chắn muốn hủy đơn hàng này không?")
                    .setPositiveButton("Đồng ý", (dialog, which) -> {
                        String newStatus = "Đã hủy"; // Trạng thái mới
                        deleteOrder(orderId, newStatus);

                        // Chuyển về Home_screen
                        Intent intent1 = new Intent(Confirm_Detail.this, Home_screen.class);
                        startActivity(intent1);
                    })
                    .setNegativeButton("Hủy", (dialog, which) -> {
                        // Người dùng chọn "Hủy", chỉ đóng dialog
                        dialog.dismiss();
                    })
                    .show();
        });

        imgBack.setOnClickListener(v -> onBackPressed());
        if (nightMode) {
            imgBack.setImageResource(R.drawable.back_icon);
        } else {
            imgBack.setImageResource(R.drawable.back_icon_1);
        }
        swipeRefreshLayout.setOnRefreshListener(() -> {
            loadOrderDetails(orderId); // Gọi lại API để làm mới danh sách
        });
    }
    private void deleteOrder(String orderId, String newStatus) {
        // Tạo model để gửi dữ liệu
        Order_Model orderModel = new Order_Model();
        orderModel.setOrderStatus(newStatus); // Thiết lập trạng thái đơn hàng mới

        // Gọi API qua Retrofit
        Call<Order_Model> call = apiService.putorderUpdate(orderId, orderModel);
        call.enqueue(new Callback<Order_Model>() {
            @Override
            public void onResponse(Call<Order_Model> call, Response<Order_Model> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // Thành công, hiển thị kết quả
                    Toast.makeText(Confirm_Detail.this, "Cập nhật thành công!", Toast.LENGTH_SHORT).show();
                    Log.d("API", "Cập nhật thành công: " + response.body().toString());
                } else {
                    // Xử lý lỗi trả về từ server
                    Toast.makeText(Confirm_Detail.this, "Cập nhật thất bại!", Toast.LENGTH_SHORT).show();
                    Log.e("API", "Lỗi trả về: " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<Order_Model> call, Throwable t) {
                // Lỗi kết nối hoặc các lỗi khác
                Toast.makeText(Confirm_Detail.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("API", "Lỗi kết nối: " + t.getMessage());
            }
        });
    }

    private void loadOrderDetails(String orderId) {
        // Gọi API để lấy thông tin sản phẩm
        apiService.getOrderById(orderId).enqueue(new Callback<Order_Model>() {
            @Override
            public void onResponse(Call<Order_Model> call, Response<Order_Model> response) {
                if (response.isSuccessful() && response.body() != null) {
                    swipeRefreshLayout.setRefreshing(false);
                    Order_Model orderModel = response.body();

                    // Cập nhật thông tin đơn hàng
                    tvOrderStatus.setText(orderModel.getOrderStatus());
                    tvPaymentMethod.setText(orderModel.getPayment_method());
                    address_name.setText(orderModel.getName_order());
                    address_phone.setText(orderModel.getPhone_order());
                    address_detail.setText(orderModel.getAddress_order());

                    int revenueAll = orderModel.getRevenue_all();
                    NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
                    String formattedRevenue = currencyFormat.format(revenueAll);
                    tvTotalPrice.setText( formattedRevenue);

                    if (orderModel.getProdDetails() != null && !orderModel.getProdDetails().isEmpty()) {
                        APIService apiService = RetrofitClient.getAPIService();
                        adapter = new OrderHist_Detail_Adapter(Confirm_Detail.this, orderModel.getProdDetails(),apiService);
                        rvProductList.setAdapter(adapter);
                    } else {
                        Log.e("OrderHist_Detail", "Danh sách sản phẩm rỗng hoặc null");
                        swipeRefreshLayout.setRefreshing(false);
                    }
                } else {
                    Log.e("OrderHist_Detail", "Response thất bại hoặc body null");
                    swipeRefreshLayout.setRefreshing(false);
                }
            }

            @Override
            public void onFailure(Call<Order_Model> call, Throwable t) {
                swipeRefreshLayout.setRefreshing(false);
                // Xử lý khi gọi API thất bại
            }
        });
    }
}