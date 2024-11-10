package com.example.datn_toystoryshop.Server;

import com.example.datn_toystoryshop.Model.ArtStoryModel;
import com.example.datn_toystoryshop.Model.Cart_Model;
import com.example.datn_toystoryshop.Model.Favorite_Model;
import com.example.datn_toystoryshop.Model.FeebackApp_Model;
import com.example.datn_toystoryshop.Model.Feeback_Model;
import com.example.datn_toystoryshop.Model.Order_Model;
import com.example.datn_toystoryshop.Model.Product_Model;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface APIService {


        String BASE_URL  = "http://192.168.96.248:3000/";// cương
   //   String BASE_URL  = "http://192.168.1.10:3000/";// huy
   // String BASE_URL = "http://192.168.101.10:3000/";


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

    ///
    @GET("/api/favorites")
    Call<List<Favorite_Model>> getFavorites();

    @POST("/api/add/add-to-favorites")
    Call<Favorite_Model> addToFavorites(@Body Favorite_Model favoriteModel);

    @GET("/api/{prodId}")
    Call<Product_Model> getProductById(@Path("prodId") String prodId);

    @DELETE("/api/delete/{id}")
    Call<Void> deleteFavorite(@Path("id") String productId);

    @GET("api/list-popular")
    Call<List<Product_Model>> getPopular();
    @GET("/api/check-favorite/{prodId}")
    Call<Map<String, Boolean>> checkFavorite(@Path("prodId") String productId);

    @POST("/api/add/add-to-app-feeback")
    Call<FeebackApp_Model> addToFeebackApp(@Body FeebackApp_Model feebackAppModel);

    @POST("/api/add/add-to-feeback")
    Call<Feeback_Model> addToFeeback(@Body Feeback_Model feebackModel);

    @POST("/api/add/add-to-cart")
    Call<Cart_Model> addToCart(@Body Cart_Model cartModel);

    @POST("/api/add/add-to-order")
    Call<Order_Model> addToOrder(@Body Order_Model orderModel);

    @GET("/api/carts")
    Call<List<Cart_Model>> getCarts();

    @GET("/api/orders")
    Call<List<Order_Model>> getOrders();

    @GET("/api/feebacks")
    Call<List<Feeback_Model>> getFeeback();
    @GET("/api/artstories")
    Call<List<ArtStoryModel>> getArtStories();

}
