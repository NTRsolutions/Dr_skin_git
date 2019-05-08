package com.sismatix.drskin.Retrofit;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiInterface {

    @POST("AppCreateUser.php")
    @FormUrlEncoded
    Call<ResponseBody> signup(@Field("firstname") String fullname,
                              @Field("email") String email,
                              @Field("password") String password);

    /*https://ihealkuwait.com/customapi/AppLogin.php?email=developertest2018@gmail.com&password=admin@123&quote_id=963*/

    @POST("AppLogin.php")
    @FormUrlEncoded
    Call<ResponseBody> login(@Field("email") String username,
                             @Field("password") String password,
                             @Field("quote_id") String quote_id,
                             @Field("token") String token,
                             @Field("device") String device);

    @POST("logout.php")
    @FormUrlEncoded
    Call<ResponseBody> logout(@Field("customer_id") String username);

    @POST("AppResetPassword.php")
    @FormUrlEncoded
    Call<ResponseBody> forgotpassword(@Field("email") String username);

    @POST("AppCategoryList.php")
    @FormUrlEncoded
    Call<ResponseBody> categorylist(@Field("type") String type);

    @POST("AppCategoryProducts.php")
    @FormUrlEncoded
    Call<ResponseBody> addcategoryprod(@Field("category_id") String category_id);

    @POST("AppProductView.php")
    @FormUrlEncoded
    Call<ResponseBody> appprodview(@Field("product_id") String product_id,
                                   @Field("customer_id") String customer_id);

    @POST("AppAddToCart.php")
    @FormUrlEncoded
    Call<ResponseBody> addtocart(@Field("product_id") String product_id,
                                 @Field("customer_id") String customer_id,
                                 @Field("qty") String qty);

    @POST("AppAddToCart.php")
    @FormUrlEncoded
    Call<ResponseBody> withoutlogin_quote_addtocart(@Field("product_id") String product_id,
                                                    @Field("qty") String qty);

    @POST("AppAddToCart.php")
    @FormUrlEncoded
    Call<ResponseBody> withoutlogin_addtocart(@Field("product_id") String product_id,
                                              @Field("quote_id") String quote_id,
                                              @Field("qty") String qty);

    @POST("AppAddToCart.php")
    @FormUrlEncoded
    Call<ResponseBody> addtocart_wish(@Field("product_id") String product_id,
                                      @Field("customer_id") String customer_id,
                                      @Field("removeWishlist") String removeWishlist);

    @POST("AppAddToCart.php")
    @FormUrlEncoded
    Call<ResponseBody> withoutlg_quote_addtocart_wish(@Field("product_id") String product_id,
                                                      @Field("removeWishlist") String removeWishlist);

    @POST("AppCartList.php")
    @FormUrlEncoded
    Call<ResponseBody> Cartlist(@Field("email") String email);

    @POST("AppCartList.php")
    @FormUrlEncoded
    Call<ResponseBody> Cartlist_totoal(@Field("email") String email,
                                       @Field("review") String review,
                                       @Field("country_id") String country_id,
                                       @Field("paymentmethod") String paymentmethod,
                                       @Field("shipping") String shipping);

    @POST("AppCartList.php")
    @FormUrlEncoded
    Call<ResponseBody> getlistcart(@Field("quote_id") String quote_id);

    //wishlist api
    //https://ihealkuwait.com/customapi/AppAddWishlist.php?productid=50&customerid=1

    @POST("AppAddWishlist.php")
    @FormUrlEncoded
    Call<ResponseBody> add_to_wishlist(@Field("productid") String product_id,
                                       @Field("customer_id") String customerid,
                                       @Field("action") String action);

    //remove from wishlist api
    //https://ihealkuwait.com/customapi/AppRemoveWishlistProduct.php?productid=50&customerid=1

    @POST("AppRemoveWishlistProduct.php")
    @FormUrlEncoded
    Call<ResponseBody> remove_from_wishlist(@Field("productid") String product_id,
                                            @Field("customer_id") String customerid);

    //remove from cartlist
    //https://ihealkuwait.com/customapi/AppRemoveFromCart.php?product_id=4&email=developertest2018@gmail.com

    @POST("AppRemoveFromCart.php")
    @FormUrlEncoded
    Call<ResponseBody> remove_from_cartlist(@Field("product_id") String product_id, @Field("email") String email);

    @POST("AppRemoveFromCart.php")
    @FormUrlEncoded
    Call<ResponseBody> withoutlogin_remove_from_cartlist(@Field("product_id") String product_id, @Field("quote_id") String email);

    @POST("AppGetWishlist.php")
    @FormUrlEncoded
    Call<ResponseBody> GetWishlist(@Field("customer_id") String email);

    @POST("AppAddWishlist.php")
    @FormUrlEncoded
    Call<ResponseBody> Wishlistactions(@Field("action") String action,
                                       @Field("productid") String productid,
                                       @Field("customer_id") String customerid);

    //country list
    //https://ihealkuwait.com/customapi/AppGetCountryList.php
    @GET("AppGetCountryList.php")
    Call<ResponseBody> get_country_list();

    @POST("AppCreateAddress.php")
    @FormUrlEncoded
    Call<ResponseBody> AppCreateAddress(@Field("customer_id") String customer_id,
                                        @Field("FirstName") String FirstName,
                                        @Field("countryid") String countryid,
                                        @Field("postcode") String postcode,
                                        @Field("city") String city,
                                        @Field("street") String street,
                                        @Field("apartment") String apartment,
                                        @Field("phone") String telephone);

    @POST("AppUpdateAddress.php")
    @FormUrlEncoded
    Call<ResponseBody> AppUpdateAddress(@Field("address_id") String address_id,
                                        @Field("customer_id") String customer_id,
                                        @Field("FirstName") String FirstName,
                                        @Field("countryid") String countryid,
                                        @Field("postcode") String postcode,
                                        @Field("city") String city,
                                        @Field("street") String street,
                                        @Field("apartment") String apartment,
                                        @Field("phone") String telephone);


    //call shipping address api
    //https://ihealkuwait.com/customapi/AppGetAddressList.php?customer_id=1

    @POST("AppGetAddressList.php")
    @FormUrlEncoded
    Call<ResponseBody> GET_SHIPPING_ADDRESS(@Field("customer_id") String customer_id);

    @GET("AppShippingMethod.php")
    Call<ResponseBody> getShippingMethods();

    @GET("AppPaymentMethod.php")
    Call<ResponseBody> getPaymentMethods();

    @POST("AppUpdateCart.php")
    @FormUrlEncoded
    Call<ResponseBody> appUpdatecart(@Field("quote_id") String quote_id,
                                     @Field("qty") String qty,
                                     @Field("item_id") String item_id);

    @POST("AppCreateOrder.php")
    @FormUrlEncoded
    Call<ResponseBody> AppCreateOrder(@Field("customer_id") String customer_id,
                                      @Field("email") String email,
                                      @Field("quote_id") String quote_id,
                                      @Field("FirstName") String FirstName,
                                      @Field("countryid") String countryid,
                                      @Field("postcode") String postcode,
                                      @Field("city") String city,
                                      @Field("telephone") String telephone,
                                      @Field("street") String street,
                                      @Field("shippingcode") String shippingcode,
                                      @Field("paymentcode") String paymentcode);
    //https://ihealkuwait.com/customapi/AppUpdateAddress.php?
    // address_id=137&customer_id=1&firstname=uaua&middlename=mama&city=yryr&postcode=888888&telephone=2222222222&company=dreamdcm

    @POST("AppUpdateAddress.php")
    @FormUrlEncoded
    Call<ResponseBody> AppUpdateAddress(@Field("address_id") String address_id,
                                        @Field("customer_id") String customer_id,
                                        @Field("FirstName") String FirstName,
                                        @Field("countryid") String countryid,
                                        @Field("street") String street,
                                        @Field("city") String city,
                                        @Field("telephone") String telephone);

    @POST("AppOrderList.php")
    @FormUrlEncoded
    Call<ResponseBody> AppOrderList(@Field("customer_id") String customer_id);

    @POST("AppReorder.php")
    @FormUrlEncoded
    Call<ResponseBody> AppReorder(@Field("customer_id") String customer_id,
                                  @Field("order_id") String order_id);

    @POST("AppCatalogSearch.php")
    @FormUrlEncoded
    Call<ResponseBody> AppSearchCategory(@Field("searchterm") String searchterm);

    @POST("AppTapPaymentResponse.php")
    @FormUrlEncoded
    Call<ResponseBody> AppTapPaymentResponse(@Field("ref") String customer_id,
                                             @Field("result") String result,
                                             @Field("trackid") String trackid,
                                             @Field("crdtype") String crdtype,
                                             @Field("payid") String payid,
                                             @Field("hash") String hash);


    //http://doctorskin.net/customapi/AppGetCmspage.php?cmspage=about-store
//http://doctorskin.net/customapi/AppGetCmspage.php?cmspage=terms-conditions
    @POST("AppGetCmspage.php")
    @FormUrlEncoded
    Call<ResponseBody> cmslist(@Field("cmspage") String cmspage);

    //http://doctorskin.net/customapi/AppAddCoupon.php?quote_id=36&couponcode=test
    @POST("AppAddCoupon.php")
    @FormUrlEncoded
    Call<ResponseBody> addcoupon(@Field("quote_id") String customer_id,
                                 @Field("couponcode") String order_id);

    //http://doctorskin.net/customapi/AppRemoveCoupon.php?quote_id=36
    @POST("AppRemoveCoupon.php")
    @FormUrlEncoded
    Call<ResponseBody> removcoupon(@Field("quote_id") String quote_id);

   //http://doctorskin.net/customapi/AppOrderSummary.php?order_id=1
    @POST("AppOrderSummary.php")
    @FormUrlEncoded
    Call<ResponseBody> GetOrderSummary(@Field("order_id") String order_id);


    //http://doctorskin.net/customapi/AppPayFatoorahResponce.php?paymentId=0505275071956352
    @POST("AppPayFatoorahResponce.php")
    @FormUrlEncoded
    Call<ResponseBody> AppPayFatoorahResponce(@Field("paymentId") String order_id);

    @POST("AppContact.php")
    @FormUrlEncoded
    Call<ResponseBody> AppContact(@Field("name") String customer_id,
                                             @Field("email") String result,
                                             @Field("que") String trackid);

    @GET("AppHomeVideo.php")
    Call<ResponseBody> getVideos();

    @GET("AppVideostreamingdetails.php")
    Call<ResponseBody> AppLivevideo();

}
