package com.example.datn_toystoryshop.History_purchase;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.datn_toystoryshop.Adapter.Order_History_Purchase_Adapter;
import com.example.datn_toystoryshop.Model.Order_Model;
import com.example.datn_toystoryshop.R;
import com.example.datn_toystoryshop.Server.APIService;
import com.example.datn_toystoryshop.Server.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Delivered_Fragment extends Fragment {
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView rvOrderHistory;
    private Order_History_Purchase_Adapter adapter;
    private List<Order_Model> orderList = new ArrayList<>();
    private List<Order_Model> filteredOrderList = new ArrayList<>();
    private String documentId;

    public Delivered_Fragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_delivered, container, false);
        Bundle bundle = getArguments();
        if (bundle != null) {
            documentId = bundle.getString("documentId");
        }
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        rvOrderHistory = view.findViewById(R.id.rvOrderHistory);

        // Thiết lập RecyclerView
        APIService apiService = RetrofitClient.getAPIService();
        adapter = new Order_History_Purchase_Adapter(requireContext(), filteredOrderList, apiService, documentId);
        rvOrderHistory.setAdapter(adapter);
        rvOrderHistory.setLayoutManager(new LinearLayoutManager(requireContext()));

        // Lấy dữ liệu từ API
        fetchOrders();

        swipeRefreshLayout.setOnRefreshListener(this::fetchOrders);

        return view;
    }

    private void fetchOrders() {
        if (documentId == null || documentId.isEmpty()) {
            Log.e("DeliveredFragment", "documentId không được để trống");
            return;
        }

        APIService apiService = RetrofitClient.getAPIService();
        Call<List<Order_Model>> call = apiService.getOrders_successful(documentId);
        call.enqueue(new Callback<List<Order_Model>>() {
            @Override
            public void onResponse(Call<List<Order_Model>> call, Response<List<Order_Model>> response) {
                swipeRefreshLayout.setRefreshing(false);
                if (response.isSuccessful() && response.body() != null) {
                    orderList.clear();
                    orderList.addAll(response.body());
                    filteredOrderList.clear();
                    filteredOrderList.addAll(orderList);
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(requireContext(), "Không có dữ liệu đơn hàng", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Order_Model>> call, Throwable t) {
                swipeRefreshLayout.setRefreshing(false);
                Toast.makeText(requireContext(), "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}