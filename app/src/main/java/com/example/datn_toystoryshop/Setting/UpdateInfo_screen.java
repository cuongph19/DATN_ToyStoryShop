package com.example.datn_toystoryshop.Setting;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import com.example.datn_toystoryshop.Profile.Setting_screen;
import com.example.datn_toystoryshop.R;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;


public class UpdateInfo_screen extends AppCompatActivity {
    private EditText etEmail, etName, etPhone;
    private Button btnSave;
    private String documentId, phone, name, email,password;
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
         documentId = intent.getStringExtra("documentId");

        // Ghi log để kiểm tra dữ liệu nhận được
        Log.d("ChangePassword_screen", "ChangePassword_screenaaaaaaaaaaaaaaaaa: " + documentId);

            loadUserDataByDocumentId(documentId);



        // Xử lý sự kiện khi người dùng bấm nút lưu
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (documentId  != null) {
                    saveDataToFirestore(); // Lưu với documentId hiện có
                }
                Intent intent = new Intent(UpdateInfo_screen.this, Setting_screen.class);
                startActivity(intent);
                finish();

            }
        });
    }

    private void saveDataToFirestore() {
        email = etEmail.getText().toString().trim();
        name = etName.getText().toString().trim();
        phone = etPhone.getText().toString().trim();

        if (email.isEmpty() || name.isEmpty() || phone.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Tạo đối tượng map để lưu trữ dữ liệu
        Map<String, Object> userData = new HashMap<>();
        userData.put("email", email);
        userData.put("name", name);
        userData.put("phoneNumber", phone);
        userData.put("password", password);
        // Lưu dữ liệu vào Firestore
        DocumentReference docRef = db.collection("users").document(documentId);
        docRef.set(userData)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(UpdateInfo_screen.this, "User info updated successfully", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(UpdateInfo_screen.this, "Failed to update user info", Toast.LENGTH_SHORT).show();
                });
    }

    private void loadUserDataByDocumentId(String documentId) {
        DocumentReference docRef = db.collection("users").document(documentId);

        docRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    // Lấy tất cả dữ liệu từ tài liệu
                    name = document.getString("name");
                    phone = document.getString("phoneNumber");
                    email = document.getString("email");
                    password = document.getString("password");

                    etEmail.setText(email);
                    etName.setText(name);
                    etPhone.setText(phone);
                } else {
                    Log.d("UserData", "No such document");
                }
            } else {
                Log.w("UserData", "get failed with ", task.getException());
            }
        });
    }

}