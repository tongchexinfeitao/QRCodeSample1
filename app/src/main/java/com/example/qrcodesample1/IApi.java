package com.example.qrcodesample1;

import com.example.qrcodesample1.bean.CartBean;
import com.example.qrcodesample1.bean.LoginBean;
import com.example.qrcodesample1.bean.ProductBean;

import java.util.Map;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.POST;
import retrofit2.http.Url;

public interface IApi {

    //联网请求获取商品信息列表
    @GET("small/commodity/v1/commodityList")
    Call<ProductBean> getProductList();   //get请求对应的 @Query

    @FormUrlEncoded
    @POST("small/user/v1/login")
    Call<LoginBean> login(@Field("phone") String phone, @Field("pwd") String pwd);


    @FormUrlEncoded
    @POST("small/user/v1/login")
    Call<LoginBean> loginForMap(@FieldMap Map<String, String> paramsMap);

    //如果请求需要传递请求头的时候，使用  @Header  或者  @HeaderMap
    @FormUrlEncoded
    @POST("small/user/v1/login")
    Call<LoginBean> loginForMap(@HeaderMap Map<String, String> headerMap, @FieldMap Map<String, String> paramsMap);


    //当该接口和Base_url不一致的时候，使用@Url注解来提供完整的url
    @FormUrlEncoded
    @POST()
    Call<LoginBean> forDifferentBaseUrl(@Url String url, @HeaderMap Map<String, String> headerMap, @FieldMap Map<String, String> paramsMap);


    //将Call转成 io.reactivex 包下的 Observable
    @FormUrlEncoded
    @POST("small/user/v1/login")
    Observable<LoginBean> loginForRxJava(@FieldMap Map<String, String> paramasMap);

    //联网请求获取商品信息列表
    @GET("small/commodity/v1/commodityList")
    Observable<ProductBean> getProductListForRxJava();   //get请求对应的 @Query

    //联网请求获取商品信息列表
    @GET("http://47.103.92.63:33003/wx/cart/index")
    Observable<ResponseBody> test1();   //getsmall/commodity/v1/commodityList请求对应的 @Query


    //将Call转成 io.reactivex 包下的 Observable
    @GET("small/order/verify/v1/findShoppingCart")
    Observable<CartBean> getCartInfo(@HeaderMap Map<String, String> headersMap);


}
