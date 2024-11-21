package com.example.datn_toystoryshop;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import java.math.BigDecimal;

public class PayPalActivity extends Activity {

    public static final String PAYPAL_CLIENT_ID = "AT0xBhMFqCfxlpeEKUpJsYxVmoBEtMUWU3Ig_Fbt6F3LNmK48D55nJ2cv3qKw_u51Kodb6PsdAX4DQpd";  // Sử dụng clientId sandbox
    public static final int REQUEST_CODE_PAYMENT = 1;

    // Cấu hình PayPal
    public static PayPalConfiguration config = new PayPalConfiguration()
            .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX) // Môi trường Sandbox
            .clientId(PAYPAL_CLIENT_ID); // Dùng Client ID của app trong Sandbox


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_pal);  // Set layout cho activity này

        // Khởi tạo PayPalService khi Activity được khởi tạo
        Log.d("PayPalActivity", "Starting PayPal service...");
        Intent intent = new Intent(this, com.paypal.android.sdk.payments.PayPalService.class);
        intent.putExtra(com.paypal.android.sdk.payments.PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        startService(intent);

        // Tạo Button và thiết lập sự kiện khi người dùng chọn thanh toán
        Button payButton = findViewById(R.id.btn_pay);
        payButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("PayPalActivity", "Payment button clicked.");
                startPayPalPayment();
            }
        });
    }

    // Phương thức để bắt đầu thanh toán
    private void startPayPalPayment() {
        Log.d("PayPalActivity", "Starting PayPal payment...");
        PayPalPayment payment = new PayPalPayment(
                new BigDecimal("19.99"), // Số tiền
                "USD",                   // Mã tiền tệ (đúng format ISO 4217)
                "Toy Item",              // Mô tả sản phẩm
                PayPalPayment.PAYMENT_INTENT_SALE); // Loại giao dịch (sale)


        Intent intent = new Intent(this, PaymentActivity.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payment);
        startActivityForResult(intent, REQUEST_CODE_PAYMENT);
    }

    // Xử lý kết quả trả về từ PayPal
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_PAYMENT) {
            if (resultCode == RESULT_OK) {
                PaymentConfirmation confirmation = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                if (confirmation != null) {
                    // Log thông tin xác nhận thanh toán
                    Log.i("PayPalActivity", "Payment successful: " + confirmation.toJSONObject().toString());
                    Toast.makeText(this, "Payment successful!", Toast.LENGTH_LONG).show();
                } else {
                    // Log nếu xác nhận thanh toán bị null
                    Log.e("PayPalActivity", "Payment confirmation is null.");
                    Toast.makeText(this, "Payment confirmation failed.", Toast.LENGTH_LONG).show();
                }
            } else if (resultCode == RESULT_CANCELED) {
                // Người dùng hủy thanh toán
                Log.i("PayPalActivity", "User canceled the payment.");
                Toast.makeText(this, "Payment canceled!", Toast.LENGTH_LONG).show();
            } else {
                // Thanh toán thất bại, thêm log chi tiết
                Log.e("PayPalActivity", "Payment failed with result code: " + resultCode);

                // Kiểm tra chi tiết lỗi từ dữ liệu trả về
                if (data != null) {
                    // Lấy thêm thông tin lỗi nếu có
                    String errorDetails = data.getStringExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                    if (errorDetails != null) {
                        Log.e("PayPalActivity", "Error details: " + errorDetails);
                    } else {
                        Log.e("PayPalActivity", "Error but no additional details provided.");
                    }
                } else {
                    Log.e("PayPalActivity", "No data received from PayPal.");
                }


                Toast.makeText(this, "Payment failed! Result code: " + resultCode, Toast.LENGTH_LONG).show();
            }
        }
    }



    // Dừng PayPalService khi Activity bị hủy
    @Override
    protected void onDestroy() {
        Log.d("PayPalActivity", "Stopping PayPal service...");
        stopService(new Intent(this, com.paypal.android.sdk.payments.PayPalService.class));
        super.onDestroy();
    }
}
