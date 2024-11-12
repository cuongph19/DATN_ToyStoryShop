package com.example.datn_toystoryshop.Shopping;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.datn_toystoryshop.Adapter.Cart_Adapter;
import com.example.datn_toystoryshop.Home_screen;
import com.example.datn_toystoryshop.Model.Cart_Model;
import com.example.datn_toystoryshop.Model.Product_Model;
import com.example.datn_toystoryshop.R;
import com.example.datn_toystoryshop.Server.APIService;
import com.example.datn_toystoryshop.Server.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Cart_screen extends AppCompatActivity {


    private ImageView imgBack;
    private String productId;
    private int owerId;
    private boolean statusPro;
    private double productPrice;
    private String desPro;
    private String creatDatePro;
    private int quantity;
    private String listPro;
    private ArrayList<String> productImg;
    private String productName;
    private int cateId;
    private String brand;
    private String favoriteId;
    private List<Product_Model> productList;
    private int currentQuantity;
    private String customerId;
    private String selectedColor;
    private RecyclerView recyclerViewCart;
    private Cart_Adapter cartAdapter;
    private CheckBox checkBoxSelectAll ;
    private TextView TotalPayment,btnCheckout ;
    private LinearLayout tvVoucher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_cart_screen);

        imgBack = findViewById(R.id.imgBack_cart);
        recyclerViewCart = findViewById(R.id.recyclerViewCart);
        checkBoxSelectAll  = findViewById(R.id.checkBoxSelectAll);
        TotalPayment  = findViewById(R.id.tvTotalPayment);
        btnCheckout  = findViewById(R.id.btnCheckout);
        tvVoucher  = findViewById(R.id.tvVoucher);


// Nhận dữ liệu từ Intent
        Intent intent = getIntent();
        productId = intent.getStringExtra("productId");
        owerId = intent.getIntExtra("owerId", -1);
        statusPro = intent.getBooleanExtra("statusPro", false);
        productPrice = intent.getDoubleExtra("productPrice", 0.0);
        desPro = intent.getStringExtra("desPro");
        creatDatePro = intent.getStringExtra("creatDatePro");
        quantity = intent.getIntExtra("quantity", 0);
        listPro = intent.getStringExtra("listPro");
        productImg = intent.getStringArrayListExtra("productImg");
        productName = intent.getStringExtra("productName");
        cateId = intent.getIntExtra("cateId", -1);
        brand = intent.getStringExtra("brand");
        favoriteId = intent.getStringExtra("favoriteId");

        // Nhận thêm thuộc tính currentQuantity, customerId, và productSpecification
        currentQuantity = intent.getIntExtra("currentQuantity", 1);
        customerId = intent.getStringExtra("customerId");
        selectedColor = intent.getStringExtra("selectedColor");

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Cart_screen.this, Home_screen.class);
                startActivity(intent);
                finish();
            }});
        btnCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Cart_screen.this, Oder_screen.class);
                // Truyền tất cả dữ liệu qua Intent
                intent.putExtra("productId", productId);
                intent.putExtra("owerId", owerId);
                intent.putExtra("statusPro", statusPro);
                intent.putExtra("productPrice", productPrice);
                intent.putExtra("desPro", desPro);
                intent.putExtra("creatDatePro", creatDatePro);
                intent.putExtra("quantity", quantity);
                intent.putExtra("listPro", listPro);
                intent.putStringArrayListExtra("productImg", productImg);
                intent.putExtra("productName", productName);
                intent.putExtra("cateId", cateId);
                intent.putExtra("brand", brand);
                intent.putExtra("favoriteId", favoriteId);
                // Truyền thêm các thuộc tính currentQuantity, customerId, và productSpecification
                intent.putExtra("currentQuantity", currentQuantity);
                intent.putExtra("customerId", "8iPTPiB47jBO0EKMkn7K"); // ID khách hàng
                intent.putExtra("selectedColor", selectedColor);

                startActivity(intent);
            }});
        tvVoucher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Cart_screen.this, Voucher_screen.class);
                startActivity(intent);
            }});

        // Initialize product list and add sample products
        productList = new ArrayList<>();
        productList.add(new Product_Model("Ghế văn phòng ergonomic", "759.000", "1.300.000"));

        // Set up RecyclerView with LinearLayoutManager and Adapter
        recyclerViewCart.setLayoutManager(new LinearLayoutManager(this));
        checkBoxSelectAll.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (cartAdapter != null) {
                cartAdapter.updateTotalPayment(isChecked);

                // Nếu bỏ chọn tất cả, đặt tổng thành 0
                if (!isChecked) {
                    // Gọi phương thức trong Adapter để bỏ chọn tất cả các sản phẩm
                    cartAdapter.deselectAllItems();
                    updateTotalPayment(0);
                }
            }
        });
        loadCartProducts();
    }
    public void updateTotalPayment(double total) {
        TotalPayment.setText(String.format("Tổng thanh toán: %.2f VNĐ", total));
    }
    private void loadCartProducts() {
        APIService apiService = RetrofitClient.getAPIService();
        apiService.getCarts().enqueue(new Callback<List<Cart_Model>>() {
            @Override
            public void onResponse(Call<List<Cart_Model>> call, Response<List<Cart_Model>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d("CartScreen", "Cart items retrieved: " + response.body().size());

                    // Tạo và thiết lập Cart_Adapter
                    cartAdapter = new Cart_Adapter(Cart_screen.this, response.body(), apiService);
                    recyclerViewCart.setAdapter(cartAdapter);
                    // Thiết lập ItemTouchHelper cho RecyclerView
                    setupItemTouchHelper();
                }
            }

            @Override
            public void onFailure(Call<List<Cart_Model>> call, Throwable t) {
                // Xử lý lỗi khi gọi API thất bại
            }
        });
    }

    private void setupItemTouchHelper() {
        // Tạo một ItemTouchHelper từ phương thức getItemTouchHelper() của Cart_Adapter
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(cartAdapter.getItemTouchHelper());
        itemTouchHelper.attachToRecyclerView(recyclerViewCart);
    }

}

