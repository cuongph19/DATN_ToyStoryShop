package com.example.datn_toystoryshop.Model;

import java.util.List;

public class Product_Model {

    private String _id;          // Mã ID của sản phẩm
    private int prodId;          // Mã sản phẩm
    private int owerId;          // ID chủ sở hữu
    private boolean statusPro;   // Tồn kho (true nếu còn hàng)
    private double price;        // Giá của sản phẩm
    private String desPro;       // Mô tả sản phẩm
    private String creatDatePro; // Ngày tạo sản phẩm
    private int quantity;        // Số lượng sản phẩm
    private String listPro;      // Danh sách trạng thái sản phẩm
    private List<String> imgPro;       // URL hình ảnh của sản phẩm
    private String namePro;      // Tên sản phẩm
    private int cateId;          // ID của danh mục sản phẩm

    public Product_Model(String _id, int prodId, int owerId, boolean statusPro, double price,
                         String desPro, String creatDatePro, int quantity, String listPro,
                         List<String> imgPro, String namePro, int cateId) {
        this._id = _id;
        this.prodId = prodId;
        this.owerId = owerId;
        this.statusPro = statusPro;
        this.price = price;
        this.desPro = desPro;
        this.creatDatePro = creatDatePro;
        this.quantity = quantity;
        this.listPro = listPro;
        this.imgPro = imgPro;
        this.namePro = namePro;
        this.cateId = cateId;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public int getProdId() {
        return prodId;
    }

    public void setProdId(int prodId) {
        this.prodId = prodId;
    }

    public int getOwerId() {
        return owerId;
    }

    public void setOwerId(int owerId) {
        this.owerId = owerId;
    }

    public boolean isStatusPro() {
        return statusPro;
    }

    public void setStatusPro(boolean statusPro) {
        this.statusPro = statusPro;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getDesPro() {
        return desPro;
    }

    public void setDesPro(String desPro) {
        this.desPro = desPro;
    }

    public String getCreatDatePro() {
        return creatDatePro;
    }

    public void setCreatDatePro(String creatDatePro) {
        this.creatDatePro = creatDatePro;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getListPro() {
        return listPro;
    }

    public void setListPro(String listPro) {
        this.listPro = listPro;
    }

    public List<String> getImgPro() {
        return imgPro;
    }

    public void setImgPro(List<String> imgPro) {
        this.imgPro = imgPro;
    }

    public String getNamePro() {
        return namePro;
    }

    public void setNamePro(String namePro) {
        this.namePro = namePro;
    }

    public int getCateId() {
        return cateId;
    }

    public void setCateId(int cateId) {
        this.cateId = cateId;
    }
}

