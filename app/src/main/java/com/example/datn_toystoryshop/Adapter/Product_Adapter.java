package com.example.datn_toystoryshop.Adapter;

import android.app.Activity;
import android.content.Context;
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
import com.example.datn_toystoryshop.R;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
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
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_popular, parent, false);
        return new ProductViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {

        // Kiểm tra xem vị trí có hợp lệ không trước khi truy cập
        if (position < productModelList.size()) {
            Product_Model product = productModelList.get(position);
            holder.tvName.setText(product.getNamePro());
            String shortId = product.get_id().substring(0, 6);
//            holder.tvSKU.setText("Mã SP: " + product.get_id());
            holder.tvSKU.setText("Mã SP: " + shortId);
            holder.tvPrice.setText(String.format("%,.0fđ", product.getPrice()));
            holder.tvStatus.setText(product.isStatusPro() ? "Còn hàng" : "Hết hàng");

            List<String> images = product.getImgPro();
            if (images != null && !images.isEmpty()) {
                holder.setImageRotation(images);
            }

        }
    }

    @Override
    public int getItemCount() {
        Log.d("Product_Adapter", "Item count: " + productModelList.size());
        return productModelList.size();
    }


    @Override
    public void onViewRecycled(@NonNull ProductViewHolder holder) {
        super.onViewRecycled(holder);
        holder.stopImageRotation(); // Dừng Handler khi ViewHolder bị tái chế
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvSKU, tvPrice, tvStatus;
        ImageView imgProduct;
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
        }

        public void setImageRotation(List<String> images) {
            // Dừng runnable cũ nếu có
            stopImageRotation();

            // Tải ảnh đầu tiên ngay lập tức
            if (isValidContextForGlide(imgProduct.getContext()) && !images.isEmpty()) {
                currentImageIndex = 0; // Đặt chỉ số hình ảnh về 0 trước khi tải hình
                Glide.with(imgProduct.getContext())
                        .load(images.get(currentImageIndex))
                        .placeholder(R.drawable.product1)
                        .into(imgProduct);
            }

            // Tạo runnable mới để thay đổi ảnh sau mỗi 3 giây
            runnable = new Runnable() {
                @Override
                public void run() {
                    if (isValidContextForGlide(imgProduct.getContext()) && !images.isEmpty()) {
                        currentImageIndex = (currentImageIndex + 1) % images.size(); // Cập nhật vị trí ảnh
                        Glide.with(imgProduct.getContext())
                                .load(images.get(currentImageIndex))
                                .placeholder(R.drawable.product1)
                                .into(imgProduct);

                        handler.postDelayed(this, 3000); // Tiếp tục sau 3 giây
                    }
                }
            };

            // Bắt đầu chạy runnable sau 3 giây
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
            productModelList.addAll(newProductList); // Cập nhật danh sách hiện tại
            // Không cần phải xóa productModelListFull
            productModelListFull = new ArrayList<>(newProductList); // Cập nhật lại bản sao danh sách gốc
        }
        notifyDataSetChanged(); // Cập nhật RecyclerView
        Log.d("ApplyFilter", "Filtered product list size: " + productModelList.size());
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




    private String removeDiacritics(String input) {
        String normalized = Normalizer.normalize(input, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(normalized).replaceAll("");
    }
}

