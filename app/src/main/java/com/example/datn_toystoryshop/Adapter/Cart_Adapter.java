package com.example.datn_toystoryshop.Adapter;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.datn_toystoryshop.Model.Cart_Model;
import com.example.datn_toystoryshop.Model.Product_Model;
import com.example.datn_toystoryshop.Product_detail;
import com.example.datn_toystoryshop.R;
import com.example.datn_toystoryshop.Server.APIService;
import com.example.datn_toystoryshop.Server.ProductCallback;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Cart_Adapter extends RecyclerView.Adapter<Cart_Adapter.CartViewHolder> {

    private Context context;
    private List<Cart_Model> cartList;
    private com.example.datn_toystoryshop.Server.APIService APIService;

    public Cart_Adapter(Context context, List<Cart_Model> cartList, APIService apiService) {
        this.context = context;
        this.cartList = cartList;
        this.APIService = apiService;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_cart, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Cart_Adapter.CartViewHolder holder, int position) {
        Cart_Model cart = cartList.get(position);
        String cartId = cart.get_id();
        String prodId = cart.getProdId();

        int quantity = cart.getQuantity();
        holder.tvQuantity.setText(String.valueOf(quantity)); // Đặt giá trị ban đầu cho số lượng là 1
        // Sự kiện tăng số lượng
        holder.btnIncrease.setOnClickListener(v -> {
            int currentQuantity = Integer.parseInt(holder.tvQuantity.getText().toString());
            currentQuantity++;
            holder.tvQuantity.setText(String.valueOf(currentQuantity));
            Toast.makeText(context, "Số lượng: " + currentQuantity, Toast.LENGTH_SHORT).show();
        });

        // Sự kiện giảm số lượng với giá trị tối thiểu là 1
        holder.btnDecrease.setOnClickListener(v -> {
            int currentQuantity = Integer.parseInt(holder.tvQuantity.getText().toString());
            if (currentQuantity > 1) {
                currentQuantity--;
                holder.tvQuantity.setText(String.valueOf(currentQuantity));
                Toast.makeText(context, "Số lượng: " + currentQuantity, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "Số lượng tối thiểu là 1", Toast.LENGTH_SHORT).show();
            }
        });
        // Thiết lập Spinner với dữ liệu từ arrays
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(context,
                R.array.color_options, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        holder.colorSpinner.setAdapter(adapter);
// Lấy giá trị `prodSpecification` từ Cart_Model
        String prodSpecification = cart.getProdSpecification();

// Tìm vị trí của `prodSpecification` trong adapter
        int defaultPosition = adapter.getPosition(prodSpecification);

// Nếu tìm thấy, thiết lập vị trí mặc định cho Spinner
        if (defaultPosition >= 0) {
            holder.colorSpinner.setSelection(defaultPosition);
        } else {
            // Nếu không tìm thấy, đặt mặc định là item đầu tiên (hoặc bạn có thể xử lý khác nếu muốn)
            holder.colorSpinner.setSelection(0);
        }

        // Thiết lập onItemSelectedListener cho colorSpinner
        holder.colorSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Hiển thị Toast với giá trị được chọn khi người dùng chọn item
                String selectedItem = parent.getItemAtPosition(position).toString();
                Toast.makeText(context, "Bạn đã chọn: " + selectedItem, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Không làm gì khi không có gì được chọn
            }
        });
        // Gọi phương thức lấy dữ liệu từ MongoDB với prodId
        loadProductById(APIService, prodId, new ProductCallback() {
            @Override
            public void onSuccess(Product_Model product) {
                holder.productName.setText(product.getNamePro());
                holder.productPrice.setText(String.valueOf(product.getPrice()));
                List<String> images = product.getImgPro();
                if (images != null && !images.isEmpty()) {
                    Glide.with(context).load(images.get(0)).into(holder.productImage);
                }
                // Thiết lập sự kiện click để mở màn hình chi tiết
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, Product_detail.class);

                        // Truyền các thuộc tính sản phẩm qua Intent
                        intent.putExtra("productId", product.get_id());
                        intent.putExtra("owerId", product.getOwerId());
                        intent.putExtra("statusPro", product.isStatusPro());
                        intent.putExtra("productPrice", product.getPrice());
                        intent.putExtra("desPro", product.getDesPro());
                        intent.putExtra("creatDatePro", product.getCreatDatePro());
                        intent.putExtra("quantity", product.getQuantity());
                        intent.putExtra("listPro", product.getListPro());
                        intent.putStringArrayListExtra("productImg", new ArrayList<>(product.getImgPro()));
                        intent.putExtra("productName", product.getNamePro());
                        intent.putExtra("cateId", product.getCateId());
                        intent.putExtra("brand", product.getBrand());
                        // Truyền _id của cart vào Intent
                        intent.putExtra("cartId", cartId);
                        context.startActivity(intent);
                    }
                });
