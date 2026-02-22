import SwiftUI
import ComposeApp

@main
struct iOSApp: App {

    init() {
        KoinInitializer().start()
    }

    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}
