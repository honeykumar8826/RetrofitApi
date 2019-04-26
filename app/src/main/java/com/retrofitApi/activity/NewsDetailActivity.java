package com.retrofitApi.activity;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.retrofitApi.R;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

import retrofit2.http.Url;

public class NewsDetailActivity extends AppCompatActivity {
    private static final String TAG = "NewsDetailActivity";
    ImageView newsImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail_actvitiy);
//        initialize the id
        inItId();
        Intent intent = getIntent();
        String imageUrl = intent.getStringExtra("imageUrl");
        // Uri uri = Uri.parse( imageUrl );
        Glide.with(NewsDetailActivity.this).load(imageUrl).placeholder(R.drawable.placeholder)
                .into(newsImg);

        //  newsImg.setImageURI(uri);
        Log.i(TAG, "onCreate: " + imageUrl);

    }

    private void inItId() {
        newsImg = findViewById(R.id.img_expanded);
    }
}
