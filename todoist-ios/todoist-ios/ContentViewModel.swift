//
//  ContentViewModel.swift
//  todoist-ios
//
//  Created by nyuksoon.vong on 12/6/25.
//

import Foundation
import TodoistCore

extension ContentView {
    final class ViewModel: ObservableObject {
        
        private let authService = Authentication.companion.getInstance()
        private let taskService = Tasks.companion.getInstance()

        @Published private(set) var tasks = [TaskEntityDTO]()
        
        func getAuthorizationURL() -> URL? {
            let path = authService.getAuthorizationUrl().encodedPath
            return URL(string: path)
        }
        
        func getTasks() async throws {
            self.tasks = try await taskService.getAll()
        }
        
        func onTaskCreated(_ task: TaskEntityDTO) {
            self.tasks.insert(task, at: 0)
        }
    }
}
