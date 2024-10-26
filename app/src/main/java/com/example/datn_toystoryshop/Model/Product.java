package com.example.datn_toystoryshop.Model;

public class Product {
    private String name;
    private String sku;
    private String price;
    private String status;
    private int imageResourceId;

    public Product(String name, String sku, String price, String status, int imageResourceId) {
        this.name = name;
        this.sku = sku;
        this.price = price;
        this.status = status;
        this.imageResourceId = imageResourceId;
    }

    public String getName() { return name; }
    public String getSku() { return sku; }
    public String getPrice() { return price; }
    public String getStatus() { return status; }
    public int getImageResourceId() { return imageResourceId; }

    public void setName(String name) {
        this.name = name;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setImageResourceId(int imageResourceId) {
        this.imageResourceId = imageResourceId;
    }
}

