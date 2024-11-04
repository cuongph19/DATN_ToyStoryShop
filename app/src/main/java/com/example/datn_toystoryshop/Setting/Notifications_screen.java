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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications_screen);

        imgBackNoti = findViewById(R.id.imgBackNoti);
        switchNotif = findViewById(R.id.switch_notif);

        sharedPreferences = getSharedPreferences("Settings", MODE_PRIVATE);
        nightMode = sharedPreferences.getBoolean("night", false);
        documentId = getIntent().getStringExtra("documentId");
        Log.d("ContactSupport_screen", "Document ID received: " + documentId);
        if (nightMode) {
            imgBackNoti.setImageResource(R.drawable.back_icon);
        } else {
            imgBackNoti.setImageResource(R.drawable.back_icon_1);
        }

        imgBackNoti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Notifications_screen.this, Setting_screen.class));
            }
        });
        boolean notificationsEnabled = sharedPreferences.getBoolean("notificationsEnabled", false);
        switchNotif.setChecked(notificationsEnabled);


    }
}