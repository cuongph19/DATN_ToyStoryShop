package com.example.datn_toystoryshop.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;
import com.example.datn_toystoryshop.Adapter.Product_Adapter;
import com.example.datn_toystoryshop.Adapter.Suggestion_Adapter;
import com.example.datn_toystoryshop.Add_address_screen;
import com.example.datn_toystoryshop.Home.All_new_screen;
import com.example.datn_toystoryshop.Home.ArtStory_screen;
import com.example.datn_toystoryshop.Home.BlindBox_screen;
import com.example.datn_toystoryshop.Home.Figuring_screen;
import com.example.datn_toystoryshop.Home.LimitedFigure_screen;
import com.example.datn_toystoryshop.Home.NewArrivals_screen;
import com.example.datn_toystoryshop.Home.OtherProducts_screen;
import com.example.datn_toystoryshop.Home.Popular_screen;
import com.example.datn_toystoryshop.Home.Store_follow_screen;
import com.example.datn_toystoryshop.Home_screen;
import com.example.datn_toystoryshop.Model.Product_Model;
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

public class Home_Fragment extends Fragment {
    private Button btn_follow_store_1, btn_follow_store_2, btn_follow_store_3, btn_follow_store_4, btn_see_all_new, btn_see_all_popular, btn_view_all_stores;
    private EditText search_bar;
    private TextView recyclertextviewsuggestions;
    private ViewPager2 viewPager;
    private Handler handler = new Handler();
    private Runnable runnable;
    private FrameLayout new_arrivals, blind_box, figuring, other_products, art_story, limited_figure;
    private int currentPage = 0;
    private RecyclerView recyclerViewNew;
    private RecyclerView recyclerViewSuggestions;
    private List<Product_Model> listProductModel;
    private Product_Adapter productAdapter;
    private Suggestion_Adapter suggestionAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("Settings", Context.MODE_PRIVATE);
        boolean nightMode = sharedPreferences.getBoolean("night", false);
        AppCompatDelegate.setDefaultNightMode(nightMode ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO);

        btn_follow_store_1 = view.findViewById(R.id.btn_follow_store_1);
        btn_follow_store_2 = view.findViewById(R.id.btn_follow_store_2);
        btn_follow_store_3 = view.findViewById(R.id.btn_follow_store_3);
        btn_follow_store_4 = view.findViewById(R.id.btn_follow_store_4);
        btn_see_all_new = view.findViewById(R.id.btn_see_all_new);
        btn_see_all_popular = view.findViewById(R.id.btn_see_all_popular);
        btn_view_all_stores = view.findViewById(R.id.btn_view_all_stores);
        search_bar = view.findViewById(R.id.search_bar);
        recyclertextviewsuggestions = view.findViewById(R.id.recycler_textview_suggestions);
        recyclerViewSuggestions = view.findViewById(R.id.recycler_view_suggestions);
        viewPager = view.findViewById(R.id.view_pager);
        new_arrivals = view.findViewById(R.id.new_arrivals);
        blind_box = view.findViewById(R.id.blind_box);
        figuring = view.findViewById(R.id.figuring);
        other_products = view.findViewById(R.id.other_products);
        art_story = view.findViewById(R.id.art_story);
        limited_figure = view.findViewById(R.id.limited_figure);
        recyclerViewNew = view.findViewById(R.id.recyclerViewNewProducts);

