package com.aotasoft.photo_view;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.embedding.engine.plugins.activity.ActivityAware;
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;

/**
 * PhotoViewPlugin
 */
public class PhotoViewPlugin implements FlutterPlugin, MethodCallHandler, ActivityAware {
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

      int position = 0;
      try {
        position = call.argument("position");
      } catch (Exception e) {
        e.printStackTrace();
      }
      try {
//        Intent intent = new Intent(context, BrowserPhotoViewActivity.class);
//        intent.putExtra("photos", photos);
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        context.startActivity(intent);
        if(mainActivity!=null) {
          FragmentManager manager = mainActivity.getSupportFragmentManager();
          PhotoViewerFragment.create(photos, position).show(manager, "gallery_show");
        }
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

  FragmentActivity mainActivity;
  @Override
  public void onAttachedToActivity(@NonNull ActivityPluginBinding binding) {
    if(binding.getActivity() instanceof FragmentActivity) {
      mainActivity = (FragmentActivity) binding.getActivity();
    }
  }

  @Override
  public void onDetachedFromActivityForConfigChanges() {
  }

  @Override
  public void onReattachedToActivityForConfigChanges(@NonNull ActivityPluginBinding binding) {
    if(binding.getActivity() instanceof FragmentActivity) {
      mainActivity = (FragmentActivity) binding.getActivity();
    }
  }

  @Override
  public void onDetachedFromActivity() {
  }
}
