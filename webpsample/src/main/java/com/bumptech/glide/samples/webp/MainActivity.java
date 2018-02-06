package com.bumptech.glide.samples.webp;


import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

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
          "https://www.gstatic.com/webp/animated/1.webp",
          "https://mathiasbynens.be/demo/animated-webp-supported.webp",
          "https://isparta.github.io/compare-webp/image/gif_webp/webp/2.webp",
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
  private WebpImageAdapter mWebpAdapter;

  private Menu mActionMenu;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    mTextView = (TextView) findViewById(R.id.webp_image_type);
    mRecyclerView = (RecyclerView) findViewById(R.id.webp_recycler_view);
    mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

    List<String> imageList = new ArrayList<>();
    imageList.addAll(Arrays.asList(ANIM_WEBP));

    mWebpAdapter = new WebpImageAdapter(this, imageList);
    mRecyclerView.setAdapter(mWebpAdapter);
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
      mTextView.setText("static lossy webp");
      mWebpAdapter.updateData(Arrays.asList(SIMPLE_WEBP));
    } else if (id == R.id.alpha_webp_action) {
      mTextView.setText("static lossless (with alpha) webp");
      mWebpAdapter.updateData(Arrays.asList(ALPHA_WEBP));
    } else if(id == R.id.animate_webp_action) {
      mTextView.setText("animated webp");
      mWebpAdapter.updateData(Arrays.asList(ANIM_WEBP));
    } else if(id == R.id.animate_gif_action) {
      mTextView.setText("animated gif");
      mWebpAdapter.updateData(Arrays.asList(ANIM_GIF));
    }

    return true;
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
