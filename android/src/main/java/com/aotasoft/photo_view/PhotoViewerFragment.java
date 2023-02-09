package com.aotasoft.photo_view;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.OnScaleChangedListener;
import com.github.chrisbanes.photoview.PhotoView;

import org.json.JSONArray;

public class PhotoViewerFragment extends DialogFragment {
  private int dismissPathLength;
  TextView tvPosition;

  static PhotoViewerFragment create(String data, int position) {
    PhotoViewerFragment fragment = new PhotoViewerFragment();
    Bundle bundle = new Bundle();
    bundle.putString("photos", data);
    bundle.putInt("position", position);
    fragment.setArguments(bundle);
    return fragment;
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setStyle(DialogFragment.STYLE_NORMAL, R.style.Dialog_FullScreen);
    Dialog dialog = getDialog();
    if (dialog != null) {
      dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
      dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }
  }

  @Override
  public Dialog onCreateDialog(Bundle savedInstanceState) {
    Dialog dialog = super.onCreateDialog(savedInstanceState);
    dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
    int width = ViewGroup.LayoutParams.MATCH_PARENT;
    int height = ViewGroup.LayoutParams.MATCH_PARENT;
    dialog.getWindow().setLayout(width, height);
    return dialog;
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View viewGroup = inflater.inflate(R.layout.view_pager, container, false);
    dismissPathLength = getResources().getDimensionPixelSize(R.dimen.dismiss_path_length);
    String photos = getArguments().getString("photos");
    int initPosition = getArguments().getInt("position", 0);

    tvPosition = viewGroup.findViewById(R.id.position);
    final HackyViewPager viewPager = viewGroup.findViewById(R.id.view_pager);
    final VerticalDragLayout dragLayout = viewGroup.findViewById(R.id.dragLayout);
    final View backgroundColorView = viewGroup.findViewById(R.id.backgroundColorView);

    viewGroup.findViewById(R.id.btnClose).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        dismiss();
      }
    });
    final PhotoViewPagerAdapter adapter = new PhotoViewPagerAdapter();
    viewPager.setAdapter(adapter);
    adapter.setOnScaleListener(new OnScaleListener() {
      @Override
      public void onScale(boolean isZoomIn) {
          viewPager.setPagingEnable(!isZoomIn);
          dragLayout.setDraggingIsEnabled(!isZoomIn);
      }
    });
    try {
      JSONArray arr = new JSONArray(photos);
      if (initPosition < 0) initPosition = 0;
      if (initPosition > arr.length() - 1) initPosition = arr.length() - 1;
      tvPosition.setText((initPosition + 1) + "/" + arr.length());
      adapter.setData(arr);
      if (initPosition > 0) {
        viewPager.setCurrentItem(initPosition);
      }
      viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageSelected(int position) {
          tvPosition.setText((position + 1) + "/" + adapter.getCount());
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
      });
    } catch (Exception e) {
      e.printStackTrace();
    }
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
          dismiss();
        } else {
          backgroundColorView.setAlpha(1f);
          viewPager.setTranslationY(0f);
        }
      }
    });
    return viewGroup;
  }

  class PhotoViewPagerAdapter extends PagerAdapter {
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
      photoView.setBackgroundColor(Color.TRANSPARENT);
      photoView.setOnScaleChangeListener(new OnScaleChangedListener() {
        @Override
        public void onScaleChange(float scaleFactor, float focusX, float focusY) {
          if(onScaleListener!=null) {
            onScaleListener.onScale(scaleFactor > 1);
          }
        }
      });
      try {
        Glide.with(getActivity())
            .load(sDrawables.getString(position))
            .into(photoView);
      } catch (Exception e) {
        e.printStackTrace();
      }
      container.setBackgroundColor(Color.TRANSPARENT);
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

    OnScaleListener onScaleListener;
    void setOnScaleListener(OnScaleListener onScaleListener) {
      this.onScaleListener = onScaleListener;
    }
  }

  static interface OnScaleListener {
    void onScale(boolean isZoomIn);
  }
}
