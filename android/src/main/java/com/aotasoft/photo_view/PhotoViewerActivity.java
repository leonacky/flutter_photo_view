package com.aotasoft.photo_view;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.OnScaleChangedListener;
import com.github.chrisbanes.photoview.PhotoView;

import org.json.JSONArray;
import org.json.JSONException;

public class PhotoViewerActivity extends AppCompatActivity {
  private static final int SWIPE_MIN_DISTANCE = 60;
  private static final int SWIPE_THRESHOLD_VELOCITY = 100;
  private int dismissPathLength;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    dismissPathLength = getResources().getDimensionPixelSize(R.dimen.dismiss_path_length);
    String photos = getIntent().getStringExtra("photos");
    setContentView(R.layout.view_pager);
    ViewPager viewPager = findViewById(R.id.view_pager);
    findViewById(R.id.btnClose).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        finish();
      }
    });
    PhotoViewPagerAdapter adapter = new PhotoViewPagerAdapter(viewPager);
    viewPager.setAdapter(adapter);
    try {
      JSONArray arr = new JSONArray(photos);
      adapter.setData(arr);
    } catch (JSONException e) {
      e.printStackTrace();
    }
    VerticalDragLayout dragLayout = findViewById(R.id.dragLayout);
    View backgroundColorView = findViewById(R.id.backgroundColorView);
    dragLayout.setOnDragListener(new VerticalDragLayout.OnDragListener() {
      @Override
      public void onDrag(float dy) {
        backgroundColorView.setAlpha(1f - Math.min(Math.abs(dy / (3 * dismissPathLength)), 1f));
        viewPager.setTranslationY(-dy);
      }
    });
    dragLayout.setOnReleaseDragListener(new VerticalDragLayout.OnReleaseDragListener() {
      @Override
      public void onReleaseDrag(float dy) {
        if (Math.abs(dy) > dismissPathLength) {
          viewPager.setVisibility(View.GONE);
          finish();
        } else {
          backgroundColorView.setAlpha(1f);
          viewPager.setTranslationY(0f);
        }
      }
    });
  }

  class PhotoViewPagerAdapter extends PagerAdapter {
    //    private final String[] sDrawables = {"https://placehold.jp/150x150.png", "https://placehold.jp/150x150.png", "https://placehold.jp/150x150.png"};
    ViewPager pager;

    public PhotoViewPagerAdapter(ViewPager pager) {
      super();
      this.pager = pager;
    }

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
      photoView.setOnScaleChangeListener(new OnScaleChangedListener() {
        @Override
        public void onScaleChange(float scaleFactor, float focusX, float focusY) {
          if (pager != null && pager instanceof HackyViewPager)
            ((HackyViewPager) pager).setPagingEnable(scaleFactor <= 1);
        }
      });
//      photoView.setOnSingleFlingListener(new OnSingleFlingListener() {
//        @Override
//        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
//          if (e1.getY() - e2.getY() > SWIPE_MIN_DISTANCE
//              && Math.abs(velocityY) > SWIPE_THRESHOLD_VELOCITY) {
////            onBottomToTop();
//            finish();
//            return true;
//          } else if (e2.getY() - e1.getY() > SWIPE_MIN_DISTANCE
//              && Math.abs(velocityY) > SWIPE_THRESHOLD_VELOCITY) {
////            onTopToBottom();
//            finish();
//            return true;
//          }
//          return false;
//        }
//      });
      try {
        Glide.with(PhotoViewerActivity.this)
            .load(sDrawables.getString(position))
            .into(photoView);
      } catch (Exception e) {
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
