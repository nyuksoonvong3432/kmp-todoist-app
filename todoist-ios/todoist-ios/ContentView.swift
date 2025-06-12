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
    @State private var isLoading = false
    
    private var dateFormmater: DateFormatter {
        let formatter = DateFormatter()
        formatter.locale = Locale(identifier: "en_US_POSIX")
        formatter.timeZone = TimeZone(secondsFromGMT: 0)
        formatter.dateFormat = "yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'"
        return formatter
    }
    
    var body: some View {
        NavigationStack {
            
            VStack(alignment: .leading, spacing: 18) {
                Text("📖 My tasks")
                    .font(.largeTitle)
                    
                if viewModel.isLoggedIn {
                    Button("Logout") {
                        logout()
                    }.disabled(isLoading)
                } else {
                    Button("Grant access") {
                        guard let authorizationURL = viewModel.getAuthorizationURL() else { return }
                        UIApplication.shared.open(authorizationURL, options: [:], completionHandler: nil)
                    }.disabled(isLoading)
                }
            }.padding(.horizontal)
            .frame(maxWidth: .infinity, alignment: .leading)
            
            List {
                if isLoading {
                    ProgressView()
                        .frame(maxWidth: .infinity, alignment: .center)
                }
                
                ForEach(viewModel.tasks, id: \.self) { task in
                    VStack(alignment: .leading) {
                        Text(task.content)
                            .font(.headline)
                        Text(task.description_)
                            .font(.subheadline)
                            .foregroundStyle(.gray)
                        if let date = parseDate(str: task.addedAt) {
                            Text(date.formatted())
                                .font(.subheadline)
                                .foregroundStyle(.gray)
                        }
                    }.frame(maxWidth: .infinity, alignment: .leading)
                    .frame(minHeight: 60)
                    .padding()
                    .clipShape(
                        RoundedRectangle(cornerRadius: 12.0)
                    )
                    .overlay(
                        RoundedRectangle(cornerRadius: 12.0)
                            .stroke(.white, lineWidth: 1.5)
                    )
                    .listRowInsets(.init(top: 0, leading: 0, bottom: 0, trailing: 0))
                    .padding()
                }
            }.listStyle(.plain)
            .task {
                await loadData()
            }
            .refreshable {
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
            .toolbar {
                ToolbarItem(placement: .topBarTrailing) {
                    if viewModel.isLoggedIn {
                        Button("", systemImage: "plus.app.fill") {
                            self.showingCreateForm = true
                        }
                    }
                }
            }
            .sheet(isPresented: $showingCreateForm) {
                FormView(
                    onCreated: { created in
                        viewModel.onTaskCreated(created)
                        self.showingCreateForm = false
                    }
                ).presentationDetents([.medium])
            }
        }.onOpenURL { url in
            self.handleDeeplink(url)
        }.colorScheme(.dark)
    }
    
    private func parseDate(str: String) -> Date? {
        dateFormmater.date(from: str)
    }
    
    private func loadData() async {
        defer { self.isLoading = false }
        do {
            self.isLoading = true
            try await viewModel.tryRestoreAccess()
            guard viewModel.isLoggedIn else { return }
            try await viewModel.getTasks()
        } catch {
            self.error = error.localizedDescription
        }
    }
    
    private func handleDeeplink(_ url: URL) {
        guard let urlComponent = URLComponents(url: url, resolvingAgainstBaseURL: false),
              urlComponent.scheme == "todoist-app",
              let code = urlComponent.queryItems?.first(where: { $0.name == "code" })?.value
        else {
            return
        }
        
        Task {
            defer { self.isLoading = false }
            do {
                self.isLoading = true
                try await viewModel.authenticate(clientSecret: Secrets.clientSecret, code: code)
                await self.loadData()
            } catch {
                print("Failed to authenticate: \(error.localizedDescription)")
            }
        }
    }
    
    private func logout() {
        Task {
            defer { self.isLoading = false }
            
            do {
                self.isLoading = true
                try await viewModel.logout()
            } catch {
                self.error = "Failed to logout"
            }
        }
    }
}

#Preview {
    ContentView()
}
