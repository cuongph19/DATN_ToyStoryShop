package com.example.datn_toystoryshop;


import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
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
    private static final String PREFS_NAME = "MyPrefs"; // Khai báo biến ở đây
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Kiểm tra trạng thái thông báo
        SharedPreferences sharedPreferences = getSharedPreferences("Settings", MODE_PRIVATE);
        boolean notificationShown = sharedPreferences.getBoolean("notificationShown", false);

        if (!notificationShown) {
            // Hiển thị thông báo
            sendNotification();

            // Đặt trạng thái thông báo đã được hiển thị
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("notificationShown", true);
            editor.apply();
        }

// Nhận dữ liệu từ Intent
        Intent intent = getIntent();
        String documentId = intent.getStringExtra("documentId");


        // Truyền dữ liệu cho Fragment
        Profile_Fragment profileFragment = new Profile_Fragment();
        Bundle bundle = new Bundle();
        // Chỉ thêm vào Bundle nếu không null
        if (documentId != null) {
            bundle.putString("documentId", documentId);
            Log.d("Profile_Fragment", "aaaaaaa: " + documentId);
        }

        profileFragment.setArguments(bundle);

        // Hiển thị Fragment
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentLayout, profileFragment)
                .commit();

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
        loadFragment(new Home_Fragment(), null);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if(itemId == R.id.nav_home){
                    loadFragment(new Home_Fragment(),null);
                } else if (itemId == R.id.nav_browse){
                    loadFragment(new Browse_Fragment(),null);
                } else if (itemId == R.id.nav_store){
                    loadFragment(new Store_Fragment(),null);
                } else if (itemId == R.id.nav_history){
                    loadFragment(new History_Fragment(),null);
                } else{
                    loadFragment(new Profile_Fragment(), bundle);
                }


                return true;
            }
        });
    }

    private void loadFragment(Fragment fragment, Bundle args) {
        if (args != null) {
            fragment.setArguments(args);
        }
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragmentLayout, fragment);
        fragmentTransaction.commit();
    }
    private void sendNotification() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "MY_NOTIFICATION_CHANNEL")
                .setSmallIcon(R.drawable.bell) // Icon thông báo
                .setContentTitle("Chào mừng bạn đến với ứng dụng!") // Tiêu đề thông báo
                .setContentText("Bạn đã vào màn hình Home.") // Nội dung thông báo
                .setPriority(NotificationCompat.PRIORITY_DEFAULT) // Độ ưu tiên của thông báo
                .setAutoCancel(true); // Tự động hủy thông báo khi nhấn vào

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (notificationManager != null) {
            notificationManager.notify(1, builder.build()); // Hiển thị thông báo
        }
    }

}
