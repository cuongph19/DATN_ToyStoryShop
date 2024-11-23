package com.example.datn_toystoryshop.Profile;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.example.datn_toystoryshop.R;
import com.example.datn_toystoryshop.Setting.ChangePassword_screen;
import com.example.datn_toystoryshop.Setting.Currency_Language_screen;
import com.example.datn_toystoryshop.Setting.UpdateInfo_screen;

public class Setting_screen extends AppCompatActivity {

    private TextView tvUpdateInfo, tvChangePassword, tvNotifications,tvLanguageCurrency;
    private ImageView btnBack;
    private Switch switchDarkMode;
    private SharedPreferences sharedPreferences;
    private String documentId;
    private SharedPreferences.Editor editor;
    boolean nightMode;

    private Switch switchNotif;
    private static final String PREFS_NAME = "NotificationPrefs";
    private static final String NOTIFICATION_BLOCKED_KEY = "isNotificationBlocked";
    private NotificationManager notificationManager;
    private static final String CHANNEL_ID = "notification_channel";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_screen);

        // Khởi tạo NotificationManager
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Kiểm tra và yêu cầu quyền thông báo nếu đang trên Android 13 hoặc cao hơn
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (checkSelfPermission(android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{android.Manifest.permission.POST_NOTIFICATIONS}, 1);
            }
        } else {
            // Nếu phiên bản thấp hơn Android 13, không cần yêu cầu quyền
            createNotificationChannel();
        }


        // Initialize Views
        tvUpdateInfo = findViewById(R.id.tv_update_info);
        tvChangePassword = findViewById(R.id.tv_change_password);
        tvNotifications = findViewById(R.id.tv_notifications);
        switchDarkMode = findViewById(R.id.switch_dark_mode);
        tvLanguageCurrency = findViewById(R.id.tv_languagecurrency);
        btnBack = findViewById(R.id.btnBack);
        switchNotif = findViewById(R.id.switch_notif);

        Intent intent = getIntent();
        documentId = intent.getStringExtra("documentId");


        // SharedPreferences setup for dark mode toggle
        sharedPreferences = getSharedPreferences("Settings", MODE_PRIVATE);
        nightMode = sharedPreferences.getBoolean("night", false);
        editor = sharedPreferences.edit();

        if (nightMode) {
            switchDarkMode.setChecked(true);
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            btnBack.setImageResource(R.drawable.back_icon);
        } else {
            btnBack.setImageResource(R.drawable.back_icon_1);
        }

        // Xử lý sự kiện cho mục "Ngôn ngữ & tiền tệ"
        tvLanguageCurrency.setOnClickListener(v -> {
            // Chuyển tới màn hình ngôn ngữ và tiền tệ
            Intent intent1 = new Intent(Setting_screen.this, Currency_Language_screen.class);
            startActivity(intent1);
        });

        switchDarkMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (switchDarkMode.isChecked()) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    editor.putBoolean("night", true);
                    nightMode = true;
                    Toast.makeText(Setting_screen.this, getString(R.string.dark_mode_enabled_set) + " " + getString(R.string.dark_mode_on_set), Toast.LENGTH_SHORT).show();
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    editor.putBoolean("night", false);
                    nightMode = false;
                    Toast.makeText(Setting_screen.this, getString(R.string.dark_mode_enabled_set) + " " + getString(R.string.dark_mode_off_set), Toast.LENGTH_SHORT).show();
                }
                editor.apply();
            }
        });


        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        tvUpdateInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Chuyển sang UpdateInfo_screen
                Intent intent = new Intent(Setting_screen.this, UpdateInfo_screen.class);
                intent.putExtra("documentId", documentId);
                startActivity(intent);
            }
        });

        tvChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Chuyển sang NewActivity
                Intent intent = new Intent(Setting_screen.this, ChangePassword_screen.class);
                intent.putExtra("documentId", documentId);
                startActivity(intent);
            }
        });


        // Load trạng thái chặn thông báo từ SharedPreferences và đặt trạng thái cho Switch
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        boolean isNotificationBlocked = sharedPreferences.getBoolean(NOTIFICATION_BLOCKED_KEY, false);
        switchNotif.setChecked(isNotificationBlocked);

        if (isNotificationBlocked) {
            tvNotifications.setText("Tắt thông báo"); // Khi Switch bật
        } else {
            tvNotifications.setText("Bật thông báo"); // Khi Switch tắt
        }

        // Xử lý sự kiện khi người dùng bật/tắt Switch
        switchNotif.setOnCheckedChangeListener((buttonView, isChecked) -> {
            // Lưu trạng thái của Switch vào SharedPreferences
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean(NOTIFICATION_BLOCKED_KEY, isChecked);
            editor.apply();

            if (isChecked) {
                // Nếu Switch bật, hiển thị thông báo
                showNotification();
                tvNotifications.setText("Tắt thông báo");
                Toast.makeText(Setting_screen.this, "Thông báo đã bật", Toast.LENGTH_SHORT).show();

            } else {
                // Nếu Switch tắt, hủy thông báo
                cancelNotification();
                tvNotifications.setText("Bật thông báo");
                Toast.makeText(Setting_screen.this, "Thông báo đã tắt", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Tạo Notification Channel (dành cho Android 8.0 trở lên)
    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Notification Channel";
            String description = "Channel for notification toggle";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);

            // Đăng ký channel với hệ thống
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    // Hiển thị thông báo
    private void showNotification() {
        Notification.Builder builder = new Notification.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_logo)  // Icon mặc định của Android
                .setContentTitle("Chào mừng bạn!")
                .setContentText("Bạn đã vào màn hình Notifications_screen của ứng dụng.")
                .setPriority(Notification.PRIORITY_DEFAULT);

        // Hiển thị thông báo
        notificationManager.notify(1, builder.build());
    }

    // Hủy thông báo
    private void cancelNotification() {
        notificationManager.cancel(1); // Hủy thông báo với ID = 1
    }

    // Xử lý kết quả yêu cầu quyền thông báo
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Nếu quyền đã được cấp, tạo thông báo
                createNotificationChannel();
            } else {
                // Nếu quyền bị từ chối, thông báo cho người dùng
                Toast.makeText(this, "Bạn cần cấp quyền thông báo để sử dụng tính năng này.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Kiểm tra trạng thái của Switch và hiển thị thông báo khi quay lại màn hình
    @Override
    protected void onResume() {
        super.onResume();

        // Lấy trạng thái của Switch từ SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        boolean isNotificationBlocked = sharedPreferences.getBoolean(NOTIFICATION_BLOCKED_KEY, false);
        if (isNotificationBlocked) {
            showNotification(); // Nếu Switch đã bật trước đó, hiển thị thông báo
            tvNotifications.setText("Tắt thông báo");
        } else {
            tvNotifications.setText("Bật thông báo");
        }
    }

}