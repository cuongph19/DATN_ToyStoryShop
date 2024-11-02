package com.example.datn_toystoryshop.Setting;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.example.datn_toystoryshop.Profile.Setting_screen;
import com.example.datn_toystoryshop.R;

public class Notifications_screen extends AppCompatActivity {
    ImageView imgBackNoti;
    private Switch switchNotif;
    private SharedPreferences sharedPreferences;
    private boolean nightMode;;
    private String documentId;
    private SharedPreferences.Editor editor;
    private static final String NOTIFICATION_BLOCKED_KEY = "notificationBlocked"; // Tên khóa cho trạng thái chặn thông báo
    private static final String PREFS_NAME = "MyPrefs";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications_screen);

        imgBackNoti = findViewById(R.id.imgBackNoti);
        switchNotif = findViewById(R.id.switch_notif);

        // Thay dòng này trong Notifications_screen
        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        nightMode = sharedPreferences.getBoolean("night", false);
        documentId = getIntent().getStringExtra("documentId");
        Log.d("ContactSupport_screen", "Document ID received: " + documentId);
        if (nightMode) {
            imgBackNoti.setImageResource(R.drawable.back_icon);
        } else {
            imgBackNoti.setImageResource(R.drawable.back_icon_1);
        }

        // Load trạng thái chặn thông báo và đặt trạng thái cho switch
        boolean isNotificationBlocked = sharedPreferences.getBoolean(NOTIFICATION_BLOCKED_KEY, false);
        switchNotif.setChecked(isNotificationBlocked);

        imgBackNoti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Notifications_screen.this, Setting_screen.class));
            }
        });


        // Set listener cho Switch
        switchNotif.setOnCheckedChangeListener((buttonView, isChecked) -> {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean(NOTIFICATION_BLOCKED_KEY, isChecked); // Lưu trạng thái chặn thông báo
            editor.apply(); // Lưu trạng thái vào SharedPreferences

            if (isChecked) {
                // Chặn thông báo
                Toast.makeText(this, "Thông báo đã bị chặn", Toast.LENGTH_SHORT).show();
            } else {
                // Bỏ chặn thông báo
                Toast.makeText(this, "Thông báo được phép hiển thị", Toast.LENGTH_SHORT).show();
            }
        });

    }
}