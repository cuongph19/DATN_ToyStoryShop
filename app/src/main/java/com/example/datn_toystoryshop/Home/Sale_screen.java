package com.example.datn_toystoryshop.Home;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.datn_toystoryshop.Adapter.ProductNewAdapter;

import com.example.datn_toystoryshop.Adapter.Product_Adapter;
import com.example.datn_toystoryshop.Adapter.Sale_Adapter;
import com.example.datn_toystoryshop.Home_screen;
import com.example.datn_toystoryshop.Model.Product_Model;
import com.example.datn_toystoryshop.R;
import com.example.datn_toystoryshop.Server.APIService;
import com.example.datn_toystoryshop.Server.RetrofitClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Sale_screen extends AppCompatActivity {
    private RecyclerView recyclerViewSaleProducts;
   private Sale_Adapter saleAdapter;
    private List<Product_Model> productList;
    private String documentId;
    private  ImageView imgBack;
    private static final String CHANNEL_ID = "home_notification_channel";
    private static final String PREFS_NAME = "NotificationPrefs";
    private static final String NOTIFICATION_BLOCKED_KEY = "isNotificationBlocked";
    private NotificationManager notificationManager;
    private SharedPreferences sharedPreferences;
    private boolean nightMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sale_screen);

        sharedPreferences = getSharedPreferences("Settings", MODE_PRIVATE);
        nightMode = sharedPreferences.getBoolean("night", false);
//        if (nightMode) {
//            imgBack.setImageResource(R.drawable.back_icon);
//        } else {
//            imgBack.setImageResource(R.drawable.back_icon_1);
//        }

        // Khởi tạo NotificationManager
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Tạo Notification Channel nếu cần (dành cho Android 8.0 trở lên)
        createNotificationChannel();

        // Hiển thị thông báo chào mừng nếu thông báo đang được bật
        showWelcomeNotification();


        Intent intent = getIntent();
        documentId = intent.getStringExtra("documentId");
        Log.e("OrderHistoryAdapter", "j8888888888888888All_new_screen" + documentId);

        recyclerViewSaleProducts = findViewById(R.id.recyclerViewSaleProducts);

        recyclerViewSaleProducts.setLayoutManager(new GridLayoutManager(this,2));
        imgBack = findViewById(R.id.ivBack);
        APIService apiService = RetrofitClient.getAPIService();
        apiService.getSale().enqueue(new Callback<List<Product_Model>>() {
            @Override
            public void onResponse(Call<List<Product_Model>> call, Response<List<Product_Model>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Product_Model> products = response.body();
                    Log.d("API_RESPONSE", "Dữ liệu nhận được: " + products.toString()); // Kiểm tra dữ liệu trả về

                    saleAdapter = new Sale_Adapter(Sale_screen.this, products, documentId);
                    recyclerViewSaleProducts.setAdapter(saleAdapter);
                } else {
                    Log.e("API_RESPONSE", "Không có dữ liệu hoặc phản hồi không thành công: " + response.errorBody());
                    Toast.makeText(Sale_screen.this, "Không có dữ liệu", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Product_Model>> call, Throwable t) {
                Log.e("API_ERROR", "Lỗi: " + t.getMessage());
                Toast.makeText(Sale_screen.this, "Lỗi kết nối API", Toast.LENGTH_SHORT).show();
            }
        });


        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();            }
        });
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed(); // Quay lại activity trước đó
    }
    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Home Notification Channel";
            String description = "Channel for home screen notifications";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);

            // Đăng ký channel với hệ thống
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }
    }

    private void showWelcomeNotification() {
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        boolean isNotificationBlocked = sharedPreferences.getBoolean(NOTIFICATION_BLOCKED_KEY, false);

        if (!isNotificationBlocked) {
            // Tạo thông báo chào mừng
            Notification.Builder builder = new Notification.Builder(this, CHANNEL_ID)
                    .setSmallIcon(R.drawable.ic_logo) // Thay bằng icon của ứng dụng
                    .setContentTitle("Khuyến mãi đặc biệt!")
                    .setContentText("Giảm giá các sản phẩm đang hot. Mua ngay!")
                    .setPriority(Notification.PRIORITY_DEFAULT);

            if (notificationManager != null) {
                notificationManager.notify(2, builder.build());
            }

        } else {
            // Thông báo bị tắt
            Toast.makeText(this, "Thông báo đang bị tắt.", Toast.LENGTH_SHORT).show();
        }
    }

}