import SwiftUI
import SharedSpin

@main
struct iOSApp: App {
    @UIApplicationDelegateAdaptor(AppDelegate.self) var appDelegate

	var body: some Scene {
		WindowGroup {
			ContentView()
		}
	}
}
