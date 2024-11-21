package fr.quentin.spotify.exceptions;

// Exception thrown when a song is not found
public class SongNotFoundException extends Exception {

    // Constructor that accepts a message
    public SongNotFoundException(String message) {
        super(message);
    }
}
