package com.aotasoft.photo_view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.viewpager.widget.ViewPager;

public class HackyViewPager extends ViewPager {

  boolean isPagingEnable = true;

  public HackyViewPager(Context context) {
    super(context);
  }

  public HackyViewPager(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  public void setPagingEnable(boolean pagingEnable) {
    isPagingEnable = pagingEnable;
  }

  @Override
  public boolean onTouchEvent(MotionEvent ev) {
    return isPagingEnable && super.onTouchEvent(ev);
  }

  @Override
  public boolean onInterceptTouchEvent(MotionEvent ev) {
    try {
      return isPagingEnable && super.onInterceptTouchEvent(ev);
    } catch (IllegalArgumentException e) {
      return false;
    }
  }
}