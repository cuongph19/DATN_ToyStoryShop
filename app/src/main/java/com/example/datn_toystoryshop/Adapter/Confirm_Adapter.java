package com.example.datn_toystoryshop.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.datn_toystoryshop.Confirm_Detail;
import com.example.datn_toystoryshop.Model.Order_Model;
import com.example.datn_toystoryshop.OrderHist_Detail;
import com.example.datn_toystoryshop.Profile.ContactSupport_screen;
import com.example.datn_toystoryshop.R;
import com.example.datn_toystoryshop.Server.APIService;

import java.util.List;

public class Confirm_Adapter extends RecyclerView.Adapter<Confirm_Adapter.OrderViewHolder> {
    private Context context;
    private List<Order_Model> orderList;
    private APIService apiService;

    public Confirm_Adapter(Context context, List<Order_Model> orderList, APIService apiService) {
        this.context = context;
        this.orderList = orderList;
        this.apiService = apiService;
    }

    @NonNull
    @Override
    public Confirm_Adapter.OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_history, parent, false);
        return new Confirm_Adapter.OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Confirm_Adapter.OrderViewHolder holder, int position) {
        Order_Model order = orderList.get(position);

        Log.e("OrderHistoryAdapter", "j66666666666666666gggghhhhConfirm_Adapter " );
        holder.textStatus.setText(order.getOrderStatus());
        holder.textRevenueAll.setText(String.format("Tổng số tiền: %,.0f Đ", (double) order.getRevenue_all()));

        // Setup RecyclerView con để hiển thị sản phẩm
        Confirm_Product_Adapter productAdapter = new Confirm_Product_Adapter(context, order.getProdDetails(),apiService, order.get_id());
        holder.recyclerViewProducts.setLayoutManager(new LinearLayoutManager(context));
        holder.recyclerViewProducts.setAdapter(productAdapter);

//                 Thiết lập sự kiện click để mở màn hình chi tiết sản phẩm
        holder.itemView.setOnClickListener(v -> {
            // Chuyển đến màn hình chi tiết sản phẩm
            Intent intent = new Intent(context, Confirm_Detail.class);
            intent.putExtra("orderId", order.get_id());
            context.startActivity(intent);
        });
        // Sự kiện liên hệ shop
        holder.textContactShop.setOnClickListener(v -> {
            Intent intent = new Intent(context, ContactSupport_screen.class);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public static class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView textStatus, textRevenueAll, textContactShop;
        RecyclerView recyclerViewProducts;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            textStatus = itemView.findViewById(R.id.tvOrderStatus);
            textRevenueAll = itemView.findViewById(R.id.tvTotalPrice);
            textContactShop = itemView.findViewById(R.id.btnContactShop);
            recyclerViewProducts = itemView.findViewById(R.id.rvProductList);
        }
    }
}
