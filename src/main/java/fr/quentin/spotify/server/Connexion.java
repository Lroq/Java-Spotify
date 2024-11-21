package fr.quentin.spotify.server;

import java.io.IOException;
import java.util.Scanner;
import fr.quentin.spotify.utils.ANSIColorUtil;

// Represents a class handling user login and signup for the Spotify application
public class Connexion extends Account {

    // Constructor that initializes by calling super constructor
    public Connexion() throws IOException {
        super();
    }

    // Method to handle user login or signup
    public Account logInOrSignUp() throws IOException {
        // Display initial menu for login or signup
        System.out.println("\n" + "---------------------------------------------------------");
        System.out.println("Welcome to Spotify! Please log in or sign up to continue.");
        System.out.println("---------------------------------------------------------");
        System.out.println("1. Log in");
        System.out.println("2. Sign up");
        System.out.println("3. Exit");
        System.out.println("---------------------------------------------------------");

        // Read user choice
        Scanner scanner = new Scanner(System.in);
        int choice = scanner.nextInt();
        scanner.nextLine(); // Consume newline left by nextInt()

        // Process user choice
        switch (choice) {
            case 1:
                return logIn(); // Perform login
            case 2:
                return signUp(); // Perform signup
            case 3:
                System.exit(0); // Exit the application
                return null; // Return null as a fallback
            default:
                // Handle invalid choice
                ANSIColorUtil.printColoredText("Invalid choice. Please try again.\n", ANSIColorUtil.ANSI_RED);
                return logInOrSignUp(); // Recursively call logInOrSignUp() until valid choice is made
        }
    }

    // Method to handle user login
    public Account logIn() throws IOException {
        Scanner scanner = new Scanner(System.in);

        // Prompt for email
        System.out.println("Enter your email: ");
        String email = scanner.nextLine();

        // Validate email format
        if (!checkMailValidFormat(email)) {
            ANSIColorUtil.printColoredText("Invalid email format\n", ANSIColorUtil.ANSI_RED);
            return logIn(); // Retry login
        }
        setClientEmail(email);

        // Prompt for password
        System.out.println("Enter your password: ");
        String password = scanner.nextLine();

        // Validate password format
        if (!checkPasswordValidFormat(password)) {
            ANSIColorUtil.printColoredText("Invalid password format (Password must contain at least one digit, one uppercase letter, one lowercase letter, and at least 8 characters)\n", ANSIColorUtil.ANSI_RED);
            return logIn(); // Retry login
        }
        setClientPassword(password);

        // Check if account credentials are valid
        if (checkAccountInfo()) {
            // Load client information
            Account account = loadClientInfo(getClientEmail());
            System.out.print("---------------------------------------------------------\n");
            ANSIColorUtil.printColoredText("Log in successful!\n", ANSIColorUtil.ANSI_GREEN);
            System.out.println("---------------------------------------------------------\n");
            return account; // Return logged-in account
        } else {
            // Handle failed login attempt
            ANSIColorUtil.printColoredText("Log in failed. Please try again.", ANSIColorUtil.ANSI_RED);
            return logIn(); // Retry login
        }
    }

    // Method to handle user signup
    public Account signUp() throws IOException {
        Scanner scanner = new Scanner(System.in);

        // Prompt for email
        System.out.println("Enter your email: ");
        String email = scanner.nextLine();

        // Validate email format
        if (!checkMailValidFormat(email)) {
            ANSIColorUtil.printColoredText("Invalid email format", ANSIColorUtil.ANSI_RED + "\n");
            return signUp(); // Retry signup
        }
        setClientEmail(email);

        // Prompt for password
        System.out.println("Enter your password: ");
        String password = scanner.nextLine();

        // Validate password format
        if (!checkPasswordValidFormat(password)) {
            ANSIColorUtil.printColoredText("Invalid password format (Password must contain at least one digit, one uppercase letter, one lowercase letter, and at least 8 characters)", ANSIColorUtil.ANSI_RED + "\n");
            return signUp(); // Retry signup
        }
        setClientPassword(password);

        // Add additional account details
        addDetailsAccount();

        // Save client information
        if (saveClientInfo()) {
            ANSIColorUtil.printColoredText("Account created successfully!\n", ANSIColorUtil.ANSI_GREEN);
            return this; // Return newly signed-up account
        } else {
            ANSIColorUtil.printColoredText("Account creation failed. Please try again.\n", ANSIColorUtil.ANSI_RED);
            return signUp(); // Retry signup
        }
    }
}
