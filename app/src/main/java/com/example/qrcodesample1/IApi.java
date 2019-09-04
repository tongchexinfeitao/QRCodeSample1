package com.example.qrcodesample1;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface IApi {

    //联网请求获取商品信息列表
    @GET("small/commodity/v1/commodityList")
    Call<ProductBean> getProductList();   //get请求对应的 @Query

    @FormUrlEncoded
    @POST("small/user/v1/login")
    Call<LoginBean> login(@Field("phone") String phone, @Field("pwd") String pwd);


}
