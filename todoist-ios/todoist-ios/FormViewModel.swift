//
//  FormViewModel.swift
//  todoist-ios
//
//  Created by nyuksoon.vong on 12/6/25.
//

import Foundation
import TodoistCore

extension FormView {
    final class ViewModel: ObservableObject {
        @Published var content = "" {
            didSet {
                guard self.error != nil else { return }
                self.validate()
            }
        }
        @Published var description = "" {
            didSet {
                guard self.error != nil else { return }
                self.validate()
            }
        }
        @Published var error: String?
        
        @MainActor
        func submit() async throws -> TaskEntityDTO? {
            guard validate() else { return nil }
            let created = try await TaskKoinHelper().create(
                payload: .init(
                    content: content,
                    description: description.isEmpty ? nil : description,
                    projectId: nil
                )
            )
            return created
        }
        
        @discardableResult
        private func validate() -> Bool {
            if content.isEmpty {
                error = "Content can't be empty"
                return false
            } else {
                error = nil 
            }
            return true
        }
    }
}
