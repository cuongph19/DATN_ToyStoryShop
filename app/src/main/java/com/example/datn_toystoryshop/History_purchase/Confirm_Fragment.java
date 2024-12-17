package com.example.datn_toystoryshop.History_purchase;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import com.example.datn_toystoryshop.Adapter.Confirm_Adapter;
import com.example.datn_toystoryshop.Model.Order_Model;
import com.example.datn_toystoryshop.R;
import com.example.datn_toystoryshop.Server.APIService;
import com.example.datn_toystoryshop.Server.RetrofitClient;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;


import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Confirm_Fragment extends Fragment {

    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private Confirm_Adapter adapter;
    private List<Order_Model> orderList = new ArrayList<>();
    private List<Order_Model> filteredOrderList = new ArrayList<>();
    private String documentId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_confirm_1, container, false);

        recyclerView = view.findViewById(R.id.rvOrderHistory);
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);


        Bundle bundle = getArguments();
        if (bundle != null) {
            documentId = bundle.getString("documentId");
        }

        APIService apiService = RetrofitClient.getAPIService();
        adapter = new Confirm_Adapter(getContext(), filteredOrderList, apiService, documentId);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Gọi API để lấy danh sách đơn hàng
        fetchOrders();
        swipeRefreshLayout.setOnRefreshListener(() -> {
            fetchOrders(); // Gọi lại API để làm mới danh sách
        });

        return view;
    }

    private void fetchOrders() {
        String cusId = documentId;
        Log.e("FavoriteScreen", "cusId không được để trống " + cusId);
        if (cusId == null || cusId.isEmpty()) {
            Log.e("FavoriteScreen", "cusId không được để trống");
            return;
        }
        APIService apiService = RetrofitClient.getAPIService();
        Call<List<Order_Model>> call = apiService.getOrders_Confirm(cusId);
        call.enqueue(new Callback<List<Order_Model>>() {
            @Override
            public void onResponse(Call<List<Order_Model>> call, Response<List<Order_Model>> response) {
                if (getContext() != null) {
                    if (response.isSuccessful() && response.body() != null) {
                        orderList.clear();
                        orderList.addAll(response.body());
                        filteredOrderList.clear();
                        filteredOrderList.addAll(orderList);
                        adapter.notifyDataSetChanged();
                        swipeRefreshLayout.setRefreshing(false);
                        Log.d("API Response", "Số lượng đơn hàngggggggggggg: " + response.body().size());
                        for (Order_Model order : response.body()) {
                            Log.d("Số lượng đơn hàngggggggggggg API Response", order.toString());
                        }
                    } else {
                        Toast.makeText(getContext(), "Không có dữ liệu đơn hàng", Toast.LENGTH_SHORT).show();
                        swipeRefreshLayout.setRefreshing(false);

                    }
                }
            }

            @Override
            public void onFailure(Call<List<Order_Model>> call, Throwable t) {
                if (getContext() != null) {
                    Toast.makeText(getContext(), "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    swipeRefreshLayout.setRefreshing(false);
                }
            }
        });
    }
}