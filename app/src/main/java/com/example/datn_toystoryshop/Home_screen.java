package com.example.datn_toystoryshop;


import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        bottomNavigationView =findViewById(R.id.bottomNaviView);
        frameLayout = findViewById(R.id.fragmentLayout);
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
        if (isAppInitialized){
            fragmentTransaction.add(R.id.fragmentLayout, fragment);
        } else {
            fragmentTransaction.replace(R.id.fragmentLayout, fragment);
        }
        fragmentTransaction.commit();
    }

}
