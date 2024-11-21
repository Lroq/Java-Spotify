package fr.quentin.spotify.artistGroup;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fr.quentin.spotify.exceptions.SongNotFoundException;
import fr.quentin.spotify.exceptions.DataAccessException;
import fr.quentin.spotify.config.PathsConfig;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

public class Song {
    private String id;
    private String albumId;
    private String name;
    private String duration;

    // Default constructor generating a unique song ID
    public Song() {
        this.id = UUID.randomUUID().toString();
    }

    // Constructor with parameters to initialize song details
    public Song(String albumId, String songName, String songDuration) {
        this();
        this.albumId = albumId;
        this.name = songName;
        this.duration = songDuration;
    }

    // Getters for song attributes
    public String getSongId() {
        return id;
    }

    public String getAlbumId() {
        return albumId;
    }

    public String getSongName() {
        return name;
    }

    public String getDuration() {
        return duration;
    }

    /**
     * Save song information to a JSON file.
     * This method writes the song information to a JSON file defined by PathsConfig.SONGS_FILE_PATH.
     * If the song already exists in the file, it replaces the existing entry.
     *
     * @throws DataAccessException if there is an error accessing the file.
     */
    public void saveSongToJson() throws DataAccessException {
        ObjectMapper mapper = new ObjectMapper();

        ObjectNode songInfo = mapper.createObjectNode();
        songInfo.put("songId", id);
        songInfo.put("albumId", albumId);
        songInfo.put("songName", name);
        songInfo.put("songDuration", duration);

        Path path = PathsConfig.SONGS_FILE_PATH;

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
                if (jsonArray.get(i) instanceof ObjectNode) {
                    ObjectNode existingSong = (ObjectNode) jsonArray.get(i);
                    if (existingSong.get("songId").asText().equals(id)) {
                        jsonArray.remove(i);
                        break;
                    }
                }
            }

            jsonArray.add(songInfo);

            Files.write(path, jsonArray.toPrettyString().getBytes());
        } catch (IOException e) {
            throw new DataAccessException("Failed to save song info", e);
        }
    }

    /**
     * Retrieve song information by song name.
     * This method reads the song information from the JSON file defined by PathsConfig.SONGS_FILE_PATH
     * and returns a Song object if a song with the given name is found.
     *
     * @param songName the name of the song to retrieve.
     * @return a Song object if found.
     * @throws IOException if there is an error accessing the file.
     * @throws SongNotFoundException if the song is not found.
     */
    public static Song getSongInfo(String songName) throws IOException, SongNotFoundException {
        ArrayNode jsonArray;
        Path path = PathsConfig.SONGS_FILE_PATH;
        ObjectMapper mapper = new ObjectMapper();

        if (Files.exists(path)) {
            String content = new String(Files.readAllBytes(path));
            if (content.isEmpty()) {
                throw new SongNotFoundException("Song not found: " + songName);
            } else {
                jsonArray = (ArrayNode) mapper.readTree(content);
            }
        } else {
            throw new SongNotFoundException("Song not found: " + songName);
        }

        for (int i = 0; i < jsonArray.size(); i++) {
            ObjectNode songInfo = (ObjectNode) jsonArray.get(i);
            if (songInfo.get("songName").asText().equals(songName)) {
                Song song = new Song();
                song.id = songInfo.get("songId").asText();
                song.albumId = songInfo.get("albumId").asText();
                song.name = songInfo.get("songName").asText();
                song.duration = songInfo.get("songDuration").asText();
                return song;
            }
        }
        throw new SongNotFoundException("Song not found: " + songName);
    }

    /**
     * Delete song information by song name.
     * This method removes the song information from the JSON file defined by PathsConfig.SONGS_FILE_PATH
     * if a song with the given name is found.
     *
     * @param songName the name of the song to delete.
     * @throws DataAccessException if there is an error accessing the file.
     */
    public void deleteSongInfo(String songName) throws DataAccessException {
        ArrayNode jsonArray;
        Path path = PathsConfig.SONGS_FILE_PATH;
        ObjectMapper mapper = new ObjectMapper();

        try {
            String content = new String(Files.readAllBytes(path));
            if (content.isEmpty()) {
                return;
            } else {
                jsonArray = (ArrayNode) mapper.readTree(content);
            }

            for (int i = 0; i < jsonArray.size(); i++) {
                ObjectNode songInfo = (ObjectNode) jsonArray.get(i);
                if (songInfo.get("songName").asText().equals(songName)) {
                    jsonArray.remove(i);
                    break;
                }
            }

            Files.write(path, jsonArray.toPrettyString().getBytes());
        } catch (IOException e) {
            throw new DataAccessException("Failed to delete song info", e);
        }
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
            e.toString();
        }
        return false;
    }

    @Override
    public String toString() {
        return "Song:" + '\n' +
                "Id = " + id + '\n' +
                " Album Id = " + albumId + '\n' +
                " Name = " + name + '\n' +
                " Duration = " + duration + '\n';
    }
}
