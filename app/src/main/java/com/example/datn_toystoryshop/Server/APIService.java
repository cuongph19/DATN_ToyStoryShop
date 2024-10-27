package com.example.datn_toystoryshop.Server;

import com.example.datn_toystoryshop.Model.Product_Model;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;

public interface APIService {

    String DOMAIN = "http://192.168.16.101:3000/";

    @GET("/api/list")
    Call<List<Product_Model>> getProducts();
}
