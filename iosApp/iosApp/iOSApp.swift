import UIKit
import SwiftUI
import ComposeApp

@main
struct iOSApp: App {

    init() {
        KoinInitializer().start()
        KeyboardWarmup.shared.warmup()
    }

    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}

// Pre-initializes the iOS keyboard to avoid first-focus lag in Compose TextField
final class KeyboardWarmup {
    static let shared = KeyboardWarmup()
    private init() {}

    func warmup() {
        DispatchQueue.main.async {
            let window = UIWindow(frame: .zero)
            let textField = UITextField()
            textField.autocorrectionType = .no
            window.addSubview(textField)
            textField.becomeFirstResponder()
            textField.resignFirstResponder()
            textField.removeFromSuperview()
        }
    }
}
