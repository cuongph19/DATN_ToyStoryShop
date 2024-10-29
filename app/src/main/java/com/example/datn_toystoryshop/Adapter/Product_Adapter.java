// Product_Adapter.java
package com.example.datn_toystoryshop.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.datn_toystoryshop.Model.Product_Model;
import com.example.datn_toystoryshop.R;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.text.Normalizer;
import java.util.regex.Pattern;

public class Product_Adapter extends RecyclerView.Adapter<Product_Adapter.ProductViewHolder> {

    private List<Product_Model> productModelList;
    private List<Product_Model> productModelListFull; // List gốc để lọc
    private Context context;

    public Product_Adapter(Context context, List<Product_Model> productModelList) {
        this.context = context;
        this.productModelList = productModelList;
        this.productModelListFull = new ArrayList<>(productModelList); // Khởi tạo bản sao cho danh sách đầy đủ
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_new_product, parent, false);
        return new ProductViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product_Model product = productModelList.get(position);

        holder.tvName.setText(product.getNamePro());
        holder.tvSKU.setText("Mã SP: " + product.getProdId());
        holder.tvPrice.setText(String.format("%,.0fđ", product.getPrice()));
        holder.tvStatus.setText(product.isStatusPro() ? "Còn hàng" : "Hết hàng");

        // Tải ảnh từ URL với Glide
        Glide.with(context)
                .load(product.getImgPro())
                .placeholder(R.drawable.product1)
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
            tvName = itemView.findViewById(R.id.tvTen);
            tvSKU = itemView.findViewById(R.id.tvId);
            tvPrice = itemView.findViewById(R.id.tvGia);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            imgProduct = itemView.findViewById(R.id.imgAvatar);
        }
    }

    // Hàm lọc sản phẩm theo tên không dấu
    public void filter(String query) {
        productModelList.clear();
        if (query.isEmpty()) {
            productModelList.addAll(productModelListFull);
        } else {
            for (Product_Model product : productModelListFull) {
                if (removeDiacritics(product.getNamePro().toLowerCase()).contains(query.toLowerCase())) {
                    productModelList.add(product);
                }
            }
        }
        notifyDataSetChanged();
    }

    // Sắp xếp theo giá cao đến thấp
    public void sortByPriceDescending() {
        Collections.sort(productModelList, (p1, p2) -> Double.compare(p2.getPrice(), p1.getPrice()));
        notifyDataSetChanged();
    }

    // Sắp xếp theo giá thấp đến cao
    public void sortByPriceAscending() {
        Collections.sort(productModelList, (p1, p2) -> Double.compare(p1.getPrice(), p2.getPrice()));
        notifyDataSetChanged();
    }

    // Hàm chuyển đổi chuỗi có dấu thành không dấu
    private String removeDiacritics(String input) {
        String normalized = Normalizer.normalize(input, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(normalized).replaceAll("");
    }
}
