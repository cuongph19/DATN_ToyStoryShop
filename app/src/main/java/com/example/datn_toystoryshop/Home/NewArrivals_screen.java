package com.example.datn_toystoryshop.Home;

import android.content.Intent;
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

public class NewArrivals_screen extends AppCompatActivity {
    private RecyclerView recyclerView;
    private Product_Adapter adapter; // Adapter để hiển thị danh sách sản phẩm
    private List<Product_Model> productList;
    private TextView headerTitle;
    private ImageView backIcon;
    private String documentId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newarrivals);

        recyclerView = findViewById(R.id.product_list);
        recyclerView.setLayoutManager(new GridLayoutManager(this,2));
        headerTitle = findViewById(R.id.header_title);
        headerTitle.setText("New Arrivals"); // Đặt tiêu đề là "Blind Box"

        Intent intent = getIntent();
        documentId = intent.getStringExtra("documentId");
        Log.e("OrderHistoryAdapter", "j8888888888888888Cart_screenNewArrivals_screen" + documentId);
        APIService apiService = RetrofitClient.getAPIService();
        apiService.getNewArrivals().enqueue(new Callback<List<Product_Model>>() {
            @Override
            public void onResponse(Call<List<Product_Model>> call, Response<List<Product_Model>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Product_Model> products = response.body();
                    Log.d("API_RESPONSE", "Dữ liệu nhận được: " + products.toString()); // Kiểm tra dữ liệu trả về

                    adapter = new Product_Adapter(NewArrivals_screen.this, products, documentId);
                    recyclerView.setAdapter(adapter);
                } else {
                    Log.e("API_RESPONSE", "Không có dữ liệu hoặc phản hồi không thành công: " + response.errorBody());
                    Toast.makeText(NewArrivals_screen.this, "Không có dữ liệu", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Product_Model>> call, Throwable t) {
                Log.e("API_ERROR", "Lỗi: " + t.getMessage());
                Toast.makeText(NewArrivals_screen.this, "Lỗi kết nối API", Toast.LENGTH_SHORT).show();
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
        super.onBackPressed(); // Quay lại activity trước đó
    }
}