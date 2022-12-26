import 'package:flutter/material.dart';
import 'package:photo_view/flutter_photo_view.dart';

void main() {
  runApp(const MyApp());
}

class MyApp extends StatefulWidget {
  const MyApp({super.key});

  @override
  State<MyApp> createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {

  @override
  void initState() {
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        body: Column(
          children: [
            Padding(
              padding: const EdgeInsets.only(top: 50),
              child: TextButton(onPressed: () {
                PhotoViewController.presentWithUrls(
                    photos: ['https://placehold.jp/150x150.png', 'https://placehold.jp/160x160.png']);
              }, child: const Text('show images')),
            ),
            const Expanded(child: PhotoView(src: 'https://placehold.jp/150x150.png'))
          ],
        ),
      ),
    );
  }
}
