package com.example.datn_toystoryshop.Home;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OtherProducts_screen extends AppCompatActivity {
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private Product_Adapter adapter; // Adapter để hiển thị danh sách sản phẩm
    private List<Product_Model> originalProductList = new ArrayList<>();
    private TextView headerTitle;
    private ImageView backIcon;

    private String documentId;
    private SharedPreferences sharedPreferences;
    private boolean nightMode;
    private int minPriceLimit = 0;// Giá tối đa là 1.000.000


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otherproducts);

        sharedPreferences = getSharedPreferences("Settings", MODE_PRIVATE);
        nightMode = sharedPreferences.getBoolean("night", false);
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        recyclerView = findViewById(R.id.product_list);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        headerTitle = findViewById(R.id.header_title);
        headerTitle.setText("Other Products");
        Intent intent = getIntent();
        documentId = intent.getStringExtra("documentId");


        APIService apiService = RetrofitClient.getAPIService();
        apiService.getOther().enqueue(new Callback<List<Product_Model>>() {
            @Override
            public void onResponse(Call<List<Product_Model>> call, Response<List<Product_Model>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    originalProductList = new ArrayList<>(response.body()); // Cập nhật danh sách gốc
                    updateBrandCounts();  // Cập nhật số lượng các thương hiệu
                    adapter = new Product_Adapter(OtherProducts_screen.this, originalProductList, documentId);
                    recyclerView.setAdapter(adapter);
                } else {
                    Toast.makeText(OtherProducts_screen.this, "Không có dữ liệu", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Product_Model>> call, Throwable t) {
                Toast.makeText(OtherProducts_screen.this, "Lỗi kết nối API", Toast.LENGTH_SHORT).show();
            }
        });

        backIcon = findViewById(R.id.back_icon);
        backIcon.setOnClickListener(v -> onBackPressed());
        Button btnFilter = findViewById(R.id.btn_filter); // Nút bộ lọc
        btnFilter.setOnClickListener(v -> showFilterDialog());
        swipeRefreshLayout.setOnRefreshListener(() -> {
            // Gọi lại API để làm mới danh sách sản phẩm
            apiService.getOther().enqueue(new Callback<List<Product_Model>>() {
                @Override
                public void onResponse(Call<List<Product_Model>> call, Response<List<Product_Model>> response) {
                    swipeRefreshLayout.setRefreshing(false); // Dừng hiệu ứng refresh
                    if (response.isSuccessful() && response.body() != null) {
                        originalProductList = response.body(); // Lưu danh sách sản phẩm mới
                        adapter.updateData(originalProductList); // Cập nhật dữ liệu trong adapter
                    } else {
                        Toast.makeText(OtherProducts_screen.this, "Không có dữ liệu mới.", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<List<Product_Model>> call, Throwable t) {
                    swipeRefreshLayout.setRefreshing(false); // Dừng hiệu ứng refresh
                    Toast.makeText(OtherProducts_screen.this, "Lỗi kết nối API", Toast.LENGTH_SHORT).show();
                }
            });
        });

    }


    private void showFilterDialog() {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(OtherProducts_screen.this);
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

        // Hiển thị số lượng sản phẩm theo từng thương hiệu
        tvCountBrand1.setText(String.valueOf(countProductsByBrand("BANPRESTO")));
        tvCountBrand2.setText(String.valueOf(countProductsByBrand("POP MART")));
        tvCountBrand3.setText(String.valueOf(countProductsByBrand("FUNISM")));

        EditText dialogMaxPrice = dialogView.findViewById(R.id.et_max_price);
        EditText dialogMinPrice = dialogView.findViewById(R.id.et_min_price);
        SeekBar dialogSeekBarMax = dialogView.findViewById(R.id.seekBar_price_max);

        // Thiết lập giá trị mặc định cho Max Price
        dialogMinPrice.setText(minPriceLimit + "đ");
        dialogMinPrice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Loại bỏ ký tự "đ" nếu đã có trong giá trị nhập
                String input = s.toString().replace("đ", "").trim();

                // Kiểm tra xem người dùng có nhập giá trị không
                if (!input.isEmpty()) {
                    try {
                        // Chuyển đổi giá trị nhập thành số nguyên
                        int minPrice = Integer.parseInt(input);

                        // Đặt lại văn bản với ký tự "đ" ở cuối
                        dialogMinPrice.removeTextChangedListener(this); // Ngăn chặn vòng lặp vô tận
                        dialogMinPrice.setText(minPrice + "đ");
                        dialogMinPrice.setSelection(dialogMinPrice.getText().length() - 1); // Đặt con trỏ trước ký tự "đ"
                        dialogMinPrice.addTextChangedListener(this);

                        // Kiểm tra điều kiện min > max và xử lý nếu cần
                        int maxPrice = dialogSeekBarMax.getProgress();
                        if (minPrice > maxPrice) {
                            Toast.makeText(OtherProducts_screen.this, "Giá trị min không được lớn hơn max", Toast.LENGTH_SHORT).show();
                        }

                    } catch (NumberFormatException e) {
                        // Xử lý ngoại lệ nếu có lỗi khi chuyển đổi giá trị
                        dialogMinPrice.setText("0đ");
                    }
                }
            }
        });

        // TextWatcher để thêm ký tự "đ" cho maxPrice
        dialogMaxPrice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String input = s.toString().replace("đ", "").trim();

                if (!input.isEmpty()) {
                    try {
                        int maxPrice = Integer.parseInt(input);
                        dialogMaxPrice.removeTextChangedListener(this);
                        dialogMaxPrice.setText(maxPrice + "đ");
                        dialogMaxPrice.setSelection(dialogMaxPrice.getText().length() - 1);
                        dialogMaxPrice.addTextChangedListener(this);

                        dialogSeekBarMax.setProgress(maxPrice);
                        int minPrice = Integer.parseInt(dialogMinPrice.getText().toString().replace("đ", "").trim());
                        if (minPrice > maxPrice) {
                            Toast.makeText(OtherProducts_screen.this, "Giá trị min không được lớn hơn max", Toast.LENGTH_SHORT).show();
                        }
                    } catch (NumberFormatException e) {
                        dialogMaxPrice.setText("1000000đ");
                    }
                }
            }
        });


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

            // Lấy giá trị từ EditText hoặc SeekBar cho minPrice và maxPrice
            int minPrice = Integer.parseInt(dialogMinPrice.getText().toString().replace("đ", "").trim()); // Loại bỏ "đ" và khoảng trắng
            int maxPrice = dialogSeekBarMax.getProgress(); // Giá trị từ SeekBar max

            // Kiểm tra xem có ít nhất một thương hiệu được chọn
            if (!(isBrand1Selected || isBrand2Selected || isBrand3Selected)) {
                Toast.makeText(OtherProducts_screen.this, "Vui lòng chọn ít nhất một thương hiệu!", Toast.LENGTH_SHORT).show();
            } else {
                // Áp dụng bộ lọc với các thương hiệu đã chọn và khoảng giá min-max
                applyFilter(isBrand1Selected, isBrand2Selected, isBrand3Selected, minPrice, maxPrice);
                dialog.dismiss();
            }
        });

    }

    private void applyFilter(boolean isBrand1Selected, boolean isBrand2Selected, boolean isBrand3Selected, int minPrice, int maxPrice) {
        if (originalProductList == null) {
            Toast.makeText(this, "Danh sách sản phẩm chưa được tải.", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean hasBrand1 = false, hasBrand2 = false, hasBrand3 = false;

        // Kiểm tra xem có sản phẩm thuộc thương hiệu được chọn hay không
        for (Product_Model product : originalProductList) {
            String brand = product.getBrand().trim();

            if (brand.equals("BANPRESTO")) hasBrand1 = true;
            if (brand.equals("POP MART")) hasBrand2 = true;
            if (brand.equals("FUNISM")) hasBrand3 = true;
        }

        // Hiển thị thông báo nếu không có sản phẩm nào thuộc thương hiệu đã chọn và dừng tiến trình lọc
        if (isBrand1Selected && !hasBrand1) {
            Toast.makeText(this, "Không có sản phẩm thuộc thương hiệu BANPRESTO.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (isBrand2Selected && !hasBrand2) {
            Toast.makeText(this, "Không có sản phẩm thuộc thương hiệu POP MART.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (isBrand3Selected && !hasBrand3) {
            Toast.makeText(this, "Không có sản phẩm thuộc thương hiệu FUNISM.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Nếu có sản phẩm phù hợp, tiếp tục lọc
        List<Product_Model> filteredList = new ArrayList<>();
        for (Product_Model product : originalProductList) {
            String brand = product.getBrand().trim();
            int price = (int) product.getPrice();

            if ((isBrand1Selected && brand.equals("BANPRESTO")) ||
                    (isBrand2Selected && brand.equals("POP MART")) ||
                    (isBrand3Selected && brand.equals("FUNISM"))) {
                if ((minPrice == 0 || price >= minPrice) && (maxPrice == 0 || price <= maxPrice)) {
                    filteredList.add(product);
                }
            }
        }

        // Kiểm tra nếu không có sản phẩm nào phù hợp với bộ lọc giá
        if (filteredList.isEmpty()) {
            Toast.makeText(this, "Không có sản phẩm phù hợp với bộ lọc giá.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Cập nhật Adapter với danh sách sản phẩm đã lọc
        adapter.updateData(filteredList);
    }






    private int countProductsByBrand(String brandName) {
        int count = 0;

        if (originalProductList != null) {
            for (Product_Model product : originalProductList) {
                String brand = product.getBrand().trim(); // Loại bỏ khoảng trắng thừa
                Log.d("Brand Count", "Checking product: " + brand);  // Debugging để xem brand hiện tại

                if (brand.equalsIgnoreCase(brandName)) {
                    count++;
                }
            }
        } else {
            Log.e("Brand Count", "originalProductList is null");
        }

        Log.d("Brand Count", "Total count for " + brandName + ": " + count); // Debugging tổng số
        return count;
    }
    // Lưu trữ số lượng các thương hiệu trong danh sách gốc
    private Map<String, Integer> brandCounts = new HashMap<>();

    private void updateBrandCounts() {
        brandCounts.clear();
        for (Product_Model product : originalProductList) {
            String brand = product.getBrand().trim();
            brandCounts.put(brand, brandCounts.getOrDefault(brand, 0) + 1);
        }
    }
}
