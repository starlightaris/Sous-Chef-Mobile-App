# 🍳 SousChef — Smart Recipe Navigator

SousChef is a lightweight Android cooking assistant that helps users follow recipes step-by-step without losing track.
It focuses on speed, clarity, and offline usability rather than being a heavy recipe management app.

Recipes are stored locally in JSON, navigated using a custom doubly linked list, and enhanced with smart timers, scaling, and unit conversion.

## ✨ Features

- 📝 Add and save recipes locally
- 📂 Load recipes instantly
- 🧭 Step-by-step navigation
- ⏱️ Built-in cooking timer with looping alarm
- 🔢 Fixed multipliers (1×, 2×, 3×…)
- 🌍 UK ↔ US unit conversion toggle
- 🗑️ Swipe-to-delete recipes
- 📊 Progress tracking
- 📶 Fully offline

## 🧠 How It Works

SousChef uses a custom Doubly Linked List (DLL) to manage recipe steps efficiently.

Why DLL?

- O(1) next/previous navigation
- Memory-efficient step traversal
- Perfect for guided cooking flows

## 🏗️ Project Structure
```
com.nibm.souschef
│
├── algorithm
│   ├── ConversionHelper.java
│   └── ScalingHelper.java
│
├── model
│   ├── Recipe.java
│   ├── RecipeDLL.java
│   └── RecipeRepository.java
│
└── ui
    ├── MainActivity.java
    ├── AddRecipeActivity.java
    ├── NavigatorActivity.java
    └── RecipeAdapter.java
```

## 📱 Screens

### 🏠 Recipe List
- Displays saved recipes
- Swip left/right to delete
- Tap to open recipe

### ➕ Add Recipe
- Title input
- Recipe text input
- Save locally as JSON

### 🧭 Navigator
- Current / Previous / Next step
- Progress bar
- Timer with alarm
- Multiplier buttons
- UK/US toggle

### ⏱️ Smart Timer
- User enters hours / minutes / seconds
- Button transforms:
```Start Timer → Stop Timer (red)```
- Alarm loops continuously
- Stops only when user presses Stop
- Navigation locked while timer runs
- Button restores to default purple after stop

### 💾 Local Storage
Recipes are saved in internal storage as JSON.

Stored data:
- Recipe title
- Instructions
- Parsed steps

This ensures:
- ⚡ Fast loading
- 🔒 Offline reliability
- 📱 No internet required

## 🛠️ Tech Stack
- Language: Java
- Platform: Android SDK 24+
- UI: XML + RecyclerView
- Storage: Local JSON
- Core DS: Doubly Linked List

## ⚠️ Known Limitations
- No cloud sync
- No user authentication
- Limited unit conversion coverage
- Single-device storage

## 🔮 Future Improvements
- 🎤 Voice-guided cooking
- ☁️ Cloud backup
- 🔍 Recipe search
- 🌙 Dark mode polish
- 🧠 Smarter ingredient parsing
- ⏱️ Multiple timers

## 👥 Team
- [SENULYA. P. H. P](https://github.com/starlightaris)
- [RUSHDEE. M. I](https://github.com/Imaadh-Rushdee)
- [RIZNI. M. F. M](https://github.com/rizinthehub)
- [HEENKENDA. S. J](https://github.com/Shalin2110)

## 📜 License

This project is developed for academic purposes.
