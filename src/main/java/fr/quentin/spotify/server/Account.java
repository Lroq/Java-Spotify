package fr.quentin.spotify.server;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import fr.quentin.spotify.config.PathsConfig;
import fr.quentin.spotify.utils.ANSIColorUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;

// Represents a user account in the Spotify application
public class Account {
    private boolean adminStatus;
    private String clientEmail;
    private String clientPassword;
    private String clientUsername;
    private String clientCountry;
    private String clientBirthDate;

    // ObjectMapper instance for JSON serialization and deserialization
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .configure(SerializationFeature.INDENT_OUTPUT, true);

    // File path where account information is stored
    private static final Path FILE_PATH = PathsConfig.ACCOUNT_FILE_PATH;

    // Constructor to create an Account object with all fields initialized
    public Account(String clientEmail, String clientPassword, String clientUsername, String clientCountry, String clientBirthDate) {
        this.clientEmail = clientEmail;
        this.clientPassword = clientPassword;
        this.clientUsername = clientUsername;
        this.clientCountry = clientCountry;
        this.clientBirthDate = clientBirthDate;
        this.adminStatus = false;
    }

    // Default constructor
    public Account() {
    }

    // Deserialize JSON string to create an Account object
    public static Account fromJson(String json) throws IOException {
        return OBJECT_MAPPER.readValue(json, Account.class);
    }

    // Serialize the Account object to JSON string
    public String toJson() throws IOException {
        return OBJECT_MAPPER.writeValueAsString(this);
    }

    // Getter for client email
    public String getClientEmail() {
        return clientEmail;
    }

    // Getter for admin status
    public boolean isAdminStatus() {
        return adminStatus;
    }

    // Setter for admin status
    public void setAdminStatus(boolean adminStatus) {
        this.adminStatus = adminStatus;
    }

    // Getter for admin status from JSON
    public boolean getAdminStatusJson() throws IOException {
        return adminStatus;
    }

    // Setter for client email
    public void setClientEmail(String clientEmail) {
        this.clientEmail = clientEmail;
    }

    // Getter for client password
    public String getClientPassword() {
        return clientPassword;
    }

    // Setter for client password
    public void setClientPassword(String clientPassword) {
        this.clientPassword = clientPassword;
    }

    // Getter for client username
    public String getClientUsername() {
        return clientUsername;
    }

    // Setter for client username
    public void setClientUsername(String clientUsername) {
        this.clientUsername = clientUsername;
    }

    // Getter for client country
    public String getClientCountry() {
        return clientCountry;
    }

    // Setter for client country
    public void setClientCountry(String clientCountry) {
        this.clientCountry = clientCountry;
    }

    // Getter for client birth date
    public String getClientBirthDate() {
        return clientBirthDate;
    }

    // Setter for client birth date
    public void setClientBirthDate(String clientBirthDate) {
        this.clientBirthDate = clientBirthDate;
    }

    // Generates a unique ID using UUID
    private String generateUniqueID() {
        return UUID.randomUUID().toString();
    }

    // Saves client information to JSON file
    public boolean saveClientInfo() throws IOException {
        List<Account> accounts = loadAllAccounts();
        accounts.add(this);
        saveAllAccounts(accounts);
        return true;
    }

    // Saves all accounts to JSON file
    private void saveAllAccounts(List<Account> accounts) throws IOException {
        Files.writeString(FILE_PATH, OBJECT_MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(accounts));
    }

    // Loads all accounts from JSON file
    private static List<Account> loadAllAccounts() throws IOException {
        if (Files.exists(FILE_PATH) && Files.size(FILE_PATH) > 0) {
            return OBJECT_MAPPER.readValue(Files.readString(FILE_PATH), new TypeReference<List<Account>>() {});
        }
        return new ArrayList<>();
    }

    // Loads client information based on email from JSON file
    public static Account loadClientInfo(String email) throws IOException {
        List<Account> accounts = loadAllAccounts();
        for (Account account : accounts) {
            if (account.getClientEmail().equals(email)) {
                return account;
            }
        }
        return null;
    }

    // Checks if client account information is valid
    public boolean checkAccountInfo() throws IOException {
        Account account = loadClientInfo(getClientEmail());
        if (account != null) {
            return this.clientEmail.equals(account.getClientEmail()) && this.clientPassword.equals(account.getClientPassword());
        }
        return false;
    }

    // Checks if client email is available
    public boolean checkMailAvailable() throws IOException {
        Account account = loadClientInfo(this.clientEmail);
        return account == null;
    }

    // Checks if client email is in valid format
    public boolean checkMailValidFormat(String clientEmail) {
        return clientEmail.matches("^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$");
    }

    // Checks if client password is in valid format
    public boolean checkPasswordValidFormat(String clientPassword) {
        return clientPassword.matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{8,}$");
    }

    // Adds additional details to the account
    public void addDetailsAccount() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("---------------------------------------------------------");
        System.out.println("Congratulations on joining Spotify!");
        System.out.println("A few more pieces of information are necessary. Please complete the fields below:");
        System.out.println("---------------------------------------------------------");

        System.out.println("Username (type '*stop*' to return to the main menu):");
        String input = scanner.nextLine().trim();
        if (input.equalsIgnoreCase("*stop*")) return;
        setClientUsername(input);
        System.out.println("---------------------------------------------------------");

        System.out.println("Birthdate (type '*stop*' to return to the main menu):");
        input = scanner.nextLine().trim();
        if (input.equalsIgnoreCase("*stop*")) return;
        setClientBirthDate(input);
        System.out.println("---------------------------------------------------------");

        System.out.println("Country (type '*stop*' to return to the main menu):");
        input = scanner.nextLine().trim();
        if (input.equalsIgnoreCase("*stop*")) return;
        setClientCountry(input);
    }

    // Displays profile information for the account
    public static void checkProfileInformation(Account account) {
        if (account != null) {
            System.out.println("---------------------------------------------------------");
            System.out.println("Account Information:");
            System.out.println("Email: " + account.getClientEmail());
            System.out.println("Username: " + account.getClientUsername());
            System.out.println("Country: " + account.getClientCountry());
            System.out.println("Birthdate: " + account.getClientBirthDate());
            System.out.println("---------------------------------------------------------");
        } else {
            ANSIColorUtil.printColoredText("Cannot access your information", ANSIColorUtil.ANSI_RED);
        }
    }

    // Modifies profile information for the account
    public static void modifyProfileInformation(Account account) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("---------------------------------------------------------");
        System.out.println("Username (type '*stop*' to return to the main menu):");
        String input = scanner.nextLine().trim();
        if (input.equalsIgnoreCase("*stop*")) return;
        account.setClientUsername(input);

        System.out.println("---------------------------------------------------------");
        System.out.println("Birthdate (type '*stop*' to return to the main menu):");
        input = scanner.nextLine().trim();
        if (input.equalsIgnoreCase("*stop*")) return;
        account.setClientBirthDate(input);

        System.out.println("---------------------------------------------------------");
        System.out.println("Country (type '*stop*' to return to the main menu):");
        input = scanner.nextLine().trim();
        if (input.equalsIgnoreCase("*stop*")) return;
        account.setClientCountry(input);

        System.out.println("---------------------------------------------------------");
        System.out.println("Information modified!");
    }

    // Deletes client information based on email
    public void deleteClientInfo(String email) throws IOException {
        List<Account> accounts = loadAllAccounts();
        accounts.removeIf(account -> account.getClientEmail().equals(email));
        saveAllAccounts(accounts);
    }
}
