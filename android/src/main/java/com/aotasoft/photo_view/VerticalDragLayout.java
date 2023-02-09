package com.aotasoft.photo_view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.widget.FrameLayout;

public class VerticalDragLayout extends FrameLayout {

  private boolean draggingIsEnabled = true;
  private int touchSlop;
  private Boolean isDetectedVerticalMove = null;
  private float startX = 0f;
  private float startY = 0f;
  private float startInnerMoveY = 0f;

  private OnDragListener onDragListener;
  private OnReleaseDragListener onReleaseDragListener;

  public VerticalDragLayout(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    touchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
  }

  public VerticalDragLayout(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public VerticalDragLayout(Context context) {
    this(context, null);
  }

  public void setOnDragListener(OnDragListener listener) {
    this.onDragListener = listener;
  }

  public void setOnReleaseDragListener(OnReleaseDragListener listener) {
    this.onReleaseDragListener = listener;
  }

  public void setDraggingIsEnabled(boolean draggingIsEnabled) {
    this.draggingIsEnabled = draggingIsEnabled;
    reset();
  }

  private void reset() {
    isDetectedVerticalMove = null;
    startX = 0f;
    startY = 0f;
    startInnerMoveY = 0f;
  }

  @Override
  public boolean dispatchTouchEvent(MotionEvent ev) {
    switch (ev.getAction()) {
      case MotionEvent.ACTION_DOWN:
        if (ev.getPointerCount() == 1) {
          startX = ev.getX();
          startY = ev.getY();
        } else {
          isDetectedVerticalMove = null;
          startX = 0f;
          startY = 0f;
        }
        break;
      case MotionEvent.ACTION_UP:
      case MotionEvent.ACTION_CANCEL:
        isDetectedVerticalMove = null;
        startX = 0f;
        startY = 0f;
        break;
      case MotionEvent.ACTION_MOVE:
        if (draggingIsEnabled
            && isDetectedVerticalMove == null
            && ev.getPointerCount() == 1
            && Math.abs(startY - ev.getY()) > touchSlop) {
          isDetectedVerticalMove = getDirection(ev, startX, startY) == Direction.UP
              || getDirection(ev, startX, startY) == Direction.DOWN;
        }
        break;
    }
    return super.dispatchTouchEvent(ev);
  }

  @Override
  public boolean onInterceptTouchEvent(MotionEvent ev) {
    return ev.getPointerCount() == 1
        && ev.getAction() == MotionEvent.ACTION_MOVE
        && (isDetectedVerticalMove!=null && isDetectedVerticalMove == true);
  }

  @Override
  public boolean onTouchEvent(MotionEvent ev) {
    switch (ev.getAction()) {
      case MotionEvent.ACTION_UP:
      case MotionEvent.ACTION_CANCEL:
        if (ev.getPointerCount() == 1) {
          onReleaseDragListener.onReleaseDrag(startInnerMoveY - ev.getY());
        }
        startInnerMoveY = 0f;
        break;
      case MotionEvent.ACTION_MOVE:
        if (startInnerMoveY == 0f) {
          startInnerMoveY = ev.getY();
        }
        onDragListener.onDrag(startInnerMoveY - ev.getY());
        break;
    }
    return true;
  }

  private Direction getDirection(MotionEvent ev, float x, float y) {
    return Direction.get((int) getAngle(x, y, ev.getX(), ev.getY()));
  }

  private double getAngle(float x1, float y1, float x2, float y2) {
    double rad = Math.atan2((y1 - y2), (x2 - x1)) + Math.PI;
    return (rad * 180 / Math.PI + 180) % 360;
  }

  private enum Direction {
    UP, DOWN, LEFT, RIGHT;

    public static final float V_ANGLE = 30F;

    public static Direction get(double angle) {
      if (inRange(angle, 90f - V_ANGLE, 90f + V_ANGLE)) {
        return UP;
      } else if (inRange(angle, 0f, 90f - V_ANGLE) || inRange(angle, 270f + V_ANGLE, 360f)) {
        return RIGHT;
      } else if (inRange(angle, 270f - V_ANGLE, 270f + V_ANGLE)) {
        return DOWN;
      } else {
        return LEFT;
      }
    }

    private static boolean inRange(double angle, float init, float end) {
      return angle >= init && angle < end;
    }
  }

  interface OnReleaseDragListener {
    void onReleaseDrag(float dy);
  }

  interface OnDragListener {
    void onDrag(float dy);
  }
}