package com.example.datn_toystoryshop.Shopping;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.datn_toystoryshop.Adapter.VoucherAdapter;
import com.example.datn_toystoryshop.Model.Voucher;
import com.example.datn_toystoryshop.R;
import com.example.datn_toystoryshop.Server.APIService;
import com.example.datn_toystoryshop.Server.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Voucher_screen extends AppCompatActivity implements VoucherAdapter.OnVoucherSelectedListener {
    private RecyclerView recyclerViewShip, recyclerViewProduct;
    private VoucherAdapter adapterShip, adapterProduct;
    private List<Voucher> shipVoucherList = new ArrayList<>();
    private List<Voucher> productVoucherList = new ArrayList<>();
    private TextView seeMoreTextViewShip, seeMoreTextViewProduct;
    private TextView selectedVoucherCountTextView, btnApplyVoucher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_voucher_screen);

        recyclerViewShip = findViewById(R.id.voucher_recycler_view_ship);
        recyclerViewProduct = findViewById(R.id.voucher_recycler_view_product);
        seeMoreTextViewShip = findViewById(R.id.show_more_ship);
        seeMoreTextViewProduct = findViewById(R.id.show_more_product);
        selectedVoucherCountTextView = findViewById(R.id.countVoucher);
        btnApplyVoucher = findViewById(R.id.confirm_button);

        recyclerViewShip.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewProduct.setLayoutManager(new LinearLayoutManager(this));

        adapterShip = new VoucherAdapter(shipVoucherList, this, false); // false vì đây là RecyclerView vận chuyển
        adapterProduct = new VoucherAdapter(productVoucherList, this, true); // true vì đây là RecyclerView sản phẩm

        recyclerViewShip.setAdapter(adapterShip);
        recyclerViewProduct.setAdapter(adapterProduct);

        APIService apiService = RetrofitClient.getAPIService();
        apiService.getVouchers().enqueue(new Callback<List<Voucher>>() {
            @Override
            public void onResponse(Call<List<Voucher>> call, Response<List<Voucher>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    shipVoucherList.clear();
                    productVoucherList.clear();

                    for (Voucher voucher : response.body()) {
                        if ("giảm giá sản phẩm".equals(voucher.getQuantityVoucher())) {
                            productVoucherList.add(voucher);
                        } else if ("giảm giá vận chuyển".equals(voucher.getQuantityVoucher())) {
                            shipVoucherList.add(voucher);
                        }
                    }

                    adapterShip.notifyDataSetChanged();
                    adapterProduct.notifyDataSetChanged();

                    if (shipVoucherList.size() > 2) {
                        seeMoreTextViewShip.setVisibility(View.VISIBLE);
                    }
                    if (productVoucherList.size() > 2) {
                        seeMoreTextViewProduct.setVisibility(View.VISIBLE);
                    }
                } else {
                    Toast.makeText(Voucher_screen.this, "Không có mã giảm giá.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Voucher>> call, Throwable t) {
                Log.e("API Error", "Lỗi khi lấy mã giảm giá", t);
                Toast.makeText(Voucher_screen.this, "Có lỗi xảy ra khi lấy mã giảm giá.", Toast.LENGTH_SHORT).show();
            }
        });

        seeMoreTextViewShip.setOnClickListener(v -> {
            adapterShip.toggleShowAll();
            seeMoreTextViewShip.setVisibility(View.GONE);
        });

        seeMoreTextViewProduct.setOnClickListener(v -> {
            adapterProduct.toggleShowAll();
            seeMoreTextViewProduct.setVisibility(View.GONE);
        });

        // Xử lý sự kiện áp dụng voucher
        btnApplyVoucher.setOnClickListener(v -> {
            List<Voucher> selectedVouchers = getSelectedVouchers();
            if (selectedVouchers.isEmpty()) {
                Toast.makeText(Voucher_screen.this, "Vui lòng chọn ít nhất một voucher.", Toast.LENGTH_SHORT).show();
            } else {
                // Tính toán tổng giá trị giảm giá từ các voucher đã chọn
                double totalProductDiscount = 0;
                double totalShipDiscount = 0;
                for (Voucher voucher : selectedVouchers) {
                    if ("giảm giá sản phẩm".equals(voucher.getQuantityVoucher())) {
                        totalProductDiscount += voucher.getPriceReduced();
                    } else if ("giảm giá vận chuyển".equals(voucher.getQuantityVoucher())) {
                        totalShipDiscount += voucher.getPriceReduced();
                    }
                }

                // Chuyển dữ liệu voucher đã chọn về màn hình Order
                Intent intent = new Intent();
                intent.putExtra("totalProductDiscount", totalProductDiscount);
                intent.putExtra("totalShipDiscount", totalShipDiscount);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    @Override
    public void onVoucherSelected(int selectedCount) {
        int totalSelected = getTotalSelectedVoucherCount();
        selectedVoucherCountTextView.setText("Số lượng voucher đã chọn: " + totalSelected);
    }

    private int getTotalSelectedVoucherCount() {
        int total = 0;
        // Đếm voucher đã chọn từ RecyclerView sản phẩm
        for (Voucher voucher : productVoucherList) {
            if (voucher.isSelected()) {
                total++;
            }
        }
        // Đếm voucher đã chọn từ RecyclerView vận chuyển
        for (Voucher voucher : shipVoucherList) {
            if (voucher.isSelected()) {
                total++;
            }
        }
        return total;
    }

    private List<Voucher> getSelectedVouchers() {
        List<Voucher> selectedVouchers = new ArrayList<>();
        // Thêm các voucher đã chọn vào danh sách
        for (Voucher voucher : productVoucherList) {
            if (voucher.isSelected()) {
                selectedVouchers.add(voucher);
            }
        }
        for (Voucher voucher : shipVoucherList) {
            if (voucher.isSelected()) {
                selectedVouchers.add(voucher);
            }
        }
        return selectedVouchers;
    }
}
