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
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.retrofitApi.R;
import com.retrofitApi.modal.ImageModal;

import java.util.List;

public class ImageLoadAdapter extends RecyclerView.Adapter<ImageLoadAdapter.ImageViewHolder> {
    private Context context;
    private List<ImageModal> imageModalList;

    public ImageLoadAdapter(Context context, List<ImageModal> imageModalList) {
        this.context = context;
        this.imageModalList = imageModalList;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.image_layout, viewGroup, false);
        return new ImageLoadAdapter.ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder imageViewHolder, int i) {
        final ImageModal newsInfoModal = imageModalList.get(i);
        final String imgUrl = newsInfoModal.getImgUrl();

        Glide.with(context)
                .asBitmap()
                .load(imgUrl)
                .placeholder(R.drawable.placeholder)
                .apply(RequestOptions.circleCropTransform())
                .into(imageViewHolder.imageView);
    }

    @Override
    public int getItemCount() {
        return imageModalList.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.img_load);
        }
    }
}

