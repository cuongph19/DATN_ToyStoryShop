package com.example.datn_toystoryshop.Home;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Rect;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.datn_toystoryshop.Adapter.ProductNewAdapter;
import com.example.datn_toystoryshop.Home_screen;
import com.example.datn_toystoryshop.Model.Product_Model;
import com.example.datn_toystoryshop.R;
import com.example.datn_toystoryshop.Server.APIService;

import java.text.Normalizer;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;

public class All_new_screen extends AppCompatActivity {
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerViewAllNewProducts;
    private ProductNewAdapter productNewAdapter;
    private List<Product_Model> productList;
    private String documentId;
    private SharedPreferences sharedPreferences;
    private boolean nightMode;
    private ImageView imgBack;
    private int minPriceLimit = 0;// Giá tối đa là 1.000.000


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_new_screen);

        recyclerViewAllNewProducts = findViewById(R.id.recyclerViewAllNewProducts);
        imgBack = findViewById(R.id.ivBack);
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);

        Intent intent = getIntent();
        documentId = intent.getStringExtra("documentId");
        sharedPreferences = getSharedPreferences("Settings", MODE_PRIVATE);
        nightMode = sharedPreferences.getBoolean("night", false);
        productList = (List<Product_Model>) getIntent().getSerializableExtra("productList");
        recyclerViewAllNewProducts.setLayoutManager(new LinearLayoutManager(this));


        productNewAdapter = new ProductNewAdapter(this, productList, false, documentId);
        recyclerViewAllNewProducts.setAdapter(productNewAdapter);
        Button btnFilter = findViewById(R.id.btn_filter); // Nút bộ lọc
        btnFilter.setOnClickListener(v -> showFilterDialog());
        Button btnSort = findViewById(R.id.btnSort);
        btnSort.setOnClickListener(v -> showSortDialog());


        // Thêm ItemDecoration để tạo khoảng cách dưới mỗi item
        recyclerViewAllNewProducts.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                // Thêm khoảng cách dưới mỗi item (16px)
                outRect.bottom = 16;
            }
        });
        if (nightMode) {
            imgBack.setImageResource(R.drawable.back_icon);
        } else {
            imgBack.setImageResource(R.drawable.back_icon_1);
        }
        swipeRefreshLayout.setOnRefreshListener(() -> {
            productNewAdapter.notifyDataSetChanged();
            // Tắt hiệu ứng tải lại
            swipeRefreshLayout.setRefreshing(false);
        });
        imgBack.setOnClickListener(v -> onBackPressed());
    }
    private void showSortDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(All_new_screen.this);
        String[] sortOptions = {
                "Sắp xếp theo A-Z",
                "Sắp xếp theo Z-A",
                "Giá từ thấp đến cao",
                "Giá từ cao đến thấp",
                "Ngày từ cũ đến mới",
                "Ngày từ mới đến cũ"
        };

        builder.setTitle("Chọn cách sắp xếp")
                .setItems(sortOptions, (dialog, which) -> {
                    switch (which) {
                        case 0:
                            sortProducts("name_asc");
                            break;
                        case 1:
                            sortProducts("name_desc");
                            break;
                        case 2:
                            sortProducts("price_asc");
                            break;
                        case 3:
                            sortProducts("price_desc");
                            break;
                        case 4:
                            sortProducts("date_asc");
                            break;
                        case 5:
                            sortProducts("date_desc");
                            break;
                        default:
                            break;
                    }
                });
        builder.show();
    }
    private String removeDiacritics(String input) {
        if (input == null) return "";
        String normalized = Normalizer.normalize(input, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(normalized).replaceAll("").replace('đ', 'd').replace('Đ', 'D');
    }
    private void sortProducts(String sortBy) {
        if (productList == null || productList.isEmpty()) {
            Toast.makeText(All_new_screen.this, "Không có sản phẩm để sắp xếp.", Toast.LENGTH_SHORT).show();
            return;
        }

        switch (sortBy) {
            case "name_asc": // Sắp xếp A-Z
                productList.sort((lhs, rhs) -> {
                    String lhsName = removeDiacritics(lhs.getNamePro() != null ? lhs.getNamePro() : "");
                    String rhsName = removeDiacritics(rhs.getNamePro() != null ? rhs.getNamePro() : "");
                    return lhsName.compareTo(rhsName);
                });
                break;

            case "name_desc": // Sắp xếp Z-A
                productList.sort((lhs, rhs) -> {
                    String lhsName = removeDiacritics(lhs.getNamePro() != null ? lhs.getNamePro() : "");
                    String rhsName = removeDiacritics(rhs.getNamePro() != null ? rhs.getNamePro() : "");
                    return rhsName.compareTo(lhsName);
                });
                break;

            case "price_asc": // Sắp xếp giá từ thấp đến cao
                productList.sort((lhs, rhs) -> Double.compare(lhs.getPrice(), rhs.getPrice()));
                break;

            case "price_desc": // Sắp xếp giá từ cao đến thấp
                productList.sort((lhs, rhs) -> Double.compare(rhs.getPrice(), lhs.getPrice()));
                break;

            case "date_asc": // Sắp xếp ngày từ cũ đến mới
                productList.sort((lhs, rhs) -> {
                    String lhsDateStr = lhs.getCreatDatePro();
                    String rhsDateStr = rhs.getCreatDatePro();

                    // Nếu một trong hai ngày null, để nguyên vị trí
                    if (lhsDateStr == null || rhsDateStr == null) {
                        return 0;
                    }

                    // Parse ngày
                    Date lhsDate = parseDate(lhsDateStr);
                    Date rhsDate = parseDate(rhsDateStr);

                    // Nếu không parse được ngày, để nguyên vị trí
                    if (lhsDate == null || rhsDate == null) {
                        return 0;
                    }

                    return lhsDate.compareTo(rhsDate);
                });
                break;

            case "date_desc": // Sắp xếp ngày từ mới đến cũ
                productList.sort((lhs, rhs) -> {
                    String lhsDateStr = lhs.getCreatDatePro();
                    String rhsDateStr = rhs.getCreatDatePro();

                    // Nếu một trong hai ngày null, để nguyên vị trí
                    if (lhsDateStr == null || rhsDateStr == null) {
                        return 0;
                    }

                    // Parse ngày
                    Date lhsDate = parseDate(lhsDateStr);
                    Date rhsDate = parseDate(rhsDateStr);

                    // Nếu không parse được ngày, để nguyên vị trí
                    if (lhsDate == null || rhsDate == null) {
                        return 0;
                    }

                    return rhsDate.compareTo(lhsDate);
                });
                break;


            default:
                break;
        }

        productNewAdapter.updateData(productList); // Cập nhật lại RecyclerView
    }
    private Date parseDate(String dateStr) {
        if (dateStr == null || dateStr.trim().isEmpty()) {
            return null; // Ngày null hoặc rỗng
        }

        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            return dateFormat.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
            return null; // Trả về null nếu không parse được
        }
    }
    private void showFilterDialog() {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(All_new_screen.this);
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
                            Toast.makeText(All_new_screen.this, "Giá trị min không được lớn hơn max", Toast.LENGTH_SHORT).show();
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
                            Toast.makeText(All_new_screen.this, "Giá trị min không được lớn hơn max", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(All_new_screen.this, "Vui lòng chọn ít nhất một thương hiệu!", Toast.LENGTH_SHORT).show();
            } else {
                // Áp dụng bộ lọc với các thương hiệu đã chọn và khoảng giá min-max
                applyFilter(isBrand1Selected, isBrand2Selected, isBrand3Selected, minPrice, maxPrice);
                dialog.dismiss();
            }
        });

    }

    private void applyFilter(boolean isBrand1Selected, boolean isBrand2Selected, boolean isBrand3Selected, int minPrice, int maxPrice) {
        if (productList == null) {
            Toast.makeText(this, "Danh sách sản phẩm chưa được tải.", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean hasBrand1 = false, hasBrand2 = false, hasBrand3 = false;

        // Kiểm tra xem có sản phẩm thuộc thương hiệu được chọn hay không
        for (Product_Model product : productList) {
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

        // Lọc sản phẩm
        List<Product_Model> filteredList = new ArrayList<>();
        for (Product_Model product : productList) {
            String brand = product.getBrand().trim();
            int price = (int) product.getPrice();

            if (((isBrand1Selected && brand.equals("BANPRESTO")) ||
                    (isBrand2Selected && brand.equals("POP MART")) ||
                    (isBrand3Selected && brand.equals("FUNISM"))) &&
                    price >= minPrice && price <= maxPrice) {
                filteredList.add(product);
            }

        }

        // Kiểm tra nếu không có sản phẩm nào phù hợp với bộ lọc giá
        if (filteredList.isEmpty()) {
            Toast.makeText(this, "Không có sản phẩm phù hợp với bộ lọc giá.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Cập nhật Adapter với danh sách sản phẩm đã lọc
        productNewAdapter.updateData(filteredList);
    }

    private int countProductsByBrand(String brandName) {
        int count = 0;

        if (productList != null) {
            for (Product_Model product : productList) {
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
        for (Product_Model product : productList) {
            String brand = product.getBrand().trim();
            brandCounts.put(brand, brandCounts.getOrDefault(brand, 0) + 1);
        }
    }
}