package com.example.datn_toystoryshop.Model;

import com.google.gson.annotations.SerializedName;

public class Address {
    @SerializedName("_id")
    private String _id;          // _id từ MongoDB
    private String userId;      // userId từ Firebase (nếu có)
    private String name;        // tên người nhận
    private String phone;       // số điện thoại
    private String address;     // địa chỉ chính
    private String addressDetail; // chi tiết địa chỉ (số nhà, tầng...)
    private boolean isDefault;

    public Address() {
    }

    public Address(String name, String phone, String address, String addressDetail, boolean isDefault) {
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.addressDetail = addressDetail;
        this.isDefault = isDefault;
    }

    public Address(String _id, String userId, String name, String phone, String address, String addressDetail, boolean isDefault) {
        this._id = _id;
        this.userId = userId;
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.addressDetail = addressDetail;
        this.isDefault = isDefault;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddressDetail() {
        return addressDetail;
    }

    public void setAddressDetail(String addressDetail) {
        this.addressDetail = addressDetail;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public void setDefault(boolean aDefault) {
        isDefault = aDefault;
    }
}
