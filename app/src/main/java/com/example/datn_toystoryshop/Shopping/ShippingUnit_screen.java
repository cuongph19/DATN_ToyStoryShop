package com.example.datn_toystoryshop.Shopping;

import android.os.Bundle;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.datn_toystoryshop.R;

public class ShippingUnit_screen extends AppCompatActivity {
        private ImageView imgBack;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shipping_unit_screen);

        imgBack = findViewById(R.id.imgBack);

        imgBack.setOnClickListener(v -> onBackPressed());
    }
}