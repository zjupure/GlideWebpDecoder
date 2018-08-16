package com.bumptech.glide.samples.webp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.bumptech.glide.integration.webp.decoder.WebpDrawable;
import com.bumptech.glide.integration.webp.decoder.WebpDrawableTransformation;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.ArrayList;
import java.util.List;

/**
 * author: liuchun
 * date:  2017/10/24
 */
public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageHolder> {
    private static final String TAG = "ImageAdapter";

    private Context mContext;
    private List<String> mImageUrls;

    private Transformation<Bitmap> mBitmapTrans;

    public ImageAdapter(Context context, List<String> urls) {
        mContext = context;
        mImageUrls = new ArrayList<>();
        mImageUrls.addAll(urls);
    }

    public void setBitmapTransformation(Transformation<Bitmap> bitmapTrans) {
        mBitmapTrans = bitmapTrans;
    }

    public void updateData(List<String> urls) {
        mImageUrls.clear();
        mImageUrls.addAll(urls);
        notifyDataSetChanged();
    }

    @Override
    public ImageHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(mContext).inflate(R.layout.webp_image_item, parent, false);

        return new ImageHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ImageHolder holder, int position) {
        long size = mImageUrls.size();
        if (position < 0 || position >= size) {
            return;
        }

        String url = mImageUrls.get(position);
        if (mBitmapTrans != null) {
            loadImageWithTransformation(holder.imageView, url);
        } else {
            loadImage(holder.imageView, url);
        }

        holder.textView.setText(url);
    }

    @Override
    public int getItemCount() {
        return mImageUrls.size();
    }

    private void loadImage(ImageView imageView, String url) {
        GlideApp.with(mContext)
                .load(url)
                .placeholder(R.drawable.image_loading)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        Log.i(TAG, "onLoadFailed: " + e.getMessage() + ", url=" + model);
                        e.printStackTrace();
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        Log.i(TAG, "onResourceReady: , url=" + model);
                        return false;
                    }
                })
                .error(R.drawable.image_error)
                .into(imageView);
    }

    private void loadImageWithTransformation(ImageView imageView, String url) {

        GlideApp.with(mContext)
                .load(url)
                .placeholder(R.drawable.image_loading)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        Log.i(TAG, "onLoadFailed: " + e.getMessage() + ", url=" + model);
                        e.printStackTrace();
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        Log.i(TAG, "onResourceReady: , url=" + model);
                        return false;
                    }
                })
                .error(R.drawable.image_error)
                .optionalTransform(mBitmapTrans)
                .optionalTransform(WebpDrawable.class, new WebpDrawableTransformation(mBitmapTrans))
                .into(imageView);
    }

    public static class ImageHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textView;

        public ImageHolder(View itemView) {
            super(itemView);

            imageView = (ImageView) itemView.findViewById(R.id.webp_image);
            textView = (TextView) itemView.findViewById(R.id.webp_text);
        }
    }
}
