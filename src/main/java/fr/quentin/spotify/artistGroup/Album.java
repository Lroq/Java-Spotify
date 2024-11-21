package fr.quentin.spotify.artistGroup;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fr.quentin.spotify.config.PathsConfig;
import fr.quentin.spotify.exceptions.AlbumNotFoundException;
import fr.quentin.spotify.exceptions.DataAccessException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Represents an album in the Spotify-like application.
 */
public class Album {
    private String id;
    private String artistId;
    private String name;
    private String date;
    private String genre;
    private List<Song> songs;

    /**
     * Default constructor initializes album ID and songs list using UUID.
     */
    public Album() {
        this.id = UUID.randomUUID().toString();
        this.songs = new ArrayList<>();
    }

    /**
     * Constructor to create an Album with specified details.
     *
     * @param artistId        The ID of the artist who created the album.
     * @param albumName       The name of the album.
     * @param albumReleaseDate The release date of the album.
     * @param albumGenre      The genre of the album.
     */
    public Album(String artistId, String albumName, String albumReleaseDate, String albumGenre) {
        this();
        this.artistId = artistId;
        this.name = albumName;
        this.date = albumReleaseDate;
        this.genre = albumGenre;
    }

    /**
     * Retrieves the ID of the album.
     *
     * @return The ID of the album.
     */
    public String getAlbumId() {
        return id;
    }

    /**
     * Retrieves the ID of the artist who created the album.
     *
     * @return The artist ID.
     */
    public String getArtistId() {
        return artistId;
    }

    /**
     * Retrieves the name of the album.
     *
     * @return The name of the album.
     */
    public String getName() {
        return name;
    }

    /**
     * Retrieves the release date of the album.
     *
     * @return The release date of the album.
     */
    public String getDate() {
        return date;
    }

    /**
     * Retrieves the genre of the album.
     *
     * @return The genre of the album.
     */
    public String getGenre() {
        return genre;
    }

    /**
     * Retrieves the list of songs in the album.
     *
     * @return The list of songs.
     */
    public List<Song> getSongs() {
        return songs;
    }

    /**
     * Adds a song to the album.
     *
     * @param songId   The ID of the song to add.
     * @param songName The name of the song to add.
     */
    public void addSong(String songId, String songName) {
        Song song = new Song(songId, songName, null); // Create a new Song object
        songs.add(song); // Add the song to the list of songs in this album
    }

    /**
     * Saves the album information to a JSON file.
     *
     * @return True if saving was successful, false otherwise.
     * @throws DataAccessException If there is an error accessing the data.
     */
    public boolean saveAlbumInfo() throws DataAccessException {
        ObjectMapper mapper = new ObjectMapper(); // Create ObjectMapper instance
        ObjectNode albumInfo = mapper.createObjectNode(); // Create root JSON object node for album info
        albumInfo.put("albumId", id); // Add album ID to JSON object
        albumInfo.put("artistId", artistId); // Add artist ID to JSON object
        albumInfo.put("albumName", name); // Add album name to JSON object
        albumInfo.put("albumReleaseDate", date); // Add album release date to JSON object
        albumInfo.put("albumGenre", genre); // Add album genre to JSON object

        ArrayNode songsArray = mapper.createArrayNode(); // Create JSON array node for songs
        for (Song song : songs) { // Iterate through each song in the album
            ObjectNode songInfo = mapper.createObjectNode(); // Create JSON object node for song info
            songInfo.put("songId", song.getSongId()); // Add song ID to JSON object
            songInfo.put("songName", song.getSongName()); // Add song name to JSON object
            songsArray.add(songInfo); // Add song info JSON object to songs array
        }

        albumInfo.set("albumSongs", songsArray); // Add songs array to album info JSON object

        Path path = PathsConfig.ALBUMS_FILE_PATH; // Get path to albums JSON file

        try {
            ArrayNode jsonArray;
            if (Files.exists(path)) { // Check if albums JSON file exists
                String content = new String(Files.readAllBytes(path)); // Read content of albums JSON file
                if (content.isEmpty()) { // If file is empty, create new empty array node
                    jsonArray = mapper.createArrayNode();
                } else { // If file is not empty, read existing JSON array
                    jsonArray = (ArrayNode) mapper.readTree(content);
                }
            } else { // If file does not exist, create new empty array node
                jsonArray = mapper.createArrayNode();
            }

            for (int i = 0; i < jsonArray.size(); i++) { // Iterate through existing albums in JSON array
                ObjectNode existingAlbum = (ObjectNode) jsonArray.get(i); // Get current album JSON object
                if (existingAlbum.get("albumId").asText().equals(id)) { // If album ID matches current album ID
                    jsonArray.remove(i); // Remove the existing album JSON object
                    break;
                }
            }

            jsonArray.add(albumInfo); // Add the new album info JSON object to JSON array

            Files.write(path, jsonArray.toPrettyString().getBytes()); // Write JSON array to albums JSON file

            return true; // Return true indicating successful save
        } catch (IOException e) { // Catch IO exception
            throw new DataAccessException("Failed to save album info", e); // Throw data access exception
        }
    }

