package com.aotasoft.photo_view;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.util.JsonReader;

import androidx.annotation.NonNull;

import org.json.JSONArray;
import org.json.JSONException;

import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;

/**
 * PhotoViewPlugin
 */
public class PhotoViewPlugin implements FlutterPlugin, MethodCallHandler {
  /// The MethodChannel that will the communication between Flutter and native Android
  ///
  /// This local reference serves to register the plugin with the Flutter Engine and unregister it
  /// when the Flutter Engine is detached from the Activity
  private MethodChannel channel;
  private Context context;

  @Override
  public void onAttachedToEngine(@NonNull FlutterPluginBinding binding) {

    binding
        .getPlatformViewRegistry()
        .registerViewFactory("plugins.aotasoft.com/photoview", new PhotoViewFactory(binding.getBinaryMessenger()));

    channel = new MethodChannel(binding.getBinaryMessenger(), "plugins.aotasoft.com/photoview_0");
    channel.setMethodCallHandler(this);
    context = binding.getApplicationContext();
  }

  @Override
  public void onMethodCall(@NonNull MethodCall call, @NonNull Result result) {
    if (call.method.equals("presentWithUrls")) {
      String photos = call.argument("photos");
      try {
        Intent intent = new Intent(context, BrowserPhotoViewActivity.class);
        intent.putExtra("photos", photos);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
      } catch (Exception e) {
        e.printStackTrace();
      }
      result.success("");
    } else {
      result.notImplemented();
    }
  }

  @Override
  public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
    channel.setMethodCallHandler(null);
  }
}
