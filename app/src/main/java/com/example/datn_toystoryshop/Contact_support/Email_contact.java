package com.example.datn_toystoryshop.Contact_support;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.datn_toystoryshop.R;
import com.example.datn_toystoryshop.Setting.ContactSupport_screen;

public class Email_contact extends AppCompatActivity {
    ImageView imgBackEmail;
    private SharedPreferences sharedPreferences;
    private boolean nightMode;;
    private String documentId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_email_contact);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        imgBackEmail = findViewById(R.id.imgBackEm);

        sharedPreferences = getSharedPreferences("Settings", MODE_PRIVATE);
        nightMode = sharedPreferences.getBoolean("night", false);
        documentId = getIntent().getStringExtra("documentId");
        Log.d("ContactSupport_screen", "Document ID received: " + documentId);
        if (nightMode) {
            imgBackEmail.setImageResource(R.drawable.back_icon);
        } else {
            imgBackEmail.setImageResource(R.drawable.back_icon_1);
        }

        imgBackEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Email_contact.this, ContactSupport_screen.class));
            }
        });
    }
}