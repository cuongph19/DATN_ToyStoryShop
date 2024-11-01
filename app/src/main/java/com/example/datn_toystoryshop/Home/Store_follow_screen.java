package com.example.datn_toystoryshop.Home;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.datn_toystoryshop.R;

public class Store_follow_screen extends AppCompatActivity {
    private Button btn_follow_store_1, btn_follow_store_2, btn_follow_store_3, btn_follow_store_4;
    private ImageView ivBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_follow_screen);
        ivBack = findViewById(R.id.ivBack);
        btn_follow_store_1 = findViewById(R.id.btn_follow_store_1);
        btn_follow_store_2 = findViewById(R.id.btn_follow_store_2);
        btn_follow_store_3 = findViewById(R.id.btn_follow_store_3);
        btn_follow_store_4 = findViewById(R.id.btn_follow_store_4);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed(); // Gọi phương thức quay lại activity trước đó
            }
        });

        btn_follow_store_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("https://youtube.com/@ToyStationVietnam"));
                startActivity(intent);
            }
        });
        btn_follow_store_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("https://www.youtube.com/@dochoirobot9952"));
                startActivity(intent);
            }
        });
        btn_follow_store_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("https://www.youtube.com/@mykingdom"));
                startActivity(intent);
            }
        });
        btn_follow_store_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("https://www.youtube.com/@otocancaumayxuc"));
                startActivity(intent);
            }
        });
    }
}