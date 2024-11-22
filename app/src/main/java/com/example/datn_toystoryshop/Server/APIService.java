package com.example.datn_toystoryshop.Server;

import com.example.datn_toystoryshop.Model.Address;
import com.example.datn_toystoryshop.Model.ArtStoryModel;
import com.example.datn_toystoryshop.Model.Cart_Model;
import com.example.datn_toystoryshop.Model.ChatHistoryResponse_Model;
import com.example.datn_toystoryshop.Model.ChatMessage_Model;
import com.example.datn_toystoryshop.Model.Favorite_Model;
import com.example.datn_toystoryshop.Model.FeebackApp_Model;
import com.example.datn_toystoryshop.Model.Feeback_Model;
import com.example.datn_toystoryshop.Model.Order_Model;
import com.example.datn_toystoryshop.Model.Product_Model;
import com.example.datn_toystoryshop.Model.Voucher;
import com.google.gson.JsonObject;

import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface APIService {


//    String BASE_URL  = "http://192.168.16.101:3000/";// cương
     // String BASE_URL  = "http://192.168.1.10:3000/";// huy
    String BASE_URL = "http://192.168.101.10:3000/";


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
    Call<List<Favorite_Model>> getFavorites(@Query("cusId") String cusId);

    @GET("/api/carts")
    Call<List<Cart_Model>> getCarts(@Query("cusId") String cusId);

    @GET("/api/orders")
    Call<List<Order_Model>> getOrders(@Query("cusId") String cusId);

    @GET("/api/feebacks")
    Call<List<Feeback_Model>> getFeeback();

    @POST("/api/add/add-to-favorites")
    Call<Favorite_Model> addToFavorites(@Body Favorite_Model favoriteModel);

    @GET("/api/product-by/{prodId}")
    Call<Product_Model> getProductById(@Path("prodId") String prodId);

    @PUT("/api/update/cart/{cartId}")
    Call<Cart_Model> putCartUpdate(@Path("cartId") String cartId, @Body Cart_Model cartModel);

    @GET("/api/cart-by/{cartId}")
    Call<Cart_Model> getCartById(@Path("cartId") String cartId);

    @DELETE("/api/deleteFavorite/{id}")
    Call<Void> deleteFavorite(@Path("id") String productId);

    @DELETE("/api/deleteCart/{id}")
    Call<Void> deleteCart(@Path("id") String productId);

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

    @GET("/api/artstories")
    Call<List<ArtStoryModel>> getArtStories();
    @GET("/api/vouchers")
    Call<List<Voucher>> getVouchers();

    @GET("/api/addresses")
    Call<List<Address>> getAllAddresses();

    @GET("/api/addresses/{id}")
    Call<Address> getAddressById(@Path("id") String addressId);


    @GET("/api/cart/check-product")
    Call<JsonObject> checkProductInCart(@Query("prodId") String prodId, @Query("cusId") String cusId);
    @GET("/api/cart/get-cart-id")
    Call<JsonObject> getCartId(@Query("prodId") String prodId, @Query("cusId") String cusId);

    @POST("/api/chat/send")
    Call<ResponseBody> sendMessage(@Body ChatMessage_Model chatMessage);

    // Lấy lịch sử tin nhắn
    @GET("/api/chat/history")
    Call<ChatHistoryResponse_Model> getChatHistory(@Query("user1") String user1, @Query("user2") String user2);
}
