package com.example.datn_toystoryshop.Adapter;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.datn_toystoryshop.Model.Product_Model;
import com.example.datn_toystoryshop.R;

import java.util.List;
public class Cart_Adapter extends RecyclerView.Adapter<Cart_Adapter.CartViewHolder> {

    private final Context context;
    private final List<Product_Model> productList;

    public Cart_Adapter(Context context, List<Product_Model> productList) {
        this.context = context;
        this.productList = productList;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_cart, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        Product_Model productModel = productList.get(position);
        holder.productName.setText(productModel.getNamePro());
        holder.productPrice.setText(String.format("₫%.0f", productModel.getPrice()));
        holder.originalPrice.setText(String.format("₫%.0f", productModel.getPrice()));

        // Set OnClickListener cho CheckBox
        holder.checkBoxSelectItem.setOnClickListener(v ->
                Toast.makeText(context, "CheckBox selected: " + productModel.getNamePro(), Toast.LENGTH_SHORT).show()
        );

        // Set OnClickListener cho ImageView sản phẩm
        holder.productImage.setOnClickListener(v ->
                Toast.makeText(context, "Image clicked: " + productModel.getNamePro(), Toast.LENGTH_SHORT).show()
        );

        // Set OnClickListener cho TextView tên sản phẩm
        holder.productName.setOnClickListener(v ->
                Toast.makeText(context, "Product name clicked: " + productModel.getNamePro(), Toast.LENGTH_SHORT).show()
        );

        // Set OnClickListener cho Spinner màu sắc
        holder.colorSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedColor = parent.getItemAtPosition(position).toString();
                Toast.makeText(context, "Color selected: " + selectedColor, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        // Set OnClickListener cho nút giảm số lượng
        holder.btnDecrease.setOnClickListener(v ->
                Toast.makeText(context, "Decrease quantity for: " + productModel.getNamePro(), Toast.LENGTH_SHORT).show()
        );

        // Set OnClickListener cho nút tăng số lượng
        holder.btnIncrease.setOnClickListener(v ->
                Toast.makeText(context, "Increase quantity for: " + productModel.getNamePro(), Toast.LENGTH_SHORT).show()
        );
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class CartViewHolder extends RecyclerView.ViewHolder {
        CheckBox checkBoxSelectItem;
        ImageView productImage;
        TextView productName, productPrice, originalPrice, btnDecrease, tvQuantity, btnIncrease;
        Spinner colorSpinner;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            checkBoxSelectItem = itemView.findViewById(R.id.checkBoxSelectItem);
            productImage = itemView.findViewById(R.id.productImage);
            productName = itemView.findViewById(R.id.productName);
            productPrice = itemView.findViewById(R.id.productPrice);
            originalPrice = itemView.findViewById(R.id.originalPrice);
            btnDecrease = itemView.findViewById(R.id.btnDecrease);
            tvQuantity = itemView.findViewById(R.id.tvQuantity);
            btnIncrease = itemView.findViewById(R.id.btnIncrease);
            colorSpinner = itemView.findViewById(R.id.colorSpinner);
        }
    }
}