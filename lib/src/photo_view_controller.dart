import 'dart:convert';

import 'package:flutter/services.dart';

class PhotoViewController {
  late MethodChannel _channel;
  static PhotoViewController? _controller;

  PhotoViewController({required int id}) {
    _channel = MethodChannel('plugins.aotasoft.com/photoview_$id');
    _channel.setMethodCallHandler(_handleMethodCalls);
    init();
  }

  init() async {}

  Future<dynamic> _handleMethodCalls(MethodCall call) async {
    switch (call.method) {
      case 'onChangePage':
        break;
      default:
        print('Unknowm method ${call.method} ');
        break;
    }
    return Future.value();
  }

  _presentWithUrls({required List<String> photos, int position = 0}) async {
    try {
      await _channel.invokeMethod('presentWithUrls', {'photos': json.encode(photos), 'position': position});
    } on PlatformException catch (e) {
      print('${e.code}: ${e.message}');
    }
  }

  static presentWithUrls({required List<String> photos, int position = 0}) async {
    _controller ??= PhotoViewController(id: 0);
    await _controller?._presentWithUrls(photos: photos, position: position);
  }
}
