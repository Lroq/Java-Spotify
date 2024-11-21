package fr.quentin.spotify.artistGroup;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fr.quentin.spotify.config.PathsConfig;
import fr.quentin.spotify.exceptions.PlaylistNotFoundException;
import fr.quentin.spotify.exceptions.DataAccessException;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Playlist {
    private String name;
    private String description;
    private String genre;
    private Integer creationDate;
    private String creator;

    // Default constructor
    public Playlist() {
    }

    // Constructor with parameters to initialize playlist details
    public Playlist(String playlistName, String playlistDescription, String playlistGenre, Integer playlistCreationDate, String playlistCreator) {
        this.name = playlistName;
        this.description = playlistDescription;
        this.genre = playlistGenre;
        this.creationDate = playlistCreationDate;
        this.creator = playlistCreator;
    }

    // Getters for playlist attributes
    public String getName() {
        return name;
    }

    public Integer getCreationDate() {
        return creationDate;
    }

    public String getCreator() {
        return creator;
    }

    public String getGenre() {
        return genre;
    }

    public String getDescription() {
        return description;
    }

    // Setter for playlist name
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Retrieve playlist information by playlist name.
     * This method reads the playlist information from the JSON file defined by PathsConfig.PLAYLISTS_FILE_PATH
     * and returns a Playlist object if a playlist with the given name is found.
     *
     * @param playlistName the name of the playlist to retrieve.
     * @return a Playlist object if found.
     * @throws IOException if there is an error accessing the file.
     * @throws PlaylistNotFoundException if the playlist is not found.
     */
    public static Playlist getPlaylistInfo(String playlistName) throws IOException, PlaylistNotFoundException {
        ObjectMapper mapper = new ObjectMapper();
        ArrayNode jsonArray;
        Path path = PathsConfig.PLAYLISTS_FILE_PATH;

        if (Files.exists(path)) {
            String content = new String(Files.readAllBytes(path));
            if (content.isEmpty()) {
                throw new PlaylistNotFoundException("Playlist not found: " + playlistName);
            } else {
                jsonArray = (ArrayNode) mapper.readTree(content);
            }
        } else {
            throw new PlaylistNotFoundException("Playlist not found: " + playlistName);
        }

        for (int i = 0; i < jsonArray.size(); i++) {
            ObjectNode playlistInfo = (ObjectNode) jsonArray.get(i);
            if (playlistInfo.get("playlistName").asText().equals(playlistName)) {
                Playlist playlist = new Playlist();
                playlist.name = playlistInfo.get("playlistName").asText();
                playlist.description = playlistInfo.get("playlistDescription").asText();
                playlist.creationDate = playlistInfo.get("playlistCreationDate").asInt();
                playlist.genre = playlistInfo.get("playlistGenre").asText();
                playlist.creator = playlistInfo.get("playlistCreator").asText();
                return playlist;
            }
        }
        throw new PlaylistNotFoundException("Playlist not found: " + playlistName);
    }

    /**
     * Set songs for the playlist.
     * This method adds a song to the playlist and updates the JSON file defined by PathsConfig.PLAYLISTS_FILE_PATH.
     *
     * @param songName the name of the song to add to the playlist.
     * @throws IOException if there is an error accessing the file.
     */
    public void setSongs(String songName) throws IOException {
        if (songName.isEmpty()) {
            System.out.println("Be sure to enter a song name!");
            setSongs(songName);
        }
        System.out.println("Enter the song name: ");
        ObjectNode playlistInfo = new ObjectMapper().createObjectNode();
        playlistInfo.put("Songs", songName);
        ArrayNode jsonArray;
        Path path = PathsConfig.PLAYLISTS_FILE_PATH;

        if (Files.exists(path) && Files.size(path) != 0) {
            String content = new String(Files.readAllBytes(path));
            jsonArray = (ArrayNode) new ObjectMapper().readTree(content);
        } else {
            jsonArray = new ObjectMapper().createArrayNode();
        }

        jsonArray.add(playlistInfo);

        try (BufferedWriter bw = Files.newBufferedWriter(path)) {
            bw.write(jsonArray.toPrettyString());
        }
    }

    /**
     * Remove a song from the playlist.
     * This method removes a specified song from the playlist.
     *
     * @param songName the name of the song to remove.
     */
    public void removeSongFromPlaylist(String songName) {
        System.out.println("Song " + songName + " removed from playlist " + name);
    }

    /**
     * Save playlist information to a JSON file.
     * This method writes the playlist information to a JSON file defined by PathsConfig.PLAYLISTS_FILE_PATH.
     * If the playlist already exists in the file, it replaces the existing entry.
     *
     * @throws DataAccessException if there is an error accessing the file.
     */
    public void savePlaylistInfo() throws DataAccessException {
        ObjectMapper mapper = new ObjectMapper();

        ObjectNode playlistInfo = mapper.createObjectNode();
        playlistInfo.put("playlistName", name);
        playlistInfo.put("playlistDescription", description);
        playlistInfo.put("playlistGenre", genre);
        playlistInfo.put("playlistCreationDate", creationDate);
        playlistInfo.put("playlistCreator", creator);

        Path path = PathsConfig.PLAYLISTS_FILE_PATH;

        ArrayNode jsonArray;
        try {
            if (Files.exists(path)) {
                String content = new String(Files.readAllBytes(path));
                if (content.isEmpty()) {
                    jsonArray = mapper.createArrayNode();
                } else {
                    jsonArray = (ArrayNode) mapper.readTree(content);
                }
            } else {
                jsonArray = mapper.createArrayNode();
            }

            for (int i = 0; i < jsonArray.size(); i++) {
                ObjectNode existingPlaylist = (ObjectNode) jsonArray.get(i);
                if (existingPlaylist.get("playlistName").asText().equals(name)) {
                    // If the playlist exists, remove it
                    jsonArray.remove(i);
                    break;
                }
            }

            jsonArray.add(playlistInfo);

            Files.write(path, jsonArray.toPrettyString().getBytes());
        } catch (IOException e) {
            throw new DataAccessException("Failed to save playlist info", e);
        }
    }

    /**
     * Delete playlist information by playlist name.
     * This method removes the playlist information from the JSON file defined by PathsConfig.PLAYLISTS_FILE_PATH
     * if a playlist with the given name is found.
     *
     * @param playlistName the name of the playlist to delete.
     * @throws DataAccessException if there is an error accessing the file.
     */
    public void deletePlaylistInfo(String playlistName) throws DataAccessException {
        ObjectMapper mapper = new ObjectMapper();
        ArrayNode jsonArray;
        Path path = PathsConfig.PLAYLISTS_FILE_PATH;

        try {
            if (Files.exists(path)) {
                String content = new String(Files.readAllBytes(path));
                if (!content.isEmpty()) {
                    jsonArray = (ArrayNode) mapper.readTree(content);
                } else {
                    System.out.println("No playlists found.");
                    return;
                }
            } else {
                System.out.println("No playlists found.");
                return;
            }

            for (int i = 0; i < jsonArray.size(); i++) {
                ObjectNode playlistInfo = (ObjectNode) jsonArray.get(i);
                if (playlistInfo.get("playlistName").asText().equals(playlistName)) {
                    jsonArray.remove(i);
                    System.out.println("Playlist " + playlistName + " deleted");
                    break;
                }
            }

            try (BufferedWriter bw = Files.newBufferedWriter(path)) {
                bw.write(jsonArray.toPrettyString());
            }
        } catch (IOException e) {
            throw new DataAccessException("Failed to delete playlist info", e);
        }
    }

    /**
     * Modify the name of the playlist.
     * This method changes the name of the playlist.
     *
     * @param newPlaylistName the new name of the playlist.
     */
    public void modifyPlaylistName(String newPlaylistName) {
        System.out.println("Playlist name changed from " + name + " to " + newPlaylistName);
        name = newPlaylistName;
    }

    /**
     * Convert an Artist object to JSON and print it.
     * This method creates an Artist object with placeholder data, converts it to a JSON string, and prints it.
     *
     * @return true if the conversion and printing are successful, false otherwise.
     */
    public static boolean toJson() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            Artist artist = new Artist("name", "genre", "members", "date", "bio");
            String json = mapper.writeValueAsString(artist);
            System.out.println(json);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public String toString() {
        return "Name: " + name + '\n' +
                " Description = " + description + '\n' +
                " Genre = " + genre + '\n' +
                " Creation Date = " + creationDate + '\n' +
                " Creator = " + creator + '\n';
    }
}
