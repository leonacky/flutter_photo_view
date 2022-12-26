#import "PhotoViewPlugin.h"
#if __has_include(<photo_view/photo_view-Swift.h>)
#import <photo_view/photo_view-Swift.h>
#else
// Support project import fallback if the generated compatibility header
// is not copied when this plugin is created as a library.
// https://forums.swift.org/t/swift-static-libraries-dont-copy-generated-objective-c-header/19816
#import "photo_view-Swift.h"
#endif

@implementation PhotoViewPlugin
+ (void)registerWithRegistrar:(NSObject<FlutterPluginRegistrar>*)registrar {
  [SwiftPhotoViewPlugin registerWithRegistrar:registrar];
}
@end
