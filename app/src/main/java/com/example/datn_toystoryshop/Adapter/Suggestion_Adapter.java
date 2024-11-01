package com.example.datn_toystoryshop.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.datn_toystoryshop.Model.Product_Model;
import com.example.datn_toystoryshop.Product_detail;
import com.example.datn_toystoryshop.R;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class Suggestion_Adapter extends RecyclerView.Adapter<Suggestion_Adapter.SuggestionViewHolder> {
    private List<Product_Model> suggestionList;
    private List<Product_Model> originalList;
    private Context context;

    public Suggestion_Adapter(Context context, List<Product_Model> suggestionList) {
        this.context = context;
        this.suggestionList = suggestionList;
        this.originalList = new ArrayList<>(suggestionList); // Sao chép danh sách ban đầu
    }

    @NonNull
    @Override
    public SuggestionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_suggestion, parent, false);
        return new SuggestionViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull SuggestionViewHolder holder, int position) {
        Product_Model product = suggestionList.get(position);

        holder.tvName.setText(product.getNamePro());
        holder.tvPrice.setText(String.format("%,.0fđ", product.getPrice()));
        // Tải ảnh từ URL với Glide
        Glide.with(context)
                .load(product.getImgPro())
                .placeholder(R.drawable.product1)
                .into(holder.imgProduct);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Product_detail.class);
                // Truyền dữ liệu sản phẩm vào Intent
                intent.putExtra("productId", product.get_id());
                intent.putExtra("productName", product.getNamePro());
                intent.putExtra("productPrice", product.getPrice());
                intent.putExtra("productImg", product.getImgPro());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return suggestionList.size();
    }

    // Phương thức loại bỏ dấu trong chuỗi
    private String removeDiacritics(String str) {
        String normalized = Normalizer.normalize(str, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(normalized).replaceAll("");
    }

    // Phương thức lọc danh sách dựa trên tên sản phẩm không dấu
    public void filterList(String query) {
        String queryWithoutDiacritics = removeDiacritics(query).toLowerCase();
        suggestionList.clear();

        if (query.isEmpty()) {
            suggestionList.addAll(originalList);
        } else {
            for (Product_Model product : originalList) {
                String productNameWithoutDiacritics = removeDiacritics(product.getNamePro()).toLowerCase();
                if (productNameWithoutDiacritics.contains(queryWithoutDiacritics)) {
                    suggestionList.add(product);
                }
            }
        }
        notifyDataSetChanged();
    }

    public static class SuggestionViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvPrice;
        ImageView imgProduct;
        public SuggestionViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.suggestion_product_name);
            tvPrice = itemView.findViewById(R.id.suggestion_product_price);
            imgProduct = itemView.findViewById(R.id.suggestion_product_img);
        }
    }
}
