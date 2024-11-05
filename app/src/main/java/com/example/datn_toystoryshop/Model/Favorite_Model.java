package com.example.datn_toystoryshop.Model;

public class Favorite_Model {
    private String _id;          // Mã ID của yeu thic
    private String prodId;           // Mã ID của sản phẩm
    private String cusId;   // id cua khach hang
    private String imgFavPl;        // hinh anh yeu thich

    public Favorite_Model(String _id, String prodId, String cusId, String imgFavPl) {
        this._id = _id;
        this.prodId = prodId;
        this.cusId = cusId;
        this.imgFavPl = imgFavPl;
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

    public String getCusId() {
        return cusId;
    }

    public void setCusId(String cusId) {
        this.cusId = cusId;
    }

    public String getImgFavPl() {
        return imgFavPl;
    }

    public void setImgFavPl(String imgFavPl) {
        this.imgFavPl = imgFavPl;
    }
}
