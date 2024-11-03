package com.example.datn_toystoryshop.Server;

import com.example.datn_toystoryshop.Model.Brand;
import com.example.datn_toystoryshop.Model.Product_Model;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;

public interface APIService {



//    String BASE_URL  = "http://192.168.16.101:3000/";// cương
    String BASE_URL  = "http://192.168.1.15:3000/";// huy



    @GET("/api/list")
    Call<List<Product_Model>> getProducts();

    @GET("/api/new-arrivals")
    Call<List<Product_Model>> getNewArrivals();
    @GET("/api/limited")
    Call<List<Product_Model>> getLimited();
    @GET("/api/blind_box")
    Call<List<Product_Model>> getBlindBox();
    @GET("/api/figuring")
    Call<List<Product_Model>> getFiguring();
    @GET("/api/other")
    Call<List<Product_Model>> getOther();
    @GET("/api/art-story")
    Call<List<Product_Model>> getArtStory();
}
