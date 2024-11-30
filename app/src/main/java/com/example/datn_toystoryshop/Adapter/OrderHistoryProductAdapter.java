package com.example.datn_toystoryshop.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.datn_toystoryshop.Model.Order_Model;
import com.example.datn_toystoryshop.Model.Product_Model;
import com.example.datn_toystoryshop.OrderHist_Detail;
import com.example.datn_toystoryshop.R;
import com.example.datn_toystoryshop.Server.APIService;
import com.example.datn_toystoryshop.Server.ProductCallback;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderHistoryProductAdapter extends RecyclerView.Adapter<OrderHistoryProductAdapter.ProductViewHolder> {
    private Context context;
    private List<Order_Model.ProductDetail> productList;
    private APIService apiService;
    private String orderId;

    public OrderHistoryProductAdapter(Context context, List<Order_Model.ProductDetail> productList , APIService apiService, String orderId) {
        this.context = context;
        this.productList = productList;
        this.apiService = apiService;
        this.orderId = orderId;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_history_1, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Order_Model.ProductDetail product = productList.get(position);
        Log.e("OrderHistoryAdapter", "j66666666666666666gggg 11 2" + orderId);
        // Hiển thị thông tin sản phẩm
        holder.productName.setText(product.getProdSpecification());
        holder.productQuantity.setText(String.format("Số lượng: %d", product.getQuantity()));
        holder.productPrice.setText(String.format("₫%,.0f", product.getRevenue()));
        holder.itemView.setOnClickListener(v -> {
            // Chuyển đến màn hình chi tiết sản phẩm
            Intent intent = new Intent(context, OrderHist_Detail.class);
            intent.putExtra("orderId", orderId);
            context.startActivity(intent);
        });
        String prodId = product.getProdId();
        loadProductById(apiService, prodId, new ProductCallback() {
            @Override
            public void onSuccess(Product_Model product) {
                List<String> images = product.getImgPro();
                if (images != null && !images.isEmpty()) {
                    Glide.with(context).load(images.get(0)).into(holder.productImage);
                }
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        ImageView productImage;
        TextView productName, productQuantity, productPrice;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.ivProductImage);
            productName = itemView.findViewById(R.id.tvProductName);
            productQuantity = itemView.findViewById(R.id.tvProductQuantity);
            productPrice = itemView.findViewById(R.id.tvProductPrice);
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
    }}
