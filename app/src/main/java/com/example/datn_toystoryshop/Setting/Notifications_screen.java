package com.example.datn_toystoryshop.Setting;

import static com.example.datn_toystoryshop.Home_screen.CHANNEL_ID;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import com.example.datn_toystoryshop.Profile.Setting_screen;
import com.example.datn_toystoryshop.R;

public class Notifications_screen extends AppCompatActivity {
    ImageView imgBackNoti;
    private Switch switchNotif;
    private SharedPreferences sharedPreferences;
    private boolean nightMode;
    private String documentId;
    private SharedPreferences.Editor editor;
    private static final String NOTIFICATION_BLOCKED_KEY = "notificationBlocked"; // Tên khóa cho trạng thái chặn thông báo
    private static final String PREFS_NAME = "MyPrefs";
    private static final String CHANNEL_ID = "notification_channel";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications_screen);

        imgBackNoti = findViewById(R.id.imgBackNoti);
        switchNotif = findViewById(R.id.switch_notif);

        // Thay dòng này trong Notifications_screen
        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        createNotificationChannel();

//        nightMode = sharedPreferences.getBoolean("night", false);
        documentId = getIntent().getStringExtra("documentId");
        Log.d("ContactSupport_screen", "Document ID received: " + documentId);
        nightMode = getIntent().getBooleanExtra("nightMode", false);

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


        switchNotif.setOnCheckedChangeListener((buttonView, isChecked) -> {
            editor = sharedPreferences.edit();
            editor.putBoolean(NOTIFICATION_BLOCKED_KEY, isChecked); // Lưu trạng thái chặn thông báo
            editor.apply();

            if (isChecked) {
                // Gọi phương thức để hiển thị thông báo khi chặn thông báo
                showNotification(getString(R.string.notification), getString(R.string.dark_mode_off_set));
            } else {
                // Gọi phương thức để hiển thị thông báo khi bỏ chặn thông báo
                showNotification(getString(R.string.notification), getString(R.string.dark_mode_on_set));
            }
        });

    }
    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Notification Channel";
            String description = "Channel for notification toggle";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
    private void showNotification(String title, String message) {
        Intent intent = new Intent(this, Notifications_screen.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.bell) // Icon thông báo
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, builder.build());
    }
}