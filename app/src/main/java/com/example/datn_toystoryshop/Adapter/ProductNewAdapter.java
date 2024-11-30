package com.example.datn_toystoryshop.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
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
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

public class ProductNewAdapter extends RecyclerView.Adapter<ProductNewAdapter.ProductViewHolder> {

    private List<Product_Model> productModelList;
    private List<Product_Model> productModelListFull;
    private Context context;
    private boolean isInHomeFragment;
    private String documentId;

    public ProductNewAdapter(Context context, List<Product_Model> productModelList) {
        this.context = context;
        this.productModelList = productModelList;
        this.productModelListFull = new ArrayList<>(productModelList);
    }

    public ProductNewAdapter(Context context, List<Product_Model> productModelList, boolean isInHomeFragment, String documentId) {
        this.context = context;
        this.productModelList = productModelList;
        this.isInHomeFragment = isInHomeFragment;
        this.documentId = documentId;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_new_product, parent, false);
        return new ProductViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Log.e("OrderHistoryAdapter", "j66666666666666666ProductNewAdapter" + documentId);
        if (position < productModelList.size()) {
            Product_Model product = productModelList.get(position);
            holder.tvName.setText(product.getNamePro());
            String shortId = product.get_id().substring(0, 6);
            holder.tvSKU.setText("Mã SP: " + shortId);
            holder.tvPrice.setText(String.format("%,.0fđ", product.getPrice()));
            holder.tvStatus.setText(product.isStatusPro() ? "Còn hàng" : "Hết hàng");
            if (isInHomeFragment) {
                holder.newIcon.setVisibility(View.VISIBLE);
            } else {
                holder.newIcon.setVisibility(View.GONE);
            }
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, Product_detail.class);
                    intent.putExtra("documentId", documentId);
                    intent.putExtra("productId", product.get_id());// Truyền mã ID của sản phẩm
                    intent.putExtra("owerId", product.getOwerId());// Truyền ID của chủ sở hữu sản phẩm
                    intent.putExtra("statusPro", product.isStatusPro());// Truyền trạng thái tồn kho của sản phẩm (true nếu còn hàng, false nếu hết hàng)
                    intent.putExtra("productPrice", product.getPrice());// Truyền giá của sản phẩm
                    intent.putExtra("desPro", product.getDesPro());// Truyền mô tả của sản phẩm
                    intent.putExtra("creatDatePro", product.getCreatDatePro());// Truyền ngày tạo sản phẩm
                    intent.putExtra("quantity", product.getQuantity());// Truyền số lượng sản phẩm có sẵn
                    intent.putExtra("listPro", product.getListPro());// Truyền danh sách trạng thái của sản phẩm (danh sách dưới dạng chuỗi)
                    intent.putStringArrayListExtra("productImg", new ArrayList<>(product.getImgPro()));// Truyền danh sách URL hình ảnh của sản phẩm
                    intent.putExtra("productName", product.getNamePro());// Truyền tên của sản phẩm
                    intent.putExtra("cateId", product.getCateId());// Truyền ID danh mục của sản phẩm
                    intent.putExtra("brand", product.getBrand());
                    context.startActivity(intent);
                }
            });
            List<String> images = product.getImgPro();
            if (images != null && !images.isEmpty()) {
                holder.setImageRotation(images);
            }
        }
    }

    @Override
    public int getItemCount() {
        return productModelList.size();
    }

    @Override
    public void onViewRecycled(@NonNull ProductViewHolder holder) {
        super.onViewRecycled(holder);
        holder.stopImageRotation();
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvSKU, tvPrice, tvStatus;
        ImageView imgProduct, newIcon;
        private Handler handler = new Handler();
        private Runnable runnable;
        private int currentImageIndex = 0;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvTen);
            tvSKU = itemView.findViewById(R.id.tvId);
            tvPrice = itemView.findViewById(R.id.tvGia);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            imgProduct = itemView.findViewById(R.id.imgAvatar);
            newIcon = itemView.findViewById(R.id.new_icon);
        }

        public void setImageRotation(List<String> images) {
            stopImageRotation();

            if (isValidContextForGlide(imgProduct.getContext()) && !images.isEmpty()) {
                currentImageIndex = 0;
                Glide.with(imgProduct.getContext())
                        .load(images.get(currentImageIndex))
                        .placeholder(R.drawable.product1)
                        .into(imgProduct);
            }

            runnable = new Runnable() {
                @Override
                public void run() {
                    if (isValidContextForGlide(imgProduct.getContext()) && !images.isEmpty()) {
                        currentImageIndex = (currentImageIndex + 1) % images.size();
                        Glide.with(imgProduct.getContext())
                                .load(images.get(currentImageIndex))
                                .placeholder(R.drawable.product1)
                                .into(imgProduct);

                        handler.postDelayed(this, 3000);
                    }
                }
            };

            handler.postDelayed(runnable, 3000);
        }

        public void stopImageRotation() {
            if (runnable != null) {
                handler.removeCallbacks(runnable);
            }
        }

        private boolean isValidContextForGlide(Context context) {
            if (context instanceof Activity) {
                Activity activity = (Activity) context;
                return !activity.isDestroyed() && !activity.isFinishing();
            }
            return true;
        }
    }

    public void updateData(List<Product_Model> newProductList) {
        productModelList.clear();
        if (newProductList != null) {
            productModelList.addAll(newProductList);
        }
        notifyDataSetChanged();
    }

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

    public void sortByPriceDescending() {
        Collections.sort(productModelList, (p1, p2) -> Double.compare(p2.getPrice(), p1.getPrice()));
        notifyDataSetChanged();
    }

    public void sortByPriceAscending() {
        Collections.sort(productModelList, (p1, p2) -> Double.compare(p1.getPrice(), p2.getPrice()));
        notifyDataSetChanged();
    }

    private String removeDiacritics(String input) {
        String normalized = Normalizer.normalize(input, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(normalized).replaceAll("");
    }
}
