package com.example.datn_toystoryshop.Profile;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.datn_toystoryshop.Home_screen;
import com.example.datn_toystoryshop.R;

import java.util.Random;

public class Introduce_Friends_screen extends AppCompatActivity {

    private String[] referralCodes = {"TOYSTORY123", "TOYSTORY456", "TOYSTORY789", "TOYSTORY101"};
    private ImageView btnBack;
    private TextView tvReferralCode;
    private Button btnShare;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_introduce_friends_screen);
        btnBack = findViewById(R.id.btnBack);
        tvReferralCode = findViewById(R.id.tvReferralCode);
        btnShare = findViewById(R.id.btnShare);


        String referralCode = getRandomReferralCode();
        tvReferralCode.setText(referralCode);

//        btnShare.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                shareApp(referralCode);
//            }
//        });
//
//        btnBack.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                onBackPressed();
//            }
//        });
    }

    // Phương thức chọn mã ngẫu nhiên từ danh sách
    private String getRandomReferralCode() {
        Random random = new Random();
        int index = random.nextInt(referralCodes.length); // Lấy chỉ số ngẫu nhiên
        return referralCodes[index]; // Trả về mã tương ứng
    }

    private void shareApp(String referralCode) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.intro_title_int) + referralCode);
        sendIntent.setType("text/plain");

        Intent shareIntent = Intent.createChooser(sendIntent, getString(R.string.share_with_friends_int));
        startActivity(shareIntent);
    }
}
