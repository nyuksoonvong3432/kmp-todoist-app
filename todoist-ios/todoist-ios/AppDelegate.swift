//
//  AppDelegate.swift
//  todoist-ios
//
//  Created by nyuksoon.vong on 12/6/25.
//

import Foundation
import TodoistCore
import UIKit

class AppDelegate: NSObject, UIApplicationDelegate {
    func application(_ application: UIApplication, didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey : Any]? = nil) -> Bool {
        return true
    }
    
    func application(_ app: UIApplication, open url: URL, options: [UIApplication.OpenURLOptionsKey : Any] = [:]) -> Bool {
        
        let urlComponent = URLComponents(url: url, resolvingAgainstBaseURL: false)
        
        if urlComponent?.path.contains("access_token") == true {
            guard let code = urlComponent?.queryItems?.first(where: { $0.name == "code" })?.value,
                  let state = urlComponent?.queryItems?.first(where: { $0.name == "state" })?.value
            else {
                print("Code and state not found")
                return false
            }
            
            Task {
                do {
                    try await AuthKoinHelper().authenticate(
                        state: state,
                        code: code,
                        redirectUrlPath: "com.example.todoist-ios://authorization"
                    )
                } catch {
                    print("Failed to authenticate: \(error.localizedDescription)")
                }
            }
            return true
        }
            
        return false
    }
}
