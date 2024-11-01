package com.example.datn_toystoryshop.Profile;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.datn_toystoryshop.R;

public class Evaluate_screen extends AppCompatActivity {

    private RatingBar ratingBar;
    private EditText etFeedback;
    private Button btnSubmit;
    private ImageView btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evaluate_screen);
        ratingBar = findViewById(R.id.ratingBar);
        etFeedback = findViewById(R.id.etFeedback);
        btnSubmit = findViewById(R.id.btnSubmit);
        btnBack = findViewById(R.id.btnBack);

        // Nút quay lại đăng nhập
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitFeedback();
            }
        });
    }

    private void submitFeedback() {
        float rating = ratingBar.getRating();
        String feedback = etFeedback.getText().toString().trim();

        if (rating == 0) {
            Toast.makeText(this, getString(R.string.star_rating_eva), Toast.LENGTH_SHORT).show();
            return;
        }

        // Xử lý gửi đánh giá và phản hồi đến Firebase hoặc máy chủ
        // Ví dụ: gửi dữ liệu đến Firebase Firestore

        // Hiển thị thông báo cho người dùng
        Toast.makeText(this, getString(R.string.thank_review_eva), Toast.LENGTH_SHORT).show();

        // Reset lại các trường
        ratingBar.setRating(0);
        etFeedback.setText("");
    }
}