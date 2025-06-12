//
//  ContentView.swift
//  todoist-ios
//
//  Created by nyuksoon.vong on 12/6/25.
//

import SwiftUI
import TodoistCore

struct ContentView: View {
    
    @StateObject private var viewModel = ViewModel()
    @State private var showingCreateForm = false
    @State private var error: String?
    
    var body: some View {
        NavigationStack {
            List {
                ForEach(viewModel.tasks, id: \.self) { task in
                    VStack {
                        Text(task.content)
                        Text(task.description())
                    }
                }
            }.task {
                await loadData()
            }
            .alert(
                "Something went wrong.",
                isPresented: .init(
                    get: { error != nil },
                    set: { _ in error = nil }
                ),
                actions: {
                    Button("OK") {
                        error = nil
                    }
                },
                message: {
                    Text(error ?? "-")
                }
            )
            .padding()
            .toolbar {
                ToolbarItem(placement: .topBarTrailing) {
                    Button("", systemImage: "plus.app.fill") {
                        self.showingCreateForm = true
                    }
                }
            }
            .sheet(isPresented: $showingCreateForm) {
                FormView(
                    onCreated: { created in
                        viewModel.onTaskCreated(created)
                        self.showingCreateForm = false
                    }
                )
            }
        }
    }
    
    private func loadData() async {
        do {
            try await viewModel.getTasks()
        } catch {
            self.error = error.localizedDescription
        }
    }
}

#Preview {
    ContentView()
}
