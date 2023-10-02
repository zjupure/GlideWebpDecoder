package com.bumptech.glide.samples.webp;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.integration.webp.WebpBitmapFactory;
import com.bumptech.glide.integration.webp.decoder.WebpDownsampler;
import com.bumptech.glide.integration.webp.decoder.WebpDrawable;
import com.bumptech.glide.integration.webp.decoder.WebpDrawableTransformation;
import com.bumptech.glide.load.Transformation;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.request.ImageRequest;

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
        if (holder.imageView instanceof AspectRatioImageView) {
            AspectRatioImageView view = (AspectRatioImageView)holder.imageView;
            view.setAspectRatio(720.0f / 1268.0f);
        }

        if (holder.imageView instanceof SimpleDraweeView) {
            SimpleDraweeView view = (SimpleDraweeView)(holder.imageView) ;
            loadImageWithFresco(view, url);
        } else if (mBitmapTrans != null) {
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

    private void loadImageWithFresco(SimpleDraweeView draweeView, String url) {
        ImageRequest imageRequest = ImageRequest.fromUri(url);
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setImageRequest(imageRequest)
                .setAutoPlayAnimations(true)
                .setOldController(draweeView.getController())
                .build();
        draweeView.setController(controller);
    }


    private void loadImage(ImageView imageView, String url) {
        WebpBitmapFactory.sUseSystemDecoder = false;
        GlideApp.with(mContext)
                //.asBitmap()
                .load(url)
                .placeholder(R.drawable.image_loading)
                .error(R.drawable.image_error)
                //.set(WebpFrameLoader.FRAME_CACHE_STRATEGY, WebpFrameCacheStrategy.AUTO)
                .set(WebpDownsampler.USE_SYSTEM_DECODER, false)
                .into(imageView);
    }

    private void loadImageWithTransformation(ImageView imageView, String url) {
        WebpBitmapFactory.sUseSystemDecoder = false;
        GlideApp.with(mContext)
                //.asBitmap()
                .load(url)
                .placeholder(R.drawable.image_loading)
                .error(R.drawable.image_error)
                .optionalTransform(mBitmapTrans)
                .optionalTransform(WebpDrawable.class, new WebpDrawableTransformation(mBitmapTrans))
                //.set(WebpFrameLoader.FRAME_CACHE_STRATEGY, WebpFrameCacheStrategy.AUTO)
                .set(WebpDownsampler.USE_SYSTEM_DECODER, false)
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
