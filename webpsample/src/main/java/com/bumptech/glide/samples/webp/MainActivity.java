package com.bumptech.glide.samples.webp;


import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.Arrays;

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



  private TextView mTextView;
  private RecyclerView mRecyclerView;
  private WebpImageAdapter mWebpAdapter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    mTextView = (TextView) findViewById(R.id.webp_image_type);
    mRecyclerView = (RecyclerView) findViewById(R.id.webp_recycler_view);
    mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    mWebpAdapter = new WebpImageAdapter(this, Arrays.asList(ANIM_WEBP));
    mRecyclerView.setAdapter(mWebpAdapter);
  }

  @Override
  protected void onStart() {
    super.onStart();
  }


  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.menu_main, menu);
    return super.onCreateOptionsMenu(menu);
  }


  @Override
  public boolean onOptionsItemSelected(MenuItem item) {

    int id = item.getItemId();
    item.setChecked(!item.isChecked());
    if (!item.isChecked()) {
      return super.onOptionsItemSelected(item);
    }

    if (id == R.id.static_webp_action) {
      mTextView.setText("static lossy webp");
      mWebpAdapter.updateData(Arrays.asList(SIMPLE_WEBP));
    } else if (id == R.id.alpha_webp_action) {
      mTextView.setText("static lossless (with alpha) webp");
      mWebpAdapter.updateData(Arrays.asList(ALPHA_WEBP));
    } else if(id == R.id.animate_webp_action) {
      mTextView.setText("animated webp");
      mWebpAdapter.updateData(Arrays.asList(ANIM_WEBP));
    }

    return true;
  }

}
