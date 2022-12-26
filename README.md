<!--
This README describes the package. If you publish this package to pub.dev,
this README's contents appear on the landing page for your package.

For information about how to write a good package README, see the guide for
[writing package pages](https://dart.dev/guides/libraries/writing-package-pages).

For general information about developing packages, see the Dart guide for
[creating packages](https://dart.dev/guides/libraries/create-library-packages)
and the Flutter guide for
[developing packages and plugins](https://flutter.dev/developing-packages).
-->

A state builder.

## Features

PhotoView aims to help produce an easily usable implementation of a zooming ImageView.

Android use: https://github.com/Baseflow/PhotoView

iOS use: https://github.com/suzuki-0000/SKPhotoBrowser and https://github.com/huynguyencong/ImageScrollView

## Getting started

Run this command:
```
$ flutter pub add flutter_photo_view
```

This will add a line like this to your package's pubspec.yaml (and run an implicit dart pub get):
```
dependencies:
    flutter_photo_view: ^1.0.0
```

Alternatively, your editor might support dart pub get or flutter pub get. Check the docs for your editor to learn more.

## Usage

```dart
import 'package:photo_view/flutter_photo_view.dart';

PhotoViewController.presentWithUrls(
  photos: ['https://placehold.jp/150x150.png', 'https://placehold.jp/160x160.png']
);
```

## Additional information
Email: leonacky@gmail.com
