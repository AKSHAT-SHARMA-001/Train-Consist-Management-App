/**
 * UC1 - Application Entry & Welcome Message
 * Book My Stay App
 * Handles the entry point of the application and displays a welcome message to the user.
 */
public class UC1_ApplicationEntry {

    public static void main(String[] args) {
        displayWelcomeMessage();
        startApplication();
    }

    public static void displayWelcomeMessage() {
        System.out.println("============================================");
        System.out.println("       Welcome to Book My Stay App!        ");
        System.out.println("============================================");
        System.out.println("Your trusted platform for room bookings.");
        System.out.println("Please proceed to search for available rooms.");
        System.out.println();
    }

    public static void startApplication() {
        System.out.println("Application started successfully.");
        System.out.println("Initializing system modules...");
        System.out.println("All modules loaded. Ready to serve you!");
    }
}
