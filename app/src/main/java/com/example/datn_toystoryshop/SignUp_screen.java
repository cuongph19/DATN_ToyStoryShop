package com.example.datn_toystoryshop;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class SignUp_screen extends AppCompatActivity {
    private TextInputEditText edemail, edpassword, edrppassword;
    private Button btnsignup;
    private FirebaseAuth mAuth;
    private String verificationId;
    private TextView txtLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        // Khởi tạo các thành phần trong giao diện
        edemail = findViewById(R.id.edemail);
        edpassword = findViewById(R.id.edpassword);
        edrppassword = findViewById(R.id.edrppassword);
        btnsignup = findViewById(R.id.btnsignup);
        txtLogin = findViewById(R.id.txtLogin);
        mAuth = FirebaseAuth.getInstance();

        // Xử lý khi nhấn nút đăng ký
        btnsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String input = edemail.getText().toString().trim();
                String password = edpassword.getText().toString().trim();
                String rppassword = edrppassword.getText().toString().trim();

                if (input.isEmpty()) {
                    Toast.makeText(SignUp_screen.this, "Vui lòng nhập email hoặc số điện thoại!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!password.equals(rppassword)) {
                    Toast.makeText(SignUp_screen.this, "Mật khẩu không khớp!", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Kiểm tra nếu là số điện thoại hay email
                if (isPhoneNumber(input)) {
                    // Đăng ký qua số điện thoại
                    String phoneNumber = mAuth.getCurrentUser().getPhoneNumber();
                    savePasswordToFirestore(phoneNumber, password); // Lưu mật khẩu

                    sendVerificationCode(input);
                } else if (isValidEmail(input)) {
                    // Đăng ký qua email
                    registerWithEmail(input, password);
                } else {
                    Toast.makeText(SignUp_screen.this, "Vui lòng nhập email hoặc số điện thoại hợp lệ!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Điều hướng sang màn hình đăng nhập khi nhấn nút "Sign In Now"
        txtLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignUp_screen.this, SignIn_screen.class);
                startActivity(intent);
            }
        });
    }

    // Kiểm tra định dạng email
    private boolean isValidEmail(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    // Kiểm tra định dạng số điện thoại
    private boolean isPhoneNumber(String phoneNumber) {
        return phoneNumber.matches("^0[3|5|7|8|9][0-9]{8}$");
    }

    // Đăng ký bằng email
    private void registerWithEmail(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(SignUp_screen.this, "Đăng ký thành công!", Toast.LENGTH_SHORT).show();
                        // Chuyển tới trang chủ
                        startActivity(new Intent(SignUp_screen.this, SignIn_screen.class));
                        finish();
                    } else {
                        Toast.makeText(SignUp_screen.this, "Đăng ký thất bại: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    // Gửi mã OTP tới số điện thoại
    private void sendVerificationCode(String phoneNumber) {
        if (!phoneNumber.startsWith("+84")) {
            phoneNumber = "+84" + phoneNumber.substring(1); // Bỏ số 0 đầu và thêm +84
        }
        PhoneAuthOptions options = PhoneAuthOptions.newBuilder(mAuth)
                .setPhoneNumber(phoneNumber)
                .setTimeout(60L, TimeUnit.SECONDS)
                .setActivity(this)
                .setCallbacks(mCallbacks)
                .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    // Callbacks để xử lý OTP
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(PhoneAuthCredential credential) {
            String code = credential.getSmsCode();
            if (code != null) {
                verifyCode(code);
            }
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Toast.makeText(SignUp_screen.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            verificationId = s;
            Intent intent = new Intent(SignUp_screen.this, PhoneOTP_screen.class);
            intent.putExtra("verificationId", verificationId);
            intent.putExtra("email", edemail.getText().toString().trim()); // Lưu email vào intent
            intent.putExtra("password", edpassword.getText().toString().trim()); // Lưu mật khẩu vào intent
            startActivity(intent);
        }
    };

    // Xác minh mã OTP
    private void verifyCode(String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        signInWithCredential(credential);
    }

    // Đăng nhập bằng OTP đã xác minh
    private void signInWithCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        String phoneNumber = mAuth.getCurrentUser().getPhoneNumber();
                        String password = getIntent().getStringExtra("password"); // Lấy mật khẩu từ intent
                        savePasswordToFirestore(phoneNumber, password); // Lưu mật khẩu
                    } else {
                        Toast.makeText(SignUp_screen.this, "Xác minh OTP thất bại!", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    // Lưu mật khẩu vào Firestore
    private void savePasswordToFirestore(String phoneNumber, String password) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String, Object> user = new HashMap<>();
        user.put("password", password);

        // Sử dụng số điện thoại làm ID tài liệu
        db.collection("users").document(phoneNumber)
                .set(user)
                .addOnSuccessListener(aVoid -> {
                    Log.d("Firestore", "Mật khẩu đã được lưu cho số điện thoại: " + phoneNumber);
                    Toast.makeText(SignUp_screen.this, "Mật khẩu đã được lưu!", Toast.LENGTH_SHORT).show();
                    // Kiểm tra dữ liệu đã lưu
                    checkUserPasswordInFirestore(phoneNumber);
                })
                .addOnFailureListener(e -> {
                    Log.e("Firestore", "Lưu mật khẩu thất bại: " + e.getMessage());
                    Toast.makeText(SignUp_screen.this, "Lưu mật khẩu thất bại: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    // Kiểm tra dữ liệu đã lưu trong Firestore
    private void checkUserPasswordInFirestore(String phoneNumber) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users").document(phoneNumber).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String password = documentSnapshot.getString("password");
                        Log.d("Firestore", "Mật khẩu đã lưu: " + password);
                        // Chuyển tới trang chủ sau khi lưu

                    } else {
                        Log.d("Firestore", "Không tìm thấy tài liệu cho số điện thoại: " + phoneNumber);
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e("Firestore", "Lỗi khi kiểm tra dữ liệu: " + e.getMessage());
                });
    }
}
