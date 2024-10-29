// Browse_Fragment.java
package com.example.datn_toystoryshop.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
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
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

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
        searchBar = view.findViewById(R.id.search_bar);
        btnSort = view.findViewById(R.id.btnSort);

        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));

        // Tạo danh sách sản phẩm mẫu
        productList = new ArrayList<>();
        productList.add(new Product_Model("1", 12345, 101, true, 100000, "Mô tả sản phẩm 1", "01/01/2024", 10, "Mới", "https://link_anh.png", "San pham 1", 1));
        productList.add(new Product_Model("2", 12346, 102, false, 200000, "Mô tả sản phẩm 2", "02/01/2024", 5, "Hết hàng", "https://link_anh.png", "San pham 2", 2));

        // Khởi tạo Adapter và gán vào RecyclerView
        productAdapter = new Product_Adapter(getContext(), productList);
        recyclerView.setAdapter(productAdapter);

        // Xử lý sự kiện nhấn nút sắp xếp (Giá)
        btnSort.setOnClickListener(v -> {
            // Tạo một PopupMenu bên dưới nút btnSort
            PopupMenu popupMenu = new PopupMenu(getContext(), btnSort);
            popupMenu.getMenuInflater().inflate(R.menu.sort_menu, popupMenu.getMenu());

            // Xử lý khi người dùng chọn một tùy chọn
            popupMenu.setOnMenuItemClickListener(item -> {
                if (item.getItemId() == R.id.menu_price_low_to_high) {
                    productAdapter.sortByPriceAscending(); // Sắp xếp từ giá thấp đến cao
                    return true;
                } else if (item.getItemId() == R.id.menu_price_high_to_low) {
                    productAdapter.sortByPriceDescending(); // Sắp xếp từ giá cao đến thấp
                    return true;
                }
                return false;
            });

            // Hiển thị PopupMenu
            popupMenu.show();
        });

        return view;
    }

    // Hàm chuyển đổi chuỗi có dấu thành không dấu
    public static String removeDiacritics(String input) {
        String normalized = Normalizer.normalize(input, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(normalized).replaceAll("");
    }
}
