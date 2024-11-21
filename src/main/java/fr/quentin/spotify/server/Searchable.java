package fr.quentin.spotify.server;

import fr.quentin.spotify.artistGroup.Album;
import fr.quentin.spotify.artistGroup.Artist;
import fr.quentin.spotify.artistGroup.Playlist;
import fr.quentin.spotify.artistGroup.Song;
import fr.quentin.spotify.exceptions.*;

import java.io.IOException;
import java.util.Scanner;

// Interface defining methods for searching songs, albums, artists, and playlists
public interface Searchable {
    // Method to search for a song based on a search term
    Song searchForSong(Scanner searchTerm) throws IOException, SongNotFoundException;

    // Method to search for an album based on a search term
    Album searchForAlbum(Scanner searchTerm) throws IOException, AlbumNotFoundException, DataAccessException;

    // Method to search for an artist based on a search term
    Artist searchForArtist(Scanner searchTerm) throws IOException, ArtistNotFoundException, DataAccessException;

    // Method to search for a playlist based on a search term
    Playlist searchForPlaylist(Scanner searchTerm) throws IOException, PlaylistNotFoundException;
}
