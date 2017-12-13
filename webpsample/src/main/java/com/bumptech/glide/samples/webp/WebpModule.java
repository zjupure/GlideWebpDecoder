package com.bumptech.glide.samples.webp;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Registry;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.module.AppGlideModule;

/**
 * Created by liuchun on 2017/10/17.
 */
@GlideModule
public class WebpModule extends AppGlideModule {

    @Override
    public void registerComponents(Context context, Glide glide, Registry registry) {

    }

    @Override
    public boolean isManifestParsingEnabled() {
        return false;
    }
}
