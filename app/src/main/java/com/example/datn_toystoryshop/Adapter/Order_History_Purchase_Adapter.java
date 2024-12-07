package com.example.datn_toystoryshop.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.datn_toystoryshop.Confirm_Detail;
import com.example.datn_toystoryshop.Model.Cart_Model;
import com.example.datn_toystoryshop.Model.Order_Model;
import com.example.datn_toystoryshop.Model.Refund_Model;
import com.example.datn_toystoryshop.OrderHist_Detail;
import com.example.datn_toystoryshop.Product_detail;
import com.example.datn_toystoryshop.Profile.ContactSupport_screen;
import com.example.datn_toystoryshop.R;
import com.example.datn_toystoryshop.Server.APIService;
import com.example.datn_toystoryshop.Shopping.Cart_screen;
import com.example.datn_toystoryshop.history.History_purchase_screen;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Order_History_Purchase_Adapter extends RecyclerView.Adapter<Order_History_Purchase_Adapter.OrderViewHolder> {
    private Context context;
    private List<Order_Model> orderList;
    private APIService apiService;
    private String documentId;
    public Order_History_Purchase_Adapter(Context context, List<Order_Model> orderList, APIService apiService,String documentId) {
        this.context = context;
        this.orderList = orderList;
        this.apiService = apiService;
        this.documentId = documentId;
    }

    @NonNull
    @Override
    public Order_History_Purchase_Adapter.OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_history_purchase, parent, false);
        return new Order_History_Purchase_Adapter.OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Order_History_Purchase_Adapter.OrderViewHolder holder, int position) {
        Order_Model order = orderList.get(position);

        holder.textStatus.setText(order.getOrderStatus());
        holder.textRevenueAll.setText(String.format(": %,.0f Đ", (double) order.getRevenue_all()));

        List<Order_Model.ProductDetail> productDetails = order.getProdDetails();
        boolean isMoreThanTwo = productDetails.size() > 2;

        // Hiển thị tối đa 2 sản phẩm đầu tiên
        List<Order_Model.ProductDetail> displayProductDetails = isMoreThanTwo ? productDetails.subList(0, 2) : productDetails;

        // Setup RecyclerView con để hiển thị sản phẩm
        OrderHistoryProductAdapter productAdapter = new OrderHistoryProductAdapter(context, displayProductDetails,apiService, order.get_id());
        holder.recyclerViewProducts.setLayoutManager(new LinearLayoutManager(context));
        holder.recyclerViewProducts.setAdapter(productAdapter);

//                 Thiết lập sự kiện click để mở màn hình chi tiết sản phẩm
        holder.itemView.setOnClickListener(v -> {
            // Chuyển đến màn hình chi tiết sản phẩm
            Intent intent = new Intent(context, OrderHist_Detail.class);
            intent.putExtra("orderId", order.get_id());
            context.startActivity(intent);
        });
        holder.btnBuyBack.setOnClickListener(v -> {
            for (Order_Model.ProductDetail productDetail : order.getProdDetails()) {
                String productId = productDetail.getProdId();
                int currentQuantity = productDetail.getQuantity();
                String selectedColor = productDetail.getProdSpecification();

                // Gọi hàm addToCart với thông tin từ sản phẩm
               addToCart(productId, currentQuantity, documentId, selectedColor);
            }
        });
        holder.btnreturn.setOnClickListener(v -> {
            for (Order_Model.ProductDetail productDetail : order.getProdDetails()) {
                String productId = productDetail.getProdId();

                // Gọi hàm addToRefund với các tham số
                addToRefund(order.get_id(), documentId, productId);
                deleteOrder(order.get_id(),"Hoàn hàng");
            }
        });


        // Hiển thị/ẩn nút "Xem thêm" dựa vào số lượng sản phẩm
        if (isMoreThanTwo) {
            holder.showMoreOrder.setVisibility(View.VISIBLE);

            // Flag để theo dõi trạng thái của nút "Xem thêm"
            final boolean[] isExpanded = {false};

            holder.showMoreOrder.setOnClickListener(v -> {
                if (isExpanded[0]) {
                    // Nếu đang mở rộng, thu gọn lại về 2 sản phẩm đầu tiên
                    productAdapter.updateProductList(productDetails.subList(0, 2));
                    holder.showMoreOrder.setText("Xem thêm"); // Đổi text về "Xem thêm"
                } else {
                    // Nếu đang thu gọn, hiển thị tất cả sản phẩm
                    productAdapter.updateProductList(productDetails);
                    holder.showMoreOrder.setText("Thu gọn"); // Đổi text thành "Thu gọn"
                }
                isExpanded[0] = !isExpanded[0]; // Đảo trạng thái
            });
        } else {
            holder.showMoreOrder.setVisibility(View.GONE); // Ẩn nút "Xem thêm" nếu không có nhiều hơn 2 sản phẩm
        }
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public static class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView textStatus, textRevenueAll, showMoreOrder,btnBuyBack,btnreturn ;
        RecyclerView recyclerViewProducts;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            textStatus = itemView.findViewById(R.id.tvOrderStatus);
            btnreturn = itemView.findViewById(R.id.btnreturn);
            showMoreOrder  = itemView.findViewById(R.id.show_more_oder);
            textRevenueAll = itemView.findViewById(R.id.tvTotalPrice);
            btnBuyBack = itemView.findViewById(R.id.btnBuyBack);
            recyclerViewProducts = itemView.findViewById(R.id.rvProductList);
        }
    }
    private void addToCart(String productId, int currentQuantity, String documentId, String selectedColor) {

        Cart_Model cartModel = new Cart_Model(
                productId,                 // ID của sản phẩm
                currentQuantity,        // Số lượng sản phẩm
                documentId,                   // ID khách hàng (thay thế bằng ID thực tế của người dùng)
                selectedColor              // Thông số sản phẩm (ví dụ: màu sắc đã chọn)
        );

        // Gọi API để thêm sản phẩm vào giỏ hàng
        Call<Cart_Model> call = apiService.addToCart(cartModel);
        call.enqueue(new Callback<Cart_Model>() {
            @Override
            public void onResponse(Call<Cart_Model> call, Response<Cart_Model> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(context, "Thêm vào giỏ hàng thành công!", Toast.LENGTH_SHORT).show();
                    // Chuyển đến màn hình Cart_screen
                    Intent intent = new Intent(context, Cart_screen.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.putExtra("documentId", documentId);
                    context.startActivity(intent);
                } else {
                    Toast.makeText(context, "Không thể thêm vào giỏ hàng!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Cart_Model> call, Throwable t) {
                Toast.makeText(context, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addToRefund(String orderId, String cusId, String productId) {

        Refund_Model refundModel = new Refund_Model(
                null, // ID sẽ được backend tạo tự động
                orderId, // ID đơn hàng
                cusId, // ID khách hàng
                "Yêu cầu hoàn hàng cho sản phẩm: " + productId, // Nội dung lý do hoàn hàng
                String.valueOf(System.currentTimeMillis()), // Thời gian hoàn hàng
                "Chờ xác nhận" // Trạng thái mặc định là đang chờ xử lý
        );

        // Gọi API để thêm sản phẩm vào giỏ hàng
        Call<Refund_Model> call = apiService.addToRefund(refundModel);
        call.enqueue(new Callback<Refund_Model>() {
            @Override
            public void onResponse(Call<Refund_Model> call, Response<Refund_Model> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(context, "Thêm vào refund thành công!", Toast.LENGTH_SHORT).show();
                    // Chuyển đến màn hình Cart_screen
//                    Intent intent = new Intent(context, Cart_screen.class);
//                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                    intent.putExtra("documentId", documentId);
//                    context.startActivity(intent);
                } else {
                    Toast.makeText(context, "Không thể thêm refund", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Refund_Model> call, Throwable t) {
                Toast.makeText(context, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void deleteOrder(String orderId, String newStatus) {
        // Tạo model để gửi dữ liệu
        Order_Model orderModel = new Order_Model();
        orderModel.setOrderStatus(newStatus); // Thiết lập trạng thái đơn hàng mới

        // Gọi API qua Retrofit
        Call<Order_Model> call = apiService.putorderUpdate(orderId, orderModel);
        call.enqueue(new Callback<Order_Model>() {
            @Override
            public void onResponse(Call<Order_Model> call, Response<Order_Model> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // Thành công, hiển thị kết quả
                    Toast.makeText(context, "Cập nhật thành công!", Toast.LENGTH_SHORT).show();
                    Log.d("API", "Cập nhật thành công: " + response.body().toString());
                } else {
                    // Xử lý lỗi trả về từ server
                    Toast.makeText(context, "Cập nhật thất bại!", Toast.LENGTH_SHORT).show();
                    Log.e("API", "Lỗi trả về: " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<Order_Model> call, Throwable t) {
                // Lỗi kết nối hoặc các lỗi khác
                Toast.makeText(context, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("API", "Lỗi kết nối: " + t.getMessage());
            }
        });
    }

}
