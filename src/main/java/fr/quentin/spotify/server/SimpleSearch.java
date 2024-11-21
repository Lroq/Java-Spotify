package fr.quentin.spotify.server;

import fr.quentin.spotify.artistGroup.Album;
import fr.quentin.spotify.artistGroup.Artist;
import fr.quentin.spotify.artistGroup.Playlist;
import fr.quentin.spotify.artistGroup.Song;
import fr.quentin.spotify.exceptions.*;

import java.io.IOException;
import java.util.Scanner;

// Implementation of the Searchable interface for searching songs, albums, artists, and playlists
public class SimpleSearch implements Searchable {

    // Method to search for a song based on user input
    public Song searchForSong(Scanner scanner) throws IOException, SongNotFoundException {
        String songName = "";
        boolean isFound = false;

        // Loop until the song is found or user chooses to stop
        while (!isFound) {
            System.out.println("Enter the name of the song you want to search for (type '*stop*' to return to the main menu): ");
            songName = scanner.nextLine().trim();

            if (songName.equalsIgnoreCase("*stop*")) {
                return null;  // Return null to indicate user wants to return to main menu
            }

            // Attempt to fetch song information
            Song song = Song.getSongInfo(songName);
            if (song == null) {
                System.out.println("Song not found. Please try again.");
            } else {
                isFound = true;
                System.out.println("Song found!");
                System.out.println("This is the song's information: ");
                return song;
            }
        }
        return null;  // Return null if song is not found or user chooses to stop
    }

    // Method to search for an album based on user input
    public Album searchForAlbum(Scanner scanner) throws IOException, AlbumNotFoundException, DataAccessException {
        String albumName = "";
        boolean isFound = false;

        // Loop until the album is found or user chooses to stop
        while (!isFound) {
            System.out.println("Enter the name of the album you want to search for (type '*stop*' to return to the main menu): ");
            albumName = scanner.nextLine().trim();

            if (albumName.equalsIgnoreCase("*stop*")) {
                return null;  // Return null to indicate user wants to return to main menu
            }

            // Attempt to fetch album information
            Album album = Album.getAlbumInfo(albumName);
            if (album == null) {
                System.out.println("Album not found. Please try again.");
            } else {
                isFound = true;
                System.out.println("Album found!");
                System.out.println("This is the album's information: ");
                return album;
            }
        }
        return null;  // Return null if album is not found or user chooses to stop
    }

    // Method to search for an artist based on user input
    public Artist searchForArtist(Scanner scanner) throws IOException, ArtistNotFoundException, DataAccessException {
        String artistName = "";
        boolean isFound = false;

        // Loop until the artist is found or user chooses to stop
        while (!isFound) {
            System.out.println("Enter the name of the artist you want to search for (type '*stop*' to return to the main menu): ");
            artistName = scanner.nextLine().trim();

            if (artistName.equalsIgnoreCase("*stop*")) {
                return null;  // Return null to indicate user wants to return to main menu
            }

            // Attempt to fetch artist information
            Artist artist = Artist.getArtistInfo(artistName);
            if (artist == null) {
                System.out.println("Artist not found. Please try again.");
            } else {
                isFound = true;
                System.out.println("Artist found!");
                System.out.println("This is the artist's information: ");
                return artist;
            }
        }
        return null;  // Return null if artist is not found or user chooses to stop
    }

    // Method to search for a playlist based on user input
    public Playlist searchForPlaylist(Scanner scanner) throws IOException, PlaylistNotFoundException {
        String playlistName = "";
        boolean isFound = false;

        // Loop until the playlist is found or user chooses to stop
        while (!isFound) {
            System.out.println("Enter the name of the playlist you want to search for (type '*stop*' to return to the main menu): ");
            playlistName = scanner.nextLine().trim();

            if (playlistName.equalsIgnoreCase("*stop*")) {
                return null;  // Return null to indicate user wants to return to main menu
            }

            // Attempt to fetch playlist information
            Playlist playlist = Playlist.getPlaylistInfo(playlistName);
            if (playlist == null) {
                System.out.println("Playlist not found. Please try again.");
            } else {
                isFound = true;
                System.out.println("Playlist found!");
                System.out.println("This is the playlist's information: ");
                return playlist;
            }
        }
        return null;  // Return null if playlist is not found or user chooses to stop
    }
}
