package com.example.datn_toystoryshop.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.datn_toystoryshop.Adapter.Product_Adapter;
import com.example.datn_toystoryshop.Home.ArtStory_screen;
import com.example.datn_toystoryshop.Home.BlindBox_screen;
import com.example.datn_toystoryshop.Home.Figuring_screen;
import com.example.datn_toystoryshop.Home.LimitedFigure_screen;
import com.example.datn_toystoryshop.Model.Product_Model;
import com.example.datn_toystoryshop.Home.NewArrivals_screen;
import com.example.datn_toystoryshop.Home.OtherProducts_screen;
import com.example.datn_toystoryshop.R;
import com.example.datn_toystoryshop.Adapter.Image_Adapter;
import com.example.datn_toystoryshop.Server.APIService;

import java.util.Arrays;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import androidx.recyclerview.widget.LinearLayoutManager;

public class Home_Fragment extends Fragment {
    private Button btn_follow_store_1, btn_follow_store_2, btn_follow_store_3, btn_follow_store_4;
    private ViewPager2 viewPager;
    private Handler handler = new Handler();
    private Runnable runnable;
    private FrameLayout new_arrivals,blind_box,figuring,other_products,art_story,limited_figure;
    private int currentPage = 0;
    private RecyclerView recyclerViewMain;
    private List<Product_Model> listProductModel;
    private Product_Adapter productAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("Settings", Context.MODE_PRIVATE);
        boolean nightMode = sharedPreferences.getBoolean("night", false);
        if (nightMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
        btn_follow_store_1 = view.findViewById(R.id.btn_follow_store_1);
        btn_follow_store_2 = view.findViewById(R.id.btn_follow_store_2);
        btn_follow_store_3 = view.findViewById(R.id.btn_follow_store_3);
        btn_follow_store_4 = view.findViewById(R.id.btn_follow_store_4);
        viewPager = view.findViewById(R.id.view_pager);
        new_arrivals = view.findViewById(R.id.new_arrivals);
        blind_box = view.findViewById(R.id.blind_box);
        figuring = view.findViewById(R.id.figuring);
        other_products = view.findViewById(R.id.other_products);
        art_story = view.findViewById(R.id.art_story);
        limited_figure = view.findViewById(R.id.limited_figure);
        recyclerViewMain = view.findViewById(R.id.recyclerViewNewProducts);

        // Thiết lập LayoutManager theo chiều ngang
        recyclerViewMain.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));

        // Cấu hình Retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(APIService.DOMAIN)  // Đảm bảo APIService.DOMAIN là URL chính xác của bạn
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // Tạo instance của APIService
        APIService apiService = retrofit.create(APIService.class);

        // Gọi API để lấy danh sách ProductModel
        Call<List<Product_Model>> call = apiService.getProducts();
        call.enqueue(new Callback<List<Product_Model>>() {
            @Override
            public void onResponse(Call<List<Product_Model>> call, Response<List<Product_Model>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // Nhận dữ liệu từ API
                    listProductModel = response.body();

                    // Khởi tạo Adapter với dữ liệu nhận được
                    productAdapter = new Product_Adapter(requireContext(), listProductModel);

                    // Gắn Adapter vào RecyclerView
                    recyclerViewMain.setAdapter(productAdapter);
                } else {
                    Log.e("ProductFragment", "Response unsuccessful or body is null");
                }}
            @Override
            public void onFailure(Call<List<Product_Model>> call, Throwable t) {
                // Xử lý khi thất bại
                Log.e("ProductFragment", "API call failed: " + t.getMessage());
            }
        });

        new_arrivals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Chuyển sang NewActivity
                Intent intent = new Intent(getActivity(), NewArrivals_screen.class);
                startActivity(intent);
            }
        });
        blind_box.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Chuyển sang NewActivity
                Intent intent = new Intent(getActivity(), BlindBox_screen.class);
                startActivity(intent);
            }
        });
        figuring.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Chuyển sang NewActivity
                Intent intent = new Intent(getActivity(), Figuring_screen.class);
                startActivity(intent);
            }
        });
        other_products.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Chuyển sang NewActivity
                Intent intent = new Intent(getActivity(), OtherProducts_screen.class);
                startActivity(intent);
            }
        });
        art_story.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Chuyển sang NewActivity
                Intent intent = new Intent(getActivity(), ArtStory_screen.class);
                startActivity(intent);
            }
        });
        limited_figure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Chuyển sang NewActivity
                Intent intent = new Intent(getActivity(), LimitedFigure_screen.class);
                startActivity(intent);
            }
        });
        // Setup ViewPager2 for image slider
        List<Integer> images = Arrays.asList(
                R.drawable.ic_logo,
                R.drawable.ic_google,
                R.drawable.ic_search
        );
        List<String> texts = Arrays.asList(
                "aaaa",  // Text cho ảnh R.drawable.ic_logo
                "bbbb",  // Text cho ảnh R.drawable.ic_google
                "cccc"   // Text cho ảnh R.drawable.ic_search
        );
        Image_Adapter adapter = new Image_Adapter(images, texts);
        viewPager.setAdapter(adapter);

        // Setup auto slide for images in ViewPager2
        runnable = new Runnable() {
            @Override
            public void run() {
                if (currentPage == images.size()) {
                    currentPage = 0;
                }
                viewPager.setCurrentItem(currentPage++, true);
                handler.postDelayed(this, 3000);
            }
        };
        handler.postDelayed(runnable, 3000);

        btn_follow_store_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("https://youtube.com/@LapTrinhJava"));
                startActivity(intent);
            }
        });
        btn_follow_store_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("https://youtube.com/@LapTrinhJava"));
                startActivity(intent);
            }
        });
        btn_follow_store_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("https://youtube.com/@LapTrinhJava"));
                startActivity(intent);
            }
        });
        btn_follow_store_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("https://youtube.com/@LapTrinhJava"));
                startActivity(intent);
            }
        });
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(runnable);
    }
}
