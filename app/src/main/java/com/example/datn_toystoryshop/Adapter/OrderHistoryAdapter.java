package com.example.datn_toystoryshop.Adapter;

import static java.util.Locale.*;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.datn_toystoryshop.Model.Order_Model;
import com.example.datn_toystoryshop.Model.Product_Model;
import com.example.datn_toystoryshop.OrderHist_Detail;
import com.example.datn_toystoryshop.Product_detail;
import com.example.datn_toystoryshop.Profile.ContactSupport_screen;
import com.example.datn_toystoryshop.R;
import com.example.datn_toystoryshop.Server.APIService;
import com.example.datn_toystoryshop.Server.ProductCallback;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderHistoryAdapter extends RecyclerView.Adapter<OrderHistoryAdapter.OrderViewHolder> {
    private Context context;
    private List<Order_Model> orderList;
    private APIService apiService;
    // Constructor để khởi tạo adapter với danh sách sản phẩm


    public OrderHistoryAdapter(Context context, List<Order_Model> orderList, APIService apiService) {
        this.context = context;
        this.orderList = orderList;
        this.apiService = apiService;
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

        String prodId = order.getProdDetails().get(0).getProdId();
        int Quantity = order.getProdDetails().get(0).getQuantity();
        String ProdSpecification = order.getProdDetails().get(0).getProdSpecification();

        holder.textStatus.setText(order.getOrderStatus());
        holder.textRevenue_all.setText(String.format("Tổng số tiền: %,.0f Đ", (double) order.getRevenue_all()));
        holder.textType.setText(ProdSpecification);
        holder.textQuantity.setText(String.format("x"+ Quantity));
        // Lấy thông tin sản phẩm từ API
        loadProductById(apiService, prodId, new ProductCallback() {
            @Override
            public void onSuccess(Product_Model product) {
                // Cập nhật thông tin sản phẩm vào view
                holder.textProductName.setText(product.getNamePro());
                holder.textPrice.setText(String.format("%,.0fĐ", product.getPrice()));
                // Thiết lập ảnh sản phẩm bằng Glide
                List<String> images = product.getImgPro();
                if (images != null && !images.isEmpty()) {
                    Glide.with(context).load(images.get(0)).into(holder.imageViewOr);
                }

//                 Thiết lập sự kiện click để mở màn hình chi tiết sản phẩm
                holder.itemView.setOnClickListener(v -> {
                    String prodId = order.getProdDetails().get(0).getProdId();
                    // Chuyển đến màn hình chi tiết sản phẩm
                    Intent intent = new Intent(context, OrderHist_Detail.class);
                    intent.putExtra("prodId", prodId);
                    intent.putExtra("orderStatus", order.getOrderStatus());
                    intent.putExtra("orderRevenue", (double) order.getRevenue_all());
                    Log.e("OrderHistoryAdapter", "j66666666666666666 " +  order.getRevenue_all());
                    intent.putExtra("orderContent", order.getContent());

                    context.startActivity(intent);
                });
            }

            @Override
            public void onFailure(Throwable t) {
                Log.e("OrderHistoryAdapter", "Failed to load product details: " + t.getMessage());
            }
        });

        holder.ContactShop.setOnClickListener(v -> {
            Intent intent = new Intent(context, ContactSupport_screen.class);

            context.startActivity(intent);
        });
//        // Chuyển đổi ngày từ chuỗi ISO sang định dạng "dd/MM/yyyy"
//        String orderDate = order.getOrderDate();
//
//        // Cập nhật định dạng để bao gồm phần milliseconds
//        SimpleDateFormat isoFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault());
//        isoFormat.setTimeZone(TimeZone.getTimeZone("UTC")); // Đặt múi giờ UTC cho dữ liệu từ server
//        SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
//
//        try {
//            // Ghi log giá trị ngày từ server
//            Log.d("OrderHistoryAdapter", "Original orderDate: " + orderDate);
//
//            Date date = isoFormat.parse(orderDate); // Chuyển chuỗi thành đối tượng Date
//            String formattedDate = outputFormat.format(date); // Định dạng ngày
//
//            // Ghi log ngày đã chuyển đổi
//            Log.d("OrderHistoryAdapter", "Formatted orderDate: " + formattedDate);
//
//            holder.textDate.setText(formattedDate);
//        } catch (ParseException e) {
//            e.printStackTrace();
//            holder.textDate.setText(orderDate); // Hiển thị gốc nếu có lỗi
//        }
    }



    @Override
    public int getItemCount() {
        return orderList.size();
    }

    // ViewHolder để giữ các view cho mỗi item
    public static class OrderViewHolder extends RecyclerView.ViewHolder {
        ImageView imageViewOr;
        TextView textProductName,textType, textPrice, textStatus, textQuantity, textRevenue_all, ContactShop;


        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewOr = itemView.findViewById(R.id.ivProductImage);
            textProductName = itemView.findViewById(R.id.tvProductName);
            textPrice = itemView.findViewById(R.id.tvProductPrice);
            textType = itemView.findViewById(R.id.tvProductType);
            textStatus = itemView.findViewById(R.id.tvOrderStatus);
            textQuantity = itemView.findViewById(R.id.tvProductQuantity);
            textRevenue_all = itemView.findViewById(R.id.tvTotalPrice);
            ContactShop = itemView.findViewById(R.id.btnContactShop);
        }
    }

    // Phương thức gọi API để lấy chi tiết sản phẩm
    private void loadProductById(APIService apiService, String prodId, ProductCallback callback) {
        Call<Product_Model> call = apiService.getProductById(prodId);
        call.enqueue(new Callback<Product_Model>() {
            @Override
            public void onResponse(Call<Product_Model> call, Response<Product_Model> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onFailure(new Throwable("Product not found"));
                }
            }

            @Override
            public void onFailure(Call<Product_Model> call, Throwable t) {
                callback.onFailure(t);
            }
        });
    }
}
