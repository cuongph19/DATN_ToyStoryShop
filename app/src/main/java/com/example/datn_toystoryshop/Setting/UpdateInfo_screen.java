package com.example.datn_toystoryshop.Setting;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.datn_toystoryshop.R;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;


public class UpdateInfo_screen extends AppCompatActivity {
    private EditText etEmail, etName, etPhone;
    private Button btnSave;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_info_screen);
        etEmail = findViewById(R.id.et_email);
        etName = findViewById(R.id.et_name);
        etPhone = findViewById(R.id.et_phone);
        btnSave = findViewById(R.id.btn_save);

        // Khởi tạo Firestore
        db = FirebaseFirestore.getInstance();

        // Lấy dữ liệu từ Intent
        Intent intent = getIntent();
        String email = intent.getStringExtra("email");
        String name = intent.getStringExtra("name");
        String phoneNumber = intent.getStringExtra("phoneNumber");

        // Đặt dữ liệu vào các EditText
        etEmail.setText(email);
        etName.setText(name);
        etPhone.setText(phoneNumber);

        // Xử lý sự kiện khi người dùng bấm nút lưu
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveDataToFirestore();
                finish();
                onBackPressed();

            }
        });
    }

    private void saveDataToFirestore() {
        String email = etEmail.getText().toString().trim();
        String name = etName.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();

        if (email.isEmpty() || name.isEmpty() || phone.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Tạo đối tượng map để lưu trữ dữ liệu
        Map<String, Object> userData = new HashMap<>();
        userData.put("email", email);
        userData.put("name", name);
        userData.put("phoneNumber", phone);

        // Lưu dữ liệu vào Firestore
        String documentId = phone;  // Sử dụng số điện thoại làm ID cho tài liệu
        DocumentReference docRef = db.collection("users").document(documentId);
        docRef.set(userData)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(UpdateInfo_screen.this, "User info updated successfully", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(UpdateInfo_screen.this, "Failed to update user info", Toast.LENGTH_SHORT).show();
                });
    }
}