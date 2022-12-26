package com.aotasoft.photo_view;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.PhotoView;

import org.json.JSONArray;
import org.json.JSONException;

public class BrowserPhotoViewActivity extends AppCompatActivity {
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    String photos = getIntent().getStringExtra("photos");
    setContentView(R.layout.activity_view_pager);
    ViewPager viewPager = findViewById(R.id.view_pager);
    findViewById(R.id.btnClose).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        finish();
      }
    });
    PhotoViewPagerAdapter adapter = new PhotoViewPagerAdapter();
    viewPager.setAdapter(adapter);
    try {
      JSONArray arr = new JSONArray(photos);
      adapter.setData(arr);
    } catch (JSONException e) {
      e.printStackTrace();
    }
  }

  class PhotoViewPagerAdapter extends PagerAdapter {

//    private final String[] sDrawables = {"https://placehold.jp/150x150.png", "https://placehold.jp/150x150.png", "https://placehold.jp/150x150.png"};
    private JSONArray sDrawables = new JSONArray();
    @Override
    public int getCount() {
      return sDrawables.length();
    }

    public void setData(JSONArray arr) {
      sDrawables = arr;
      notifyDataSetChanged();
    }

    @Override
    public View instantiateItem(ViewGroup container, int position) {
      PhotoView photoView = new PhotoView(container.getContext());
      try {
        Glide.with(BrowserPhotoViewActivity.this)
            .load(sDrawables.getString(position))
            .into(photoView);
      } catch (JSONException e) {
        e.printStackTrace();
      }
      container.addView(photoView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
      return photoView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
      container.removeView((View) object);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
      return view == object;
    }
  }
}
