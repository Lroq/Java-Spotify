package fr.quentin.spotify.exceptions;

// Exception thrown when there is an issue accessing data
public class DataAccessException extends Exception {
    // Constructor that accepts a message and a cause
    public DataAccessException(String message, Throwable cause) {
        super(message, cause);
    }
}
