package com.example.datn_toystoryshop.Setting;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.datn_toystoryshop.Contact_support.Email_contact;
import com.example.datn_toystoryshop.R;

public class ContactSupport_screen extends AppCompatActivity {
    TextView tvChat, tvPhone, tvEmail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_support_screen);

        tvChat = findViewById(R.id.tvChat);
        tvPhone = findViewById(R.id.tvPhone);
        tvEmail = findViewById(R.id.tvEmail);

        tvPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneNumber = "tel:0123987456";
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse(phoneNumber));
                startActivity(intent);
            }
        });

        tvEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ContactSupport_screen.this, Email_contact.class));
            }
        });
    }
}