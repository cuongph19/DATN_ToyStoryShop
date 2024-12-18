package com.example.datn_toystoryshop.history.History_purchase;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.datn_toystoryshop.Adapter.OrderHistoryAdapter;
import com.example.datn_toystoryshop.Model.Order_Model;
import com.example.datn_toystoryshop.R;
import com.example.datn_toystoryshop.Server.APIService;
import com.example.datn_toystoryshop.Server.RetrofitClient;


import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReturnGoods_Fragment extends Fragment {


    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private OrderHistoryAdapter adapter;
    private List<Order_Model> orderList = new ArrayList<>();
    private List<Order_Model> filteredOrderList = new ArrayList<>();
    private String documentId;
    private LinearLayout llnot;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_get_goods_1, container, false);

        recyclerView = view.findViewById(R.id.rvOrderHistory);
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        llnot = view.findViewById(R.id.llnot);
        Bundle bundle = getArguments();
        documentId = bundle.getString("documentId");

        APIService apiService = RetrofitClient.getAPIService();
        adapter = new OrderHistoryAdapter(getContext(), filteredOrderList, apiService);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

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
        Call<List<Order_Model>> call = apiService.getOrders_getgoods(cusId);
        call.enqueue(new Callback<List<Order_Model>>() {
            @Override
            public void onResponse(Call<List<Order_Model>> call, Response<List<Order_Model>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    swipeRefreshLayout.setRefreshing(false);
                    orderList.clear();
                    orderList.addAll(response.body());
                    filteredOrderList.clear();
                    filteredOrderList.addAll(orderList);
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(getContext(), "Không có dữ liệu đơn hàng", Toast.LENGTH_SHORT).show();
                    swipeRefreshLayout.setVisibility(View.GONE);
                    llnot.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<List<Order_Model>> call, Throwable t) {
                Toast.makeText(getContext(), "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                swipeRefreshLayout.setVisibility(View.GONE);
                llnot.setVisibility(View.VISIBLE);
            }
        });
    }


}