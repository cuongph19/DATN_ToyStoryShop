package com.example.datn_toystoryshop;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.datn_toystoryshop.Profile.Setting_screen;
import com.example.datn_toystoryshop.Setting.UpdateInfo_screen;

public class My_cart_screen extends AppCompatActivity {

    private TextView btnIncrease, btnDecrease;
    private TextView tvQuantity, add_address;
    private ImageView imgBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_cart_screen);

        // Tìm các view theo ID
        btnIncrease = findViewById(R.id.btnIncrease);
        btnDecrease = findViewById(R.id.btnDecrease);
        tvQuantity = findViewById(R.id.tvQuantity);
        add_address = findViewById(R.id.add_address);
        imgBack = findViewById(R.id.imgBack_cart);

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(My_cart_screen.this, Home_screen.class);
                startActivity(intent);
                finish();
            }});

        add_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Chuyển sang UpdateInfo_screen
                Intent intent = new Intent(My_cart_screen.this, Add_address_screen.class);

                startActivity(intent);
            }
        });
        // Thiết lập hành động khi bấm nút tăng
        btnIncrease.setOnClickListener(v -> {
            int quantity = Integer.parseInt(tvQuantity.getText().toString());
            tvQuantity.setText(String.valueOf(quantity + 1));
        });

        // Thiết lập hành động khi bấm nút giảm
        btnDecrease.setOnClickListener(v -> {
            int quantity = Integer.parseInt(tvQuantity.getText().toString());
            if (quantity > 1) {
                tvQuantity.setText(String.valueOf(quantity - 1));
            }
        });
    }
}
