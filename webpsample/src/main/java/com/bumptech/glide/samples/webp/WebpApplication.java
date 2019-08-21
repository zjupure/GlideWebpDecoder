package com.bumptech.glide.samples.webp;

import android.app.Application;

import com.facebook.drawee.backends.pipeline.Fresco;

/**
 * author: liuchun
 * date: 2019-08-20
 */
public class WebpApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Fresco.initialize(this);
    }
}
