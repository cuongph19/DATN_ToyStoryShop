package com.example.datn_toystoryshop.Setting;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.datn_toystoryshop.Profile.Setting_screen;
import com.example.datn_toystoryshop.R;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Map;

public class ChangePassword_screen extends AppCompatActivity {

    private EditText etpass;
    private Button btnSave;
    private String documentId, phone, name, email,password;
    private FirebaseFirestore db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password_screen);
        etpass = findViewById(R.id.et_pass);
        btnSave = findViewById(R.id.btn_save);

        // Khởi tạo Firestore
        db = FirebaseFirestore.getInstance();

        // Lấy dữ liệu từ Intent
        Intent intent = getIntent();
        documentId = intent.getStringExtra("documentId");
        // Kiểm tra documentId có null không
        if (documentId == null) {
            Toast.makeText(this, "Document ID is null. Cannot load user data.", Toast.LENGTH_SHORT).show();
            finish(); // Quay lại màn hình trước đó
            return; // Dừng thực thi còn lại
        }
        // Ghi log để kiểm tra dữ liệu nhận được
        Log.d("UpdateInfo_screen", "Document ID: " + documentId);

        loadUserDataByDocumentId(documentId);

        // Xử lý sự kiện khi người dùng bấm nút lưu
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnSave.setEnabled(false); // Vô hiệu hóa nút để ngăn người dùng nhấn lại
                if (documentId != null) {
                    saveDataToFirestore(); // Lưu với documentId hiện có
                    finish();
                } else {
                    Toast.makeText(ChangePassword_screen.this, "Document ID is null", Toast.LENGTH_SHORT).show();
                    btnSave.setEnabled(true); // Kích hoạt lại nút nếu không có Document ID
                }
                Intent intent = new Intent(ChangePassword_screen.this, Setting_screen.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                finish();


            }
        });
    }

    private void saveDataToFirestore() {
        password = etpass.getText().toString().trim();

        if (password.isEmpty()) {
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
                    Toast.makeText(ChangePassword_screen.this, "User info updated successfully", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(ChangePassword_screen.this, Setting_screen.class);
                    intent.putExtra("documentId", documentId); // Đảm bảo truyền documentId
                    startActivity(intent);
                    finish(); // Kết thúc UpdateInfo_screen
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(ChangePassword_screen.this, "Failed to update user info", Toast.LENGTH_SHORT).show();
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

                    etpass.setText(password);
                } else {
                    Log.d("UserData", "No such document");
                }
            } else {
                Log.w("UserData", "get failed with ", task.getException());
            }
        });
    }

}