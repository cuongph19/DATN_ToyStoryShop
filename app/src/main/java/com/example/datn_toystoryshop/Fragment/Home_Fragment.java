package com.example.datn_toystoryshop.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.example.datn_toystoryshop.ArtStory_screen;
import com.example.datn_toystoryshop.BlindBox_screen;
import com.example.datn_toystoryshop.Figuring_screen;
import com.example.datn_toystoryshop.LimitedFigure_screen;
import com.example.datn_toystoryshop.NewArrivals_screen;
import com.example.datn_toystoryshop.OtherProducts_screen;
import com.example.datn_toystoryshop.R;
import com.example.datn_toystoryshop.adapter.ImageAdapter;
import java.util.Arrays;
import java.util.List;

public class Home_Fragment extends Fragment {
    private ViewPager2 viewPager;
    private Handler handler = new Handler();
    private Runnable runnable;
    private FrameLayout new_arrivals,blind_box,figuring,other_products,art_story,limited_figure;
    private int currentPage = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        viewPager = view.findViewById(R.id.view_pager);
        new_arrivals = view.findViewById(R.id.new_arrivals);
        blind_box = view.findViewById(R.id.blind_box);
        figuring = view.findViewById(R.id.figuring);
        other_products = view.findViewById(R.id.other_products);
        art_story = view.findViewById(R.id.art_story);
        limited_figure = view.findViewById(R.id.limited_figure);

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
        ImageAdapter adapter = new ImageAdapter(images, texts);
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

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(runnable);
    }
}
