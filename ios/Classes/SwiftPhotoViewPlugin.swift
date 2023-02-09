import Flutter
import UIKit
import SKPhotoBrowser

public class SwiftPhotoViewPlugin: NSObject, FlutterPlugin {
    public static func register(with registrar: FlutterPluginRegistrar) {
        let factory = PhotoViewFactory(messenger: registrar.messenger())
        registrar.register(factory, withId: "plugins.aotasoft.com/photoview")
        
        let channel = FlutterMethodChannel(name: "plugins.aotasoft.com/photoview_0", binaryMessenger: registrar.messenger())
        let instance = SwiftPhotoViewPlugin()
        registrar.addMethodCallDelegate(instance, channel: channel)
    }
    
    func presentWithUrls(photos: String, position: Int) {
        var images = [SKPhoto]()
        let decoder = JSONDecoder()
        do {
            let arr = try decoder.decode([String].self, from: photos.data(using: .utf8)!)
            print(arr)
            arr.forEach { url in
                let photo = SKPhoto.photoWithImageURL(url)
                photo.shouldCachePhotoURLImage = true
                images.append(photo)
            }
        } catch {
            print(error.localizedDescription)
        }

        let browser = SKPhotoBrowser(photos: images)
        browser.initializePageIndex(position)

        let viewController: UIViewController =
                    (UIApplication.shared.delegate?.window??.rootViewController)!;
        viewController.present(browser, animated: true)
    }

  public func handle(_ call: FlutterMethodCall, result: @escaping FlutterResult) {
      switch call.method {
      case "presentWithUrls":
          var initPosition = 0
          guard let argMaps = call.arguments as? Dictionary<String, Any>,
                let photos = argMaps["photos"] as? String else {
              result(FlutterError(code: call.method, message: "Missing argument", details: nil))
              return
          }
          if let argMaps = call.arguments as? Dictionary<String, Any>, let position = argMaps["position"] as? Int {
              initPosition = position
          }
          presentWithUrls(photos: photos, position: initPosition)
      default:
//          result(FlutterMethodNotImplemented)
          result(FlutterError(code: call.method, message: "Method not implemented", details: nil))
      }
  }
}
