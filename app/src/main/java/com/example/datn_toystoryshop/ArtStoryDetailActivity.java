package com.example.datn_toystoryshop;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class ArtStoryDetailActivity extends AppCompatActivity {
    private TextView titleView, authorView, dateView, contentView;
    private ImageView mainImageView, imageView1, imageView2, imageView3;
    private TextView captionView1, captionView2, captionView3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_art_story_detail);

        // Ánh xạ các view
        titleView = findViewById(R.id.detailTitle);
        authorView = findViewById(R.id.detailAuthor);
        dateView = findViewById(R.id.detailDate);
        contentView = findViewById(R.id.detailContent);
        mainImageView = findViewById(R.id.mainImage);
        imageView1 = findViewById(R.id.image1);
        imageView2 = findViewById(R.id.image2);
        imageView3 = findViewById(R.id.image3);
        captionView1 = findViewById(R.id.imageCaption1);
        captionView2 = findViewById(R.id.imageCaption2);
        captionView3 = findViewById(R.id.imageCaption3);

        // Nhận dữ liệu từ Intent
        Intent intent = getIntent();
        String title = intent.getStringExtra("title");
        String author = intent.getStringExtra("author");
        long dateMillis = intent.getLongExtra("date", 0);
        String content = intent.getStringExtra("content");
        ArrayList<String> imageUrl = intent.getStringArrayListExtra("imageUrl");
        ArrayList<String> caption = intent.getStringArrayListExtra("caption");

        // Đặt dữ liệu lên các view
        titleView.setText(title);
        authorView.setText(author);
        contentView.setText(content);

        // Chuyển đổi date từ milliseconds thành định dạng ngày tháng
        if (dateMillis > 0) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            dateView.setText(dateFormat.format(new Date(dateMillis)));
        }

        // Đặt ảnh và chú thích
        if (imageUrl != null) {
            // Ảnh chính đầu trang
            if (imageUrl.size() > 0) {
                Glide.with(this).load(imageUrl.get(0)).into(mainImageView);
            }
            // Ảnh 1 với chú thích 1
            if (imageUrl.size() > 1) {
                Glide.with(this).load(imageUrl.get(1)).into(imageView1);
                imageView1.setVisibility(View.VISIBLE);
                if (caption != null && caption.size() > 0) {
                    captionView1.setText(caption.get(0));
                    captionView1.setVisibility(View.VISIBLE);
                }
            } else {
                imageView1.setVisibility(View.GONE);
                captionView1.setVisibility(View.GONE);
            }

            // Ảnh 2 với chú thích 2
            if (imageUrl.size() > 2) {
                Glide.with(this).load(imageUrl.get(2)).into(imageView2);
                imageView2.setVisibility(View.VISIBLE);
                if (caption != null && caption.size() > 1) {
                    captionView2.setText(caption.get(1));
                    captionView2.setVisibility(View.VISIBLE);
                }
            } else {
                imageView2.setVisibility(View.GONE);
                captionView2.setVisibility(View.GONE);
            }

            // Ảnh 3 với chú thích 3
            if (imageUrl.size() > 3) {
                Glide.with(this).load(imageUrl.get(3)).into(imageView3);
                imageView3.setVisibility(View.VISIBLE);
                if (caption != null && caption.size() > 2) {
                    captionView3.setText(caption.get(2));
                    captionView3.setVisibility(View.VISIBLE);
                }
            } else {
                imageView3.setVisibility(View.GONE);
                captionView3.setVisibility(View.GONE);
            }
        }
    }


}