#  Personal Event Planner App
### SIT708 - Task 4.1 | Android Development

An Android application that allows users to organise and manage their upcoming events, trips, and appointments. All data is stored locally on the device using the Room Persistence Library.

---

##  Features

- **Add Events** — Create events with a Title, Category, Location, and Date/Time
- **View Events** — Dashboard displaying all events split into Upcoming and Past sections, sorted by date
- **Edit Events** — Tap any event to modify its details
- **Delete Events** — Tap any event and choose delete, with a confirmation dialog
- **Data Persistence** — All events are saved locally using Room Database and survive app restarts
- **Input Validation** — Title and Date fields are required; past dates are blocked for new events
- **User Feedback** — Snackbars confirm saves, updates, and deletions

---

##  Tech Stack

| Component | Technology |
|---|---|
| Language | Java |
| Minimum SDK | API 24 (Android 7.0) |
| Target SDK | API 36 |
| Database | Room Persistence Library |
| Navigation | Jetpack Navigation Component |
| UI Pattern | Fragments + Bottom Navigation Bar |
| Architecture | ViewModel + LiveData |
| Build System | Gradle (Kotlin DSL) |

---

##  Project Structure

```
com.keerthana.eventplannerapp
│
├── db/
│   ├── Event.java             # Room Entity — data model
│   ├── EventDao.java          # DAO — database queries (insert, update, delete, getAll)
│   └── EventDatabase.java     # Room Database — singleton instance
│
├── viewmodel/
│   └── EventViewModel.java    # ViewModel — bridges UI and database
│
├── ui/
│   ├── EventListFragment.java     # Displays upcoming and past events
│   ├── AddEditEventFragment.java  # Form to create or edit an event
│   └── EventAdapter.java          # RecyclerView adapter with section headers
│
└── MainActivity.java          # Hosts Navigation Component and Bottom Nav Bar

res/
├── layout/
│   ├── activity_main.xml              # Root layout with FragmentContainerView + BottomNav
│   ├── fragment_event_list.xml        # Event list screen layout
│   ├── fragment_add_edit_event.xml    # Add/Edit form layout
│   └── item_event.xml                 # Individual event card layout
├── navigation/
│   └── nav_graph.xml          # Navigation graph connecting fragments
└── menu/
    └── bottom_nav_menu.xml    # Bottom navigation bar menu items
```

---

##  Getting Started

### Prerequisites
- Android Studio Meerkat (2024.3.x) or later
- JDK 21
- Android device or emulator running API 24+

### Installation

1. **Clone the repository**
   ```bash
   git clone https://github.com/YOUR_USERNAME/EventPlannerApp.git
   ```

2. **Open in Android Studio**
   - Open Android Studio
   - Click **File → Open**
   - Select the cloned `EventPlannerApp` folder

3. **Sync Gradle**
   - Android Studio will automatically prompt you to sync
   - Click **Sync Now**

4. **Run the app**
   - Connect an Android device via USB with USB Debugging enabled, OR
   - Set up an Android Virtual Device (AVD) via AVD Manager
   - Click the **▶ Run** button or press `Shift + F10`

---

## 📸 App Screenshots

> _Add your screenshots here after recording your demo video_

| Event List | Add Event | Edit/Delete Dialog |
|---|---|---|
| _(screenshot)_ | _(screenshot)_ | _(screenshot)_ |

---

##  Dependencies

```kotlin
// Room Database
implementation("androidx.room:room-runtime:2.6.1")
annotationProcessor("androidx.room:room-compiler:2.6.1")

// Jetpack Navigation Component
implementation("androidx.navigation:navigation-fragment:2.7.7")
implementation("androidx.navigation:navigation-ui:2.7.7")

// ViewModel and LiveData
implementation("androidx.lifecycle:lifecycle-viewmodel:2.7.0")
implementation("androidx.lifecycle:lifecycle-livedata:2.7.0")
```

---

##  Task Requirements Checklist

| Requirement | Status |
|---|---|
| Add event with Title, Category, Location, Date/Time |  Done |
| Dashboard listing all events sorted by date |  Done |
| Edit existing events |  Done |
| Delete events with confirmation |  Done |
| Room Database for local persistence |  Done |
| Data persists after app close/restart |  Done |
| Jetpack Navigation Component |  Done |
| Bottom Navigation Bar |  Done |
| Fragments instead of multiple Activities |  Done |
| Title and Date validation (non-empty) |  Done |
| Block past dates for new events |  Done |
| Snackbar feedback for actions |  Done |

---

##  LLM Declaration

This project was developed with assistance from **Claude (Anthropic)** as a learning and development aid.

AI assistance was used for:
- Guidance on Room Database setup (Entity, DAO, Database class)
- Navigation Component configuration and nav_graph structure
- RecyclerView adapter implementation with section headers
- ViewModel and LiveData wiring
- XML layout structure and debugging runtime errors

All code was reviewed, understood, and integrated manually by the student.

---
