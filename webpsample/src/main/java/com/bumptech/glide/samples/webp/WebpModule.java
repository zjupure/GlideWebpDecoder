package com.bumptech.glide.samples.webp;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Registry;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.module.AppGlideModule;

import java.io.InputStream;

import okhttp3.OkHttpClient;

/**
 * Created by liuchun on 2017/10/17.
 */
@GlideModule
public class WebpModule extends AppGlideModule {

    @Override
    public void registerComponents(Context context, Glide glide, Registry registry) {
        //OkHttpClient okHttpClient = new OkHttpClient.Builder().build();
        //registry.replace(GlideUrl.class, InputStream.class, new OkHttpUrlLoader.Factory(okHttpClient));
    }

    @Override
    public boolean isManifestParsingEnabled() {
        return false;
    }
}
