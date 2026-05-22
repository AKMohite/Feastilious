# Migration Plan: Feast It (Views to Jetpack Compose)

This document tracks the progress of migrating the "Feast It" application from XML/Views to Jetpack Compose.

## Goals
- [x] Create a new `:app-compose` module.
- [x] Copy and decouple ViewModels from `:app`.
- [x] Modular package structure for features.
- [x] Adaptive UI for all screen sizes (BottomBar/NavRail).
- [ ] Comprehensive Unit and UI testing.

## Phase 1: Setup
- [x] Add Compose dependencies to `libs.versions.toml`.
- [x] Configure `:app-compose` module.
    - [x] Setup `build.gradle.kts` (Added Paging, Datastore, Hilt Work).
    - [x] Removed `:app` module dependency from `:app-compose`.
    - [x] Setup Hilt and Navigation.
    - [x] Implement Material 3 Theme (Colors, Type, Theme).
- [x] Define the package structure in `:app-compose`.
- [x] Migrate base classes (`BaseViewModel`).
- [x] Migrate utility classes (`FeastPrefManager`, `StringExt`, `AppExt`, `AppDispatcher`).
- [x] Migrate worker management (`WorkerScheduler`, `BackgroundScheduler`, `StaleDataWorker`, `MealPlanNotifyWorker`).
- [x] Migrate notification management (`NotificationManager`, `YumNotificationManager`, `PostNotificationBroadcastReceiver`).
- [x] Copy all resources (drawable, values, fonts, etc.) to `:app-compose`.
- [x] Update imports to use `:app-compose`'s resources (`com.ak.feastit.compose.R`).

## Phase 2: Feature Migration
- [x] **Splash & Onboarding**
    - [x] Migrate Splash screen and ViewModel.
    - [x] Migrate Onboarding screen and ViewModel.
- [ ] **Main Navigation & Home**
    - [x] Implement Bottom Navigation / Navigation Rail (Adaptive).
    - [ ] Migrate Explore/Home screen. (ViewModel and State copied, basic Screen implemented).
- [ ] **Search**
    - [ ] Migrate Search screen. (ViewModel and State copied, basic Screen implemented).
- [ ] **Collection**
    - [ ] Migrate Collection entry screen. (Screen implemented).
- [ ] **Favorites**
    - [ ] Migrate Favorites screen. (ViewModel and State copied).
- [ ] **Meal Planner**
    - [ ] Migrate Meal Planner. (ViewModel and State copied).
- [ ] **Cart (Shopping List)**
    - [ ] Migrate Shopping Cart. (ViewModel and State copied).
- [ ] **Recipe Details (YumDetail)**
    - [ ] Migrate Recipe Detail screen. (ViewModel and State copied).
- [ ] **View All**
    - [ ] Migrate View All screen. (ViewModel and State copied).

## Phase 3: Adaptive UI & Polish
- [x] Implement Window Size Class handling.
- [x] Ensure all screens are responsive.
- [ ] Refine animations and transitions.

## Phase 4: Testing
- [ ] Unit tests for ViewModels in `:app-compose`.
- [ ] UI tests for Compose screens.
- [ ] Snapshot/Screenshot testing (optional).

## Progress Notes
- All ViewModels and their dependencies (State, Workers, DI modules, Utils) have been copied to `:app-compose`.
- The `:app-compose` module is now decoupled from the original `:app` module.
- Resources have been duplicated to `:app-compose` to maintain independence.
- Next steps: Fix minor compilation errors in ViewModels (mostly missing imports or small API tweaks) and build out the UI screens.
