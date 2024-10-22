package com.example.datn_toystoryshop.Register_login;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
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
import com.google.firebase.auth.FirebaseAuth;

public class Forgot_pass extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private ImageView btnBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_pass);

        Button btnsenemail = findViewById(R.id.btnsenemail);
        EditText edmail = findViewById(R.id.edmail);
         btnBack = findViewById(R.id.btnBack); // Khởi tạo nút quay lại

        mAuth = FirebaseAuth.getInstance();

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Chuyển đến màn hình chính
                Intent intent = new Intent(Forgot_pass.this, SignIn_screen.class); // Thay HomeActivity bằng tên activity chính của bạn
                startActivity(intent);
                finish(); // Kết thúc activity hiện tại nếu không cần quay lại
            }
        });
        btnsenemail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = edmail.getText().toString();

                if (email.isEmpty()) {
                    Toast.makeText(Forgot_pass.this, getString(R.string.Toast_email), Toast.LENGTH_SHORT).show();
                    return;
                }

                mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(Forgot_pass.this,  getString(R.string.Toast_send) + email, Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(Forgot_pass.this,  getString(R.string.Toast_send_error), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
}
