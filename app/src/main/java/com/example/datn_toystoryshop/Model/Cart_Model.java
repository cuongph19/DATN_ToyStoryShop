package com.example.datn_toystoryshop.Model;

import java.util.List;

public class Cart_Model {
    private String _id;          // Mã ID của gio hang
    private String prodId;          // Mã ID của sản phẩm
    private int quantity;        // Số lượng sản phẩm
    private String cusId;      // id cua khach hang

    public Cart_Model(String _id, String prodId, int quantity, String cusId) {
        this._id = _id;
        this.prodId = prodId;
        this.quantity = quantity;
        this.cusId = cusId;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getProdId() {
        return prodId;
    }

    public void setProdId(String prodId) {
        this.prodId = prodId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getCusId() {
        return cusId;
    }

    public void setCusId(String cusId) {
        this.cusId = cusId;
    }
}
