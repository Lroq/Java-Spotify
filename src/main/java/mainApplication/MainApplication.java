package mainApplication;

import fr.quentin.spotify.exceptions.*;
import fr.quentin.spotify.server.Account;
import fr.quentin.spotify.server.Connexion;
import fr.quentin.spotify.server.SimpleSearch;
import fr.quentin.spotify.client.Hub;

import java.io.IOException;

// Main class responsible for starting the application
public class MainApplication {

    // Main method where the application execution begins
    public static void main(String[] args) throws IOException, ArtistNotFoundException, DataAccessException, PlaylistNotFoundException, SongNotFoundException, AlbumNotFoundException {
        // Initialize Connexion to handle user login or signup
        Connexion connexion = new Connexion();
        // Perform login or signup and retrieve the user account
        Account account = connexion.logInOrSignUp();
        // Initialize SimpleSearch for searching songs, albums, artists, and playlists
        SimpleSearch simpleSearch = new SimpleSearch();
        // Initialize Hub to manage user interaction and navigation
        Hub hub = new Hub(account, simpleSearch);
        // Start the recursive menu loop to interact with the user
        hub.recursiveMenu();
    }
}
