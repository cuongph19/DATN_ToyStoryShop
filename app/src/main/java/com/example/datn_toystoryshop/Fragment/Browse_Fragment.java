package com.example.datn_toystoryshop.Fragment;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
    private List<Product_Model> productList;
    private EditText searchBar;
    private Button btnSort;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_browse, container, false);

        recyclerView = view.findViewById(R.id.recycler_view_products);
//        searchBar = view.findViewById(R.id.search_bar);
//        btnSort = view.findViewById(R.id.btnSort);

        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));

        // Khởi tạo danh sách sản phẩm
        productList = new ArrayList<>();
        productAdapter = new Product_Adapter(getContext(), productList);
        recyclerView.setAdapter(productAdapter);

        // Gọi API để lấy sản phẩm từ MongoDB
        getProductsFromApi();

//        // Thiết lập TextWatcher cho searchBar
//        searchBar.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                productAdapter.filter(s.toString()); // Gọi hàm filter
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {}
//        });
//
//
//        // Xử lý sự kiện nhấn nút sắp xếp (Giá)
//        btnSort.setOnClickListener(v -> {
//            PopupMenu popupMenu = new PopupMenu(getContext(), btnSort);
//            popupMenu.getMenuInflater().inflate(R.menu.sort_menu, popupMenu.getMenu());
//
//            popupMenu.setOnMenuItemClickListener(item -> {
//                if (item.getItemId() == R.id.menu_price_low_to_high) {
//                    productAdapter.sortByPriceAscending();
//                    return true;
//                } else if (item.getItemId() == R.id.menu_price_high_to_low) {
//                    productAdapter.sortByPriceDescending();
//                    return true;
//                }
//                return false;
//            });
//
//            popupMenu.show();
//        });
//
        return view;
    }


    private void getProductsFromApi() {
        APIService apiService = RetrofitClient.getAPIService();
        Call<List<Product_Model>> call = apiService.getProducts();

        call.enqueue(new Callback<List<Product_Model>>() {
            @Override
            public void onResponse(Call<List<Product_Model>> call, Response<List<Product_Model>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    productList.clear();
                    productList.addAll(response.body()); // Thêm tất cả sản phẩm vào danh sách
                    productAdapter.notifyDataSetChanged(); // Cập nhật RecyclerView
                    productAdapter.productModelListFull = new ArrayList<>(productList); // Khởi tạo danh sách đầy đủ
                }
            }

            @Override
            public void onFailure(Call<List<Product_Model>> call, Throwable t) {
                t.printStackTrace();
            }
        });

    }
}
