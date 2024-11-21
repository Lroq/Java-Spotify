package fr.quentin.spotify.config;

import java.nio.file.Path;
import java.nio.file.Paths;

public class PathsConfig {
    // Path to the JSON file containing account data
    public static final Path ACCOUNT_FILE_PATH = Paths.get("src/resources/data/account.json");

    // Path to the JSON file containing album data
    public static final Path ALBUMS_FILE_PATH = Paths.get("src/resources/data/albums.json");

    // Path to the JSON file containing artist data
    public static final Path ARTISTS_FILE_PATH = Paths.get("src/resources/data/artists.json");

    // Path to the JSON file containing playlist data
    public static final Path PLAYLISTS_FILE_PATH = Paths.get("src/resources/data/playlists.json");

    // Path to the JSON file containing song data
    public static final Path SONGS_FILE_PATH = Paths.get("src/resources/data/songs.json");
}
