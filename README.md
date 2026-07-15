# RecipeBox

An Android app for browsing, adding, and saving recipes, built with Kotlin and Jetpack Compose.

## Features

- **Authentication** — sign up, sign in, forgot password (Firebase Auth), profile editing and account deletion
- **Recipes** — full CRUD (add, edit, delete, list), categories, favorites, and title search
- **Pantry** — full CRUD for a personal ingredient inventory, with an out-of-stock indicator

## Tech stack

- Kotlin, Jetpack Compose (Material 3)
- Firebase Authentication
- Firebase Realtime Database
- MVVM: `model` / `repo` (Firebase-backed) / `viewmodel` per feature, with `ComponentActivity` + `@Composable` screens

## Project structure

```
app/src/main/java/com/example/recipebox/
├── model/       # RecipeModel, PantryItemModel, UserModel
├── repo/        # Firebase-backed CRUD repositories (interface + impl per feature)
├── viewmodel/   # LiveData-based ViewModels per feature
└── *.kt         # Compose screens (Splash, Login, SignUp, Dashboard, Recipe/Pantry/Profile screens)
```
