package com.example.datn_toystoryshop;


import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.datn_toystoryshop.Fragment.Browse_Fragment;
import com.example.datn_toystoryshop.Fragment.History_Fragment;
import com.example.datn_toystoryshop.Fragment.Home_Fragment;
import com.example.datn_toystoryshop.Fragment.Profile_Fragment;
import com.example.datn_toystoryshop.Fragment.Store_Fragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Home_screen extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;
    private FrameLayout frameLayout ;
    private TextView header_title ;
    private ImageView cart_icon,heart_icon ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        bottomNavigationView =findViewById(R.id.bottomNaviView);
        frameLayout = findViewById(R.id.fragmentLayout);
        header_title = findViewById(R.id.header_title);
        cart_icon = findViewById(R.id.cart_icon);
        heart_icon = findViewById(R.id.heart_icon);

        header_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Load lại màn hình hiện tại
                recreate();
            }
        });
        cart_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //////////////chua tao man gio hàng//////////////////////
                Intent intent = new Intent(Home_screen.this, Favorite_products.class);
                startActivity(intent);
            }
        });
        heart_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Home_screen.this, Favorite_products.class);
                startActivity(intent);
            }
        });

        // Hiển thị Home_Fragment ngay khi vào màn hình Home
        loadFragment(new Home_Fragment(), false);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if(itemId == R.id.nav_home){
                    loadFragment(new Home_Fragment(),false);
                } else if (itemId == R.id.nav_browse){
                    loadFragment(new Browse_Fragment(),false);
                } else if (itemId == R.id.nav_store){
                    loadFragment(new Store_Fragment(),false);
                } else if (itemId == R.id.nav_history){
                    loadFragment(new History_Fragment(),false);
                } else{
                    loadFragment(new Profile_Fragment(),false);
                }


                return true;
            }
        });
    }
    private void loadFragment(Fragment fragment, boolean isAppInitialized){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragmentLayout, fragment);
        fragmentTransaction.commit();
    }

}
