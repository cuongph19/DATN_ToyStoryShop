package com.example.datn_toystoryshop;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Welcome_screen extends AppCompatActivity {
    private static final int SPLASH_SCREEN_TIME_OUT = 3000; // 3 giây

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        // Tạo hiệu ứng load cho thanh trắng
        View loadingBar = findViewById(R.id.loadingBar);
        ObjectAnimator animator = ObjectAnimator.ofFloat(loadingBar, "scaleX", 0f, 1f);
        animator.setDuration(SPLASH_SCREEN_TIME_OUT); // 3 giây
        animator.start();

        // Điều hướng đến màn hình chính sau khi load xong
        new Handler().postDelayed(() -> {
            Intent intent = new Intent(Welcome_screen.this, SignIn_screen.class);
            startActivity(intent);
            finish();
        }, SPLASH_SCREEN_TIME_OUT);
    }
}
