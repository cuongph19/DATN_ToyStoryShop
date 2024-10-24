package com.example.datn_toystoryshop.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.example.datn_toystoryshop.Profile.Setting_screen;
import com.example.datn_toystoryshop.Profile.Terms_Conditions_screen;
import com.example.datn_toystoryshop.R;
import com.example.datn_toystoryshop.Register_login.SignIn_screen;

public class Profile_Fragment extends Fragment {

    private TextView tvSettings, tvLanguageCurrency, tvRate, tvIntroduceFriend, tvTerms, tvLogout, tvmail, tvname, tvphone;
    private ImageView ivAvatar;

    public void ProfileFragment() {
        // Constructor mặc định
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        // Khởi tạo các view từ layout
        ivAvatar = view.findViewById(R.id.iv_avatar);
        tvname = view.findViewById(R.id.tv_user_name);
          tvphone = view.findViewById(R.id.tv_phone_number);
        tvmail = view.findViewById(R.id.tv_email);

        tvSettings = view.findViewById(R.id.tv_settings);
        tvLanguageCurrency = view.findViewById(R.id.tv_languagecurrency);
        tvRate = view.findViewById(R.id.tv_rate);
        tvIntroduceFriend = view.findViewById(R.id.tv_introducefriend);
        tvTerms = view.findViewById(R.id.tv_terms);
        tvLogout = view.findViewById(R.id.tv_logout);

        Bundle bundle = getArguments();
        if (bundle != null) {
            String phoneNumber = bundle.getString("phoneNumber");
            String email = bundle.getString("email");
            String gmail = bundle.getString("gmail");
            // Kiểm tra và hiển thị số điện thoại nếu không null
            if (phoneNumber != null) {




                //////////////////////////code lỗi//////////////////////////////////////
                Toast.makeText(getActivity(), "Phone number: " + phoneNumber, Toast.LENGTH_SHORT).show();
                tvphone.setText(phoneNumber);
                Log.d("ProfileFragment", "Phone number set: " + tvphone.getText().toString());
                Toast.makeText(getActivity(), "Phone set: " + tvphone.getText().toString(), Toast.LENGTH_SHORT).show();
                //////////////////////////code lỗi//////////////////////////////////////

            }

            // Kiểm tra và hiển thị email nếu không null
            if (email != null) {
                Toast.makeText(getActivity(), "Email: " + email, Toast.LENGTH_SHORT).show();
                tvmail.setText(email);
            } else {
                tvmail.setText("");
            }
            if (gmail != null) {
                Toast.makeText(getActivity(), "gmail: " + gmail, Toast.LENGTH_SHORT).show();
                Log.d("gmail", "Gmail3: " + gmail);
                tvmail.setText(gmail);
            } else {
                tvmail.setText("");
            }
        }

        // Xử lý sự kiện cho mục "Cài đặt"
        tvSettings.setOnClickListener(v -> {
            // Chuyển tới màn hình cài đặt
            Intent intent = new Intent(getActivity(), Setting_screen.class);
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

        // Xử lý sự kiện cho mục "Đăng xuất"
        tvLogout.setOnClickListener(v -> {
            // Đăng xuất người dùng
            Toast.makeText(getActivity(), getString(R.string.sign_out_success_pro), Toast.LENGTH_SHORT).show();
            // Chuyển tới màn hình đăng nhập
            Intent intent = new Intent(getActivity(), SignIn_screen.class);
            startActivity(intent);
            getActivity().finish(); // Đóng tất cả các Activity hiện tại
        });
        return view;

    }
}