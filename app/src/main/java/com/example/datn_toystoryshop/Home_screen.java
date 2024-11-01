package com.example.datn_toystoryshop;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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
    private RelativeLayout cart_full_icon;
    private static final String CHANNEL_ID = "my_channel_id";
    private static final String PREFS_NAME = "MyPrefs";
    private static final String NOTIFICATION_SHOWN_KEY = "notificationShown";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        createNotificationChannel();

        // Kiểm tra xem thông báo đã được hiển thị chưa
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        boolean notificationShown = prefs.getBoolean(NOTIFICATION_SHOWN_KEY, false);

        if (!notificationShown) {
            // Hiển thị thông báo
            showNotification("Chào mừng bạn!", "Cảm ơn bạn đã mở ứng dụng của chúng tôi.");

            // Cập nhật trạng thái đã hiển thị thông báo
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean(NOTIFICATION_SHOWN_KEY, true);
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
        cart_full_icon = findViewById(R.id.cart_full_icon);
        heart_icon = findViewById(R.id.heart_icon);

        header_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Load lại màn hình hiện tại
                recreate();
            }
        });
        cart_full_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //////////////chua tao man gio hàng//////////////////////
                Intent intent = new Intent(Home_screen.this, Shopping_cart_screen.class);
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
                    //loadFragment(new Browse_Fragment(),null);
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

        // Cập nhật tiêu đề dựa trên Fragment
        if (fragment instanceof Home_Fragment) {
            header_title.setText(getString(R.string.app_name));
        } else if (fragment instanceof Browse_Fragment) {
            header_title.setText(getString(R.string.browse_menu));
        } else if (fragment instanceof Store_Fragment) {
            header_title.setText(getString(R.string.store_menu));
        } else if (fragment instanceof History_Fragment) {
            header_title.setText(getString(R.string.history_menu));
        } else if (fragment instanceof Profile_Fragment) {
            header_title.setText(getString(R.string.profile_menu));
        }
    }


    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "My Notification Channel";
            String description = "Channel for app notifications";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);

            // Register the channel with the system
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void showNotification(String title, String message) {
        Intent intent = new Intent(this, Home_screen.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.bell) // Biểu tượng thông báo của bạn
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent) // Thiết lập intent
                .setAutoCancel(true);

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(1, builder.build());
    }

}
