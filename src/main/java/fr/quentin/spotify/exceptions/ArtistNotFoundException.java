package fr.quentin.spotify.exceptions;

// Exception thrown when an artist is not found
public class ArtistNotFoundException extends Exception {
    // Constructor that accepts a message
    public ArtistNotFoundException(String message) {
        super(message);
    }
}
