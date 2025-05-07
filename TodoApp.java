import java.util.*;
import java.io.*;

/**
 * A console-based Todo application supporting multiple users with task queue functionality.
 * Features:
 * - Multiple user accounts
 * - Tasks are stored in a queue per user
 * - Add, complete, view, and prioritize tasks
 * - Save/load user data
 */
public class TodoApp {
    private static final String DATA_FILE = "todo_data.ser";
    private static Map<String, User> users = new HashMap<>();
    private static User currentUser = null;
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        loadData();
        boolean running = true;

        System.out.println("====================================");
        System.out.println("WELCOME TO MULTI-USER TODO APP");
        System.out.println("====================================");

        while (running) {
            if (currentUser == null) {
                displayLoginMenu();
            } else {
                displayMainMenu();
            }
            
            String choice = scanner.nextLine().trim();
            
            try {
                if (currentUser == null) {
                    running = processLoginChoice(choice);
                } else {
                    running = processMainMenuChoice(choice);
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
        
        saveData();
        System.out.println("Thank you for using the Todo App. Goodbye!");
    }

    private static void displayLoginMenu() {
        System.out.println("\n--- LOGIN MENU ---");
        System.out.println("1. Login");
        System.out.println("2. Register");
        System.out.println("3. Exit");
        System.out.print("Enter your choice: ");
    }

    private static void displayMainMenu() {
        System.out.println("\n--- MAIN MENU (Logged in as: " + currentUser.getUsername() + ") ---");
        System.out.println("1. Add task");
        System.out.println("2. View next task");
        System.out.println("3. Complete next task");
        System.out.println("4. View all tasks");
        System.out.println("5. Add priority task (add to front of queue)");
        System.out.println("6. Logout");
        System.out.println("7. Exit");
        System.out.print("Enter your choice: ");
    }

    private static boolean processLoginChoice(String choice) {
        switch (choice) {
            case "1": // Login
                login();
                return true;
            case "2": // Register
                register();
                return true;
            case "3": // Exit
                return false;
            default:
                System.out.println("Invalid choice. Please try again.");
                return true;
        }
    }

    private static boolean processMainMenuChoice(String choice) {
        switch (choice) {
            case "1": // Add task
                addTask();
                return true;
            case "2": // View next task
                viewNextTask();
                return true;
            case "3": // Complete next task
                completeNextTask();
                return true;
            case "4": // View all tasks
                viewAllTasks();
                return true;
            case "5": // Add priority task
                addPriorityTask();
                return true;
            case "6": // Logout
                logout();
                return true;
            case "7": // Exit
                return false;
            default:
                System.out.println("Invalid choice. Please try again.");
                return true;
        }
    }

    private static void login() {
        System.out.print("Enter username: ");
        String username = scanner.nextLine().trim();
        System.out.print("Enter password: ");
        String password = scanner.nextLine().trim();

        if (users.containsKey(username) && users.get(username).checkPassword(password)) {
            currentUser = users.get(username);
            System.out.println("Login successful!");
        } else {
            System.out.println("Invalid username or password!");
        }
    }

    private static void register() {
        System.out.print("Enter new username: ");
        String username = scanner.nextLine().trim();
        
        if (users.containsKey(username)) {
            System.out.println("Username already exists!");
            return;
        }
        
        System.out.print("Enter password: ");
        String password = scanner.nextLine().trim();
        
        User newUser = new User(username, password);
        users.put(username, newUser);
        System.out.println("Registration successful!");
    }

    private static void logout() {
        currentUser = null;
        System.out.println("Logged out successfully!");
    }

    private static void addTask() {
        System.out.print("Enter task description: ");
        String description = scanner.nextLine().trim();
        
        Task task = new Task(description);
        currentUser.addTask(task);
        
        System.out.println("Task added successfully!");
    }

    private static void addPriorityTask() {
        System.out.print("Enter priority task description: ");
        String description = scanner.nextLine().trim();
        
        Task task = new Task(description, true);
        currentUser.addPriorityTask(task);
        
        System.out.println("Priority task added successfully!");
    }

    private static void viewNextTask() {
        Task nextTask = currentUser.peekNextTask();
        
        if (nextTask != null) {
            System.out.println("\nNext task: " + nextTask);
        } else {
            System.out.println("No tasks in the queue!");
        }
    }

    private static void completeNextTask() {
        Task completedTask = currentUser.completeNextTask();
        
        if (completedTask != null) {
            System.out.println("Completed task: " + completedTask.getDescription());
        } else {
            System.out.println("No tasks in the queue!");
        }
    }

    private static void viewAllTasks() {
        List<Task> tasks = currentUser.getAllTasks();
        
        if (tasks.isEmpty()) {
            System.out.println("No tasks in the queue!");
            return;
        }
        
        System.out.println("\n--- Your Tasks ---");
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println((i + 1) + ". " + tasks.get(i));
        }
    }

    private static void saveData() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(DATA_FILE))) {
            oos.writeObject(users);
            System.out.println("Data saved successfully!");
        } catch (IOException e) {
            System.out.println("Error saving data: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    private static void loadData() {
        File file = new File(DATA_FILE);
        if (!file.exists()) {
            System.out.println("No existing data found. Starting fresh.");
            return;
        }
        
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(DATA_FILE))) {
            users = (Map<String, User>) ois.readObject();
            System.out.println("Data loaded successfully!");
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading data: " + e.getMessage());
        }
    }
}

/**
 * Represents a user in the system
 */
class User implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String username;
    private String password; // In a real app, this should be hashed
    private Deque<Task> taskQueue; // Using Deque for both queue and priority functionality
    
    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.taskQueue = new LinkedList<>();
    }
    
    public String getUsername() {
        return username;
    }
    
    public boolean checkPassword(String password) {
        return this.password.equals(password);
    }
    
    public void addTask(Task task) {
        taskQueue.addLast(task);
    }
    
    public void addPriorityTask(Task task) {
        taskQueue.addFirst(task);
    }
    
    public Task peekNextTask() {
        return taskQueue.isEmpty() ? null : taskQueue.peekFirst();
    }
    
    public Task completeNextTask() {
        return taskQueue.isEmpty() ? null : taskQueue.pollFirst();
    }
    
    public List<Task> getAllTasks() {
        return new ArrayList<>(taskQueue);
    }
}

/**
 * Represents a task in the todo list
 */
class Task implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String description;
    private Date creationDate;
    private boolean isPriority;
    
    public Task(String description) {
        this(description, false);
    }
    
    public Task(String description, boolean isPriority) {
        this.description = description;
        this.creationDate = new Date();
        this.isPriority = isPriority;
    }
    
    public String getDescription() {
        return description;
    }
    
    @Override
    public String toString() {
        return (isPriority ? "[PRIORITY] " : "") + description + " (Created: " + creationDate + ")";
    }
}