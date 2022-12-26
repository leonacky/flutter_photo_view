package com.aotasoft.photo_view;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;

import java.util.Map;

import io.flutter.plugin.common.BinaryMessenger;
import io.flutter.plugin.platform.PlatformView;

public class PhotoView implements PlatformView {
  private final ViewGroup mainView;
  private final ImageView imageView;

  PhotoView(Context context, BinaryMessenger messenger, int id, Map<String, Object> params) {
    LayoutInflater layoutInflater = LayoutInflater.from(context);
    mainView = (ViewGroup) layoutInflater.inflate(R.layout.photo_view, null);
    imageView = mainView.findViewById(R.id.image);
//    methodChannel = new MethodChannel(messenger, "plugins.aotasoft.com/mupdf_" + id);
//    methodChannel.setMethodCallHandler(this);

    if (params.get("src") != null) {
      String src = (String) params.get("src");
      Glide.with(getView().getContext())
          .load(src)
          .into(imageView);
    }
  }

  @Nullable
  @Override
  public View getView() {
    if (mainView == null)
      Log.d("tuandv", "document null");
    return mainView;
  }

  @Override
  public void dispose() {

  }
}
