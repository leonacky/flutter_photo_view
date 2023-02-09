import 'package:flutter/foundation.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

class PhotoView extends StatelessWidget {
  const PhotoView({
    Key? key,
    required this.src,
  }) : super(key: key);

  final String src;
  final String viewType = 'plugins.aotasoft.com/photoview';

  void _onPlatformViewCreated(int id) {}

  Widget buildNativeView() {
    final Map<String, dynamic> creationParams = <String, dynamic>{'src': src};
    switch (defaultTargetPlatform) {
      case TargetPlatform.android:
        return AndroidView(
          viewType: viewType,
          layoutDirection: TextDirection.ltr,
          creationParams: creationParams,
          onPlatformViewCreated: _onPlatformViewCreated,
          creationParamsCodec: const StandardMessageCodec(),
        );
      case TargetPlatform.iOS:
        return UiKitView(
          viewType: viewType,
          layoutDirection: TextDirection.ltr,
          creationParams: creationParams,
          onPlatformViewCreated: _onPlatformViewCreated,
          creationParamsCodec: const StandardMessageCodec(),
        );
      default:
        throw UnsupportedError('Unsupported platform view');
    }
  }

  @override
  Widget build(BuildContext context) {
    return Container(
      child: buildNativeView(),
    );
  }
}
