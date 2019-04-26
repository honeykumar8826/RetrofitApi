package com.retrofitApi.activity;

import android.content.Context;
import android.net.ConnectivityManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.retrofitApi.Api;
import com.retrofitApi.MainActivity;
import com.retrofitApi.NewsInfoModal;
import com.retrofitApi.R;
import com.retrofitApi.adapter.ImageLoadAdapter;
import com.retrofitApi.adapter.NewsAdapter;
import com.retrofitApi.modal.ImageModal;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ImageLoadActivity extends AppCompatActivity {
    public static final String API_KEY = "9e5ef71432c64196a16273c85cfb94c1";
    private List<ImageModal> imageModalList;
    private RecyclerView recyclerViewNews;
    private Context context;
    private SwipeRefreshLayout swipeRefreshLayout;
    private static final String TAG = "ImageLoadActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_load);
        //        intialize the id
        inItId();
        //list initialization
        imageModalList = new ArrayList<>();
        //intialize the context
        context = ImageLoadActivity.this;
        recyclerViewNews.setLayoutManager(new LinearLayoutManager(this, LinearLayout.HORIZONTAL,true));
        if(isNetworkConnected())
        {
            // for News Api Result
            callNewsApi();

            swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    callNewsApi();
                }
            });
            // Toast.makeText(context, "internet connected", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(context, "internet disconnected", Toast.LENGTH_SHORT).show();
        }
    }
    private void inItId() {
        recyclerViewNews = findViewById(R.id.recycles_news);
        swipeRefreshLayout = findViewById(R.id.on_swipe_layout);
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null;
    }


    private void callNewsApi() {
        swipeRefreshLayout.setRefreshing(true);
        Retrofit retrofit = new Retrofit.Builder().baseUrl(Api.BASE_URL1)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        Api api = retrofit.create(Api.class);

        Call<ResponseBody> call = api.getNews("in", API_KEY);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.i(TAG, "onResponse: " + response.body());
                try {
                    String result = response.body().string();
                    JSONObject jsonObject = new JSONObject(result);
                    String status = jsonObject.getString("status");
                    int totalItem = jsonObject.getInt("totalResults");
                    if (status.equals("ok") && totalItem > 0) {
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
                    }

                    Log.i(TAG, "onResponse:1 " + status + "" + totalItem);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if (t instanceof IOException) {
                    // Toast.makeText(MainActivity.this, "Internet Issue", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ImageLoadActivity.this, "Some Big Issue", Toast.LENGTH_SHORT).show();
                }

                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }
}
//







