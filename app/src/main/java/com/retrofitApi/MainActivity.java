package com.retrofitApi;

import android.content.Context;
import android.net.ConnectivityManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.retrofitApi.adapter.NewsAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = "TAG";
    public static final String API_KEY = "9e5ef71432c64196a16273c85cfb94c1";
    private List<NewsInfoModal> newsInfoModalList;
    private RecyclerView recyclerViewNews;
    private Context context;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //for internet connection check

//        intialize the id
        inItId();
        //list initialization
        newsInfoModalList = new ArrayList<>();
        //intialize the context
        context = MainActivity.this;
        recyclerViewNews.setLayoutManager(new LinearLayoutManager(this));
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
                                NewsInfoModal newsInfoModal = new NewsInfoModal();
                                JSONObject jsonArticle = jsonArray.getJSONObject(i);
                                String authorName = jsonArticle.getString("author");
                                newsInfoModal.setAuthor(authorName);
                                String title = jsonArticle.getString("title");
                                newsInfoModal.setTitle(title);
                                String imgUrl = jsonArticle.getString("urlToImage");
                                newsInfoModal.setUrlToImage(imgUrl);
                                Log.i(TAG, "values inside the for loop: " + authorName + "title" + title + "imgUrl" + imgUrl);
                                newsInfoModalList.add(newsInfoModal);
                            }
                            NewsAdapter newsAdapter = new NewsAdapter(newsInfoModalList, context);
                            newsAdapter.notifyDataSetChanged();
                            recyclerViewNews.setAdapter(newsAdapter);
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
                    Toast.makeText(MainActivity.this, "Some Big Issue", Toast.LENGTH_SHORT).show();
                }

                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    private void inItId() {
        recyclerViewNews = findViewById(R.id.recycles_news);
        swipeRefreshLayout = findViewById(R.id.on_swipe_layout);
    }
    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null;
    }
}
