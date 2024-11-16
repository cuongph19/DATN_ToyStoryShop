package com.example.datn_toystoryshop.Register_login;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.datn_toystoryshop.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class Forgot_pass extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private ImageView btnBack;
    private EditText edmail;
    private TextView btnsenemail;
    private String verificationId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_pass);

        btnsenemail = findViewById(R.id.btnsenemail);
        edmail = findViewById(R.id.edmail);
        btnBack = findViewById(R.id.btnBack);
        // Khởi tạo Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Xử lý sự kiện cho nút quay lại
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Forgot_pass.this, SignIn_screen.class);
                startActivity(intent);
                finish();
            }
        });

        // Xử lý sự kiện cho nút gửi email hoặc OTP
        btnsenemail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String input = edmail.getText().toString();

                if (input.isEmpty()) {
                    Toast.makeText(Forgot_pass.this, getString(R.string.Toast_email), Toast.LENGTH_SHORT).show();
                    return;
                }

                // Phân biệt giữa email và số điện thoại
                if (Patterns.EMAIL_ADDRESS.matcher(input).matches()) {
                    sendPasswordResetEmail(input); // Gửi email reset mật khẩu
                } else if (input.matches("\\d+")) { // Trường hợp nhập số điện thoại
                    sendVerificationCode(input);   // Gửi mã OTP
                } else {
                    Toast.makeText(Forgot_pass.this, getString(R.string.Toast_failure), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // Hàm gửi email đặt lại mật khẩu
    private void sendPasswordResetEmail(String email) {
        mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(Forgot_pass.this, getString(R.string.Toast_send) + email, Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Forgot_pass.this, SignIn_screen.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(Forgot_pass.this, getString(R.string.Toast_send_error), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // Hàm gửi mã OTP qua số điện thoại
    private void sendVerificationCode(String phoneNumber) {
        // Thêm mã quốc gia (Việt Nam: +84)
        String countryCode = "+84";
        if (phoneNumber.startsWith("0")) {
            phoneNumber = phoneNumber.substring(1); // Loại bỏ số 0 đầu
        }
        String fullPhoneNumber = countryCode + phoneNumber;

        // Kiểm tra định dạng số điện thoại
        if (!fullPhoneNumber.matches("^\\+[1-9]\\d{1,14}$")) {
            Toast.makeText(Forgot_pass.this, getString(R.string.Toast_wrong), Toast.LENGTH_LONG).show();
            return;
        }

        // Cấu hình gửi mã OTP
        PhoneAuthOptions options = PhoneAuthOptions.newBuilder(mAuth)
                .setPhoneNumber(fullPhoneNumber)  // Số điện thoại E.164
                .setTimeout(60L, TimeUnit.SECONDS) // Thời gian timeout
                .setActivity(this)                // Activity hiện tại
                .setCallbacks(mCallbacks)         // Callback xử lý kết quả
                .build();

        // Gửi mã OTP
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    // Callback xử lý khi gửi mã OTP
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(PhoneAuthCredential credential) {
            // Xác minh tự động thành công, không cần nhập mã OTP
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Toast.makeText(Forgot_pass.this, getString(R.string.send_otp_error) + e.getMessage(), Toast.LENGTH_LONG).show();
        }

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);

            verificationId = s; // Lưu mã OTP

            // Chuyển sang màn hình nhập OTP
            Intent intent = new Intent(Forgot_pass.this, ForgotOTP_screen.class);
            intent.putExtra("verificationId", verificationId);
            intent.putExtra("phoneNumber", edmail.getText().toString().trim());
            startActivity(intent);
        }
    };
}
