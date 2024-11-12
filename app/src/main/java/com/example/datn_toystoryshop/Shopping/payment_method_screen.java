package com.example.datn_toystoryshop.Shopping;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.datn_toystoryshop.R;

public class payment_method_screen extends AppCompatActivity {
    private RadioButton radioCOD, radioShopeePay;
    private TextView tvAgree;
    private LinearLayout layoutShopeePay, layoutCOD;
    private String paytext = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_method_screen);
        radioCOD = findViewById(R.id.radioCOD);
        radioShopeePay = findViewById(R.id.radioShopeePay);
        tvAgree = findViewById(R.id.tvAgree);
        layoutShopeePay = findViewById(R.id.layoutShopeePay);
        layoutCOD = findViewById(R.id.layoutCOD);
        layoutCOD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Khi click vào layout, đánh dấu RadioButton là checked
                radioCOD.setChecked(true);
            }
        });
        layoutShopeePay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Khi click vào layout, đánh dấu RadioButton là checked
                radioShopeePay.setChecked(true);
            }
        });


        tvAgree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (radioCOD.isChecked()) {
                    paytext = "Thanh toán khi nhận hàng";
                } else if (radioShopeePay.isChecked()) {
                    paytext = "Ví ShopeePay (Yêu thích)";
                }
                Intent intent = new Intent();
                intent.putExtra("paytext", paytext);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }
}