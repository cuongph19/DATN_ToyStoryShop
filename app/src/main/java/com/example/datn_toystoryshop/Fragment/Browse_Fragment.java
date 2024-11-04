package com.example.datn_toystoryshop.Fragment;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

public class Browse_Fragment extends Fragment {

    private RecyclerView recyclerView;
    private Product_Adapter productAdapter;
    private List<Product_Model> productList; // Danh sách hiện tại đang hiển thị trên RecyclerView
    private List<Product_Model> originalProductList; // Danh sách gốc lưu toàn bộ sản phẩm từ API
    private Button btnFilter;
    private int maxPriceLimit = 10000000;
    private int minPriceLimit = 0;// Giá tối đa là 1.000.000

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_browse, container, false);

        recyclerView = view.findViewById(R.id.recycler_view_products);
        btnFilter = view.findViewById(R.id.btnFilter);

        // Thiết lập LayoutManager cho RecyclerView
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));

        // Khởi tạo danh sách sản phẩm
        productList = new ArrayList<>();
        productAdapter = new Product_Adapter(getContext(), productList);
        recyclerView.setAdapter(productAdapter);

        // Gọi API để lấy sản phẩm từ MongoDB
        getProductsFromApi();

        // Xử lý sự kiện nhấn nút bộ lọc
        btnFilter.setOnClickListener(v -> showFilterDialog());

        return view;
    }

    private void getProductsFromApi() {
        APIService apiService = RetrofitClient.getAPIService();
        Call<List<Product_Model>> call = apiService.getProducts();

        call.enqueue(new Callback<List<Product_Model>>() {
            @Override
            public void onResponse(Call<List<Product_Model>> call, Response<List<Product_Model>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    originalProductList = new ArrayList<>(response.body()); // Lưu toàn bộ sản phẩm vào originalProductList
                    productList.clear();
                    productList.addAll(originalProductList); // Sao chép originalProductList vào productList
                    productAdapter.notifyDataSetChanged(); // Cập nhật RecyclerView
                }
            }

            @Override
            public void onFailure(Call<List<Product_Model>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void showFilterDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.filter_dialog, null);
        builder.setView(dialogView);
        AlertDialog dialog = builder.create();

        dialog.getWindow().setGravity(Gravity.START);
        dialog.show();

        CheckBox checkboxBrand1 = dialogView.findViewById(R.id.checkbox_brand_1);
        CheckBox checkboxBrand2 = dialogView.findViewById(R.id.checkbox_brand_2);
        CheckBox checkboxBrand3 = dialogView.findViewById(R.id.checkbox_brand_3);
        EditText dialogMaxPrice = dialogView.findViewById(R.id.et_max_price);
        EditText dialogMinPrice = dialogView.findViewById(R.id.et_min_price);
        SeekBar dialogSeekBarMax = dialogView.findViewById(R.id.seekBar_price_max);

        // Thiết lập giá trị mặc định cho Max Price
        dialogSeekBarMax.setMax(maxPriceLimit); // Đặt giá trị tối đa cho SeekBar Max
        dialogMinPrice.setText(minPriceLimit + "đ");

        dialogSeekBarMax.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // Kiểm tra nếu giá trị nhỏ hơn 100000
                if (progress < 100000) {
                    seekBar.setProgress(100000); // Đặt lại giá trị về 100000
                    dialogMaxPrice.setText(100000 + "đ"); // Cập nhật giá hiển thị
                } else {
                    dialogMaxPrice.setText(progress + "đ"); // Cập nhật giá trị Max khi SeekBar thay đổi
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        Button btnApplyFilter = dialogView.findViewById(R.id.btn_apply_filter);
        btnApplyFilter.setOnClickListener(v -> {
            boolean isBrand1Selected = checkboxBrand1.isChecked();
            boolean isBrand2Selected = checkboxBrand2.isChecked();
            boolean isBrand3Selected = checkboxBrand3.isChecked();
            int maxPrice = dialogSeekBarMax.getProgress(); // Lấy giá trị từ SeekBar Max

            // Kiểm tra điều kiện trước khi áp dụng bộ lọc
            if ((isBrand1Selected || isBrand2Selected || isBrand3Selected) && maxPrice == 0) {
                // Người dùng đã chọn ít nhất một checkbox nhưng chưa chọn giá
                Toast.makeText(getContext(), "Vui lòng điều chỉnh giá!", Toast.LENGTH_SHORT).show();
            } else if (!(isBrand1Selected || isBrand2Selected || isBrand3Selected) && maxPrice > 0) {
                // Người dùng đã chọn giá nhưng chưa chọn checkbox nào
                Toast.makeText(getContext(), "Vui lòng chọn ít nhất một thương hiệu!", Toast.LENGTH_SHORT).show();
            } else {
                // Nếu đã chọn cả checkbox và giá, áp dụng bộ lọc
                applyFilter(isBrand1Selected, isBrand2Selected, isBrand3Selected, maxPrice);
                dialog.dismiss();
            }
        });


    }

    private void applyFilter(boolean isBrand1Selected, boolean isBrand2Selected, boolean isBrand3Selected, int maxPrice) {
        List<Product_Model> filteredList = new ArrayList<>();

        // Nếu không có CheckBox nào được chọn, hiển thị lại toàn bộ sản phẩm
        if (!isBrand1Selected && !isBrand2Selected && !isBrand3Selected) {
            filteredList.addAll(originalProductList);
        } else {
            // Lọc sản phẩm dựa trên CheckBox được chọn và giá
            for (Product_Model product : originalProductList) {
                String brand = product.getBrand();
                int price = (int) product.getPrice(); // Giả sử bạn có phương thức getPrice() trong Product_Model

                if ((isBrand1Selected && brand.equals("BANPRESTO")) ||
                        (isBrand2Selected && brand.equals("POP MART")) ||
                        (isBrand3Selected && brand.equals("FUNISM"))) {
                    if (price <= maxPrice) { // Chỉ kiểm tra giá tối đa
                        filteredList.add(product);
                    }
                }
            }
        }

        // Cập nhật Adapter với danh sách sản phẩm đã lọc
        productAdapter.updateData(filteredList);
    }
}
