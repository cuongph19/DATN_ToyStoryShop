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
    import com.example.datn_toystoryshop.Shopping.Favorite_screen;
    import com.example.datn_toystoryshop.Shopping.Cart_screen;
    import com.google.android.material.bottomnavigation.BottomNavigationView;

    public class Home_screen extends AppCompatActivity {
        private BottomNavigationView bottomNavigationView;
        private FrameLayout frameLayout ;
        private TextView header_title ;
        private ImageView heart_icon ;
        private RelativeLayout cart_full_icon;
        public static final String CHANNEL_ID = "notification_channel";
        private static final String PREFS_NAME = "MyPrefs";
        private static final String NOTIFICATION_SHOWN_KEY = "notificationShown";
        private static final String NOTIFICATION_BLOCKED_KEY = "notificationBlocked";

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_home);

            createNotificationChannel();

            SharedPreferences shared = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
            boolean notificationShown = shared.getBoolean(NOTIFICATION_SHOWN_KEY, false);
            boolean isNotificationBlocked = shared.getBoolean(NOTIFICATION_BLOCKED_KEY, false); // Kiểm tra trạng thái chặn thông báo

            if (!notificationShown && !isNotificationBlocked) {
                // Gọi thông báo khi màn hình được mở
                showNotification("Chào mừng bạn đến với Toy Story Shop!", "Chúc bạn một ngày tốt lành!");

                // Cập nhật trạng thái đã hiển thị thông báo
                SharedPreferences.Editor editor = shared.edit();
                editor.putBoolean(NOTIFICATION_SHOWN_KEY, true);
                editor.apply();
            }

//            if (!isNotificationBlocked) {
//                // Gọi thông báo khi màn hình được mở
//                showNotification("Chào mừng bạn đến với Toy Story Shop!", "Chúc bạn một ngày tốt lành!");
//            }


            // Nhận dữ liệu từ Intent
            Intent intent = getIntent();
            String documentId = intent.getStringExtra("documentId");
            Log.e("OrderHistoryAdapter", "j66666666666666666Home_screen" + documentId);

            // Truyền dữ liệu cho Fragment
            Profile_Fragment profileFragment = new Profile_Fragment();
            Home_Fragment homeFragment = new Home_Fragment();
            Bundle bundle = new Bundle();
            // Chỉ thêm vào Bundle nếu không null
            if (documentId != null) {
                bundle.putString("documentId", documentId);

            }

        //    profileFragment.setArguments(bundle);
            homeFragment.setArguments(bundle);

            // Hiển thị Fragment
//            getSupportFragmentManager().beginTransaction()
//                    .replace(R.id.fragmentLayout, profileFragment)
//                    .commit();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentLayout, homeFragment)
                    .commit();

            bottomNavigationView =findViewById(R.id.bottomNaviView);
            frameLayout = findViewById(R.id.fragmentLayout);
            header_title = findViewById(R.id.header_title);
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
                    Intent intent = new Intent(Home_screen.this, Cart_screen.class);
                    intent.putExtra("documentId", documentId);
                    startActivity(intent);
                }
            });
            heart_icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Home_screen.this, Favorite_screen.class);
                    intent.putExtra("documentId", documentId);
                    startActivity(intent);
                }
            });

            // Hiển thị Home_Fragment ngay khi vào màn hình Home
            loadFragment(new Home_Fragment(), bundle);
            bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    int itemId = item.getItemId();
                    if(itemId == R.id.nav_home){
                        loadFragment(new Home_Fragment(),bundle);
                    } else if (itemId == R.id.nav_browse){
                        loadFragment(new Browse_Fragment(),bundle);
                        //loadFragment(new Browse_Fragment(),null);
                    } else if (itemId == R.id.nav_store){
                        loadFragment(new Store_Fragment(),null);
                    } else if (itemId == R.id.nav_history){
                        loadFragment(new History_Fragment(),bundle);
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
//                SharedPreferences shared = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
//                boolean isNotificationBlocked = shared.getBoolean(NOTIFICATION_BLOCKED_KEY, false);
//                boolean isSwitchChecked = !isNotificationBlocked; // Nếu thông báo không bị chặn thì switch được bật
//
//                if (isSwitchChecked) {
//                    showNotification("Chào mừng bạn đến với Toy Story Shop!", "Chúc bạn một ngày tốt lành!");
//                }
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

                // Đăng ký kênh thông báo với hệ thống
                NotificationManager notificationManager = getSystemService(NotificationManager.class);
                notificationManager.createNotificationChannel(channel);
            }
        }

        private void showNotification(String title, String message) {
            Intent intent = new Intent(this, Home_screen.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setSmallIcon(R.drawable.bell) // Biểu tượng thông báo
                    .setContentTitle(title)
                    .setContentText(message)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setContentIntent(pendingIntent) // Thiết lập intent
                    .setAutoCancel(true);

            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            notificationManager.notify(1, builder.build());
        }

    }
