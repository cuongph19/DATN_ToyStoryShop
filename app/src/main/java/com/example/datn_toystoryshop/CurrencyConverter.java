package com.example.datn_toystoryshop;

import java.text.NumberFormat;
import java.util.Locale;

public class CurrencyConverter {
    // Tỷ giá mẫu (có thể cập nhật từ API sau này)
    private static final double EXCHANGE_RATE_USD = 0.00004; // 1 VND = 0.00004 USD
    private static final double EXCHANGE_RATE_CNY = 0.00028; // 1 VND = 0.00028 CNY
    private static final double EXCHANGE_RATE_JPY = 0.006;   // 1 VND = 0.006 JPY

    /**
     * Chuyển đổi giá trị tiền từ VND sang đơn vị tiền khác
     * @param value Giá trị gốc (VND)
     * @param currency Loại tiền (e.g., "USD", "CNY", "JPY", "VND")
     * @return Giá trị đã chuyển đổi
     */
    public static double convertCurrency(double value, String currency) {
        switch (currency) {
            case "USD":
                return value * EXCHANGE_RATE_USD;
            case "CNY":
                return value * EXCHANGE_RATE_CNY;
            case "JPY":
                return value * EXCHANGE_RATE_JPY;
            default: // Mặc định là VND
                return value;
        }
    }

    /**
     * Định dạng giá trị tiền tệ cho hiển thị
     * @param value Giá trị tiền đã chuyển đổi
     * @param currency Loại tiền (e.g., "USD", "CNY", "JPY", "VND")
     * @return Chuỗi định dạng tiền tệ
     */
    public static String formatCurrency(double value, String currency) {
        if (currency.equals("VND")) {
            // Định dạng tiền VND
            NumberFormat numberFormat = NumberFormat.getNumberInstance(new Locale("vi", "VN"));
            return numberFormat.format(value) + "đ";
        } else {
            // Định dạng tiền cho các loại khác
            return String.format(Locale.getDefault(), "%.2f %s", value, currency);
        }
    }

    /**
     * Chuyển đổi và định dạng giá trị tiền
     * @param baseValue Giá trị gốc (VND)
     * @param currency Loại tiền cần chuyển đổi
     * @return Chuỗi định dạng tiền tệ
     */
    public static String convertAndFormat(double baseValue, String currency) {
        double convertedValue = convertCurrency(baseValue, currency);
        return formatCurrency(convertedValue, currency);
    }
}