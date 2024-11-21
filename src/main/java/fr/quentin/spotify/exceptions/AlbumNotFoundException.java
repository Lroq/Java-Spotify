package fr.quentin.spotify.exceptions;

// Exception thrown when an album is not found
public class AlbumNotFoundException extends Exception {
    // Constructor that accepts a message
    public AlbumNotFoundException(String message) {
        super(message);
    }
}