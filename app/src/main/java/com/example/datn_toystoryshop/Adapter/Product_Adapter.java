package com.example.datn_toystoryshop.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.datn_toystoryshop.Model.Product_Model;
import com.example.datn_toystoryshop.R;

import java.util.List;

public class Product_Adapter extends RecyclerView.Adapter<Product_Adapter.ProductViewHolder> {

    private List<Product_Model> productModelList;
    private Context context;

    public Product_Adapter(Context context, List<Product_Model> productModelList) {
        this.context = context;
        this.productModelList = productModelList;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_product, parent, false);
        return new ProductViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product_Model product = productModelList.get(position);

        // Set dữ liệu cho từng thuộc tính
        holder.tvName.setText(product.getNamePro());
        holder.tvSKU.setText("SKU: " + product.getProdId());
        holder.tvPrice.setText(String.format("%,.0fđ", product.getPrice()));
        holder.tvStatus.setText(product.isStatusPro() ? "Còn hàng" : "Hàng sắp về");

        // Tải ảnh từ URL với Glide
        Glide.with(context)
                .load(product.getImgPro())
                .placeholder(R.drawable.product1) // Ảnh placeholder trong khi chờ tải ảnh
                .into(holder.imgProduct);
    }

    @Override
    public int getItemCount() {
        return productModelList.size();
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvSKU, tvPrice, tvStatus;
        ImageView imgProduct;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.product_name);
            tvSKU = itemView.findViewById(R.id.product_sku);
            tvPrice = itemView.findViewById(R.id.product_price);
            tvStatus = itemView.findViewById(R.id.product_status);
            imgProduct = itemView.findViewById(R.id.product_image);
        }
    }
}