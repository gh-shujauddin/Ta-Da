# Ta-Da! - Turn your to-dos into ta-das!

**Ta-Da!** is a minimalistic, offline-first ToDo app built with **Jetpack Compose** and powered by a **Spring Boot backend**. Designed for learning and experimentation, this app demonstrates best practices in local-first data persistence, JWT-based authentication, and clean Android architecture.

> 💡 The project is currently undergoing a renovation to simplify the architecture and improve maintainability with a clean local-first approach before reintroducing backend sync and auth.

---

## 🚀 Tech Stack

| Layer             | Tech Used                             |
|-------------------|----------------------------------------|
| UI                | Jetpack Compose                        |
| Architecture      | MVVM + Repository Pattern             |
| Dependency Injection | Dagger-Hilt                       |
| Local DB          | Room Database                          |
| Auth Storage      | SharedPreferences (JWT)    |
| Background Tasks  | WorkManager                            |
| API calls         | Retrofit + OkHttp                      |
| Backend           | Spring Boot (JWT Authentication)       |

---

## 📦 Project Structure

```bash
ta-da/
├── android-app/          # 📱 Android app (Jetpack Compose + Room + WorkManager)
├── backend-api/          # 🌐 Spring Boot backend (JWT Auth + REST API)
├── docs/                 # 📄 Diagrams or system design planning documents
├── .gitignore
├── LICENSE
└── README.md             # 👋 You're here
```

## Renovation plan
1. Clean up basic UI and re-align with MVVM principles.
2. Set up Room DB with TaskEntity, TaskDao, and AppDatabase.
3. Implement Repository pattern for local DB access.
4. Configure Dagger-Hilt for dependency injection.
5. Restructure the ViewModel for task management with StateFlow.
6. Connect the UI layer and composables with the updated entities or models\
7. Connect ViewModel with Room DB using clean MVVM pattern.
8. Test the working and debug
9. Integrate WorkManager for background sync jobs.
10. Scaffold backend with Spring Boot (JWT Auth, REST APIs).
11. Integrate login with JWT-based auth with both email/password combination and google login.
12. Store JWT token using SharedPreferences.
13. Setup backend sync mechanism (WorkManager + API calls).
14. Add support for task conflict resolution on sync (basic logic).
15. Add light/dark theme support.
16. Add error handling and empty state UI.
17. Add unit + UI tests (JUnit, Compose UI Test).

---

## Screenshots
| Home screen | Add task | Edit task |
|-------------|----------|-----------|
| <img src="https://github.com/gh-shujauddin/To-Do_App/assets/73093103/53c3f72b-81f3-4682-9d1e-4a2a1fc93c8c" width="250"/> | <img src="https://github.com/gh-shujauddin/To-Do_App/assets/73093103/6b249ae1-93a5-4be9-a79e-9affe95fed6f" width="250"/> | <img src="https://github.com/gh-shujauddin/To-Do_App/assets/73093103/13a457ec-0dc9-45ca-acd0-1a9e199f7577" width="250"/> |

---

> Made with 💙 for learning Android architecture the right way.
