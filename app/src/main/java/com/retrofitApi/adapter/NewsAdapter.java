package com.retrofitApi.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.retrofitApi.NewsInfoModal;
import com.retrofitApi.R;
import com.retrofitApi.activity.NewsDetailActivity;

import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder> {
    private Context context;
    private List<NewsInfoModal> newsInfoModalList;
    public NewsAdapter(List<NewsInfoModal> newsInfoModalList,Context context) {
        this.newsInfoModalList = newsInfoModalList;
        this.context = context;
    }

    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.news_layout,viewGroup,false);
        return new NewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder newsViewHolder, int i) {
        final NewsInfoModal newsInfoModal = newsInfoModalList.get(i);
        newsViewHolder.tvAuthorName.setText(newsInfoModal.getAuthor());
        newsViewHolder.tvTitle.setText(newsInfoModal.getTitle());
        final String imgUrl = newsInfoModal.getUrlToImage();

        Glide.with(context)
                .asBitmap()
                .load(newsInfoModal.getUrlToImage())
                .placeholder(R.drawable.placeholder)
                .apply(RequestOptions.circleCropTransform())
                .into(newsViewHolder.newsImg);
        newsViewHolder.cardViewListener.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, NewsDetailActivity.class);
                intent.putExtra("imageUrl",imgUrl);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return newsInfoModalList.size();
    }

    public class NewsViewHolder extends RecyclerView.ViewHolder {
        TextView tvAuthorName,tvTitle;
        ImageView newsImg;
        CardView cardViewListener;
        public NewsViewHolder(@NonNull View itemView) {
            super(itemView);
            tvAuthorName = itemView.findViewById(R.id.tv_author_name);
            tvTitle = itemView.findViewById(R.id.tv_title);
            newsImg = itemView.findViewById(R.id.news_related_img);
            cardViewListener = itemView.findViewById(R.id.card_layout);


        }
    }
}
