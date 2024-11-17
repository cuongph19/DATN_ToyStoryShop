package com.example.datn_toystoryshop.Adapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.datn_toystoryshop.Model.Cart_Model;
import com.example.datn_toystoryshop.Model.Product_Model;
import com.example.datn_toystoryshop.R;
import com.example.datn_toystoryshop.Server.APIService;
import com.example.datn_toystoryshop.Server.ProductCallback;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Order_Adapter_Cart extends RecyclerView.Adapter<Order_Adapter_Cart.ProductViewHolder> {
private List<String> productIds;
private APIService apiService;
    private boolean showAll = false; // Trạng thái hiển thị

    public Order_Adapter_Cart(List<String> productIds, APIService apiService) {
        this.productIds = productIds;
        this.apiService = apiService;
    }
    // Thay đổi trạng thái hiển thị
    public void toggleShowAll() {
        showAll = !showAll;
        notifyDataSetChanged(); // Cập nhật lại giao diện
    }

@NonNull
@Override
public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View itemView = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.item_oder, parent, false);
    return new ProductViewHolder(itemView);
}

@Override
public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
    String cartId = productIds.get(position);
    // Gọi API để lấy dữ liệu chi tiết từ MongoDB
    apiService.getCartById(cartId).enqueue(new Callback<Cart_Model>() {
        @Override
        public void onResponse(Call<Cart_Model> call, Response<Cart_Model> response) {
            if (response.isSuccessful() && response.body() != null) {
                Cart_Model cartModel = response.body();

                // Set dữ liệu vào các view
                holder.productName.setText(cartModel.getProdId());
              //  holder.productPrice.setText(String.format("%,.0fđ", cartModel.getPrice()));
               // holder.productQuantity.setText(cartModel.getQuantity()); // Số lượng mặc định
                holder.productQuantity.setText("x" + cartModel.getQuantity());
                holder.productType.setText(cartModel.getProdSpecification());
                // Gọi phương thức lấy dữ liệu từ MongoDB với prodId

                loadProductById(apiService, cartModel.getProdId(), new ProductCallback() {
                    @Override
                    public void onSuccess(Product_Model product) {
                        holder.productName.setText(product.getNamePro());
                        holder.productPrice.setText(String.format("%,.0fđ",product.getPrice()));
                        List<String> images = product.getImgPro();
                        Glide.with(holder.productImage.getContext())
                                .load(images.get(0))
                                .into(holder.productImage);


                    }

                    @Override
                    public void onFailure(Throwable t) {
                        Log.e("cartAdapter", "Failed to load product details: " + t.getMessage());
                    }
                });

            } else {
                holder.productName.setText("Product Not Found");
            }
        }

        @Override
        public void onFailure(Call<Cart_Model> call, Throwable t) {
            holder.productName.setText("Error loading product");
            Log.e("Oder_Adapter", "Error: " + t.getMessage());
        }
    });
}

    @Override
    public int getItemCount() {
        if (!showAll && productIds.size() > 2) {
            return 2; // Chỉ hiển thị tối đa 2 sản phẩm khi chưa nhấn "Xem thêm"
        }
        return productIds.size(); // Hiển thị tất cả khi nhấn "Xem thêm"
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        TextView productName, productQuantity, productType, productPrice;
        ImageView productImage;

        public ProductViewHolder(View view) {
            super(view);
            productName = view.findViewById(R.id.product_name);
            productQuantity = view.findViewById(R.id.product_quantity);
            productType = view.findViewById(R.id.product_type);
            productImage = view.findViewById(R.id.product_image);
            productPrice = view.findViewById(R.id.product_price);
        }
    }
    private void loadProductById(APIService apiService, String prodId, ProductCallback callback) {
        Call<Product_Model> call = apiService.getProductById(prodId);
        call.enqueue(new Callback<Product_Model>() {
            @Override
            public void onResponse(Call<Product_Model> call, Response<Product_Model> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Product_Model product = response.body();
                    Log.d("cartAdapter", "Product retrieved: " + product.getNamePro() + ", Price: " + product.getPrice());
                    callback.onSuccess(product);
                } else {
                    Log.e("cartAdapter", "Response unsuccessful or product body is null");
                }
            }

            @Override
            public void onFailure(Call<Product_Model> call, Throwable t) {
                Log.e("cartAdapter", "API call failed: " + t.getMessage());
                callback.onFailure(t);
            }
        });
    }
}