// Browse_Fragment.java
package com.example.datn_toystoryshop.Fragment;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import com.example.datn_toystoryshop.Adapter.ProductAdapter;
import com.example.datn_toystoryshop.Model.Product;
import com.example.datn_toystoryshop.R;
import java.util.ArrayList;
import java.util.List;

public class Browse_Fragment extends Fragment {

    private RecyclerView recyclerView;
    private ProductAdapter productAdapter;
    private List<Product> productList;
    private EditText searchBar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_browse, container, false);

        recyclerView = view.findViewById(R.id.recycler_view_products);
        searchBar = view.findViewById(R.id.search_bar);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));

        // Tạo danh sách sản phẩm mẫu
        productList = new ArrayList<>();
        productList.add(new Product("Sản phẩm 1", "1234567890", "100.000đ", "Còn hàng", R.drawable.product1));
        productList.add(new Product("Sản phẩm 2", "0987654321", "200.000đ", "Hết hàng", R.drawable.product1));
        // Thêm các sản phẩm khác...

        // Khởi tạo Adapter và gán vào RecyclerView
        productAdapter = new ProductAdapter(productList);
        recyclerView.setAdapter(productAdapter);

        // Lắng nghe thay đổi văn bản trong searchBar
        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Gọi hàm filter trong Adapter để lọc sản phẩm
                productAdapter.filter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        return view;
    }
}
