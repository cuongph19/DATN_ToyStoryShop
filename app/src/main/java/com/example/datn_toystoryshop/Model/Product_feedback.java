package com.example.datn_toystoryshop.Model;

public class Product_feedback {
    private String orderId;
    private ProdDetails_feedback prodDetails;
    private ProductInfo_feedback productInfo;

    public Product_feedback(String orderId, ProdDetails_feedback prodDetails, ProductInfo_feedback productInfo) {
        this.orderId = orderId;
        this.prodDetails = prodDetails;
        this.productInfo = productInfo;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public ProdDetails_feedback getProdDetails() {
        return prodDetails;
    }

    public void setProdDetails(ProdDetails_feedback prodDetails) {
        this.prodDetails = prodDetails;
    }

    public ProductInfo_feedback getProductInfo() {
        return productInfo;
    }

    public void setProductInfo(ProductInfo_feedback productInfo) {
        this.productInfo = productInfo;
    }
}