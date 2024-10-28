package com.example.datn_toystoryshop.Register_login;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.datn_toystoryshop.Home_screen;
import com.example.datn_toystoryshop.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ForgotOTP_screen extends AppCompatActivity {
    private EditText otp1, otp2, otp3, otp4, otp5, otp6; // Các EditText cho OTP
    private Button verifyButton;
    private String verificationId;
    private FirebaseAuth mAuth;
    private String phoneNumber, password;
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
                Intent intent = new Intent(ForgotOTP_screen.this, SignIn_screen.class); // Thay HomeActivity bằng tên activity chính của bạn
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
                    Toast.makeText(ForgotOTP_screen.this, getString(R.string.otp_verify), Toast.LENGTH_SHORT).show();
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

    private void signInWithCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Sau khi xác minh thành công, lưu dữ liệu người dùng vào Firestore
                        saveUserDataToFirestore();
                    } else {
                        Toast.makeText(ForgotOTP_screen.this, getString(R.string.otp_fail), Toast.LENGTH_SHORT).show();
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
        user.put("phoneNumber", phoneNumber);
        user.put("password", password);

        // Kiểm tra xem số điện thoại đã tồn tại trong Firestore chưa
        db.collection("users")
                .whereEqualTo("phoneNumber", phoneNumber)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && !task.getResult().isEmpty()) {
                        // Nếu tài liệu đã tồn tại, cập nhật dữ liệu
                        DocumentSnapshot document = task.getResult().getDocuments().get(0);
                        document.getReference().update(user)
                                .addOnSuccessListener(aVoid -> {
                                    // Hiển thị dialog với thông tin người dùng
                                    showUserInfoDialog(phoneNumber);
                                    Log.d("ForgotOTP_screen", "Phoneaaaaaaanumber: " + phoneNumber);
                                })
                                .addOnFailureListener(e -> {
                                    Toast.makeText(ForgotOTP_screen.this, getString(R.string.Toast_wrong), Toast.LENGTH_SHORT).show();
                                });
                    }
//                    else {
//                        // Nếu tài liệu không tồn tại, tạo một tài liệu mới
//                        db.collection("users").add(user)
//                                .addOnSuccessListener(documentReference -> {
//                                    // Hiển thị dialog với thông tin người dùng
//                                    showUserInfoDialog(phoneNumber);
//                                })
//                                .addOnFailureListener(e -> {
//                                    Toast.makeText(ForgotOTP_screen.this, getString(R.string.Toast_wrong), Toast.LENGTH_SHORT).show();
//                                });
//                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(ForgotOTP_screen.this, getString(R.string.Toast_wrong), Toast.LENGTH_SHORT).show();
                });
    }


    private void showUserInfoDialog(String phoneNumber) {
        // Tạo AlertDialog để hiển thị phoneNumber và cho phép nhập mật khẩu mới
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(ForgotOTP_screen.this);
        builder.setTitle(getString(R.string.forgot_otp_title));

        // Tạo layout cho dialog
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_reset_password, null);
        builder.setView(dialogView);

        // Liên kết các view trong dialog
        TextInputEditText newPasswordInput = dialogView.findViewById(R.id.newPasswordInput);
        TextView phoneNumberText = dialogView.findViewById(R.id.phoneNumberText);
        phoneNumberText.setText(phoneNumber); // Hiển thị số điện thoại

        // Nút "Update" để cập nhật mật khẩu
        builder.setPositiveButton("Update", (dialog, which) -> {
            String newPassword = Objects.requireNonNull(newPasswordInput.getText()).toString().trim();
            if (!isPasswordValid(newPassword)) {
                Toast.makeText(ForgotOTP_screen.this, getString(R.string.Toast_pass), Toast.LENGTH_SHORT).show();
                return;
            }
            if (!newPassword.isEmpty()) {
                // Cập nhật mật khẩu mới lên Firestore
                updatePasswordInFirestore(phoneNumber, newPassword);
                // Truyền dữ liệu phoneNumber và newPassword sang màn SignIn_screen
                Intent intent = new Intent(ForgotOTP_screen.this, SignIn_screen.class);
                intent.putExtra("phoneNumber", phoneNumber);
                intent.putExtra("newPassword", newPassword);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            } else {
                Toast.makeText(ForgotOTP_screen.this, getString(R.string.forgot_otp_pass), Toast.LENGTH_SHORT).show();
            }
        });

        // Nút "Cancel" để đóng dialog
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        // Hiển thị dialog
        builder.create().show();
    }
    private boolean isPasswordValid(String password) {
        return password.length() >= 6 && password.chars().anyMatch(Character::isUpperCase);
    }
    private void updatePasswordInFirestore(String phoneNumber, String newPassword) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Truy vấn tài liệu có phoneNumber khớp
        db.collection("users")
                .whereEqualTo("phoneNumber", phoneNumber)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && !task.getResult().isEmpty()) {
                        DocumentSnapshot document = task.getResult().getDocuments().get(0);
                        document.getReference().update("password", newPassword) // Cập nhật mật khẩu mới
                                .addOnSuccessListener(aVoid -> {
                                    Toast.makeText(ForgotOTP_screen.this, getString(R.string.forgot_otp_success), Toast.LENGTH_SHORT).show();
                                })
                                .addOnFailureListener(e -> {
                                    Toast.makeText(ForgotOTP_screen.this, getString(R.string.forgot_otp_error), Toast.LENGTH_SHORT).show();
                                });
                    } else {
                        Toast.makeText(ForgotOTP_screen.this, getString(R.string.Toast_wrong_sdt), Toast.LENGTH_SHORT).show();
                    }
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
