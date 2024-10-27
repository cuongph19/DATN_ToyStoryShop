// ProductAdapter.java
package com.example.datn_toystoryshop.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.datn_toystoryshop.Model.Product;
import com.example.datn_toystoryshop.R;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {
    private List<Product> productList;
    private List<Product> filteredList;

    public ProductAdapter(List<Product> productList) {
        this.productList = productList;
        this.filteredList = new ArrayList<>(productList); // Copy danh sách ban đầu vào filteredList
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = filteredList.get(position);
        holder.productName.setText(product.getName());
        holder.productSku.setText("SKU: " + product.getSku());
        holder.productPrice.setText(product.getPrice());
        holder.productStatus.setText(product.getStatus());
        holder.productImage.setImageResource(product.getImageResourceId());
    }

    @Override
    public int getItemCount() {
        return filteredList.size();
    }
    public static String removeDiacritics(String input) {
        String normalized = Normalizer.normalize(input, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(normalized).replaceAll("");
    }

    // Hàm để lọc sản phẩm theo tên không dấu
    public void filter(String text) {
        filteredList.clear();
        String searchText = removeDiacritics(text).toLowerCase(); // Chuyển đổi chuỗi tìm kiếm thành không dấu và viết thường

        if (searchText.isEmpty()) {
            filteredList.addAll(productList); // Hiển thị tất cả sản phẩm nếu ô tìm kiếm rỗng
        } else {
            for (Product product : productList) {
                // Chuyển đổi tên sản phẩm thành không dấu và viết thường
                String productName = removeDiacritics(product.getName()).toLowerCase();
                if (productName.contains(searchText)) {
                    filteredList.add(product);
                }
            }
        }
        notifyDataSetChanged(); // Cập nhật giao diện
    }


    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        ImageView productImage;
        TextView productName, productSku, productPrice, productStatus;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.product_image);
            productName = itemView.findViewById(R.id.product_name);
            productSku = itemView.findViewById(R.id.product_sku);
            productPrice = itemView.findViewById(R.id.product_price);
            productStatus = itemView.findViewById(R.id.product_status);
        }
    }
}
