package fr.quentin.spotify.server;

import java.io.IOException;
import java.util.Scanner;
import fr.quentin.spotify.artistGroup.Artist;
import fr.quentin.spotify.artistGroup.Album;
import fr.quentin.spotify.artistGroup.Playlist;
import fr.quentin.spotify.artistGroup.Song;
import fr.quentin.spotify.exceptions.*;

// Represents an admin account in the Spotify application
public class AdminAccount extends Account {

    private final boolean adminStatus = true; // Indicates admin status

    // Constructor that initializes as an admin account by calling super constructor
    public AdminAccount() throws IOException {
        super();
    }

    // Searches for a user based on email
    public static void searchForUser(Scanner scanner) throws IOException {
        AdminAccount adminAccount = new AdminAccount();
        String email = "";
        boolean isFound = false;

        while (!isFound) {
            System.out.println("Enter the email of the user you want to search for (type '*stop*' to return to the main menu): ");
            email = scanner.nextLine();
            if (email.equalsIgnoreCase("*stop*")) return;

            if (!adminAccount.checkMailValidFormat(email)) {
                System.out.println("Invalid email format");
            } else {
                Account account = Account.loadClientInfo(email);
                if (account == null) {
                    System.out.println("User not found. Please try again.");
                } else {
                    isFound = true;
                    System.out.println("User found!");
                    System.out.println("This is the user's information: ");
                    System.out.println(account.toJson());
                }
            }
        }
    }

    // Prints information about a song based on its name
    private Boolean printSongInfo(String songName) throws IOException, SongNotFoundException {
        Song song = Song.getSongInfo(songName);
        return song != null ? song.toJson() : null;
    }

    // Prints information about an artist based on its name
    private Boolean printArtistInfo(String artistName) throws IOException, ArtistNotFoundException, DataAccessException {
        Artist artist = Artist.getArtistInfo(artistName);
        return artist.toJson();
    }

    // Adds a new artist to the system
    public static void addArtist(Scanner scanner) throws IOException, DataAccessException {
        System.out.println("Enter the artist's name (type '*stop*' to return to the main menu): ");
        String artistName = scanner.nextLine();
        if (artistName.equalsIgnoreCase("*stop*")) return;

        System.out.println("Enter the artist's genre: ");
        String artistGenre = scanner.nextLine();
        if (artistGenre.equalsIgnoreCase("*stop*")) return;

        System.out.println("Enter the artist's members: ");
        String artistMembers = scanner.nextLine();
        if (artistMembers.equalsIgnoreCase("*stop*")) return;

        System.out.println("Enter the artist's creation date: ");
        String artistCreationDate = scanner.nextLine();
        if (artistCreationDate.equalsIgnoreCase("*stop*")) return;

        System.out.println("Enter the artist's bio: ");
        String artistBio = scanner.nextLine();
        if (artistBio.equalsIgnoreCase("*stop*")) return;

        Artist artist = new Artist(artistName, artistGenre, artistMembers, artistCreationDate, artistBio);
        artist.saveArtistInfo();

        System.out.println("Artist added successfully!");
    }

    // Adds a new album to the system
    public static void addAlbum(Scanner scanner) throws IOException, DataAccessException, ArtistNotFoundException {
        System.out.println("Enter the album's name (type '*stop*' to return to the main menu): ");
        String albumName = scanner.nextLine();
        if (albumName.equalsIgnoreCase("*stop*")) return;

        System.out.println("Enter the album's release date: ");
        String albumReleaseDate = scanner.nextLine();
        if (albumReleaseDate.equalsIgnoreCase("*stop*")) return;

        System.out.println("Enter the album's genre: ");
        String albumGenre = scanner.nextLine();
        if (albumGenre.equalsIgnoreCase("*stop*")) return;

        System.out.println("Enter the artist's name for this album: ");
        String artistName = scanner.nextLine();
        if (artistName.equalsIgnoreCase("*stop*")) return;

        Artist artist = Artist.getArtistInfo(artistName);
        if (artist == null) {
            System.out.println("Artist not found. Please add the artist first.");
            return;
        }

        Album album = new Album(artist.getId(), albumName, albumReleaseDate, albumGenre);
        artist.addAlbum(album.getAlbumId(), albumName);
        album.saveAlbumInfo();
        artist.saveArtistInfo();

        System.out.println("Album added successfully!");
    }

    // Adds a new song to the system
    public static void addSong(Scanner scanner) throws IOException, DataAccessException, AlbumNotFoundException {
        System.out.println("Enter the song's name (type '*stop*' to return to the main menu): ");
        String songName = scanner.nextLine();
        if (songName.equalsIgnoreCase("*stop*")) return;

        System.out.println("Enter the song's duration: ");
        String songDuration = scanner.nextLine();
        if (songDuration.equalsIgnoreCase("*stop*")) return;

        System.out.println("Enter the album's name for this song: ");
        String albumName = scanner.nextLine();
        if (albumName.equalsIgnoreCase("*stop*")) return;

        Album album = Album.getAlbumInfo(albumName);
        if (album == null) {
            System.out.println("Album not found. Please add the album first.");
            return;
        }

        Song song = new Song(album.getAlbumId(), songName, songDuration);
        album.addSong(song.getSongId(), songName);
        album.saveAlbumInfo();
        song.saveSongToJson();

        System.out.println("Song added successfully!");
    }

