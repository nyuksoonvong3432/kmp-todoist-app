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
    
        @Published private(set) var tasks = [TaskEntityDTO]()
        
        func getAuthorizationURL() -> URL? {
            return AuthKoinHelper().getAuthorizationUrl()
        }
        
        func getTasks() async throws {
            self.tasks = try await TaskKoinHelper().getAll()
        }
        
        func onTaskCreated(_ task: TaskEntityDTO) {
            self.tasks.insert(task, at: 0)
        }
    }
}