        recyclerViewNew.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerViewSuggestions.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerViewSuggestions.setVisibility(View.GONE);
        recyclertextviewsuggestions.setVisibility(View.GONE);
        // Lắng nghe sự kiện chạm cho toàn bộ view
        view.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                // Kiểm tra nếu view chạm không phải là search_bar hoặc recyclerViewSuggestions
                if (!isPointInsideView(event.getX(), event.getY(), search_bar) &&
                        !isPointInsideView(event.getX(), event.getY(), recyclerViewSuggestions)) {
                    recyclerViewSuggestions.setVisibility(View.GONE);
                    recyclertextviewsuggestions.setVisibility(View.GONE);
                }
            }
            return false;
        });

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(APIService.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        APIService apiService = retrofit.create(APIService.class);

        loadProducts(apiService);
        setupSearchBar(apiService);

        List<Integer> images = Arrays.asList(
                R.drawable.viewpager1,
                R.drawable.viewpager2,
                R.drawable.viewpager3,
                R.drawable.viewpager4,
                R.drawable.viewpager5
        );

        Image_Adapter adapter = new Image_Adapter(images, position -> {
            Intent intent;
            switch (position) {
                case 0:
                    intent = new Intent(getActivity(), Add_address_screen.class);
                    break;
                case 1:
                    intent = new Intent(getActivity(), Figuring_screen.class);
                    break;
                case 2:
                    intent = new Intent(getActivity(), OtherProducts_screen.class);
                    break;
                case 3:
                    intent = new Intent(getActivity(), ArtStory_screen.class);
                    break;
                case 4:
                    intent = new Intent(getActivity(), LimitedFigure_screen.class);
                    break;
                case 5:
                    intent = new Intent(getActivity(), OtherProducts_screen.class);
                    break;
                // Thêm các case cho các hình ảnh khác
                default:
                    intent = new Intent(getActivity(), Home_screen.class); // Nếu có hình mặc định
                    break;
            }
            startActivity(intent);
        });
        viewPager.setAdapter(adapter);

        runnable = new Runnable() {
            @Override
            public void run() {
                if (currentPage == images.size()) currentPage = 0;
                viewPager.setCurrentItem(currentPage++, true);
                handler.postDelayed(this, 3000);
            }
        };
        handler.postDelayed(runnable, 3000);
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
        //////////////////////
        btn_see_all_new.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Chuyển sang NewActivity
                Intent intent = new Intent(getActivity(), All_new_screen.class);
                startActivity(intent);
            }
        });
        btn_see_all_popular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Chuyển sang NewActivity
                Intent intent = new Intent(getActivity(), Popular_screen.class);
                startActivity(intent);
            }
        });
        btn_view_all_stores.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Chuyển sang NewActivity
                Intent intent = new Intent(getActivity(), Store_follow_screen.class);
                startActivity(intent);
            }
        });
        ///////////////

        btn_follow_store_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("https://youtube.com/@ToyStationVietnam"));
                startActivity(intent);
            }
        });
        btn_follow_store_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("https://www.youtube.com/@dochoirobot9952"));
                startActivity(intent);
            }
        });
        btn_follow_store_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("https://www.youtube.com/@mykingdom"));
                startActivity(intent);
            }
        });
        btn_follow_store_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("https://www.youtube.com/@HOBIVERSEVIETNAM"));
                startActivity(intent);
            }
        });
        return view;
    }



    private void loadProducts(APIService apiService) {
        Call<List<Product_Model>> call = apiService.getProducts();
        call.enqueue(new Callback<List<Product_Model>>() {
            @Override
            public void onResponse(Call<List<Product_Model>> call, Response<List<Product_Model>> response) {
                if (response.isSuccessful() && response.body() != null && isAdded()) {
                    listProductModel = response.body();
                    productAdapter = new Product_Adapter(requireContext(), listProductModel);
                    recyclerViewNew.setAdapter(productAdapter);
                } else {
                    Log.e("ProductFragment", "Response unsuccessful or body is null");
                }
            }

            @Override
            public void onFailure(Call<List<Product_Model>> call, Throwable t) {
                Log.e("ProductFragment", "API call failed: " + t.getMessage());
            }
        });
    }
    private boolean isPointInsideView(float x, float y, View view) {
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        int viewX = location[0];
        int viewY = location[1];

        return (x >= viewX && x <= viewX + view.getWidth() &&
                y >= viewY && y <= viewY + view.getHeight());
    }
    private void setupSearchBar(APIService apiService) {
        Call<List<Product_Model>> call = apiService.getProducts();
        call.enqueue(new Callback<List<Product_Model>>() {
            @Override
            public void onResponse(Call<List<Product_Model>> call, Response<List<Product_Model>> response) {
                if (response.isSuccessful() && response.body() != null && isAdded()) {
                    listProductModel = response.body();
                    suggestionAdapter = new Suggestion_Adapter(requireContext(), listProductModel);
                    recyclerViewSuggestions.setAdapter(suggestionAdapter);

                    search_bar.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                            String query = s.toString();
                            if (query.isEmpty()) {
                                recyclerViewSuggestions.setVisibility(View.GONE);
                                recyclertextviewsuggestions.setVisibility(View.GONE);
                            } else {
                                recyclerViewSuggestions.setVisibility(View.VISIBLE);
                                recyclertextviewsuggestions.setVisibility(View.VISIBLE);
                                suggestionAdapter.filterList(query);
                            }
                        }

                        @Override
                        public void afterTextChanged(Editable s) {}
                    });
                } else {
                    Log.e("HomeFragment", "Response unsuccessful or body is null");
                }
            }

            @Override
            public void onFailure(Call<List<Product_Model>> call, Throwable t) {
                Log.e("HomeFragment", "API call failed: " + t.getMessage());
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(runnable);
    }
}
