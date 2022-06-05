package com.bumptech.glide.samples.webp;


import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.DynamicDrawableSpan;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.CenterInside;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.load.resource.bitmap.FitCenter;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.target.Target;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Displays an webp image loaded from an android raw resource.
 */
public class MainActivity extends Activity {
    private static final String TAG = "WebpActivity";

    private static final String[] SIMPLE_WEBP = {
            "http://www.gstatic.com/webp/gallery/1.webp",
            "http://www.gstatic.com/webp/gallery/2.webp",
            "http://www.gstatic.com/webp/gallery/3.webp",
            "http://www.gstatic.com/webp/gallery/4.webp",
            "http://www.gstatic.com/webp/gallery/5.webp",
    };
    private static final String[] ALPHA_WEBP = {
            "https://www.gstatic.com/webp/gallery3/1_webp_ll.webp",
            "https://www.gstatic.com/webp/gallery3/2_webp_ll.webp",
            "https://www.gstatic.com/webp/gallery3/3_webp_ll.webp",
            "https://www.gstatic.com/webp/gallery3/4_webp_ll.webp",
            "https://www.gstatic.com/webp/gallery3/5_webp_ll.webp",
            "https://www.gstatic.com/webp/gallery3/1_webp_a.webp",
            "https://www.gstatic.com/webp/gallery3/2_webp_a.webp",
            "https://www.gstatic.com/webp/gallery3/3_webp_a.webp",
            "https://www.gstatic.com/webp/gallery3/4_webp_a.webp",
            "https://www.gstatic.com/webp/gallery3/5_webp_a.webp",
    };
    private static final String[] ANIM_WEBP = {
            "https://www.gstatic.com/webp/animated/1.webp",
            "https://mathiasbynens.be/demo/animated-webp-supported.webp",
            "https://isparta.github.io/compare-webp/image/gif_webp/webp/2.webp",
            //"https://video.billionbottle.com/d6e66dbb883a48f989b1b1d0e035bbbf/image/dynamic/71fcdca947d144b883949bbe368d60c3.gif?x-oss-process=image/resize,w_320/format,webp"
    };

    private static final String[] ANIM_GIF = {
            "https://78.media.tumblr.com/a0c1be3183449f0d207a022c28f4bbf7/tumblr_p1p2cduAiA1wmghc4o1_500.gif",
            "https://78.media.tumblr.com/31ff4ea771940d2403323c1416b81064/tumblr_p1ymv2Xghn1qbt8b8o2_500.gif",
            "https://78.media.tumblr.com/45c7b305f0dbdb9a3c941be1d86aceca/tumblr_p202yd8Jz11uashjdo3_500.gif",
            "https://78.media.tumblr.com/167e9c5a0534d2718853a2e3985d64e2/tumblr_p1yth5CHXk1srs2u0o1_500.gif",
            "https://78.media.tumblr.com/e7548bfe04a9fdadcac440a5802fb570/tumblr_p1zj4dyrxN1u4mwxfo1_500.gif",
    };


    private TextView mTextView;
    private RecyclerView mRecyclerView;
    private ImageAdapter mWebpAdapter;
    private TextView mImageSpan;

    private Transformation<Bitmap> mBitmapTrans = null;

    private Spinner mSpinner;
    private Menu mActionMenu;

    private int mImageType = 0;

    private LoadWebpSpanTask mSpanTask = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextView = (TextView) findViewById(R.id.webp_image_type);
        mRecyclerView = (RecyclerView) findViewById(R.id.webp_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mImageSpan = (TextView) findViewById(R.id.webp_image_span);

        mWebpAdapter = new ImageAdapter(this, getAnimatedWebpUrls());
        mRecyclerView.setAdapter(mWebpAdapter);

        mSpinner = (Spinner) findViewById(R.id.trans_selector);
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        mBitmapTrans = null;
                        break;
                    case 1:
                        mBitmapTrans = new CenterCrop();
                        break;
                    case 2:
                        mBitmapTrans = new CircleCrop();
                        break;
                    case 3:
                        mBitmapTrans = new RoundedCorners(24);
                        break;
                    case 4:
                        mBitmapTrans = new CenterInside();
                        break;
                    case 5:
                        mBitmapTrans = new FitCenter();
                        break;
                    default:
                        mBitmapTrans = null;
                        break;
                }
                refreshImageData(mImageType);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mBitmapTrans = null;
                refreshImageData(mImageType);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        mActionMenu = menu;
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        handleMenuItemCheck(item);

