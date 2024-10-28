package com.example.datn_toystoryshop.Profile;

import static android.app.PendingIntent.getActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.datn_toystoryshop.NewArrivals_screen;
import com.example.datn_toystoryshop.R;
import com.example.datn_toystoryshop.Setting.ChangePassword_screen;
import com.example.datn_toystoryshop.Setting.ContactSupport_screen;
import com.example.datn_toystoryshop.Setting.Notifications_screen;
import com.example.datn_toystoryshop.Setting.UpdateInfo_screen;

public class Setting_screen extends AppCompatActivity {

    private TextView tvUpdateInfo, tvChangePassword, tvNotifications, tvContactSupport;
    private ImageView btnBack;
    private Switch switchDarkMode;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_screen);

        // Initialize Views
        tvUpdateInfo = findViewById(R.id.tv_update_info);
        tvChangePassword = findViewById(R.id.tv_change_password);
        tvNotifications = findViewById(R.id.tv_notifications);
        tvContactSupport = findViewById(R.id.tv_contact_support);
        switchDarkMode = findViewById(R.id.switch_dark_mode);
        btnBack = findViewById(R.id.btnBack);
        // SharedPreferences setup for dark mode toggle
        sharedPreferences = getSharedPreferences("Settings", MODE_PRIVATE);
        editor = sharedPreferences.edit();

        // Load dark mode setting
        boolean isDarkMode = sharedPreferences.getBoolean("DarkMode", false);
        switchDarkMode.setChecked(isDarkMode);

        // Toggle Dark Mode
        switchDarkMode.setOnCheckedChangeListener((buttonView, isChecked) -> {
            editor.putBoolean("DarkMode", isChecked);
            editor.apply();
            // Add functionality to update UI theme here if needed
            Toast.makeText(this,getString(R.string.dark_mode_enabled_set) + (isChecked ? getString(R.string.dark_mode_on_set) : getString(R.string.dark_mode_off_set)), Toast.LENGTH_SHORT).show();
        });
        // Nút quay lại đăng nhập
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        tvUpdateInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Chuyển sang NewActivity
                Intent intent = new Intent(Setting_screen.this, UpdateInfo_screen.class);
                startActivity(intent);
            }
        });
        tvChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Chuyển sang NewActivity
                Intent intent = new Intent(Setting_screen.this, ChangePassword_screen.class);
                startActivity(intent);
            }
        });
        tvNotifications.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Chuyển sang NewActivity
                Intent intent = new Intent(Setting_screen.this, Notifications_screen.class);
                startActivity(intent);
            }
        });
        tvContactSupport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Chuyển sang NewActivity
                Intent intent = new Intent(Setting_screen.this, ContactSupport_screen.class);
                startActivity(intent);
            }
        });
    }
    }
