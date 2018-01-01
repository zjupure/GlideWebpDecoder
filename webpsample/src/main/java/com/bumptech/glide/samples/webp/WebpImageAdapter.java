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
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.BaseTarget;
import com.bumptech.glide.request.target.SizeReadyCallback;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.target.ViewTarget;
import com.bumptech.glide.request.transition.Transition;

import java.util.ArrayList;
import java.util.List;

/**
 * author: liuchun
 * date:  2017/10/24
 */
public class WebpImageAdapter extends RecyclerView.Adapter<WebpImageAdapter.ImageHolder> {
    private static final String TAG = "WebpImageAdapter";

    private Context mContext;
    private List<String> mWebpUrls;

    public WebpImageAdapter(Context context, List<String> urls) {
        mContext = context;
        mWebpUrls = new ArrayList<>();
        mWebpUrls.addAll(urls);
        String resUrl = "android.resource://" + context.getPackageName() + "/" + R.drawable.broken;
        mWebpUrls.add(resUrl);
    }

    public void updateData(List<String> urls) {
        mWebpUrls.clear();
        mWebpUrls.addAll(urls);
        notifyDataSetChanged();
    }

    @Override
    public ImageHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(mContext).inflate(R.layout.webp_image_item, parent, false);

        return new ImageHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ImageHolder holder, int position) {
        long size = mWebpUrls.size();
        if (position < 0 || position >= size) {
            return;
        }

        String url = mWebpUrls.get(position);
        Transformation<Bitmap> circleCrop = new CircleCrop();
        GlideApp.with(mContext)
                .load(url)
                .placeholder(R.drawable.image_loading)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        Log.i(TAG, "onLoadFailed: " + e.getMessage() + ", url=" + model);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        Log.i(TAG, "onResourceReady: , url=" + model);
                        return false;
                    }
                })
                .error(R.drawable.image_error)
                //.optionalTransform(circleCrop)
                //.optionalTransform(WebpDrawable.class, new WebpDrawableTransformation(circleCrop))
                .into(holder.imageView);
        holder.textView.setText(url);
    }

    @Override
    public int getItemCount() {
        return mWebpUrls.size();
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


    public static class DrawableTarget extends BaseTarget<Drawable> {

        @Override
        public void onResourceReady(Drawable resource, Transition<? super Drawable> transition) {
            Log.i("DrawableTarget", "onResourceReady called");
        }

        @Override
        public void getSize(SizeReadyCallback cb) {
            Log.i("DrawableTarget", "getSize called");
            cb.onSizeReady(ViewTarget.SIZE_ORIGINAL, ViewTarget.SIZE_ORIGINAL);
        }

        @Override
        public void removeCallback(SizeReadyCallback cb) {
            Log.i("DrawableTarget", "removeCallback called");
        }
    }
}