        int imageType = mImageType;
        if (id == R.id.static_webp_action) {
            imageType = 1;
        } else if (id == R.id.alpha_webp_action) {
            imageType = 2;
        } else if (id == R.id.animate_webp_action) {
            imageType = 0;
        } else if (id == R.id.animate_gif_action) {
            imageType = 3;
        } else if (id == R.id.webp_span_action) {
            imageType = 4;
        }

        if (imageType != mImageType)
        {
            mImageType = imageType;
            refreshImageData(mImageType);
        }

        return true;
    }

    private List<String> getAnimatedWebpUrls() {
        List<String> webpUrls = new ArrayList<>(Arrays.asList(ANIM_WEBP));
        int[] webpRes = {R.drawable.broken, R.drawable.small_frame, R.drawable.head, R.drawable.test};
        for (int resId : webpRes)
        {
            String resUrl = "android.resource://" + getPackageName() + "/" + resId;
            webpUrls.add(resUrl);
        }
        return webpUrls;
    }

    private void refreshImageData(int imageType) {

        mRecyclerView.setVisibility(View.VISIBLE);
        mImageSpan.setVisibility(View.GONE);
        mWebpAdapter.setBitmapTransformation(mBitmapTrans);
        switch (imageType) {
            case 0:
                // Animated Webp
                mTextView.setText("animated webp");
                mWebpAdapter.updateData(getAnimatedWebpUrls());
                break;
            case 1:
                // Static lossy webp
                mTextView.setText("static lossy webp");
                mWebpAdapter.updateData(Arrays.asList(SIMPLE_WEBP));
                break;
            case 2:
                // Static lossless webp
                mTextView.setText("static lossless (with alpha) webp");
                mWebpAdapter.updateData(Arrays.asList(ALPHA_WEBP));
                break;
            case 3:
                // Gif
                mTextView.setText("animated gif");
                mWebpAdapter.updateData(Arrays.asList(ANIM_GIF));
                break;
            case 4:
                // Image Span
                mRecyclerView.setVisibility(View.GONE);
                mImageSpan.setVisibility(View.VISIBLE);
                mTextView.setText("webp image span");

                String testUrl = "android.resource://" + getPackageName() + "/" + R.drawable.small_frame;
                mSpanTask = new LoadWebpSpanTask();
                mSpanTask.execute(testUrl);
            default:
                break;
        }
    }

    private void handleMenuItemCheck(MenuItem menuItem) {
        if (mActionMenu == null) {
            menuItem.setChecked(true);
            return;
        }

        for (int i = 0; i < mActionMenu.size(); i++) {
            MenuItem item = mActionMenu.getItem(i);
            if (item.getItemId() == menuItem.getItemId()) {
                item.setChecked(true);
            } else {
                item.setChecked(false);
            }
        }
    }

    /**
     * a async task to load webp images with glide in background thread
     */
    private class LoadWebpSpanTask extends AsyncTask<String, Integer, SpannableString> {

        private Drawable mDrawable;
        private Drawable.Callback mCallback;

        @Override
        protected SpannableString doInBackground(String... urls) {
            if (urls.length <= 0 || urls[0] == null) {
                return null;
            }

            String webpUrl = urls[0];
            try {
                Drawable drawable = GlideApp.with(MainActivity.this)
                        .load(webpUrl)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .submit(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                        .get(); // get a webp drawable instance
                drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
                if (drawable instanceof Animatable) {
                    ((Animatable) drawable).start();  // start animation
                }
                mCallback = new Drawable.Callback() {
                    @Override
                    public void invalidateDrawable(@NonNull Drawable who) {
                        mImageSpan.postInvalidate();
                    }

                    @Override
                    public void scheduleDrawable(@NonNull Drawable who, @NonNull Runnable what, long when) {

                    }

                    @Override
                    public void unscheduleDrawable(@NonNull Drawable who, @NonNull Runnable what) {

                    }
                };
                mDrawable = drawable;
                mDrawable.setCallback(mCallback);  // set Drawable.Callback to refresh TextView

                SpannableString ss = new SpannableString("This is a webp span ");
                // using the default alignment: ALIGN_BOTTOM
                ss.setSpan(new ImageSpan(drawable, DynamicDrawableSpan.ALIGN_BOTTOM), ss.length() - 1, ss.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                return ss;
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(SpannableString text) {
            if (text != null) {
                mImageSpan.setText(text);
            }
        }
    }
}
