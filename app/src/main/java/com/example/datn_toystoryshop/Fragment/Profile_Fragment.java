package com.example.datn_toystoryshop.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.datn_toystoryshop.Profile.Currency_Language_screen;
import com.example.datn_toystoryshop.Profile.Evaluate_screen;
import com.example.datn_toystoryshop.Profile.Introduce_Friends_screen;
import com.example.datn_toystoryshop.Profile.Privacy_Security_screen;
import com.example.datn_toystoryshop.Profile.Setting_screen;
import com.example.datn_toystoryshop.Profile.Terms_Conditions_screen;
import com.example.datn_toystoryshop.R;
import com.example.datn_toystoryshop.Register_login.SignIn_screen;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.QuerySnapshot;

public class Profile_Fragment extends Fragment {

    private TextView tvSettings, tvLanguageCurrency, tvRate, tvIntroduceFriend, tvTerms, tvLogout, tvname, tvtvinformation,tvPrivacySecurity;
    private ImageView ivAvatar;
    private FirebaseFirestore db;

    private String gmail, email, name, password, phoneNumber;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = FirebaseFirestore.getInstance();
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("Settings", Context.MODE_PRIVATE);
        boolean nightMode = sharedPreferences.getBoolean("night", false);
        if (nightMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
        // Khởi tạo các view từ layout
        ivAvatar = view.findViewById(R.id.iv_avatar);
        tvname = view.findViewById(R.id.tv_user_name);
        tvtvinformation = view.findViewById(R.id.tv_information);
        tvSettings = view.findViewById(R.id.tv_settings);
        tvLanguageCurrency = view.findViewById(R.id.tv_languagecurrency);
        tvRate = view.findViewById(R.id.tv_rate);
        tvIntroduceFriend = view.findViewById(R.id.tv_introducefriend);
        tvTerms = view.findViewById(R.id.tv_terms);
        tvPrivacySecurity = view.findViewById(R.id.tv_privacy_security);
        tvLogout = view.findViewById(R.id.tv_logout);

        Bundle bundle = getArguments();
        if (bundle != null) {
             phoneNumber = bundle.getString("phoneNumber");
             email = bundle.getString("email");
             gmail = bundle.getString("gmail");

            if (phoneNumber != null && !phoneNumber.isEmpty()) {
                Log.d("Profile_Fragment", "Phone: " + tvtvinformation.getText().toString());
                tvtvinformation.setText(phoneNumber);
                loadUserDataPhone(phoneNumber);
            }
            if (email != null) {
                Log.d("Profile_Fragment", "Email: " + email);
                tvtvinformation.setText(email);
                loadUserDataEmail(email);
            }
            if (gmail != null) {
                Log.d("Profile_Fragment", "gmail: " + gmail);
                tvtvinformation.setText(gmail);
            }

        }
        // Xử lý sự kiện cho mục "Cài đặt"
        tvSettings.setOnClickListener(v -> {
            // Chuyển tới màn hình cài đặt
            Intent intent = new Intent(getActivity(), Setting_screen.class);
            intent.putExtra("gmail", gmail);
            intent.putExtra("email", email);
            intent.putExtra("name", name);
            intent.putExtra("password", password);
            intent.putExtra("phoneNumber", phoneNumber);

            startActivity(intent);
        });

        // Xử lý sự kiện cho mục "Ngôn ngữ & tiền tệ"
        tvLanguageCurrency.setOnClickListener(v -> {
            // Chuyển tới màn hình ngôn ngữ và tiền tệ
            Intent intent = new Intent(getActivity(), Currency_Language_screen.class);
            startActivity(intent);
        });

        // Xử lý sự kiện cho mục "Đánh giá"
        tvRate.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), Evaluate_screen.class);
            startActivity(intent);
        });

        // Xử lý sự kiện cho mục "Giới thiệu bạn bè"
        tvIntroduceFriend.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), Introduce_Friends_screen.class);
            startActivity(intent);
        });

        // Xử lý sự kiện cho mục "Điều khoản & Điều kiện"
        tvTerms.setOnClickListener(v -> {
            // Chuyển tới trang điều khoản và điều kiện
            Intent intent = new Intent(getActivity(), Terms_Conditions_screen.class);
            startActivity(intent);
        });
        // Xử lý sự kiện cho mục "Quyền riêng tư &amp; Bảo mật"
        tvPrivacySecurity.setOnClickListener(v -> {
            // Chuyển tới trang điều khoản và điều kiện
            Intent intent = new Intent(getActivity(), Privacy_Security_screen.class);
            startActivity(intent);
        });

        // Xử lý sự kiện cho mục "Đăng xuất"
        tvLogout.setOnClickListener(v -> {
            // Đăng xuất người dùng
            Toast.makeText(getActivity(), getString(R.string.sign_out_success_pro), Toast.LENGTH_SHORT).show();
            SharedPreferences sharedP = requireActivity().getSharedPreferences("Settings", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedP.edit();
            editor.putBoolean("notificationShown", false); // Đặt lại trạng thái thông báo
            editor.apply();
            // Chuyển tới màn hình đăng nhập
            Intent intent = new Intent(getActivity(), SignIn_screen.class);
            startActivity(intent);
            getActivity().finish(); // Đóng tất cả các Activity hiện tại
        });
        return view;

    }

    private void loadUserDataPhone(String phoneNumber) {
        DocumentReference docRef = db.collection("users").document(phoneNumber);

        docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w("Profile_Fragment", "Listen failed.", e);
                    return;
                }

                if (snapshot != null && snapshot.exists()) {
                     email = snapshot.getString("email");
                     name = snapshot.getString("name");
                     password = snapshot.getString("password");

                    tvname.setText(name);

                } else {
                    Log.d("Profile_Fragment", "No such document");
                }
            }
        });
    }
    private void loadUserDataEmail(String email) {
        db.collection("users")
                .whereEqualTo("email", email)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot snapshots, @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.w("Profile_Fragment", "Listen failed.", e);
                            return;
                        }

                        if (snapshots != null && !snapshots.isEmpty()) {
                            DocumentSnapshot snapshot = snapshots.getDocuments().get(0);
                            name = snapshot.getString("name");
                             password = snapshot.getString("password");
                             phoneNumber = snapshot.getString("phoneNumber");

                            tvname.setText(name);

                        } else {
                            Log.d("Profile_Fragment", "No such document with the specified email");
                        }
                    }
                });
    }
}