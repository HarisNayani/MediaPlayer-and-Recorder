# Database-Free JavaFX Media Player

An intermediate-level JavaFX desktop multimedia application that manages a music playlist entirely in system memory. Instead of a traditional database, this project utilizes a custom **Doubly Linked List** data structure to manage track order, navigation, and queue manipulation in real-time.

## 🚀 Key Features
* **In-Memory Data Architecture:** Built entirely on a custom-coded Doubly Linked List (No SQL/No database dependencies).
* **Dynamic Media Playback:** Real-time MP3 audio stream decoding via the `javafx.scene.media` pipeline.
* **Interactive Playlist Controls:** Interactive play, pause, next track, and previous track capabilities mapped directly onto node pointers.
* **Real-Time Progress Tracker:** Visual audio playback timeline updates driven by dynamic media duration listeners.
* **Interactive List UI:** A responsive `ListView` panel displaying loaded files that can be clicked to skip tracks instantly.
* **File Serialization Management:** Options to wipe memory cache data, recursively import any computer folder containing `.mp3` tracks, and save/load active queue timelines out into raw flat configuration text sheets.

---

## 🛠️ Data Structure Architecture
The core engine relies on a custom implementation of a **Doubly Linked List** (`PlaylistLinkedList.java`), optimizing music navigation tracking workflows:

```text
 [Node 1: Track]  <--->  [Node 2: Track]  <--->  [Node 3: Track]
        ^                       ^                       ^
     (Head)                 (Current)                (Tail)
```

* **Forward Navigation (`nextSong()`):** Shifts the `current` active pointer down to the `.next` node address.
* **Backward Navigation (`prevSong()`):** Shifts the `current` active pointer back up to the `.prev` node address.
* **Direct Access (`getAtIndex(int index)`):** Linearly traverses the nodes from `head` to match the exact row selection clicked on the interface.

---

## 💻 Tech Stack & Requirements
* **Language:** Java (JDK 11 or higher recommended)
* **GUI Toolkit:** JavaFX (including `javafx.controls`, `javafx.fxml`, and `javafx.media` modules)
* **IDE Platform:** Eclipse 

---

## 📂 Project Directory Layout
Ensure your workspace matches this structural footprint before execution:
```text
📁 Music_Playlist_recorder/
│
├── 📁 src/
│   └── 📁 M/
│       ├── Track.java                 # Model: Data structure item element wrapper
│       ├── PlaylistLinkedList.java    # Model: Custom Doubly Linked List engine
│       ├── Main.java                  # View/Controller: Core JavaFX GUI application
│       └── Launcher.java              # Proxy runtime loader wrapper 
│
└── 📁 Music/                          # Your default local audio folder (holds your .mp3 files)
```

---

## 📥 Deployment & Execution Instructions

### Option 1: Running inside Eclipse IDE (Recommended)
1. Open Eclipse and import this project folder via **File** ➡️ **Import** ➡️ **Existing Projects into Workspace**.
2. Ensure the JavaFX SDK libraries are linked inside your project's **Build Path**.
3. Right-click **`Launcher.java`** in the Package Explorer.
4. Select **Run As** ➡️ **Java Application**.

### Option 2: Importing as an Archive (For Instructors/Evaluators)
To share this project for academic evaluation or grading:
1. In Eclipse, right-click the root project directory `Music_Playlist_recorder`.
2. Select **Export...** ➡️ Expand **General** ➡️ Choose **Archive File** ➡️ Click **Next**.
3. Save the `.zip` archive to your computer.
4. The evaluator can import this archive folder into their IDE to review the code structure and run it natively.
