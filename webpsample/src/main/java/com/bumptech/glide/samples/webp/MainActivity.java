package com.bumptech.glide.samples.webp;


import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;

import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.CenterInside;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.load.resource.bitmap.FitCenter;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
            //"https://raw.githubusercontent.com/1290846731/RecordMySelf/master/chect.webp",
            "https://www.gstatic.com/webp/animated/1.webp",
            "https://mathiasbynens.be/demo/animated-webp-supported.webp",
            "https://isparta.github.io/compare-webp/image/gif_webp/webp/2.webp",
            "http://osscdn.ixingtu.com/musi_file/20181108/a20540641eb7de9a8bf186261a8ccf57.webp",
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

    private Transformation<Bitmap> mBitmapTrans = null;

    private Spinner mSpinner;
    private Menu mActionMenu;

    private int mImageType = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextView = (TextView) findViewById(R.id.webp_image_type);
        mRecyclerView = (RecyclerView) findViewById(R.id.webp_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

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

        if (id == R.id.static_webp_action) {
            mImageType = 1;
        } else if (id == R.id.alpha_webp_action) {
            mImageType = 2;
        } else if (id == R.id.animate_webp_action) {
            mImageType = 0;
        } else if (id == R.id.animate_gif_action) {
            mImageType = 3;
        }

        refreshImageData(mImageType);
        return true;
    }

    private List<String> getAnimatedWebpUrls() {
        List<String> webpUrls = new ArrayList<>(Arrays.asList(ANIM_WEBP));
        String resUrl = "android.resource://" + getPackageName() + "/" + R.drawable.broken;
        webpUrls.add(resUrl);
        return webpUrls;
    }

    private void refreshImageData(int imageType) {

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
}
