package com.example.datn_toystoryshop;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.example.datn_toystoryshop.Adapter.ProductImage_Adapter;
import com.example.datn_toystoryshop.Model.Favorite_Model;
import com.example.datn_toystoryshop.Server.APIService;
import com.example.datn_toystoryshop.Server.RetrofitClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Product_detail extends AppCompatActivity {
    private TextView tvProductName, tvProductPrice, tvproductDescription, productStockValue, quantityText, productBrandValue1, productBrandValue2;
    private ImageView btnBack, shareButton, heartIcon ;
    private LinearLayout dotIndicatorLayout;
    private List<View> dotIndicators = new ArrayList<>();
    private double productPrice;
    private boolean statusPro;
    private int owerId, quantity, cateId;
    private String productId, productName, desPro, creatDatePro, listPro, brand;
    private Button decreaseButton , increaseButton;
    private RadioGroup radioGroup;
    private ArrayList<String> productImg; // Danh sách URL ảnh của sản phẩm
    private int currentImageIndex = 0; // Vị trí ảnh hiện tại
    private Handler handler = new Handler(); // Tạo Handler để cập nhật ảnh
    private Runnable imageSwitcherRunnable;
    private View viewDetail1, viewDetail2, viewDetail3;
    private String favoriteId;
    private boolean isFavorite = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        // Ánh xạ các view
        tvProductName = findViewById(R.id.productTitle);
        productStockValue = findViewById(R.id.productStockValue);
        productBrandValue1 = findViewById(R.id.productBrandValue1);
        productBrandValue2 = findViewById(R.id.productBrandValue2);
        tvProductPrice = findViewById(R.id.productPrice);
        tvproductDescription = findViewById(R.id.productDescription);
        dotIndicatorLayout = findViewById(R.id.dotIndicatorLayout);
        btnBack = findViewById(R.id.btnBack);
        shareButton = findViewById(R.id.shareButton);
        heartIcon = findViewById(R.id.heart_icon);
        decreaseButton  = findViewById(R.id.decreaseQuantity);
        increaseButton  = findViewById(R.id.increaseQuantity);
        quantityText  = findViewById(R.id.quantityText);
        radioGroup = findViewById(R.id.radioGroup);
        viewDetail1 = findViewById(R.id.view_detail_1);
        viewDetail2 = findViewById(R.id.view_detail_2);
        viewDetail3 = findViewById(R.id.view_detail_3);

        // Nhận dữ liệu từ Intent
        Intent intent = getIntent();
         productId = intent.getStringExtra("productId");
         owerId = intent.getIntExtra("owerId", -1);
         statusPro = intent.getBooleanExtra("statusPro", false);
         productPrice = intent.getDoubleExtra("productPrice", 0.0);
         desPro = intent.getStringExtra("desPro");
         creatDatePro = intent.getStringExtra("creatDatePro");
         quantity = intent.getIntExtra("quantity", 0);
         listPro = intent.getStringExtra("listPro");
         productImg = intent.getStringArrayListExtra("productImg");
         productName = intent.getStringExtra("productName");
         cateId = intent.getIntExtra("cateId", -1);
        brand = intent.getStringExtra("brand");
        //
        favoriteId = intent.getStringExtra("favoriteId");
        Log.e("Product_detail", "aaaaaaaaaaaaaaaa: " + productId);
        // Hiển thị dữ liệu

        tvProductName.setText(productName);
        tvProductPrice.setText(String.format("%,.0fđ", productPrice));
        productStockValue.setText(String.valueOf(quantity));
        tvproductDescription.setText(desPro);
        productBrandValue1.setText(brand);
        productBrandValue2.setText(brand);

        ViewPager2 productImagePager = findViewById(R.id.productImage);
        ProductImage_Adapter adapter = new ProductImage_Adapter(this, productImg);
        productImagePager.setAdapter(adapter);
        // Tạo dot indicator dựa trên số lượng ảnh
        createDotIndicators(productImg.size());

        // Cập nhật dot indicator khi vuốt ảnh
        productImagePager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                currentImageIndex = position;
                updateDotIndicator();
            }
        });
      checkIfFavorite(productId);
        // Tạo Runnable để tự động thay đổi ảnh sau 3 giây
        imageSwitcherRunnable = new Runnable() {
            @Override
            public void run() {
                if (currentImageIndex < productImg.size()) {
                    productImagePager.setCurrentItem(currentImageIndex++, true);
                } else {
                    currentImageIndex = 0;
                    productImagePager.setCurrentItem(currentImageIndex, true);
                }

                // Lặp lại sau 3 giây
                handler.postDelayed(this, 3000);
            }
        };
        // Bắt đầu tự động chuyển ảnh
        handler.postDelayed(imageSwitcherRunnable, 3000);



        // Sự kiện quay lại
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onBackPressed();
            }
        });
        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareApp(productName);
            }
        });
        heartIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isFavorite) {
                    // Nếu đã yêu thích, xóa khỏi danh sách yêu thích và đổi màu xám
                    heartIcon.setColorFilter(Color.parseColor("#A09595"));
                    deleteFavorite(favoriteId);
                    isFavorite = false; // Cập nhật trạng thái
                } else {
                    // Nếu chưa yêu thích, thêm vào danh sách yêu thích và đổi màu đỏ
                    heartIcon.setColorFilter(Color.RED);

                    // Tạo đối tượng yêu thích
                    Favorite_Model favoriteModel = new Favorite_Model(null, productId, "cusId");

                    // Gửi yêu cầu tới API
                    APIService apiService = RetrofitClient.getInstance().create(APIService.class);
                    Call<Favorite_Model> call = apiService.addToFavorites(favoriteModel);
                    call.enqueue(new Callback<Favorite_Model>() {
                        @Override
                        public void onResponse(Call<Favorite_Model> call, Response<Favorite_Model> response) {
                            if (response.isSuccessful()) {
                                favoriteId = response.body().get_id();
                                isFavorite = true; // Cập nhật trạng thái yêu thích
                                Toast.makeText(getApplicationContext(), "Đã thêm vào yêu thích", Toast.LENGTH_SHORT).show();
                            } else {
                                heartIcon.setColorFilter(Color.parseColor("#A09595"));
                                Toast.makeText(getApplicationContext(), "Thêm yêu thích thất bại", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<Favorite_Model> call, Throwable t) {
                            heartIcon.setColorFilter(Color.parseColor("#A09595"));
                            Toast.makeText(getApplicationContext(), "Lỗi kết nối", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

        int[] currentQuantity = {1}; // Khởi tạo giá trị tối thiểu là 1
        quantityText.setText(String.valueOf(currentQuantity[0])); // Cập nhật TextView ban đầu

        decreaseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentQuantity[0] > 1) { // Kiểm tra để không giảm dưới 1
                    currentQuantity[0]--;
                    quantityText.setText(String.valueOf(currentQuantity[0]));
                    Toast.makeText(getApplicationContext(), "Số lượng: " + currentQuantity[0], Toast.LENGTH_SHORT).show();
                }
            }
        });

        increaseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentQuantity[0]++;
                quantityText.setText(String.valueOf(currentQuantity[0]));
                Toast.makeText(getApplicationContext(), "Số lượng: " + currentQuantity[0], Toast.LENGTH_SHORT).show();
            }
        });
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rbIndividual) {
                    Toast.makeText(getApplicationContext(), "Bạn đã chọn: Mô hình riêng lẻ", Toast.LENGTH_SHORT).show();
                } else if (checkedId == R.id.rbSet) {
                    Toast.makeText(getApplicationContext(), "Bạn đã chọn: Nguyên set 12 bộ", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Hủy Handler khi Activity bị hủy để tránh rò rỉ bộ nhớ
        handler.removeCallbacks(imageSwitcherRunnable);
    }
    private void checkIfFavorite(String productId) {
        APIService apiService = RetrofitClient.getInstance().create(APIService.class);
        Call<Map<String, Boolean>> call = apiService.checkFavorite(productId);

        call.enqueue(new Callback<Map<String, Boolean>>() {
            @Override
            public void onResponse(Call<Map<String, Boolean>> call, Response<Map<String, Boolean>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Boolean exists = response.body().get("exists");

                    if (Boolean.TRUE.equals(exists)) {
                        isFavorite = true; // Cập nhật trạng thái yêu thích
                        heartIcon.setColorFilter(Color.RED); // Đổi sang màu đỏ nếu đã yêu thích
                    } else {
                        isFavorite = false; // Đặt trạng thái không yêu thích
                        heartIcon.setColorFilter(Color.parseColor("#A09595")); // Đổi sang màu xám nếu chưa yêu thích
                    }
                } else {
                    Log.e("API_ERROR", "Không lấy được trạng thái yêu thích của sản phẩm.");
                }
            }

            @Override
            public void onFailure(Call<Map<String, Boolean>> call, Throwable t) {
                Log.e("API_ERROR", "Lỗi kết nối tới API", t);
            }
        });
    }

    private void createDotIndicators(int count) {
        dotIndicators.clear();
        dotIndicatorLayout.removeAllViews();

        for (int i = 0; i < count; i++) {
            View dot = new View(this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            params.width = 10;
            params.height = 10;
            params.setMargins(4, 0, 4, 0);

            dot.setLayoutParams(params);
            dot.setBackgroundResource(R.drawable.dot_inactive); // dot màu xám
            dotIndicators.add(dot);
            dotIndicatorLayout.addView(dot);
        }

        if (!dotIndicators.isEmpty()) {
            dotIndicators.get(0).setBackgroundResource(R.drawable.dot_active); // dot đầu tiên màu xanh
        }
    }
    private void deleteFavorite(String favoriteId) {
        // Gọi API xóa yêu thích
        APIService apiService = RetrofitClient.getInstance().create(APIService.class);
        Call<Void> call = apiService.deleteFavorite(favoriteId); // Đảm bảo phương thức này đã được định nghĩa trong APIService
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "Đã xóa khỏi yêu thích", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Xóa yêu thích thất bại", Toast.LENGTH_SHORT).show();
                    Log.e("API Response", "Response code: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Lỗi kết nối", Toast.LENGTH_SHORT).show();
                Log.e("API Failure", "Error message: " + t.getMessage(), t);
            }
        });
        }
    private void updateDotIndicator() {
        for (int i = 0; i < dotIndicators.size(); i++) {
            dotIndicators.get(i).setBackgroundResource(
                    i == currentImageIndex ? R.drawable.dot_active : R.drawable.dot_inactive);
        }
    }
    private void shareApp(String productName) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.intro_title_detail) + productName);
        sendIntent.setType("text/plain");

        Intent shareIntent = Intent.createChooser(sendIntent, getString(R.string.share_with_friends_int));
        startActivity(shareIntent);
    }
}