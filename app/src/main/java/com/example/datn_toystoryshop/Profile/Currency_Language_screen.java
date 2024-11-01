package com.example.datn_toystoryshop.Profile;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Spinner;
import androidx.appcompat.app.AppCompatActivity;
import com.example.datn_toystoryshop.Home_screen;
import com.example.datn_toystoryshop.R;
import java.util.Locale;

public class Currency_Language_screen extends AppCompatActivity {

    private static final String TAG = "CurrencyLanguageScreen";  // Tag để ghi log
    private static final String PREFS_NAME = "AppPreferences";   // Tên file SharedPreferences
    private static final String LANGUAGE_KEY = "current_language"; // Key để lưu ngôn ngữ hiện tại
    private ImageView btnBack;
    private boolean isSpinnerInitial = true;  // Biến để kiểm soát lần đầu khởi tạo Spinner
    private String currentLanguage;  // Ngôn ngữ hiện tại

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_currency_language_screen);

        // Đọc ngôn ngữ hiện tại từ SharedPreferences
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        currentLanguage = prefs.getString(LANGUAGE_KEY, Locale.getDefault().getLanguage());
        btnBack = findViewById(R.id.btnBack); // Khởi tạo nút quay lại
        Spinner spinnerLanguages = findViewById(R.id.language_spinner);

        // Cập nhật vị trí của Spinner dựa trên ngôn ngữ đã lưu
        switch (currentLanguage) {
            case "vi":
                spinnerLanguages.setSelection(0); // Tiếng Việt
                break;
            case "en":
                spinnerLanguages.setSelection(1); // English
                break;
            case "zh":
                spinnerLanguages.setSelection(2); // Tiếng Trung
                break;
            case "ja":
                spinnerLanguages.setSelection(3); // Tiếng Nhật
                break;
            default:
                spinnerLanguages.setSelection(0); // Mặc định là Tiếng Việt
                break;
        }

        // Sự kiện quay lại
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onBackPressed();
            }
        });

        // Xử lý sự kiện chọn ngôn ngữ từ Spinner
        spinnerLanguages.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (isSpinnerInitial) {
                    isSpinnerInitial = false;  // Bỏ qua lần đầu khởi tạo Spinner
                    return;
                }

                String selectedLanguage;
                switch (position) {
                    case 0:
                        selectedLanguage = "vi";
                        break;
                    case 1:
                        selectedLanguage = "en";
                        break;
                    case 2:
                        selectedLanguage = "zh";
                        break;
                    case 3:
                        selectedLanguage = "ja";
                        break;
                    default:
                        return;
                }

                // So sánh ngôn ngữ đã chọn với ngôn ngữ hiện tại
                if (!selectedLanguage.equals(currentLanguage)) {
                    setLocale(selectedLanguage);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Không làm gì khi không chọn
            }
        });
    }

    // Phương thức thay đổi ngôn ngữ
    private void setLocale(String lang) {
        Log.d(TAG, "Setting locale: " + lang);  // Log ngôn ngữ được chọn

        try {
            Locale locale = new Locale(lang);
            Locale.setDefault(locale);
            Resources resources = getResources();
            Configuration config = new Configuration(resources.getConfiguration());
            config.setLocale(locale);
            DisplayMetrics dm = resources.getDisplayMetrics();
            resources.updateConfiguration(config, dm);

            // Cập nhật ngôn ngữ hiện tại trong SharedPreferences
            SharedPreferences.Editor editor = getSharedPreferences(PREFS_NAME, MODE_PRIVATE).edit();
            editor.putString(LANGUAGE_KEY, lang);
            editor.apply();

            Log.d(TAG, "Locale updated, reloading activity");  // Log sau khi thay đổi ngôn ngữ thành công

            // Invalidate the menu to reload it with new language
            invalidateOptionsMenu();  // Buộc hệ thống tạo lại menu với ngôn ngữ mới

            // Reload lại Activity để cập nhật ngôn ngữ
            recreate();

        } catch (Exception e) {
            Log.e(TAG, "Error setting locale", e);  // Log lỗi nếu có exception
        }
    }
    @Override
    public void onBackPressed() {
        // Tạo Intent để trở về Home_screen
        super.onBackPressed();
        Intent intent = new Intent(Currency_Language_screen.this, Home_screen.class);
        startActivity(intent);
        finish(); // Kết thúc activity hiện tại
    }
}
