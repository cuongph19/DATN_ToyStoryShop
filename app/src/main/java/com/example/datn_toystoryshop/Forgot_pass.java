package com.example.datn_toystoryshop;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class Forgot_pass extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_pass);

        Button btnsenemail = findViewById(R.id.btnsenemail);
        EditText edmail = findViewById(R.id.edmail);
        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        btnsenemail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = edmail.getText().toString();
                if (email.isEmpty()){
                    Toast.makeText(Forgot_pass.this, "Email không được bỏ trống!", Toast.LENGTH_SHORT).show();
                    return;
                }

                mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(Forgot_pass.this, "Chúng tôi đã gửi mail đến hộp thư của bạn để đổi mật khẩu!" + email, Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(Forgot_pass.this, "Không thể gửi mail. Hãy kiểm tra lại địa chỉ email.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });


            }
        });
    }
}