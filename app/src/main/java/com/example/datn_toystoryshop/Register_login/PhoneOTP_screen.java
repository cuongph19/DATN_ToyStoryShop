package com.example.datn_toystoryshop.Register_login;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.datn_toystoryshop.Home_screen;
import com.example.datn_toystoryshop.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class PhoneOTP_screen extends AppCompatActivity {
    private EditText otp1, otp2, otp3, otp4, otp5, otp6; // Các EditText cho OTP
    private Button verifyButton;
    private String verificationId;
    private FirebaseAuth mAuth;
    private String name, email, phoneNumber, password;
    private ImageView btnBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_otp_screen);

        // Liên kết các EditText từ file XML
        otp1 = findViewById(R.id.otpEditText1);
        otp2 = findViewById(R.id.otpEditText2);
        otp3 = findViewById(R.id.otpEditText3);
        otp4 = findViewById(R.id.otpEditText4);
        otp5 = findViewById(R.id.otpEditText5);
        otp6 = findViewById(R.id.otpEditText6);
        btnBack = findViewById(R.id.btnBack); // Khởi tạo nút quay lại
        verifyButton = findViewById(R.id.verifyButton);
        mAuth = FirebaseAuth.getInstance();

        // Nhận dữ liệu từ intent
        verificationId = getIntent().getStringExtra("verificationId");
        name = getIntent().getStringExtra("name");
        email = getIntent().getStringExtra("email");
        phoneNumber = getIntent().getStringExtra("phoneNumber");
        password = getIntent().getStringExtra("password");

        // Thiết lập tính năng tự động chuyển tiếp giữa các ô nhập OTP
        setOtpMoveListener(otp1, otp2);
        setOtpMoveListener(otp2, otp3);
        setOtpMoveListener(otp3, otp4);
        setOtpMoveListener(otp4, otp5);
        setOtpMoveListener(otp5, otp6);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Chuyển đến màn hình chính
                Intent intent = new Intent(PhoneOTP_screen.this, SignIn_screen.class); // Thay HomeActivity bằng tên activity chính của bạn
                startActivity(intent);
                finish(); // Kết thúc activity hiện tại nếu không cần quay lại
            }
        });
        // Xử lý khi người dùng nhấn nút "Verify"
        verifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = otp1.getText().toString().trim() +
                        otp2.getText().toString().trim() +
                        otp3.getText().toString().trim() +
                        otp4.getText().toString().trim() +
                        otp5.getText().toString().trim() +
                        otp6.getText().toString().trim();

                if (code.isEmpty() || code.length() < 6) {
                    Toast.makeText(PhoneOTP_screen.this, "Vui lòng nhập mã OTP hợp lệ.", Toast.LENGTH_SHORT).show();
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
                        // Sau khi xác minh thành công, lưu dữ liệu người dùng vào Firestore
                        saveUserDataToFirestore();
                    } else {
                        Toast.makeText(PhoneOTP_screen.this, "Xác minh thất bại!", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    // Lưu thông tin người dùng vào Firestore
    private void saveUserDataToFirestore() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        // Kiểm tra và chuyển đổi số điện thoại về định dạng +84 nếu nó bắt đầu bằng 0
        if (phoneNumber.startsWith("0")) {
            phoneNumber = phoneNumber.replaceFirst("0", "+84");
        }

        Map<String, Object> user = new HashMap<>();
        user.put("email", email);
        user.put("phoneNumber", phoneNumber);
        user.put("password", password); // Thay thế bằng biến mật khẩu của bạn
        user.put("name", name);

        // Sử dụng email làm Collection ID và số điện thoại làm Document ID
        db.collection("users").document(phoneNumber)
                .set(user)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(PhoneOTP_screen.this, "Đăng ký thành công!", Toast.LENGTH_SHORT).show();
                    // Chuyển sang màn hình chính sau khi xác minh thành công
                    Intent intent = new Intent(PhoneOTP_screen.this, Home_screen.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(PhoneOTP_screen.this, "Lỗi lưu dữ liệu: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    // Thiết lập tự động chuyển tiếp giữa các EditText
    private void setOtpMoveListener(final EditText currentOtp, final EditText nextOtp) {
        currentOtp.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Không cần xử lý
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 1) {
                    nextOtp.requestFocus(); // Chuyển focus sang EditText tiếp theo
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Không cần xử lý
            }
        });

        currentOtp.setOnKeyListener((v, keyCode, event) -> {
            if (keyCode == android.view.KeyEvent.KEYCODE_DEL && currentOtp.getText().length() == 0) {
                currentOtp.clearFocus(); // Xóa focus khỏi EditText hiện tại
                nextOtp.requestFocus();  // Trở lại EditText trước đó
                return true;
            }
            return false;
        });
    }
}
