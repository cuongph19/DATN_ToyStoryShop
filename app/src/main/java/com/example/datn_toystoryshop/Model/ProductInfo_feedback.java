package com.example.datn_toystoryshop.Model;

import java.util.List;

public class ProductInfo_feedback {
    private List<String> imgPro;
    private String namePro;
    private String brand;

    // Getter và Setter
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

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }
}