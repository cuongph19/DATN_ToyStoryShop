package com.example.datn_toystoryshop.Register_login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.example.datn_toystoryshop.Home_screen;
import com.example.datn_toystoryshop.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class SignIn_screen extends AppCompatActivity {
    private TextInputEditText edInput, edPassword;
    private Button btnLogin, btnGoogleLogin;
    private TextView txtSignup, txtForgotPass;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private GoogleSignInClient mGoogleSignInClient;
    private static final int RC_SIGN_IN = 9001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        // Khởi tạo Firebase Auth và Firestore
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Cấu hình Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        // Khởi tạo các thành phần giao diện
        edInput = findViewById(R.id.edemailLg); // Nhập email hoặc số điện thoại
        edPassword = findViewById(R.id.edpasswordLg);
        btnLogin = findViewById(R.id.btnLogin);
        btnGoogleLogin = findViewById(R.id.btnGoogleLogin);
        txtSignup = findViewById(R.id.txtSignup);
        txtForgotPass = findViewById(R.id.txtForgotPass);
        //truyền dữ liệu từ ForgotOTP_screen
        Intent intent = getIntent();
        if (intent != null) {
            String phoneNumber = intent.getStringExtra("phoneNumber");
            String newPassword = intent.getStringExtra("newPassword");
            if (phoneNumber != null && phoneNumber.startsWith("+84")) {
                phoneNumber = phoneNumber.replaceFirst("\\+84", "0");
            }
            // Gán dữ liệu vào các TextInputEditText
            if (phoneNumber != null) {
                edInput.setText(phoneNumber);
            }
            if (newPassword != null) {
                edPassword.setText(newPassword);
            }
        }
        // Thiết lập OnClickListener cho nút đăng nhập Google
        btnGoogleLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signInWithGoogle();
            }
        });

        // Thiết lập OnClickListener cho nút đăng nhập
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String input = edInput.getText().toString().trim();
                String password = edPassword.getText().toString().trim();
                if (input.isEmpty()) {
                    Toast.makeText(SignIn_screen.this, getString(R.string.Toast_infor), Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!isPasswordValid(password)) {
                    Toast.makeText(SignIn_screen.this, getString(R.string.Toast_pass), Toast.LENGTH_SHORT).show();
                    return;
                }

                    if (isPhoneNumber(input)) {
                        // Đăng nhập bằng số điện thoại
                        loginWithPhone(input, password);
                    } else if (isValidEmail(input)) {
                        // Đăng nhập bằng email
                        loginWithEmail(input, password);
                    } else {
                        Toast.makeText(SignIn_screen.this, getString(R.string.Toast_format), Toast.LENGTH_SHORT).show();
                    }
            }
        });

        // Thiết lập OnClickListener cho nút đăng ký
        txtSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(SignIn_screen.this, SignUp_screen.class);
                startActivity(in);
            }
        });

        // Thiết lập OnClickListener cho nút quên mật khẩu
        txtForgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(SignIn_screen.this, Forgot_pass.class);
                startActivity(in);
            }
        });
    }

    // Phương thức xử lý đăng nhập Google
    private void signInWithGoogle() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    // Xử lý kết quả đăng nhập Google
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    // Xử lý kết quả đăng nhập Google
    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            // Đăng nhập thành công, sử dụng token
            firebaseAuthWithGoogle(account);
        } catch (ApiException e) {
            Toast.makeText(this, getString(R.string.Toast_google), Toast.LENGTH_SHORT).show();
        }
    }

    // Đăng nhập với Firebase bằng token từ Google
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            // Lấy email từ tài khoản Google
                            String gmail = acct.getEmail();

                            Toast.makeText(SignIn_screen.this, getString(R.string.Toast_success), Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(SignIn_screen.this, Home_screen.class);
                            intent.putExtra("gmail", gmail);
                            startActivity(intent);
                        } else {
                            Toast.makeText(SignIn_screen.this, getString(R.string.Toast_wrong), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    // Đăng nhập bằng số điện thoại
    private void loginWithPhone(String phoneNumber, String password) {
        // Định dạng lại số điện thoại nếu cần
        if (!phoneNumber.startsWith("+84")) {
            phoneNumber = "+84" + phoneNumber.substring(1); // Bỏ số 0 đầu và thêm +84
        }

        String finalPhoneNumber = phoneNumber;
        db.collection("users").document(phoneNumber)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            String storedPassword = document.getString("password");
                            if (storedPassword != null && storedPassword.equals(password)) {
                                // Đăng nhập thành công
                                Toast.makeText(SignIn_screen.this, getString(R.string.Toast_success), Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(SignIn_screen.this, Home_screen.class);
                                intent.putExtra("phoneNumber", finalPhoneNumber);
                                startActivity(intent);
                            } else {
                                Toast.makeText(SignIn_screen.this, getString(R.string.Toast_wrong_password), Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(SignIn_screen.this, getString(R.string.Toast_wrong_sdt), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    // Đăng nhập bằng email
    private void loginWithEmail(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(SignIn_screen.this, getString(R.string.Toast_success), Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(SignIn_screen.this, Home_screen.class);
                            intent.putExtra("email", email);
                            startActivity(intent);
                        } else {
                            Toast.makeText(SignIn_screen.this, getString(R.string.Toast_failure), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    // Kiểm tra số điện thoại hợp lệ
    private boolean isPhoneNumber(String input) {
        return input.matches("^0[3|5|7|8|9][0-9]{8}$"); // Kiểm tra định dạng số điện thoại
    }

    // Kiểm tra email hợp lệ
    private boolean isValidEmail(String input) {
        return Patterns.EMAIL_ADDRESS.matcher(input).matches(); // Sử dụng Patterns để kiểm tra email
    }
    private boolean isPasswordValid(String password) {
        return password.length() >= 6 && password.chars().anyMatch(Character::isUpperCase);
    }
}
