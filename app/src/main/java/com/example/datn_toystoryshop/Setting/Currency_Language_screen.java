package com.example.datn_toystoryshop.Setting;

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
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.datn_toystoryshop.CurrencyConverter;
import com.example.datn_toystoryshop.Home_screen;
import com.example.datn_toystoryshop.R;

import java.util.Locale;

public class Currency_Language_screen extends AppCompatActivity {

    private static final String TAG = "CurrencyLanguageScreen";  // Tag để ghi log
    private static final String PREFS_NAME = "AppPreferences";   // Tên file SharedPreferences
    private static final String LANGUAGE_KEY = "current_language"; // Key để lưu ngôn ngữ hiện tại
    private static final String CURRENCY_KEY = "current_currency"; // Key lưu tiền tệ hiện tại

    private ImageView btnBack;
    private boolean isSpinnerInitial = true;  // Biến để kiểm soát lần đầu khởi tạo Spinner
    private String currentLanguage;  // Ngôn ngữ hiện tại
    private Spinner spinnerLanguages, spinnerCurrency;
    private TextView tvCurrencyValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_currency_language_screen);

        // Đọc ngôn ngữ và tiền tệ hiện tại từ SharedPreferences
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        currentLanguage = prefs.getString(LANGUAGE_KEY, Locale.getDefault().getLanguage());
        String currentCurrency = prefs.getString(CURRENCY_KEY, "VND"); // Giá trị mặc định là VND

        tvCurrencyValue = findViewById(R.id.tvCurrencyValue);
        btnBack = findViewById(R.id.btnBack); // Khởi tạo nút quay lại
        spinnerLanguages = findViewById(R.id.language_spinner);
        spinnerCurrency = findViewById(R.id.currency_spinner);

        // Cập nhật vị trí của Spinner dựa trên ngôn ngữ đã lưu
        setSpinnerLanguagePosition(currentLanguage);

        // Cập nhật vị trí của Spinner dựa trên tiền tệ đã lưu
        setSpinnerCurrencyPosition(currentCurrency);

        // Hiển thị giá trị mặc định
        updateCurrencyValue(currentCurrency);

        // Sự kiện quay lại
        btnBack.setOnClickListener(v -> onBackPressed());

        // Xử lý sự kiện chọn ngôn ngữ từ Spinner
        spinnerLanguages.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (isSpinnerInitial) {
                    isSpinnerInitial = false;  // Bỏ qua lần đầu khởi tạo Spinner
                    return;
                }

                String selectedLanguage;
                int currencyPosition;
                switch (position) {
                    case 0:
                        selectedLanguage = "vi";
                        currencyPosition = 0; // VND
                        break;
                    case 1:
                        selectedLanguage = "en";
                        currencyPosition = 1; // USD
                        break;
                    case 2:
                        selectedLanguage = "zh";
                        currencyPosition = 2; // CNY
                        break;
                    case 3:
                        selectedLanguage = "ja";
                        currencyPosition = 3; // JPY
                        break;
                    default:
                        return;
                }

                // So sánh ngôn ngữ đã chọn với ngôn ngữ hiện tại
                if (!selectedLanguage.equals(currentLanguage)) {
                    setLocale(selectedLanguage);
                }

                // Đặt giá trị mặc định cho Spinner tiền tệ
                spinnerCurrency.setSelection(currencyPosition);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Không làm gì khi không chọn
            }
        });

        // Xử lý sự kiện chọn tiền tệ từ Spinner
        spinnerCurrency.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedCurrency;
                switch (position) {
                    case 0:
                        selectedCurrency = "VND";
                        break;
                    case 1:
                        selectedCurrency = "USD";
                        break;
                    case 2:
                        selectedCurrency = "CNY";
                        break;
                    case 3:
                        selectedCurrency = "JPY";
                        break;
                    default:
                        return;
                }

                // Lưu loại tiền tệ mới vào SharedPreferences
                saveCurrency(selectedCurrency);

                // Cập nhật giá trị hiển thị tiền tệ
                updateCurrencyValue(selectedCurrency);


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Không làm gì khi không chọn
            }
        });
    }

    // Cập nhật giá trị tiền tệ hiển thị
    private void updateCurrencyValue(String currency) {
        double baseValue = 40000; // Giá trị gốc: 40.000 VND

        // Sử dụng CurrencyConverter để chuyển đổi và định dạng
        String formattedValue = CurrencyConverter.convertAndFormat(baseValue, currency);

        // Cập nhật giá trị hiển thị trong TextView
        tvCurrencyValue.setText(formattedValue);

    }

    // Đặt vị trí Spinner cho ngôn ngữ
    private void setSpinnerLanguagePosition(String language) {
        switch (language) {
            case "vi":
                spinnerLanguages.setSelection(0);
                break; // Tiếng Việt
            case "en":
                spinnerLanguages.setSelection(1);
                break; // English
            case "zh":
                spinnerLanguages.setSelection(2);
                break; // Tiếng Trung
            case "ja":
                spinnerLanguages.setSelection(3);
                break; // Tiếng Nhật
            default:
                spinnerLanguages.setSelection(0); // Mặc định là Tiếng Việt
                break;
        }
    }

    // Đặt vị trí Spinner cho tiền tệ
    private void setSpinnerCurrencyPosition(String currency) {
        switch (currency) {
            case "VND":
                spinnerCurrency.setSelection(0);
                break;
            case "USD":
                spinnerCurrency.setSelection(1);
                break;
            case "CNY":
                spinnerCurrency.setSelection(2);
                break;
            case "JPY":
                spinnerCurrency.setSelection(3);
                break;
            default:
                spinnerCurrency.setSelection(0); // Mặc định là VND
                break;
        }
    }

    // Lưu tiền tệ vào SharedPreferences
    private void saveCurrency(String currency) {
        SharedPreferences.Editor editor = getSharedPreferences(PREFS_NAME, MODE_PRIVATE).edit();
        editor.putString(CURRENCY_KEY, currency);
        editor.apply();
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