    /**
     * Retrieves album information from a JSON file based on album name.
     *
     * @param albumName The name of the album to retrieve.
     * @return The Album object containing album information.
     * @throws DataAccessException If there is an error accessing the data.
     * @throws AlbumNotFoundException If the album with the specified name is not found.
     */
    public static Album getAlbumInfo(String albumName) throws DataAccessException, AlbumNotFoundException {
        ObjectMapper mapper = new ObjectMapper(); // Create ObjectMapper instance
        Path path = PathsConfig.ALBUMS_FILE_PATH; // Get path to albums JSON file

        try {
            if (Files.exists(path)) { // Check if albums JSON file exists
                String content = new String(Files.readAllBytes(path)); // Read content of albums JSON file
                if (content.isEmpty()) { // If file is empty, throw AlbumNotFoundException
                    throw new AlbumNotFoundException("Album not found: " + albumName);
                } else { // If file is not empty, read existing JSON array
                    ArrayNode jsonArray = (ArrayNode) mapper.readTree(content); // Convert content to JSON array
                    for (int i = 0; i < jsonArray.size(); i++) { // Iterate through JSON array
                        ObjectNode albumInfo = (ObjectNode) jsonArray.get(i); // Get current album JSON object
                        if (albumInfo.get("albumName").asText().equals(albumName)) { // If album name matches
                            Album album = new Album(); // Create new Album object
                            album.id = albumInfo.get("albumId").asText(); // Set album ID
                            album.artistId = albumInfo.get("artistId").asText(); // Set artist ID
                            album.name = albumInfo.get("albumName").asText(); // Set album name
                            album.date = albumInfo.get("albumReleaseDate").asText(); // Set release date
                            album.genre = albumInfo.get("albumGenre").asText(); // Set album genre
                            ArrayNode songsArray = (ArrayNode) albumInfo.get("albumSongs"); // Get songs array
                            for (int j = 0; j < songsArray.size(); j++) { // Iterate through songs array
                                ObjectNode songInfo = (ObjectNode) songsArray.get(j); // Get song JSON object
                                album.songs.add(new Song(songInfo.get("songId").asText(), songInfo.get("songName").asText(), null)); // Add song to album
                            }
                            return album; // Return the Album object
                        }
                    }
                    throw new AlbumNotFoundException("Album not found: " + albumName); // Throw exception if album not found
                }
            } else {
                throw new AlbumNotFoundException("Album not found: " + albumName); // Throw exception if file does not exist
            }
        } catch (IOException e) { // Catch IO exception
            throw new DataAccessException("Failed to get album info", e); // Throw data access exception
        }
    }

    /**
     * Converts an Artist object to JSON format.
     *
     * @return True if conversion was successful, false otherwise.
     */
    public static boolean toJson() {
        ObjectMapper mapper = new ObjectMapper(); // Create ObjectMapper instance
        try {
            Artist artist = new Artist("name", "genre", "members", "date", "bio"); // Create new Artist object
            String json = mapper.writeValueAsString(artist); // Convert Artist object to JSON string
            System.out.println(json); // Print JSON string to console
            return true; // Return true indicating successful conversion
        } catch (IOException e) { // Catch IO exception
            e.printStackTrace(); // Print stack trace for exception
            return false; // Return false indicating failed conversion
        }
    }

    /**
     * Deletes album information from JSON file based on album name.
     *
     * @param albumName The name of the album to delete.
     * @throws DataAccessException If there is an error accessing the data.
     */
    public void deleteAlbumInfo(String albumName) throws DataAccessException {
        ObjectMapper mapper = new ObjectMapper(); // Create ObjectMapper instance
        Path path = PathsConfig.ALBUMS_FILE_PATH; // Get path to albums JSON file

        try {
            if (Files.exists(path)) { // Check if albums JSON file exists
                String content = new String(Files.readAllBytes(path)); // Read content of albums JSON file
                if (!content.isEmpty()) { // If file is not empty
                    ArrayNode jsonArray = (ArrayNode) mapper.readTree(content); // Convert content to JSON array
                    for (int i = 0; i < jsonArray.size(); i++) { // Iterate through JSON array
                        ObjectNode albumInfo = (ObjectNode) jsonArray.get(i); // Get current album JSON object
                        if (albumInfo.get("albumName").asText().equals(albumName)) { // If album name matches
                            jsonArray.remove(i); // Remove album JSON object from array
                            break; // Exit loop
                        }
                    }
                    Files.write(path, jsonArray.toPrettyString().getBytes()); // Write JSON array back to file
                }
            }
        } catch (IOException e) { // Catch IO exception
            throw new DataAccessException("Failed to delete album info", e); // Throw data access exception
        }
    }

    /**
     * Returns a string representation of the Album object.
     *
     * @return A string representation of the Album object.
     */
    @Override
    public String toString() {
        return "Album{" +
                "id='" + id + '\'' +
                ", artistId='" + artistId + '\'' +
                ", name='" + name + '\'' +
                ", date='" + date + '\'' +
                ", genre='" + genre + '\'' +
                ", songs=" + songs +
                '}';
    }
}
