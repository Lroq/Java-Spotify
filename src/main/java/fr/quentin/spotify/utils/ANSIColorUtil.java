package fr.quentin.spotify.utils;

// Utility class for handling ANSI color codes in console output
public class ANSIColorUtil {

    // ANSI color codes constants
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_PURPLE = "\u001B[35m";

    // Method to colorize text with a specified color
    public static String colorText(String text, String color) {
        return color + text + ANSI_RESET;
    }

    // Method to print text in a specified color to console
    public static void printColoredText(String text, String color) {
        System.out.print(colorText(text, color));
    }
}
