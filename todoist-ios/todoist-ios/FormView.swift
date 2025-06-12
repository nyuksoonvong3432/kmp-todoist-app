//
//  FormView.swift
//  todoist-ios
//
//  Created by nyuksoon.vong on 12/6/25.
//

import SwiftUI
import TodoistCore

struct FormView: View {
    @StateObject private var viewModel = ViewModel()
    @State private var isLoading = false
    let onCreated: (TaskEntityDTO) -> Void
    
    var body: some View {
        VStack {
            ScrollView {
                VStack(spacing: 28) {
                    VStack {
                        Text("Content")
                        TextField("Content", text: $viewModel.content)
                    }
                    VStack {
                        Text("Description")
                        TextField("Description", text: $viewModel.description)
                    }
                }
            }
            if let error = viewModel.error {
                Text(error)
                    .foregroundStyle(.red)
            }
            Button("Submit") {
                submit()
            }.disabled(isLoading)
        }.padding()
    }
    
    private func submit() {
        Task {
            defer { self.isLoading = false }
            do {
                self.isLoading = true
                guard let result = try await viewModel.submit() else { return }
                self.onCreated(result)
            } catch {
                print(error.localizedDescription)
            }
        }
    }
}

//#Preview {
//    FormView()
//}
