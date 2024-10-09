package com.example.datn_toystoryshop;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

public class PhoneOTP_screen extends AppCompatActivity {


    private EditText otpEditText;
    private Button verifyButton;
    private String verificationId;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_otp_screen);
        otpEditText = findViewById(R.id.otpEditText);
        verifyButton = findViewById(R.id.verifyButton);
        mAuth = FirebaseAuth.getInstance();

        // Nhận verificationId từ intent
        verificationId = getIntent().getStringExtra("verificationId");

        // Xử lý khi người dùng nhấn nút "Verify"
        verifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = otpEditText.getText().toString().trim();
                if (code.isEmpty() || code.length() < 6) {
                    otpEditText.setError("Vui lòng nhập mã OTP hợp lệ.");
                    otpEditText.requestFocus();
                    return;
                }
                verifyCode(code);
            }
        });
    }

    // Xác minh mã OTP
    private void verifyCode(String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        signInWithCredential(credential);
    }

    // Đăng nhập hoặc tạo tài khoản với OTP đã xác minh
    private void signInWithCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Chuyển sang màn hình chính sau khi xác minh thành công
                        Intent intent = new Intent(PhoneOTP_screen.this, Home_screen.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    } else {
                        Toast.makeText(PhoneOTP_screen.this, "Xác minh thất bại!", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}