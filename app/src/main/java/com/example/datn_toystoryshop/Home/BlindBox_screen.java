package com.example.datn_toystoryshop.Home;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.datn_toystoryshop.Adapter.Product_Adapter;
import com.example.datn_toystoryshop.Model.Product_Model;
import com.example.datn_toystoryshop.R;
import com.example.datn_toystoryshop.Server.APIService;
import com.example.datn_toystoryshop.Server.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BlindBox_screen extends AppCompatActivity {
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private Product_Adapter adapter;
    private TextView headerTitle;
    private ImageView backIcon;
    private List<Product_Model> originalProductList = new ArrayList<>();

    private String documentId;
    private SharedPreferences sharedPreferences;
    private boolean nightMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blindbox);
        sharedPreferences = getSharedPreferences("Settings", MODE_PRIVATE);
        nightMode = sharedPreferences.getBoolean("night", false);
//        if (nightMode) {
//            imgBack.setImageResource(R.drawable.back_icon);
//        } else {
//            imgBack.setImageResource(R.drawable.back_icon_1);
//        }
        recyclerView = findViewById(R.id.product_list);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        headerTitle = findViewById(R.id.header_title);
        headerTitle.setText("Blind Box"); // Đặt tiêu đề là "Blind Box"
        Intent intent = getIntent();
        documentId = intent.getStringExtra("documentId");
        Log.e("OrderHistoryAdapter", "j8888888888888888BlindBox_screen" + documentId);
        // Gọi API và xử lý dữ liệu
        APIService apiService = RetrofitClient.getAPIService();
        apiService.getBlindBox().enqueue(new Callback<List<Product_Model>>() {
            @Override
            public void onResponse(Call<List<Product_Model>> call, Response<List<Product_Model>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Product_Model> products = response.body();
                    adapter = new Product_Adapter(BlindBox_screen.this, products, documentId);
                    recyclerView.setAdapter(adapter);
                } else {
                    Toast.makeText(BlindBox_screen.this, "Không có dữ liệu", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Product_Model>> call, Throwable t) {
                Toast.makeText(BlindBox_screen.this, "Lỗi kết nối API", Toast.LENGTH_SHORT).show();
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
        Button btnFilter = findViewById(R.id.btn_filter); // Nút bộ lọc
        btnFilter.setOnClickListener(v -> showFilterDialog());
        swipeRefreshLayout.setOnRefreshListener(() -> {
            apiService.getBlindBox().enqueue(new Callback<List<Product_Model>>() {
                @Override
                public void onResponse(Call<List<Product_Model>> call, Response<List<Product_Model>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        List<Product_Model> products = response.body();
                        originalProductList.clear();
                        originalProductList.addAll(products);

                        if (adapter == null) {
                            adapter = new Product_Adapter(BlindBox_screen.this, products, documentId);
                            recyclerView.setAdapter(adapter);
                        } else {
                            adapter.updateData(products); // Cập nhật dữ liệu trong adapter
                        }

                        swipeRefreshLayout.setRefreshing(false); // Tắt trạng thái "đang làm mới"
                    } else {
                        Toast.makeText(BlindBox_screen.this, "Không có dữ liệu", Toast.LENGTH_SHORT).show();
                        swipeRefreshLayout.setRefreshing(false); // Đảm bảo tắt trạng thái "đang làm mới"
                    }
                }

                @Override
                public void onFailure(Call<List<Product_Model>> call, Throwable t) {
                    Toast.makeText(BlindBox_screen.this, "Lỗi kết nối API", Toast.LENGTH_SHORT).show();
                    swipeRefreshLayout.setRefreshing(false); // Tắt trạng thái "đang làm mới" khi lỗi
                }
            });
        });

    }
    private void showFilterDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.filter_dialog, null);
        builder.setView(dialogView);
        AlertDialog dialog = builder.create();

        dialog.getWindow().setGravity(Gravity.START);
        dialog.show();

        CheckBox checkboxBrand1 = dialogView.findViewById(R.id.checkbox_brand_1);
        CheckBox checkboxBrand2 = dialogView.findViewById(R.id.checkbox_brand_2);
        CheckBox checkboxBrand3 = dialogView.findViewById(R.id.checkbox_brand_3);
        TextView tvCountBrand1 = dialogView.findViewById(R.id.tv_count_brand_1);
        TextView tvCountBrand2 = dialogView.findViewById(R.id.tv_count_brand_2);
        TextView tvCountBrand3 = dialogView.findViewById(R.id.tv_count_brand_3);

        tvCountBrand1.setText(String.valueOf(countProductsByBrand("BANPRESTO")));
        tvCountBrand2.setText(String.valueOf(countProductsByBrand("POP MART")));
        tvCountBrand3.setText(String.valueOf(countProductsByBrand("FUNISM")));

        EditText dialogMaxPrice = dialogView.findViewById(R.id.et_max_price);
        EditText dialogMinPrice = dialogView.findViewById(R.id.et_min_price);
        SeekBar dialogSeekBarMax = dialogView.findViewById(R.id.seekBar_price_max);

        dialogSeekBarMax.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                dialogMaxPrice.setText(progress + "đ");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        Button btnApplyFilter = dialogView.findViewById(R.id.btn_apply_filter);
        btnApplyFilter.setOnClickListener(v -> {
            boolean isBrand1Selected = checkboxBrand1.isChecked();
            boolean isBrand2Selected = checkboxBrand2.isChecked();
            boolean isBrand3Selected = checkboxBrand3.isChecked();

            int minPrice = Integer.parseInt(dialogMinPrice.getText().toString().replace("đ", "").trim());
            int maxPrice = dialogSeekBarMax.getProgress();

            if (!(isBrand1Selected || isBrand2Selected || isBrand3Selected)) {
                Toast.makeText(this, "Vui lòng chọn ít nhất một thương hiệu!", Toast.LENGTH_SHORT).show();
            } else {
                applyFilter(isBrand1Selected, isBrand2Selected, isBrand3Selected, minPrice, maxPrice);
                dialog.dismiss();
            }
        });
    }

    private void applyFilter(boolean isBrand1Selected, boolean isBrand2Selected, boolean isBrand3Selected, int minPrice, int maxPrice) {
        List<Product_Model> filteredList = new ArrayList<>();

        for (Product_Model product : originalProductList) {
            String brand = product.getBrand().trim();
            int price = (int) product.getPrice();

            if ((isBrand1Selected && brand.equals("BANPRESTO")) ||
                    (isBrand2Selected && brand.equals("POP MART")) ||
                    (isBrand3Selected && brand.equals("FUNISM"))) {

                if ((minPrice == 0 || price >= minPrice) &&
                        (maxPrice == 0 || price <= maxPrice)) {
                    filteredList.add(product);
                }
            }
        }

        if (filteredList.isEmpty()) {
            Toast.makeText(this, "Không có sản phẩm phù hợp với bộ lọc.", Toast.LENGTH_SHORT).show();
        }

        adapter.updateData(filteredList);
    }

    private int countProductsByBrand(String brandName) {
        int count = 0;
        if (originalProductList != null) {
            for (Product_Model product : originalProductList) {
                if (product.getBrand().trim().equalsIgnoreCase(brandName)) {
                    count++;
                }
            }
        }
        return count;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}