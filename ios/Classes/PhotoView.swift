//
//  PhotoView.swift
//  photo_view
//
//  Created by Tuan Dinh on 23/12/2022.
//

import Flutter
import UIKit
import SDWebImage
import SKPhotoBrowser
import ImageScrollView

class PhotoViewFactory: NSObject, FlutterPlatformViewFactory {
    private var messenger: FlutterBinaryMessenger

    init(messenger: FlutterBinaryMessenger) {
        self.messenger = messenger
        super.init()
    }

    func create(
        withFrame frame: CGRect,
        viewIdentifier viewId: Int64,
        arguments args: Any?
    ) -> FlutterPlatformView {
        return PhotoView(
            frame: frame,
            viewIdentifier: viewId,
            arguments: args,
            binaryMessenger: messenger)
    }
    
    public func createArgsCodec() -> FlutterMessageCodec & NSObjectProtocol {
          return FlutterStandardMessageCodec.sharedInstance()
    }
}

class PhotoViewZoomable: UIView {
    var url: String
    var imageScrollView: ImageScrollView!
    
    init(url: String) {
        self.url = url
        imageScrollView = ImageScrollView()
        super.init(frame: .zero)
    }

    required init?(coder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }
    
    var viewController: UIViewController? {
      let viewController = sequence(first: self, next: { $0.next }).first(where: { $0 is UIViewController })
      return viewController as? UIViewController
    }
    
    override func draw(_ rect: CGRect) {
        super.draw(rect)
        self.loadImageWithUrl(url: url)
    }
    
    private func loadImageWithUrl(url: String) {
        let img = UIImageView(frame: self.superview!.frame)
        imageScrollView.frame = self.superview!.frame
        let imageURL = URL(string: url)!
        img.sd_setImage(with: imageURL) {_,_,_,_ in
            self.imageScrollView.display(image: (img.image)! )
        }
        self.addSubview(imageScrollView)
    }
    
//    private func addViewControllerAsSubview(_ vc: SKPhotoBrowser) {
//        browser = vc
//        browser!.view.frame = self.superview!.frame
//        self.viewController?.addChild(browser!)
//        let rootView = self.browser!.view!
//        self.addSubview(rootView)
//        self.viewController?.addChild(browser!)
//        //    self.readerViewController!.didMove(toParent: self.viewController!)
//
//        // bind the reader's view to be constrained to its parent
//        rootView.translatesAutoresizingMaskIntoConstraints = false
//        rootView.topAnchor.constraint(equalTo: self.topAnchor).isActive = true
//        rootView.bottomAnchor.constraint(equalTo: self.bottomAnchor).isActive = true
//        rootView.leftAnchor.constraint(equalTo: self.leftAnchor).isActive = true
//        rootView.rightAnchor.constraint(equalTo: self.rightAnchor).isActive = true
//    }
}

class PhotoView: NSObject, FlutterPlatformView {
    private var _view: PhotoViewZoomable

    init(
        frame: CGRect,
        viewIdentifier viewId: Int64,
        arguments args: Any?,
        binaryMessenger messenger: FlutterBinaryMessenger?
    ) {
        var _src = ""
        if let _args = args as? Dictionary<String, Any> {
            _src = (_args["src"] as? String) ?? ""
            print(_args["src"] ?? "")
        }
        _view = PhotoViewZoomable(url: _src)
        _view.clipsToBounds = true
        super.init()
    }

    func view() -> UIView {
        return _view
    }
}
