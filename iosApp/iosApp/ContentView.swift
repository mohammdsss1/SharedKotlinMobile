import SwiftUI
import SharedSpin

struct ContentView: View {
	let greet = Greeting().greeting()
    let repository = CityBikesRepository()

	var body: some View {
		Text(greet)
            .onAppear() {
                repository.fetchNetworkListNew(success: { data in
                    print(Date(), "completionHandler")
                    print(#function, data)
                }) { (result, error) in
                    if let errorReal = error {
                        print(errorReal)
                    }
                }
                
//                repository.fetchNetworkList { networks in
//                    print(Date(), "completionHandler")
//                    print(#function, networks)
//                } completionHandler: { t, error in
//                    print(Date(), "completionHandler")
//                }
            }
	}
}

struct ContentView_Previews: PreviewProvider {
	static var previews: some View {
	ContentView()
	}
}

class AppDelegate: UIResponder, UIApplicationDelegate {
    func application(_ application: UIApplication, didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey: Any]?) -> Bool {
        KoinKt.doInitKoin()
        return true
    }
}

