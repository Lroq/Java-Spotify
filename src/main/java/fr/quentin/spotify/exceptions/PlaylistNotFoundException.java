package fr.quentin.spotify.exceptions;

// Exception thrown when a playlist is not found
public class PlaylistNotFoundException extends Exception {

    // Constructor that accepts a message
    public PlaylistNotFoundException(String message) {
        super(message);
    }
}
