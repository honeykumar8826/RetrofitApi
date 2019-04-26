package com.retrofitApi;

import com.retrofitApi.modal.UserInfo;
import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface Api {

    String BASE_URL1 = "https://newsapi.org/v2/";
    String BASE_URL2 = "http://dummy.restapiexample.com/api/v1/";
    String BASE_URL3 = "https://jsonplaceholder.typicode.com/";
    //String BASE_URL4 = "https://spotrack-form.000webhostapp.com/Api_Project/";
    String BASE_URL4 = "https://webkapil.000webhostapp.com/";
    String BASE_URL5="http://hairinfernodev.appinventive.com/";

    @GET("top-headlines")
    Call<ResponseBody> getNews(@Query("country") String country, @Query("apiKey") String apiKey);
  /*  @GET("post-list")
    Call<ResponseBody> getNews(@Query("country") String country, @Query("apiKey") String apiKey);*/

    @POST("create")
    Call<ResponseBody> createRecord(@Body UserInfo body);

    @PUT("update/{id}")
    Call<ResponseBody> updateRecord(@Path("id") int id, @Body UserInfo body);

    @DELETE("delete/{id}")
    Call<Void> deleteRecord(@Path("id") int id);

    @PATCH("posts/{id}")
    Call<ResponseBody> updateSomeField(@Path("id") int id, @Body UserInfo body);

    @Multipart
    @POST("uploads/upload_files.php")
    Call<ResponseBody> uploadImage(@Part MultipartBody.Part photo);
/*
 @Multipart
    @POST("uploads/upload_files.php")
    Call<ResponseBody> uploadImage(@Part("description") RequestBody description, @Part MultipartBody.Part photo);
*/

    @GET("post-list")
    Call<ResponseBody> getData(@Query("type")String type,@Query("page")String page,
                               @Query("user_type")String user_type);

}