//                holder.heartIcon.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        deletecart(cart.getProdId(), holder);
//
//                        holder.heartIcon.setColorFilter(Color.parseColor("#A09595"));
//                        // Nếu bạn muốn thêm hiệu ứng thì có thể thêm logic ở đây
//                    }
//                });
            }

            @Override
            public void onFailure(Throwable t) {
                Log.e("cartAdapter", "Failed to load product details: " + t.getMessage());
            }
        });
//        holder.itemView.setOnTouchListener(new View.OnTouchListener() {
//            private float initialX = 0;
//            private boolean isSwiped = false;
//            private float moveDistance = convertDpToPx(holder.itemView, 100);
//
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                switch (event.getAction()) {
//                    case MotionEvent.ACTION_DOWN:
//                        initialX = event.getX();
//                        break;
//
//                    case MotionEvent.ACTION_MOVE:
//                        float deltaX = event.getX() - initialX;
//                        if (deltaX < -20) {
//                            if (!isSwiped) {
//                                isSwiped = true;
//                                holder.hiddenTextView.setVisibility(View.VISIBLE);
//                            }
//                            holder.itemView.setTranslationX(Math.max(deltaX, -moveDistance));
//                        }
//                        break;
//
//                    case MotionEvent.ACTION_UP:
//                    case MotionEvent.ACTION_CANCEL:
//                        if (!isSwiped) {
//                            holder.itemView.setTranslationX(0);
//                        }
//                        break;
//                }
//                return true;
//            }
//
//            private float convertDpToPx(View v, float dp) {
//                float density = v.getContext().getResources().getDisplayMetrics().density;
//                return dp * density;
//            }
//        });
//

    }

    @Override
    public int getItemCount() {
        return cartList.size();
    }

    public static class CartViewHolder extends RecyclerView.ViewHolder {
        CheckBox checkBoxSelectItem;
        ImageView productImage;
        TextView productName, freeReturn, productPrice, originalPrice, btnDecrease, tvQuantity, btnIncrease, hiddenTextView;
        Spinner colorSpinner;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            checkBoxSelectItem = itemView.findViewById(R.id.checkBoxSelectItem);
            hiddenTextView = itemView.findViewById(R.id.hiddenTextView);
            productImage = itemView.findViewById(R.id.productImage);
            productName = itemView.findViewById(R.id.productName);
            colorSpinner = itemView.findViewById(R.id.colorSpinner);
            freeReturn = itemView.findViewById(R.id.freeReturn);
            productPrice = itemView.findViewById(R.id.productPrice);
            btnDecrease = itemView.findViewById(R.id.btnDecrease);
            tvQuantity = itemView.findViewById(R.id.tvQuantity);
            btnIncrease = itemView.findViewById(R.id.btnIncrease);
        }
    }
    public ItemTouchHelper.SimpleCallback getItemTouchHelper() {
        return new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                // Xử lý khi vuốt hoàn tất, ví dụ: xóa sản phẩm
            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,
                                    float dX, float dY, int actionState, boolean isCurrentlyActive) {
                if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
                    CartViewHolder cartViewHolder = (CartViewHolder) viewHolder;
                    // Giới hạn khoảng trượt của itemContent (chỉ trượt tối đa khoảng trống của hiddenTextView)
                    float maxSwipe = -cartViewHolder.hiddenTextView.getWidth(); // Giới hạn trượt

                    // Xác định khoảng trượt hợp lý
                    float translationX = Math.max(dX, maxSwipe);

                    // Khi vuốt hết chiều dài, hiện thị hiddenTextView
                    if (translationX == maxSwipe) {
                        cartViewHolder.hiddenTextView.setVisibility(View.VISIBLE);
                    } else {
                        cartViewHolder.hiddenTextView.setVisibility(View.GONE);
                    }

                    cartViewHolder.itemView.setTranslationX(translationX);
                } else {
                    super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
                }
            }


            @Override
            public float getSwipeThreshold(RecyclerView.ViewHolder viewHolder) {
                return 0.5f; // Tỷ lệ vuốt cần thiết để hoàn tất hành động
            }
        };
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

    private void deletecart(String productId, Cart_Adapter.CartViewHolder holder) {
        // Giả sử bạn đã có một APIService đã được định nghĩa cho việc xóa yêu thích
        Call<Void> call = APIService.deleteFavorite(productId);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    // Xóa sản phẩm yêu thích thành công
                  //  holder.heartIcon.setColorFilter(Color.parseColor("#A09595")); // Chỉnh màu trái tim
                    cartList.remove(holder.getAdapterPosition()); // Xóa sản phẩm khỏi danh sách hiển thị
                    notifyItemRemoved(holder.getAdapterPosition()); // Cập nhật RecyclerView
                    notifyItemRangeChanged(holder.getAdapterPosition(), cartList.size()); // Cập nhật lại vị trí của các item
                } else {
                    Log.e("FavoriteAdapter", "Failed to delete favorite: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("FavoriteAdapter", "API call failed: " + t.getMessage());
            }
        });
    }
}

