package com.delaroystodios.metakar.network;


import com.delaroystodios.metakar.Model.Advertisement;
import com.delaroystodios.metakar.Model.Category;
import com.delaroystodios.metakar.Model.Cities;
import com.delaroystodios.metakar.Model.DashboardParent;
import com.delaroystodios.metakar.Model.DeleteNotifications;
import com.delaroystodios.metakar.Model.DetailsAdvertismentModel;
import com.delaroystodios.metakar.Model.Earnings;
import com.delaroystodios.metakar.Model.EditProfile;
import com.delaroystodios.metakar.Model.ListEarnParent;
import com.delaroystodios.metakar.Model.Login;
import com.delaroystodios.metakar.Model.MyAdvertisementModel;
import com.delaroystodios.metakar.Model.Notifications;
import com.delaroystodios.metakar.Model.Provinces;
import com.delaroystodios.metakar.Model.ReadNotifications;
import com.delaroystodios.metakar.Model.Register;
import com.delaroystodios.metakar.Model.ResetPassword;
import com.delaroystodios.metakar.Model.SendMessage;
import com.delaroystodios.metakar.Model.SendSeendAdvertisement;
import com.delaroystodios.metakar.Model.ShowListWallet;
import com.delaroystodios.metakar.Model.ShowProfile;
import com.delaroystodios.metakar.Model.SubCategory;
import com.delaroystodios.metakar.Model.SubsetUser;
import com.delaroystodios.metakar.Model.Withdraws;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiServices {


    @POST("/api/advertisements")
    @FormUrlEncoded
    Call<List<Advertisement>> getAdvertisementsLimit(@Field("offset") int start, @Field("skip") int limit);

    @POST("/api/advertisements/{id}")
    Call<DetailsAdvertismentModel> getAdvertisementDetails(@Header("Authorization") String Authorization, @Path("id") String id);

    @POST("/api/advertisements/{id}/seen")
    Call<SendSeendAdvertisement> sendSeendAdvertisement(@Header("Authorization") String Authorization, @Path("id") String id);

    @POST("api/advertisements/{id}/mail")
    @FormUrlEncoded
    Call<SendMessage> sendMessage(@Header("Authorization") String Authorization, @Path("id") String id,
                                  @Field("name") String name, @Field("email") String email, @Field("message") String message);

    @POST("/api/advertisements/search")
    @FormUrlEncoded
    Call<List<Advertisement>> getAdvertisementSearch(@Field("skip") int limit, @Field("offset") int start
            , @Field("type") String typeAdvertisement, @Field("key") String keySearch);

    @POST("/api/get-data/categories")
    Call<List<Category>> getListCategory();

    @POST("/api/get-data/categories/{idSubCategory}/sub-categories")
    Call<List<SubCategory>> getSubCategory(@Path("idSubCategory") String idSubCategory);

    @POST("/api/advertisements/category/{idShowSubCategory}")
    @FormUrlEncoded
    Call<List<Advertisement>> getAdvertisementSubCategory(@Path("idShowSubCategory") int idShowSubCategory, @Field("skip") int skip, @Field("offset") int offset);


    @POST("/api/login")
    @FormUrlEncoded
    Call<Login> login(@Field("username") String username, @Field("password") String password);


    @POST("/api/password/email")
    @FormUrlEncoded
    Call<ResetPassword> resetPassword(@Field("email") String email);


    @POST("/api/register")
    @FormUrlEncoded
    Call<Register> register(@Field("email") String email, @Field("password") String password
            , @Field("password_confirmation") String password_confirmation, @Field("name") String name
            , @Field("family") String family);


    @POST("/api/panel/users/show")
    Call<ShowProfile> getShowProfile(@Header("Authorization") String Authorization);

    @POST("/api/panel/users/edit")
    @FormUrlEncoded
    Call<EditProfile> sendEditProfile(@Header("Authorization") String Authorization ,
                                      @Field("name") String name , @Field("family") String family , @Field("mobile") String mobile ,
                                      @Field("city") String city , @Field("cart_number") String cart_number ,
                                      @Field("rules_confirmed") String rules_confirmed);

    @POST("/api/get-data/provinces")
    Call<List<Provinces>> getProvinces();

    @POST("/api/get-data/provinces/{id}/cities")
    Call<List<Cities>> getCities(@Path("id") String id);


    @POST("/api/panel/users/subset")
    Call<SubsetUser> getSubsetUser(@Header("Authorization") String Authorization);

    @POST("/api/panel/wallets")
    @FormUrlEncoded
    Call<ShowListWallet> getShowListWallet(@Header("Authorization") String Authorization,
                                       @Field("offset") int offset, @Field("skip") int skip);

    @POST("/api/panel/notifications")
    @FormUrlEncoded
    Call<List<Notifications>> getNotifications(@Header("Authorization") String Authorization,
                                               @Field("offset") int offset, @Field("skip") int skip);


    @POST("/api/panel/notifications/{id}")
    Call<ReadNotifications> getReadNotifications(@Header("Authorization") String Authorization,
                                                 @Path("id") String id);

    @POST("/api/panel/notifications/{id}/delete")
    Call<DeleteNotifications> sendForDeleteNotification(@Header("Authorization") String Authorization,
                                                        @Path("id") String id);

    @POST("/api/withdraws")
    Call<List<Withdraws>> getWithdraws();

    @POST("/api/earnings")
    Call<List<Earnings>> getEarnings();

    @POST("/api/panel/dashboard")
    @FormUrlEncoded
    Call<DashboardParent> getDashBoard(@Header("Authorization") String Authorization,
                                       @Field("offset") int offset, @Field("skip") int skip);

    @POST("/api/panel/wallets/withdraw/create")
    @FormUrlEncoded
    Call<ListEarnParent> getListEarns(@Header("Authorization") String Authorization,
                                      @Field("skip") int skip, @Field("offset") int offset);

    @POST("/api/panel/advertisements/index")
    @FormUrlEncoded
    Call<List<MyAdvertisementModel>> getListMyAdvertisements(@Header("Authorization") String Authorization,
                                      @Field("offset") int offset, @Field("skip") int skip);

    @POST("/api/panel/advertisements/{id}")
    Call<Advertisement> getMyAdvertisements(@Header("Authorization") String Authorization,
                                                        @Path("id") String id);

}