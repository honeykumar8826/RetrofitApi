package com.retrofitApi.activity;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.retrofitApi.Api;
import com.retrofitApi.R;
import com.retrofitApi.adapter.ImageLoadAdapter;
import com.retrofitApi.modal.ImageModal;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TestingApiActivity extends AppCompatActivity {
    private static final String TAG = "TestingApiActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testing_api);
        callNewsApi();
    }
    private void callNewsApi() {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(Api.BASE_URL5)
                .addConverterFactory(GsonConverterFactory.create())
                .client(getHeader())
                .build();
        Api api = retrofit.create(Api.class);

        Call<ResponseBody> call = api.getData("1", "1","1");
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.i(TAG, "onResponse: " + response.body());
                try {
                    String result = response.body().string();
                    JSONObject jsonObject = new JSONObject(result);
                    Log.i(TAG, "onResponse: "+jsonObject);
                  /*  String status = jsonObject.getString("status");
                    int totalItem = jsonObject.getInt("totalResults");*/
              /*      if (status.equals("ok") && totalItem > 0) {
                        JSONArray jsonArray = jsonObject.getJSONArray("articles");
                        if (jsonArray.length() > 0) {
                            for (int i = 0; i < jsonArray.length(); i++) {

                                JSONObject jsonArticle = jsonArray.getJSONObject(i);
                                String imgUrl = jsonArticle.getString("urlToImage");
                                ImageModal imageModal = new ImageModal(imgUrl);
//                                Log.i(TAG, "values inside the for loop: " + authorName + "title" + title + "imgUrl" + imgUrl);
                                imageModalList.add(imageModal);
                            }
                            ImageLoadAdapter imageLoadAdapter = new ImageLoadAdapter(context,imageModalList);
                            imageLoadAdapter.notifyDataSetChanged();
                            recyclerViewNews.setAdapter(imageLoadAdapter);
                            swipeRefreshLayout.setRefreshing(false);

                        } else {
                            Log.i(TAG, "JsonArray item is zero ");
                        }
                        Log.i(TAG, "jsonArray: " + jsonArray.length());
                    } else {
                        Log.i(TAG, "else part: ");
                    }*/

                    Log.i(TAG, "onResponse:1 " );
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.i(TAG, "onFailure: "+t.getMessage());
                if (t instanceof IOException) {
                    // Toast.makeText(MainActivity.this, "Internet Issue", Toast.LENGTH_SHORT).show();
                } else {
                   // Toast.makeText(ImageLoadActivity.this, "Some Big Issue", Toast.LENGTH_SHORT).show();
                }

               // swipeRefreshLayout.setRefreshing(false);
            }
        });
    }
    private OkHttpClient getHeader() {

//        sharedPreferences=getSharedPreferences("login",Context.MODE_PRIVATE);
//        email= sharedPreferences.getString("emailkey","3");
       /* sharedPreferences=getActivity().getSharedPreferences("signup", Context.MODE_PRIVATE);
        final String token =sharedPreferences.getString("token","2");
        Log.i("token", token);*/
        return new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public okhttp3.Response intercept(@NonNull Chain chain) throws IOException {
                        Request newRequest = chain.request().newBuilder()
//                                .addHeader("Authorization", "Basic cGl4YWxpdmU6REFGODdEU0ZEU0ZEU0E5OEZTQURLSkUzMjRLSkwzMkhGRDdGRFNGQjI0MzQzSjQ5RFNG")
                                .addHeader("Authorization", "Bearer "+"eyJ0eXAiOiJqd3QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJPRGc9Iiwic3ViIjoiSGFpciBJbmZlcm5vIiwiYXVkIjoiYzJSbVlYTmtaZz09IiwianRpIjoiWVhOa1ptRnpaZz09IiwiZXhwIjoiMjAxOS0wNC0yNiAxMjoyMDo0OCIsIm5iZiI6IjIwMTktMDQtMTggMDk6NTA6NDgifQ.0OKXjJhFiwykytV4uWIYFlaokMdzofV_dU2R3wg9xJQ")
                                .build();
                        return chain.proceed(newRequest);
                    }
                })
                .build();
    }
}
