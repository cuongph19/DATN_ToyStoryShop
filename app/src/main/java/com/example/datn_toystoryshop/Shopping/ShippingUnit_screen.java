package com.example.datn_toystoryshop.Shopping;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.StrikethroughSpan;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.datn_toystoryshop.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ShippingUnit_screen extends AppCompatActivity {
        private ImageView imgBack;
        private TextView tvFastShipping, tvExpressShipping, tvFastShippingOldPrice, tvFastShippingNewPrice, tvExpressShippingPrice,btnConfirm,tvFastShippingDetails,tvExpressShippingDetails,tvExpressShippingNewPrice;
        private RadioButton radioexpress, radiofast;
        private LinearLayout LLexpress, LLfast;
        private String shipping_method = "";
        private String shipping_price = "";

        private String getFormattedDate(int daysToAdd, String format) {
        // Khởi tạo Calendar với ngày hiện tại
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, daysToAdd);

        // Định dạng ngày theo yêu cầu
        SimpleDateFormat dateFormat = new SimpleDateFormat(format, new Locale("vi", "VN"));
        return dateFormat.format(calendar.getTime());
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shipping_unit_screen);

        imgBack = findViewById(R.id.imgBack);
        tvFastShipping = findViewById(R.id.tvFastShipping);
        tvExpressShipping = findViewById(R.id.tvExpressShipping);
        tvFastShippingOldPrice = findViewById(R.id.tvFastShippingOldPrice);
        tvFastShippingNewPrice = findViewById(R.id.tvFastShippingNewPrice);
        tvExpressShippingNewPrice = findViewById(R.id.tvExpressShippingNewPrice);
        tvExpressShippingPrice = findViewById(R.id.tvExpressShippingPrice);
        btnConfirm = findViewById(R.id.btnConfirm);
        tvFastShippingDetails = findViewById(R.id.tvFastShippingDetails);
        tvExpressShippingDetails = findViewById(R.id.tvExpressShippingDetails);
        radioexpress = findViewById(R.id.radioexpress);
        radiofast = findViewById(R.id.radiofast);
        LLexpress = findViewById(R.id.LLexpress);
        LLfast = findViewById(R.id.LLfast);

        Intent intent = getIntent();
        String shipDiscount1 = intent.getStringExtra("shipDiscount1");
        String ShipDiscount = intent.getStringExtra("ShipDiscount");
        if (shipDiscount1 != null && !shipDiscount1.isEmpty()) {
            // Xóa ký tự "₫", dấu "-" và dấu chấm (.) nếu có
            String cleanDiscount = shipDiscount1.replace("₫", "").replace("-", "").replace(".", "").trim();

// Chuyển chuỗi thành số (long hoặc int)
            try {
                long discountValue = Long.parseLong(cleanDiscount);  // Chuyển thành số nguyên Long

                String formattedDiscount = String.format("%,d VND", discountValue);  // Sử dụng %d thay cho %f

                Log.e("API_ERROR", "btttttttttttttttttttttffffrrfs" + formattedDiscount);

            } catch (NumberFormatException e) {
                Log.e("API_ERROR", "Error parsing discount value: " + e.getMessage());
            }

        }
        Log.e("API_ERROR", "bttttttttttttttttttttt" + shipDiscount1);

        Log.e("API_ERROR", "btttttttttttttttttttttff " + ShipDiscount);

        if (ShipDiscount != null) {
            if ("Nhanh".equals(ShipDiscount)) {
                radiofast.setChecked(true);
                radioexpress.setChecked(false);
            }
            if ("Hỏa Tốc".equals(ShipDiscount)) {
                radioexpress.setChecked(true);
                radiofast.setChecked(false);
            }
        }
        // Đặt ngày giao hàng dự kiến
        String FastDate = getFormattedDate(7, "'ngày' dd 'tháng' MM 'năm' yyyy");
        String ExpressDate = getFormattedDate(2, "'ngày' dd 'tháng' MM 'năm' yyyy");
        tvFastShippingDetails.setText("Nhận Voucher trị giá ₫15.000 nếu đơn hàng được giao đến bạn sau "+ FastDate);
        tvExpressShippingDetails.setText("Nhận Voucher trị giá ₫15.000 nếu đơn hàng được giao đến bạn sau " + ExpressDate);

//        LLfast.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // Khi click vào layout, đánh dấu RadioButton là checked
//                radiofast.setChecked(true);
//                radioexpress.setChecked(false);
//                if (totalShipDiscount != null) {
//                    tvFastShippingNewPrice.setVisibility(View.VISIBLE);
//                    tvFastShippingNewPrice.setText(String.format("-₫%,.0f", totalShipDiscount));
//                    String text = String.format("%,.0fđ", 40000);
//                    SpannableString spannableString = new SpannableString(text);
//                    spannableString.setSpan(new StrikethroughSpan(), 0, text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//                    spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#888888")), 0, text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//                    tvFastShippingOldPrice.setText(spannableString);
//                }
//            }
//        });
//        LLexpress.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // Khi click vào layout, đánh dấu RadioButton là checked
//                radioexpress.setChecked(true);
//                radiofast.setChecked(false);
//                if (totalShipDiscount != null) {
//                    tvExpressShippingNewPrice.setVisibility(View.VISIBLE);
//                tvExpressShippingNewPrice.setText(String.format("-₫%,.0f",totalShipDiscount));
//                String text = String.format("%,.0fđ", 40000);
//                SpannableString spannableString = new SpannableString(text);
//                spannableString.setSpan(new StrikethroughSpan(), 0, text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//                spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#888888")), 0, text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//                tvExpressShippingPrice.setText(spannableString);}
//            }
//        });
//        radiofast.setOnClickListener(v -> {
//            radiofast.setChecked(true);
//            radioexpress.setChecked(false);
//            if (totalShipDiscount != null) {
//                tvFastShippingNewPrice.setVisibility(View.VISIBLE);
//            tvFastShippingNewPrice.setText(String.format("-₫%,.0f",totalShipDiscount));
//            String text = String.format("%,.0fđ", 40000);
//            SpannableString spannableString = new SpannableString(text);
//            spannableString.setSpan(new StrikethroughSpan(), 0, text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//            spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#888888")), 0, text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//            tvFastShippingOldPrice.setText(spannableString);}
//        });
//        radioexpress.setOnClickListener(v -> {
//            radioexpress.setChecked(true);
//            radiofast.setChecked(false);
//            if (totalShipDiscount != null) {
//                tvExpressShippingNewPrice.setVisibility(View.VISIBLE);
//            tvExpressShippingNewPrice.setText(String.format("-₫%,.0f",totalShipDiscount));
//            String text = String.format("%,.0fđ", 40000);
//            SpannableString spannableString = new SpannableString(text);
//            spannableString.setSpan(new StrikethroughSpan(), 0, text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//            spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#888888")), 0, text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//            tvExpressShippingPrice.setText(spannableString);}
//        });
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (radiofast.isChecked()) {
                    shipping_method= "Nhanh";
                    shipping_price= "₫40.000";
                } else if (radioexpress.isChecked()) {
                    shipping_method= "Hỏa Tốc";
                    shipping_price= "₫80.000";
                }
                Intent intent = new Intent();
                intent.putExtra("shipping_method", shipping_method);
                intent.putExtra("shipping_price", shipping_price);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
        imgBack.setOnClickListener(v -> onBackPressed());
    }
}