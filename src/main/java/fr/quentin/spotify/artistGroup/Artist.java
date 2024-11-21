package fr.quentin.spotify.artistGroup;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fr.quentin.spotify.config.PathsConfig;
import fr.quentin.spotify.exceptions.ArtistNotFoundException;
import fr.quentin.spotify.exceptions.DataAccessException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Artist {
    private static String Id;
    private String name;
    private String members;
    private String creationDate;
    private String genre;
    private String artistBio;
    private List<Album> albums;

    public Artist() {
        this.Id = UUID.randomUUID().toString();
        this.albums = new ArrayList<>();
    }

    public Artist(String artistName, String artistGenre, String artistMembers, String creationDate, String artistBio) {
        this();
        this.name = artistName;
        this.genre = artistGenre;
        this.members = artistMembers;
        this.creationDate = creationDate;
        this.artistBio = artistBio;
    }

    public static String getId() {
        return Id;
    }

    public String getName() {
        return name;
    }

    public String getGenre() {
        return genre;
    }

    public String getMembers() {
        return members;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public String getArtistBio() {
        return artistBio;
    }

    public List<Album> getAlbums() {
        return albums;
    }

    public void addAlbum(String albumId, String albumName) {
        Album album = new Album(albumId, albumName, null, null);
        albums.add(album);
    }

    /**
     * Save artist information to a JSON file.
     * This method writes the artist information, including albums, to a JSON file defined by PathsConfig.ARTISTS_FILE_PATH.
     * If the artist already exists in the file, it replaces the existing entry.
     *
     * @return true if the artist information is saved successfully, false otherwise.
     * @throws DataAccessException if there is an error accessing the file.
     */
    public boolean saveArtistInfo() throws DataAccessException {
        ObjectMapper mapper = new ObjectMapper();

        ObjectNode artistInfo = mapper.createObjectNode();
        artistInfo.put("artistId", Id);
        artistInfo.put("artistName", name);
        artistInfo.put("artistMembers", members);
        artistInfo.put("artistCreationDate", creationDate);
        artistInfo.put("artistGenre", genre);
        artistInfo.put("artistBio", artistBio);

        ArrayNode albumsArray = mapper.createArrayNode();
        for (Album album : albums) {
            ObjectNode albumInfo = mapper.createObjectNode();
            albumInfo.put("albumId", album.getAlbumId());
            albumInfo.put("albumName", album.getName());
            albumsArray.add(albumInfo);
        }

        artistInfo.set("artistAlbums", albumsArray);

        Path path = PathsConfig.ARTISTS_FILE_PATH;

        try {
            ArrayNode jsonArray;
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
                ObjectNode existingArtist = (ObjectNode) jsonArray.get(i);
                if (existingArtist.get("artistId").asText().equals(Id)) {
                    // If the artist exists, remove it
                    jsonArray.remove(i);
                    break;
                }
            }

            jsonArray.add(artistInfo);

            Files.write(path, jsonArray.toPrettyString().getBytes());
        } catch (IOException e) {
            throw new DataAccessException("Failed to save artist info", e);
        }

        return true;
    }

    /**
     * Retrieve artist information by artist name.
     * This method reads the artist information from the JSON file defined by PathsConfig.ARTISTS_FILE_PATH
     * and returns an Artist object if an artist with the given name is found.
     *
     * @param artistName the name of the artist to retrieve.
     * @return an Artist object if found.
     * @throws DataAccessException if there is an error accessing the file.
     * @throws ArtistNotFoundException if the artist is not found.
     */
    public static Artist getArtistInfo(String artistName) throws DataAccessException, ArtistNotFoundException {
        ObjectMapper mapper = new ObjectMapper();
        ArrayNode jsonArray;
        Path path = PathsConfig.ARTISTS_FILE_PATH;

        try {
            if (Files.exists(path)) {
                String content = new String(Files.readAllBytes(path));
                if (content.isEmpty()) {
                    throw new ArtistNotFoundException("Artist not found: " + artistName);
                } else {
                    jsonArray = (ArrayNode) mapper.readTree(content);
                }
            } else {
                throw new ArtistNotFoundException("Artist not found: " + artistName);
            }

            for (int i = 0; i < jsonArray.size(); i++) {
                ObjectNode artistInfo = (ObjectNode) jsonArray.get(i);
                if (artistInfo.get("artistName").asText().equals(artistName)) {
                    Artist artist = new Artist();
                    Id = artistInfo.get("artistId").asText();
                    artist.name = artistInfo.get("artistName").asText();
                    artist.members = artistInfo.get("artistMembers").asText();
                    artist.creationDate = artistInfo.get("artistCreationDate").asText();
                    artist.genre = artistInfo.get("artistGenre").asText();
                    artist.artistBio = artistInfo.get("artistBio").asText();
                    ArrayNode albumsArray = (ArrayNode) artistInfo.get("artistAlbums");
                    for (int j = 0; j < albumsArray.size(); j++) {
                        ObjectNode albumInfo = (ObjectNode) albumsArray.get(j);
                        artist.albums.add(new Album(albumInfo.get("albumId").asText(), albumInfo.get("albumName").asText(), null, null));
                    }
                    return artist;
                }
            }
        } catch (IOException e) {
            throw new DataAccessException("Failed to access artist info", e);
        }

        throw new ArtistNotFoundException("Artist not found: " + artistName);
    }

    /**
     * Delete artist information by artist name.
     * This method removes the artist information from the JSON file defined by PathsConfig.ARTISTS_FILE_PATH
     * if an artist with the given name is found.
     *
     * @param artistName the name of the artist to delete.
     * @throws DataAccessException if there is an error accessing the file.
     */
    public void deleteArtistInfo(String artistName) throws DataAccessException {
        ObjectMapper mapper = new ObjectMapper();
        ArrayNode jsonArray;
        Path path = PathsConfig.ARTISTS_FILE_PATH;

        try {
            String content = new String(Files.readAllBytes(path));
            if (content.isEmpty()) {
                return;
            } else {
                jsonArray = (ArrayNode) mapper.readTree(content);
            }
        } catch (IOException e) {
            throw new DataAccessException("Failed to read artist info", e);
        }

        for (int i = 0; i < jsonArray.size(); i++) {
            ObjectNode artistInfo = (ObjectNode) jsonArray.get(i);
            if (artistInfo.get("artistName").asText().equals(artistName)) {
                jsonArray.remove(i);
                break;
            }
        }

        try {
            Files.write(path, jsonArray.toPrettyString().getBytes());
        } catch (IOException e) {
            throw new DataAccessException("Failed to write artist info", e);
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
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public String toString() {
        return "Artist{" +
                "Id='" + Id + '\'' +
                ", name='" + name + '\'' +
                ", members='" + members + '\'' +
                ", creationDate='" + creationDate + '\'' +
                ", genre='" + genre + '\'' +
                ", artistBio='" + artistBio + '\'' +
                ", albums=" + albums +
                '}';
    }
}
