package com.example.datn_toystoryshop.Fragment;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;
import com.example.datn_toystoryshop.R;
import com.example.datn_toystoryshop.adapter.ImageAdapter;
import java.util.Arrays;
import java.util.List;

public class Home_Fragment extends Fragment {
    private ViewPager2 viewPager;
    private Handler handler = new Handler();
    private Runnable runnable;
    private int currentPage = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        viewPager = view.findViewById(R.id.view_pager);

        // Setup ViewPager2 for image slider
        List<Integer> images = Arrays.asList(
                R.drawable.ic_logo,
                R.drawable.ic_google,
                R.drawable.ic_search
        );

        ImageAdapter adapter = new ImageAdapter(images);
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
