package com.example.datn_toystoryshop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class PhoneNumber_screen extends AppCompatActivity {
    private EditText edtPhoneNumber;
    private Button btnGetOTP;
    private FirebaseAuth mAuth;
    private String verificationId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_number);

        mAuth = FirebaseAuth.getInstance();
        edtPhoneNumber = findViewById(R.id.edtPhoneNumber);
        btnGetOTP = findViewById(R.id.btnGetOTP);

        btnGetOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phoneNumber = edtPhoneNumber.getText().toString().trim();
                if (!phoneNumber.startsWith("+84")) {
                    phoneNumber = "+84" + phoneNumber.substring(1);
                }
                requestOTP(phoneNumber);
            }
        });
    }

    private void requestOTP(String phoneNumber) {
        PhoneAuthOptions options = PhoneAuthOptions.newBuilder(mAuth)
                .setPhoneNumber(phoneNumber)
                .setTimeout(60L, TimeUnit.SECONDS)
                .setActivity(this)
                .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                        // Xác thực thành công
                        signInWithPhoneAuthCredential(phoneAuthCredential);
                    }

                    @Override
                    public void onVerificationFailed(@NonNull FirebaseException e) {
                        Toast.makeText(PhoneNumber_screen.this, "Xác thực thất bại: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                        super.onCodeSent(s, forceResendingToken);
                        verificationId = s;
                        Intent intent = new Intent(PhoneNumber_screen.this, PhoneOTP_screen.class);
                        intent.putExtra("verificationId", verificationId);
                        intent.putExtra("email", getIntent().getStringExtra("email"));
                        startActivity(intent);
                    }
                }).build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Intent intent = new Intent(PhoneNumber_screen.this, Home_screen.class);
                startActivity(intent);
            } else {
                Toast.makeText(PhoneNumber_screen.this, "Xác thực OTP thất bại!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
