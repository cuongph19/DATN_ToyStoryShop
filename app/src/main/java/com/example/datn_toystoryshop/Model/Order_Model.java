package com.example.datn_toystoryshop.Model;

public class Order_Model {
    private String _id;          // Mã ID của yeu thic
    private String cusId;
    private String prodId;
    private double revenue;
    private String content;
    private String orderStatus;
    private String dateFeed;

    public Order_Model(String _id, String cusId, String prodId, double revenue, String content, String orderStatus, String dateFeed) {
        this._id = _id;
        this.cusId = cusId;
        this.prodId = prodId;
        this.revenue = revenue;
        this.content = content;
        this.orderStatus = orderStatus;
        this.dateFeed = dateFeed;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getCusId() {
        return cusId;
    }

    public void setCusId(String cusId) {
        this.cusId = cusId;
    }

    public String getProdId() {
        return prodId;
    }

    public void setProdId(String prodId) {
        this.prodId = prodId;
    }

    public double getRevenue() {
        return revenue;
    }

    public void setRevenue(double revenue) {
        this.revenue = revenue;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getDateFeed() {
        return dateFeed;
    }

    public void setDateFeed(String dateFeed) {
        this.dateFeed = dateFeed;
    }
}
