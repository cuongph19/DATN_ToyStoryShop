package com.example.datn_toystoryshop.Model;

import com.google.gson.annotations.SerializedName;

public class Voucher {

    @SerializedName("quantity_voucher")  // Đảm bảo rằng tên trường trùng với tên trong MongoDB
    private String quantityVoucher;

    @SerializedName("price_reduced")
    private int priceReduced;

    @SerializedName("discount_code")
    private String discountCode;
    private boolean isSelected; // Thêm thuộc tính isSelected

    public Voucher(String quantityVoucher, int priceReduced, String discountCode, boolean isSelected) {
        this.quantityVoucher = quantityVoucher;
        this.priceReduced = priceReduced;
        this.discountCode = discountCode;
        this.isSelected = false;
    }

    public String getQuantityVoucher() {
        return quantityVoucher;
    }

    public void setQuantityVoucher(String quantityVoucher) {
        this.quantityVoucher = quantityVoucher;
    }

    public int getPriceReduced() {
        return priceReduced;
    }

    public void setPriceReduced(int priceReduced) {
        this.priceReduced = priceReduced;
    }

    public String getDiscountCode() {
        return discountCode;
    }

    public void setDiscountCode(String discountCode) {
        this.discountCode = discountCode;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}

