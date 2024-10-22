package com.example.datn_toystoryshop.Register_login;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.datn_toystoryshop.R;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class PhoneNumber_screen extends AppCompatActivity {
    private EditText edPhoneNumber;
    private Spinner spinnerCountryCode;  // Thêm Spinner để chọn mã quốc gia
    private Button btnNext;
    private FirebaseAuth mAuth;
    private String email;
    private String password;
    private String name;
    private String verificationId;
    private ImageView btnBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_number); // Layout mới

        // Khởi tạo các thành phần trong giao diện
        edPhoneNumber = findViewById(R.id.edtPhoneNumber);
        spinnerCountryCode = findViewById(R.id.spinnerCountryCode); // Khởi tạo Spinner mã quốc gia
        btnNext = findViewById(R.id.btnNext); // ID của nút "Next" đã thay đổi
        btnBack = findViewById(R.id.btnBack); // Khởi tạo nút quay lại
        mAuth = FirebaseAuth.getInstance();

        // Nhận email từ intent
        name = getIntent().getStringExtra("name");
        email = getIntent().getStringExtra("email");
        password = getIntent().getStringExtra("password");
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Chuyển đến màn hình chính
                Intent intent = new Intent(PhoneNumber_screen.this, SignIn_screen.class); // Thay HomeActivity bằng tên activity chính của bạn
                startActivity(intent);
                finish(); // Kết thúc activity hiện tại nếu không cần quay lại
            }
        });
        // Xử lý khi nhấn nút "Next"
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneNumber = edPhoneNumber.getText().toString().trim();
                String countryCode = spinnerCountryCode.getSelectedItem().toString(); // Lấy mã quốc gia từ Spinner

                if (TextUtils.isEmpty(phoneNumber)) {
                    Toast.makeText(PhoneNumber_screen.this,  getString(R.string.Toast_infor), Toast.LENGTH_SHORT).show();
                    return;
                }

                // Gửi mã OTP tới số điện thoại
                sendVerificationCode(phoneNumber); // Chỉ gửi số điện thoại
            }
        });
    }

    // Gửi mã OTP tới số điện thoại
    private void sendVerificationCode(String phoneNumber) {
        String countryCode = spinnerCountryCode.getSelectedItem().toString(); // Lấy mã quốc gia từ Spinner

        // Loại bỏ số 0 ở đầu nếu người dùng nhập
        if (phoneNumber.startsWith("0")) {
            phoneNumber = phoneNumber.substring(1); // Loại bỏ số 0 đầu tiên
        }

        String fullPhoneNumber = countryCode + phoneNumber;  // Ghép mã quốc gia và số điện thoại

        // Kiểm tra định dạng số điện thoại đúng chuẩn E.164
        if (!fullPhoneNumber.matches("^\\+[1-9]\\d{1,14}$")) {
            Toast.makeText(PhoneNumber_screen.this, getString(R.string.Toast_wrong), Toast.LENGTH_LONG).show();
            return;
        }

        // Gửi yêu cầu OTP với số điện thoại đã được chuẩn hóa
        PhoneAuthOptions options = PhoneAuthOptions.newBuilder(mAuth)
                .setPhoneNumber(fullPhoneNumber)         // Số điện thoại phải ở định dạng E.164
                .setTimeout(60L, TimeUnit.SECONDS)
                .setActivity(this)
                .setCallbacks(mCallbacks)
                .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    // Xử lý callback khi gửi mã OTP
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(PhoneAuthCredential credential) {
            // Không cần nhập OTP vì đã xác minh tự động
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Toast.makeText(PhoneNumber_screen.this, getString(R.string.send_otp_error) + e.getMessage(), Toast.LENGTH_LONG).show();
        }

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            verificationId = s;

            // Chuyển tới màn hình nhập OTP
            Intent intent = new Intent(PhoneNumber_screen.this, PhoneOTP_screen.class);
            intent.putExtra("verificationId", verificationId);
            intent.putExtra("name", name);
            intent.putExtra("email", email);
            intent.putExtra("password", password);
            intent.putExtra("phoneNumber", edPhoneNumber.getText().toString().trim());
            startActivity(intent);
        }
    };
}
