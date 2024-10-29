package com.example.datn_toystoryshop.Home;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.datn_toystoryshop.R;

public class OtherProducts_screen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otherproducts);
        ImageView ivBack = findViewById(R.id.ivBack); // Lấy đối tượng ImageView

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed(); // Gọi phương thức quay lại activity trước đó
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed(); // Quay lại activity trước đó

    }
}