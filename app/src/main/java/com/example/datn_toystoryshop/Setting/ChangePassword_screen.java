package com.example.datn_toystoryshop.Setting;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.datn_toystoryshop.R;

public class ChangePassword_screen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password_screen);
        // Lấy các dữ liệu từ Intent
        Intent intent = getIntent();
        String gmail = intent.getStringExtra("gmail");
        String documentId = intent.getStringExtra("documentId");


        // Ghi log để kiểm tra dữ liệu nhận được
        Log.d("ChangePassword_screen", "ChangePassword_screenaaaaaaaaaaaaaaaaa: " + documentId);
        Log.d("ChangePassword_screen", "ChangePassword_screenaaaaaaaaaaaaaaaaa: " + gmail);


    }
}