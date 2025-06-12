//
//  todoist_iosApp.swift
//  todoist-ios
//
//  Created by nyuksoon.vong on 12/6/25.
//

import SwiftUI
import TodoistCore

@main
struct todoist_iosApp: App {
    @UIApplicationDelegateAdaptor(AppDelegate.self) var appDelegate
    
    init() {
        KoinHelper().doInitKoin()
    }
    
    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}
