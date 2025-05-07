# 📋 Multi-User Todo App

A simple and interactive **Java console-based Todo application** that supports **multiple users**, task **queues**, and **data persistence** using serialization.

---

## ✅ Features

* 👤 **Multiple User Accounts** – Supports registration and login.
* 🧾 **Task Queue per User** – Tasks are stored in a queue.
* ⏫ **Priority Tasks** – Add priority tasks to the front of the queue.
* 👀 **View Tasks** – Peek the next task or see all pending tasks.
* ✅ **Complete Tasks** – Complete tasks in FIFO order.
* 💾 **Data Persistence** – Automatically saves user/task data between runs via file serialization.

---

## 🧠 Key Components

### 1. `TodoApp.java`

Main application controller:

* Manages user login, task operations, menu navigation, and persistent storage.

### 2. `User.java`

Represents a user with:

* Username and password
* A task queue (`Deque<Task>`)
* Methods to add/view/complete tasks

### 3. `Task.java`

Represents an individual task with:

* Description
* Creation date
* Priority flag

---

## 🗃️ How the Task Queue Works

* `Deque<Task>` is used to manage the user's task list.
* `addTask()` adds to the end of the queue (`addLast()`).
* `addPriorityTask()` adds to the front (`addFirst()`).
* `peekNextTask()` shows the task at the front without removing it.
* `completeNextTask()` removes the front task from the queue.
* Tasks are completed in **FIFO** (First-In, First-Out) unless prioritized.

---

## 💻 How to Clone, Compile, and Run

### 📥 Clone the Repository

```bash
git clone https://github.com/your-username/multi-user-todo-app.git
cd multi-user-todo-app
```

> Replace `your-username` with your GitHub username if hosted on GitHub.

---

### 🛠️ Compile the Code

Make sure you have **Java 8 or above** installed.

```bash
javac TodoApp.java
```

This will compile all classes since `User` and `Task` are inner classes or declared in the same file. If you separate classes into individual files (recommended), compile all `.java` files like this:

```bash
javac *.java
```

---

### ▶️ Run the Application

```bash
java TodoApp
```

---

### 📦 Data File

* The application saves user data to `todo_data.ser` in the same directory.
* This file is loaded on startup and updated automatically on exit.

---

## 📄 Example Usage

```text
====================================
WELCOME TO MULTI-USER TODO APP
====================================

--- LOGIN MENU ---
1. Login
2. Register
3. Exit
Enter your choice: 2
Enter new username: alice
Enter password: 1234
Registration successful!

--- MAIN MENU (Logged in as: alice) ---
1. Add task
2. View next task
3. Complete next task
4. View all tasks
5. Add priority task
6. Logout
7. Exit
Enter your choice: 1
Enter task description: Buy groceries
Task added successfully!
```

---

## 📌 Notes

* Passwords are stored in plain text (for demonstration only). Use proper hashing (e.g., BCrypt) in real apps.
* No external libraries or frameworks are used—100% pure Java.
