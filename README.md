# Notes Manager (Java Console Application)

A **console-based Notes Management System** written in **Java**.  
This program allows users to **create, view, search, modify, and delete notes** with persistence using text files.  
It demonstrates practical usage of **Java File I/O, exception handling, and user-friendly CLI design**.

---

## Features

- **Add Note**  
  - Each note requires a **title** and **content**.  
  - A **timestamp** is automatically added when the note is created.  
  - Notes are saved in a persistent file (`notes.txt`).

- **View All Notes**  
  - Displays all notes with title, content, and timestamp.  
  - Shows a friendly message if no notes exist.

- **View Notes by Title**  
  - Search notes by title.  
  - Supports multiple notes with the same title (all will be displayed).

- **Modify Note by Title**  
  - Update the content of an existing note.  
  - Updates the timestamp upon modification.  
  - Handles cases where the title does not exist.

- **Delete Note by Title**  
  - Deletes all notes that match the given title.  
  - Confirms if no notes are found before attempting deletion.

- **Delete All Notes**  
  - Safely checks if notes exist before asking for confirmation.  
  - Requires user confirmation (`yes/no`) before clearing all notes.  

- **Input Validation & Error Handling**  
  - Prevents empty titles or content.  
  - Gracefully handles invalid menu choices and malformed input.  
  - Handles `FileNotFoundException` and `IOException` robustly.

- **User-Friendly Interface**  
  - Menu-driven CLI with numbered options.  
  - Traditional **delay method** (`Thread.sleep`) for smoother UX flow.  

---

## Project Structure

```
NotesApp/
|- NotesApp.java    # Main program with menu-driven CLI
|- notes.txt        # Storage file (auto-created if missing)
|- README.md        # Project documentation
```

---

## How to Run

1. **Clone or download** the project.  

2. Open a terminal and navigate to the project folder.  

3. Compile and run the program:  

   ```bash
   javac NotesApp.java
   java NotesApp
   ```

---

## Example Usage

```bash
===== Notes Manager =====
1. Add a Note
2. View All Notes
3. View Notes by Title
4. Modify a Note by Title
5. Delete a Note by Title
6. Delete All Notes
7. Exit
Enter your choice: 1
```

**Adding a Note Example:**

```bash
Enter note title: Meeting
Enter note content: Discuss project timeline.
Note added successfully!
```

**Viewing Notes Example:**

```bash
----- All Notes -----
TITLE: Meeting
CONTENT: Discuss project timeline.
CREATED_AT: 2025-09-26 15:30:10
-----
```

**Modifying a Note Example:**

```bash
Enter title of note to modify: Meeting
Enter new content: Timeline discussion postponed to next week.
Note updated successfully!
```

**Deleting All Notes Example:**

```bash
Are you sure you want to delete ALL notes? (yes/no): yes
All notes deleted successfully!
```

**Error Handling Example (No Notes):**

```bash
Oh-no, No notes available to delete.
```

---

## Concepts Showcased

- **Java File I/O**  
  - `FileReader`, `FileWriter`, `BufferedReader`, and `BufferedWriter` for efficient read/write operations.  
  - Persistence of data in `notes.txt`.  

- **Exception Handling**  
  - Graceful handling of `IOException` and invalid input.  

- **Collections**  
  - `ArrayList` for in-memory note manipulation before saving.  

- **Date & Time API**  
  - `LocalDateTime` and `DateTimeFormatter` for timestamps.  

- **CLI Design**  
  - Menu-driven interface with clear options.  
  - Input validation to prevent invalid entries.  
  - `delay()` method for smoother user experience.  

---

## Future Enhancements

- Add **unique Note IDs** for precise identification (even with duplicate titles).  
- Implement **JSON or database persistence** for structured storage.  
- Add **search by keyword** in note content.  
- Support **multi-line note content**.  
- Develop a **GUI version** using Swing or JavaFX.  

---
