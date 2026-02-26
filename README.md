# Minha Grana

A personal finance app built with **Kotlin Multiplatform**, using Compose Multiplatform for the UI, running on **Android** and **iOS**.

## Overview

The app allows users to record entries (income and expenses) by category, view balances by month/year, and track a summary on the home screen with a pie chart by category. Data is stored locally in SQLite (SQLDelight), with shared repositories and ViewModels.

## Figma

This project was developed from a structured design system, including typography choices, a defined color palette, and a custom logo to ensure visual consistency across Android and iOS.

<a href="https://www.figma.com/design/lBwuoc1jUqLHZgJ75wLBuG/minha-grana?m=auto&t=pOHWyosJFd6sjFze-1" target="_blank" rel="noopener noreferrer">Figma file</a>

## Screens

### Home

| Android | iOS |
| --- | --- |
| <img src="./screenshots/android/android_home.png" width="320" /> | <img src="./screenshots/ios/ios_home.PNG" width="320" /> |

### New Entry

| Android | iOS |
| --- | --- |
| <img src="./screenshots/android/android_newentry.png" width="320" /> | <img src="./screenshots/ios/ios_newentry.PNG" width="320" /> |

### Entries

| Android | iOS |
| --- | --- |
| <img src="./screenshots/android/android_entries.png" width="320" /> | <img src="./screenshots/ios/ios_entries.PNG" width="320" /> |

## Stack

- **Kotlin Multiplatform** + **Compose Multiplatform** (Material 3)
- **SQLDelight** – local SQLite database with type-safe queries
- **Koin** – dependency injection (commonMain, Android, iOS)
- **Kotlinx Serialization** – serializable routes and models
- **Navigation Compose** – typed route navigation

## Project Structure

- **[composeApp](./composeApp)** – shared KMP module:
  - **commonMain** – logic, UI, DB, repositories, ViewModels, themes
  - **androidMain** – `MainActivity`, SQLDelight Android driver, `CurrentTime`, Android DI
  - **iosMain** – `MainViewController`, SQLDelight iOS driver, `CurrentTime`, iOS DI
- **[iosApp](./iosApp)** – iOS app (entry point and Compose integration)

### Main Packages (commonMain)


### Database (SQLDelight)

Schema at `composeApp/src/commonMain/sqldelight/com/minhagrana/database/MinhaGranaDatabase.sq`:

- **UserEntity**
- **CategoryEntity**
- **YearEntity** 
- **MonthEntity** 
- **EntryEntity** 

Accessed via `DatabaseHelper` and repositories; initialization (default user/year/months, categories) handled by `DatabaseInitializer`.

## Build and Run

### Android

```bash
./gradlew :composeApp:assembleDebug
```

Use the Android Studio run configuration.

### iOS

Open the [iosApp](./iosApp) folder in Xcode and run.