    // Adds a new playlist to the system
    public static void addPlaylist(Scanner scanner) throws IOException, DataAccessException {
        System.out.println("Enter the playlist's name (type '*stop*' to return to the main menu): ");
        String playlistName = scanner.nextLine();
        if (playlistName.equalsIgnoreCase("*stop*")) return;

        System.out.println("Enter the playlist's description: ");
        String playlistDescription = scanner.nextLine();
        if (playlistDescription.equalsIgnoreCase("*stop*")) return;

        System.out.println("Enter the playlist's genre: ");
        String playlistGenre = scanner.nextLine();
        if (playlistGenre.equalsIgnoreCase("*stop*")) return;

        System.out.println("Enter the playlist's creation date: ");
        Integer playlistCreationDate = Integer.valueOf(scanner.nextLine());
        if (playlistCreationDate.equals("stop")) return;

        System.out.println("Enter the playlist's creator: ");
        String playlistCreator = scanner.nextLine();
        if (playlistCreator.equalsIgnoreCase("*stop*")) return;

        Playlist playlist = new Playlist(playlistName, playlistDescription, playlistGenre, playlistCreationDate, playlistCreator);
        playlist.savePlaylistInfo();

        System.out.println("Playlist '" + playlist.getName() + "' added successfully!");
    }

    // Deletes an artist from the system
    public static void deleteArtist(Scanner scanner) throws IOException, DataAccessException, ArtistNotFoundException {
        System.out.println("Enter the name of the artist you want to delete (type '*stop*' to return to the main menu): ");
        String artistName = scanner.nextLine();
        if (artistName.equalsIgnoreCase("*stop*")) return;

        Artist artist = Artist.getArtistInfo(artistName);
        if (artist != null) {
            artist.deleteArtistInfo(artistName);
            System.out.println("Artist deleted successfully!");
        } else {
            System.out.println("Artist not found.");
        }
    }

    // Deletes an album from the system
    public static void deleteAlbum(Scanner scanner) throws IOException, DataAccessException, AlbumNotFoundException {
        System.out.println("Enter the name of the album you want to delete (type '*stop*' to return to the main menu): ");
        String albumName = scanner.nextLine();
        if (albumName.equalsIgnoreCase("*stop*")) return;

        Album album = Album.getAlbumInfo(albumName);
        if (album != null) {
            album.deleteAlbumInfo(albumName);
            System.out.println("Album deleted successfully!");
        } else {
            System.out.println("Album not found.");
        }
    }

    // Deletes a song from the system
    public static void deleteSong(Scanner scanner) throws IOException, DataAccessException, SongNotFoundException {
        System.out.println("Enter the name of the song you want to delete (type '*stop*' to return to the main menu): ");
        String songName = scanner.nextLine();
        if (songName.equalsIgnoreCase("*stop*")) return;

        Song song = Song.getSongInfo(songName);
        if (song != null) {
            song.deleteSongInfo(songName);
            System.out.println("Song deleted successfully!");
        } else {
            System.out.println("Song not found.");
        }
    }

    // Deletes a playlist from the system
    public static void deletePlaylist(Scanner scanner) throws IOException, PlaylistNotFoundException, DataAccessException {
        System.out.println("Enter the name of the playlist you want to delete (type '*stop*' to return to the main menu): ");
        String playlistName = scanner.nextLine();
        if (playlistName.equalsIgnoreCase("*stop*")) return;

        Playlist playlist = Playlist.getPlaylistInfo(playlistName);
        if (playlist != null) {
            playlist.deletePlaylistInfo(playlistName);
            System.out.println("Playlist deleted successfully!");
        } else {
            System.out.println("Playlist not found.");
        }
    }

    // Creates a new admin account in the system
    public static void createNewAdminAccount(Scanner scanner) throws IOException {
        System.out.println("Enter the email of the new admin account (type '*stop*' to return to the main menu): ");
        String email = scanner.nextLine();
        if (email.equalsIgnoreCase("*stop*")) return;

        System.out.println("Enter the password of the new admin account: ");
        String password = scanner.nextLine();
        if (password.equalsIgnoreCase("*stop*")) return;

        System.out.println("Enter the username of the new admin account: ");
        String firstName = scanner.nextLine();
        if (firstName.equalsIgnoreCase("*stop*")) return;

        System.out.println("Enter the date of birth of the new admin account: ");
        String dateOfBirth = scanner.nextLine();
        if (dateOfBirth.equalsIgnoreCase("*stop*")) return;

        System.out.println("Enter the country of the new admin account: ");
        String country = scanner.nextLine();
        if (country.equalsIgnoreCase("*stop*")) return;

        Account adminAccount = new Account();
        adminAccount.setClientEmail(email);
        adminAccount.setClientPassword(password);
        adminAccount.setClientUsername(firstName);
        adminAccount.setClientBirthDate(dateOfBirth);
        adminAccount.setClientCountry(country);
        adminAccount.setAdminStatus(true);
        adminAccount.saveClientInfo();

        System.out.println("Admin account created successfully!");
    }

    // Deletes a user from the system
    public static void deleteUser(Scanner scanner) throws IOException {
        System.out.println("Enter the email of the user you want to delete (type '*stop*' to return to the main menu): ");
        String email = scanner.nextLine();
        if (email.equalsIgnoreCase("*stop*")) return;

        Account account = Account.loadClientInfo(email);
        if (account != null) {
            account.deleteClientInfo(email);
            System.out.println("User deleted successfully!");
        } else {
            System.out.println("User not found.");
        }
    }
}
