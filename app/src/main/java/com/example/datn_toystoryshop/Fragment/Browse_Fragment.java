package com.example.datn_toystoryshop.Fragment;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.PopupMenu;
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
    private EditText searchBar;
    private Button btnSort;
    private Button btnFilter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_browse, container, false);

        recyclerView = view.findViewById(R.id.recycler_view_products);
        searchBar = view.findViewById(R.id.search_bar);
//        btnSort = view.findViewById(R.id.btnSort);
        btnFilter = view.findViewById(R.id.btnFilter);

        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));

        // Khởi tạo danh sách sản phẩm
        productList = new ArrayList<>();
        productAdapter = new Product_Adapter(getContext(), productList);
        recyclerView.setAdapter(productAdapter);

        // Gọi API để lấy sản phẩm từ MongoDB
        getProductsFromApi();

//        // Xử lý sự kiện tìm kiếm
//        searchBar.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                productAdapter.getFilter().filter(s.toString());
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) { }
//        });

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

        Button btnApplyFilter = dialogView.findViewById(R.id.btn_apply_filter);
        btnApplyFilter.setOnClickListener(v -> {
            boolean isBrand1Selected = checkboxBrand1.isChecked();
            boolean isBrand2Selected = checkboxBrand2.isChecked();
            boolean isBrand3Selected = checkboxBrand3.isChecked();

            applyFilter(isBrand1Selected, isBrand2Selected, isBrand3Selected);
            dialog.dismiss();
        });

    }
    private void applyFilter(boolean isBrand1Selected, boolean isBrand2Selected, boolean isBrand3Selected) {
        List<Product_Model> filteredList = new ArrayList<>();

        // Nếu không có CheckBox nào được chọn, hiển thị lại toàn bộ sản phẩm
        if (!isBrand1Selected && !isBrand2Selected && !isBrand3Selected) {
            filteredList.addAll(originalProductList);
        } else {
            // Lọc sản phẩm dựa trên CheckBox được chọn
            for (Product_Model product : originalProductList) {
                String brand = product.getBrand();
                if ((isBrand1Selected && brand.equals("BANPRESTO")) ||
                        (isBrand2Selected && brand.equals("POP MART")) ||
                        (isBrand3Selected && brand.equals("FUNISM"))) {
                    filteredList.add(product);
                }
            }
        }

        // Cập nhật Adapter với danh sách sản phẩm đã lọc
        productAdapter.updateData(filteredList);
    }




}
