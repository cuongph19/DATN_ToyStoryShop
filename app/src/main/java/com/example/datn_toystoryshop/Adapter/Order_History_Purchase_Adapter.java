package com.example.datn_toystoryshop.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.datn_toystoryshop.Model.Order_Model;
import com.example.datn_toystoryshop.OrderHist_Detail;
import com.example.datn_toystoryshop.Profile.ContactSupport_screen;
import com.example.datn_toystoryshop.R;
import com.example.datn_toystoryshop.Server.APIService;
import com.example.datn_toystoryshop.history.History_purchase_screen;

import java.util.List;

public class Order_History_Purchase_Adapter extends RecyclerView.Adapter<Order_History_Purchase_Adapter.OrderViewHolder> {
    private Context context;
    private List<Order_Model> orderList;
    private APIService apiService;

    public Order_History_Purchase_Adapter(Context context, List<Order_Model> orderList, APIService apiService) {
        this.context = context;
        this.orderList = orderList;
        this.apiService = apiService;
    }

    @NonNull
    @Override
    public Order_History_Purchase_Adapter.OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_history_purchase, parent, false);
        return new Order_History_Purchase_Adapter.OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Order_History_Purchase_Adapter.OrderViewHolder holder, int position) {
        Order_Model order = orderList.get(position);

        holder.textStatus.setText(order.getOrderStatus());
        holder.textRevenueAll.setText(String.format("Tổng số tiền: %,.0f Đ", (double) order.getRevenue_all()));

        // Setup RecyclerView con để hiển thị sản phẩm
        OrderHistoryProductAdapter productAdapter = new OrderHistoryProductAdapter(context, order.getProdDetails(),apiService, order.get_id());
        holder.recyclerViewProducts.setLayoutManager(new LinearLayoutManager(context));
        holder.recyclerViewProducts.setAdapter(productAdapter);

//                 Thiết lập sự kiện click để mở màn hình chi tiết sản phẩm
        holder.itemView.setOnClickListener(v -> {
            // Chuyển đến màn hình chi tiết sản phẩm
            Intent intent = new Intent(context, OrderHist_Detail.class);
            intent.putExtra("orderId", order.get_id());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public static class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView textStatus, textRevenueAll;
        RecyclerView recyclerViewProducts;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            textStatus = itemView.findViewById(R.id.tvOrderStatus);
            textRevenueAll = itemView.findViewById(R.id.tvTotalPrice);
            recyclerViewProducts = itemView.findViewById(R.id.rvProductList);
        }
    }
}
