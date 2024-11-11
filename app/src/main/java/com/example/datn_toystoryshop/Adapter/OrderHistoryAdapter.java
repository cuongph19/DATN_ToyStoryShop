package com.example.datn_toystoryshop.Adapter;

import static java.util.Locale.*;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.datn_toystoryshop.Model.Order_Model;
import com.example.datn_toystoryshop.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class OrderHistoryAdapter extends RecyclerView.Adapter<OrderHistoryAdapter.OrderViewHolder> {
    private Context context;
    private List<Order_Model> orderList;

    // Constructor để khởi tạo adapter với danh sách sản phẩm
    public OrderHistoryAdapter(Context context, List<Order_Model> orderList) {
        this.context = context;
        this.orderList = orderList;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_history, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        Order_Model order = orderList.get(position);

        String shortId = order.getProdId().substring(0, 6);
        holder.textProductName.setText(shortId);
        holder.textPrice.setText(String.format("%,.0fĐ", order.getRevenue()));
        holder.textSta.setText(order.getOrderStatus());

        // Chuyển đổi ngày từ chuỗi ISO sang định dạng "dd/MM/yyyy"
        String orderDate = order.getOrderDate();

        // Cập nhật định dạng để bao gồm phần milliseconds
        SimpleDateFormat isoFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault());
        isoFormat.setTimeZone(TimeZone.getTimeZone("UTC")); // Đặt múi giờ UTC cho dữ liệu từ server
        SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

        try {
            // Ghi log giá trị ngày từ server
            Log.d("OrderHistoryAdapter", "Original orderDate: " + orderDate);

            Date date = isoFormat.parse(orderDate); // Chuyển chuỗi thành đối tượng Date
            String formattedDate = outputFormat.format(date); // Định dạng ngày

            // Ghi log ngày đã chuyển đổi
            Log.d("OrderHistoryAdapter", "Formatted orderDate: " + formattedDate);

            holder.textDate.setText(formattedDate);
        } catch (ParseException e) {
            e.printStackTrace();
            holder.textDate.setText(orderDate); // Hiển thị gốc nếu có lỗi
        }
    }



    @Override
    public int getItemCount() {
        return orderList.size();
    }

    // ViewHolder để giữ các view cho mỗi item
    public static class OrderViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textProductName, textPrice, textDate, textSta;


        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            textProductName = itemView.findViewById(R.id.textProductName);
            textPrice = itemView.findViewById(R.id.textPrice);
            textDate = itemView.findViewById(R.id.textDate);
            textSta = itemView.findViewById(R.id.textSta);
        }
    }
}
