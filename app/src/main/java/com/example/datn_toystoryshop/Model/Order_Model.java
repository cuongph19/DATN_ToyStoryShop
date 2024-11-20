package com.example.datn_toystoryshop.Model;

import java.util.List;

public class Order_Model {
    private String _id;          // Mã ID của đơn hàng
    private String cusId;        // ID khách hàng
    private int revenue_all;        // ID khách hàng
    private List<ProductDetail> prodDetails; // Danh sách các sản phẩm trong đơn hàng
    private String content;      // Nội dung đơn hàng
    private String orderStatus;  // Trạng thái đơn hàng
    private String orderDate;    // Ngày đặt hàng

    // Lớp phụ để lưu thông tin chi tiết từng sản phẩm
    public static class ProductDetail {
        private String prodId;   // ID sản phẩm
        private double revenue;  // Doanh thu sản phẩm
        private int quantity;
        private String prodSpecification;

        public ProductDetail(String prodId, double revenue, int quantity, String prodSpecification) {
            this.prodId = prodId;
            this.revenue = revenue;
            this.quantity = quantity;
            this.prodSpecification = prodSpecification;
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

        public int getQuantity() {
            return quantity;
        }

        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }

        public String getProdSpecification() {
            return prodSpecification;
        }

        public void setProdSpecification(String prodSpecification) {
            this.prodSpecification = prodSpecification;
        }
    }

    // Constructor mặc định
    public Order_Model() {
    }

    // Constructor với tất cả các tham số

    public Order_Model(String _id, String cusId, int revenue_all, List<ProductDetail> prodDetails, String content, String orderStatus, String orderDate) {
        this._id = _id;
        this.cusId = cusId;
        this.revenue_all = revenue_all;
        this.prodDetails = prodDetails;
        this.content = content;
        this.orderStatus = orderStatus;
        this.orderDate = orderDate;
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

    public int getRevenue_all() {
        return revenue_all;
    }

    public void setRevenue_all(int revenue_all) {
        this.revenue_all = revenue_all;
    }

    public List<ProductDetail> getProdDetails() {
        return prodDetails;
    }

    public void setProdDetails(List<ProductDetail> prodDetails) {
        this.prodDetails = prodDetails;
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

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }
}
