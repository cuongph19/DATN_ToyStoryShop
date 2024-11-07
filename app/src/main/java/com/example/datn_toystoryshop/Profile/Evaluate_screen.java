package com.example.datn_toystoryshop.Profile;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.datn_toystoryshop.Model.Favorite_Model;
import com.example.datn_toystoryshop.Model.Feeback_Model;
import com.example.datn_toystoryshop.R;
import com.example.datn_toystoryshop.Server.APIService;
import com.example.datn_toystoryshop.Server.RetrofitClient;

import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Evaluate_screen extends AppCompatActivity {

    private RatingBar ratingBar;
    private EditText etFeedback;
    private Button btnSubmit;
    private ImageView btnBack;
    private String feedback;
    private float rating;
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
        rating = ratingBar.getRating();
        feedback = etFeedback.getText().toString().trim();

        if (rating == 0) {
            Toast.makeText(this, getString(R.string.star_rating_eva), Toast.LENGTH_SHORT).show();
            return;
        }

        Feeback_Model feebackModel = new Feeback_Model(null, "cusId", rating, feedback, new Date().toString());

        APIService apiService = RetrofitClient.getInstance().create(APIService.class);
        Call<Feeback_Model> call = apiService.addToFeeback(feebackModel);

        call.enqueue(new Callback<Feeback_Model>() {
            @Override
            public void onResponse(Call<Feeback_Model> call, Response<Feeback_Model> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), getString(R.string.thank_review_eva), Toast.LENGTH_SHORT).show();
                } else {
                    Log.e("API_ERROR", "Thêm đánh giá thất bại, mã phản hồi: " + response.code());
                    Toast.makeText(getApplicationContext(), "Thêm đánh giá thất bại", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Feeback_Model> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Không thể kết nối tới API", Toast.LENGTH_SHORT).show();
                Log.e("API_ERROR", "Lỗi kết nối API khi thêm đánh giá", t);
            }
        });

        // Reset lại các trường
        ratingBar.setRating(0);
        etFeedback.setText("");
    }

}