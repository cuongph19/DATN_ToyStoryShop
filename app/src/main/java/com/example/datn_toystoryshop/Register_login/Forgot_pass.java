package com.example.datn_toystoryshop.Register_login;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
    private String verificationId;  // Lưu mã xác thực OTP
    private EditText edmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_pass);

        Button btnsenemail = findViewById(R.id.btnsenemail);
        edmail = findViewById(R.id.edmail); // EditText cho email hoặc số điện thoại
        btnBack = findViewById(R.id.btnBack);

        mAuth = FirebaseAuth.getInstance();

        // Nút quay lại đăng nhập
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Forgot_pass.this, SignIn_screen.class);
                startActivity(intent);
                finish();
            }
        });

        // Nút gửi email hoặc OTP
        btnsenemail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String input = edmail.getText().toString();

                if (input.isEmpty()) {
                    Toast.makeText(Forgot_pass.this, getString(R.string.Toast_email), Toast.LENGTH_SHORT).show();
                    return;
                }

                // Kiểm tra xem người dùng nhập email hay số điện thoại
                if (Patterns.EMAIL_ADDRESS.matcher(input).matches()) {
                    sendPasswordResetEmail(input);  // Gửi email reset password
                } else if (input.matches("\\d+")) {  // Trường hợp nhập số điện thoại
                    sendVerificationCode(input);  // Gửi mã OTP cho số điện thoại
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
                } else {
                    Toast.makeText(Forgot_pass.this, getString(R.string.Toast_send_error), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // Hàm gửi mã OTP cho số điện thoại
    private void sendVerificationCode(String phoneNumber) {
        // Thêm mã quốc gia (ví dụ mã quốc gia Việt Nam là +84)
        String countryCode = "+84";
        if (phoneNumber.startsWith("0")) {
            phoneNumber = phoneNumber.substring(1); // Bỏ số 0 đầu
        }
        String fullPhoneNumber = countryCode + phoneNumber;  // Ghép mã quốc gia và số điện thoại

        // Kiểm tra định dạng số điện thoại
        if (!fullPhoneNumber.matches("^\\+[1-9]\\d{1,14}$")) {
            Toast.makeText(Forgot_pass.this, getString(R.string.Toast_wrong), Toast.LENGTH_LONG).show();
            return;
        }

        // Gửi mã OTP qua Firebase
        PhoneAuthOptions options = PhoneAuthOptions.newBuilder(mAuth)
                .setPhoneNumber(fullPhoneNumber)  // Số điện thoại phải ở định dạng E.164
                .setTimeout(60L, TimeUnit.SECONDS)
                .setActivity(this)
                .setCallbacks(mCallbacks)
                .build();
        PhoneAuthProvider.verifyPhoneNumber(options);  // Gửi yêu cầu OTP
    }

    // Xử lý callback khi mã OTP được gửi
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(PhoneAuthCredential credential) {
            // Không cần nhập OTP vì đã xác minh tự động
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Toast.makeText(Forgot_pass.this, getString(R.string.send_otp_error) + e.getMessage(), Toast.LENGTH_LONG).show();
        }

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            verificationId = s;

            // Chuyển tới màn hình nhập OTP
            Intent intent = new Intent(Forgot_pass.this, ForgotOTP_screen.class);
            intent.putExtra("verificationId", verificationId);
            intent.putExtra("phoneNumber", edmail.getText().toString().trim());
            startActivity(intent);
        }
    };
}
