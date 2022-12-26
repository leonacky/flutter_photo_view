package com.aotasoft.photo_view;

import android.content.Context;

import androidx.annotation.Nullable;

import java.util.Map;

import io.flutter.plugin.common.BinaryMessenger;
import io.flutter.plugin.common.StandardMessageCodec;
import io.flutter.plugin.platform.PlatformView;
import io.flutter.plugin.platform.PlatformViewFactory;

public class PhotoViewFactory extends PlatformViewFactory {
  private final BinaryMessenger messenger;

  public PhotoViewFactory(BinaryMessenger messenger) {
    super(StandardMessageCodec.INSTANCE);
    this.messenger = messenger;
  }

  @Override
  public PlatformView create(Context context, int viewId, @Nullable Object args) {
    Map<String, Object> params = (Map<String, Object>) args;
    return new PhotoView(context, messenger, viewId, params);
  }
}
