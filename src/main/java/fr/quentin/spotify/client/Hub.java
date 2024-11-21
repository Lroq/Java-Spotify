package fr.quentin.spotify.client;

import java.io.IOException;
import java.util.Scanner;

import fr.quentin.spotify.exceptions.*;
import fr.quentin.spotify.server.Account;
import fr.quentin.spotify.server.AdminAccount;
import fr.quentin.spotify.server.Searchable;
import fr.quentin.spotify.utils.ANSIColorUtil;

public class Hub {
    private Account account;
    private Scanner scanner;
    private Searchable searchable;

    // Constructor to initialize the Hub with an account and searchable interface
    public Hub(Account account, Searchable searchable) {
        this.account = account;
        this.scanner = new Scanner(System.in);
        this.searchable = searchable;
    }

    /**
     * Displays the menu and handles user choices recursively.
     * This method displays the menu and processes user choices in a loop until the user chooses to exit.
     *
     * @throws IOException if an I/O error occurs.
     * @throws PlaylistNotFoundException if a playlist is not found.
     * @throws ArtistNotFoundException if an artist is not found.
     * @throws DataAccessException if there is an error accessing data.
     * @throws SongNotFoundException if a song is not found.
     */
    public void recursiveMenu() throws IOException, PlaylistNotFoundException, ArtistNotFoundException, DataAccessException, SongNotFoundException, AlbumNotFoundException {
        do {
            displayMenu();
        } while (handleChoice() != 16);
    }

    /**
     * Displays the menu options to the user.
     * This method prints the menu options based on whether the user is an admin or a regular user.
     *
     * @throws IOException if an I/O error occurs.
     */
    public void displayMenu() throws IOException {
        if (account.getAdminStatusJson()) {
            System.out.print("Hello! Welcome ");
            ANSIColorUtil.printColoredText(account.getClientUsername(), ANSIColorUtil.ANSI_PURPLE);
            System.out.println(" to Spotify!");
            System.out.println("---------------------------------------------------------");
            System.out.println("1.  Search for a user");
            System.out.println("2.  Search for an artist");
            System.out.println("3.  Search for an album");
            System.out.println("4.  Search for a song");
            System.out.println("5.  Search for a playlist");
            System.out.println("6.  Add an artist");
            System.out.println("7.  Add an album");
            System.out.println("8.  Add a song");
            System.out.println("9.  Add a playlist");
            System.out.println("10. Create new admin account");
            System.out.println("11. Delete a user");
            System.out.println("12. Delete an artist");
            System.out.println("13. Delete an album");
            System.out.println("14. Delete a song");
            System.out.println("15. Delete a playlist");
            System.out.println("16. Exit");
        } else {
            System.out.print("Hello! Welcome ");
            ANSIColorUtil.printColoredText(account.getClientUsername(), ANSIColorUtil.ANSI_PURPLE);
            System.out.println(" to Spotify!");
            System.out.println("---------------------------------------------------------");
            System.out.println("1. Search for a song");
            System.out.println("2. Search for an album");
            System.out.println("3. Search for an artist");
            System.out.println("4. Search for a playlist");
            System.out.println("5. Check your profile information");
            System.out.println("6. Modify your profile information");
            System.out.println("7. Exit");
        }
    }

    /**
     * Reads the user's choice from the console.
     * This method reads an integer input from the user.
     *
     * @return the user's choice as an integer.
     */
    public Integer getChoice() {
        int choice = Integer.parseInt(scanner.nextLine());
        return choice;
    }

    /**
     * Handles the user's menu choice.
     * This method processes the user's choice based on their account type (admin or regular user).
     *
     * @return the user's choice as an integer.
     * @throws IOException if an I/O error occurs.
     * @throws PlaylistNotFoundException if a playlist is not found.
     * @throws ArtistNotFoundException if an artist is not found.
     * @throws DataAccessException if there is an error accessing data.
     * @throws SongNotFoundException if a song is not found.
     */
    public int handleChoice() throws IOException, PlaylistNotFoundException, ArtistNotFoundException, DataAccessException, SongNotFoundException, AlbumNotFoundException {
        int choice = getChoice();
        if (account.getAdminStatusJson()) {
            return handleAdminChoice(choice);
        } else {
            return handleUserChoice(choice);
        }
    }

    /**
     * Handles the admin's menu choice.
     * This method processes the admin's choice and performs the corresponding action.
     *
     * @param choice the admin's choice as an integer.
     * @return the admin's choice as an integer.
     * @throws IOException if an I/O error occurs.
     * @throws DataAccessException if there is an error accessing data.
     * @throws ArtistNotFoundException if an artist is not found.
     * @throws PlaylistNotFoundException if a playlist is not found.
     * @throws SongNotFoundException if a song is not found.
     */
    public int handleAdminChoice(int choice) throws IOException, DataAccessException, ArtistNotFoundException, PlaylistNotFoundException, SongNotFoundException, AlbumNotFoundException {
        switch (choice) {
            case 1:
                AdminAccount.searchForUser(scanner);
                break;
            case 2:
                System.out.println(searchable.searchForArtist(scanner));
                break;
            case 3:
                System.out.println(searchable.searchForAlbum(scanner));
                break;
            case 4:
                System.out.println(searchable.searchForSong(scanner));
                break;
            case 5:
                System.out.println(searchable.searchForPlaylist(scanner));
                break;
            case 6:
                AdminAccount.addArtist(scanner);
                break;
            case 7:
                AdminAccount.addAlbum(scanner);
                break;
            case 8:
                AdminAccount.addSong(scanner);
                break;
            case 9:
                AdminAccount.addPlaylist(scanner);
                break;
            case 10:
                AdminAccount.createNewAdminAccount(scanner);
                break;
            case 11:
                AdminAccount.deleteUser(scanner);
                break;
            case 12:
                AdminAccount.deleteArtist(scanner);
                break;
            case 13:
                AdminAccount.deleteAlbum(scanner);
                break;
            case 14:
                AdminAccount.deleteSong(scanner);
                break;
            case 15:
                AdminAccount.deletePlaylist(scanner);
                break;
            case 16:
                System.out.println("Goodbye!");
                System.exit(0);
                break;
            default:
                System.out.println("Invalid choice. Please try again.");
                break;
        }
        return choice;
    }

    /**
     * Handles the regular user's menu choice.
     * This method processes the regular user's choice and performs the corresponding action.
     *
     * @param choice the user's choice as an integer.
     * @return the user's choice as an integer.
     * @throws IOException if an I/O error occurs.
     * @throws ArtistNotFoundException if an artist is not found.
     * @throws DataAccessException if there is an error accessing data.
     * @throws PlaylistNotFoundException if a playlist is not found.
     * @throws SongNotFoundException if a song is not found.
     */
    public int handleUserChoice(int choice) throws IOException, ArtistNotFoundException, DataAccessException, PlaylistNotFoundException, SongNotFoundException, AlbumNotFoundException {
        switch (choice) {
            case 1:
                System.out.println(searchable.searchForSong(scanner));
                break;
            case 2:
                System.out.println(searchable.searchForAlbum(scanner));
                break;
            case 3:
                System.out.println(searchable.searchForArtist(scanner));
                break;
            case 4:
                System.out.println(searchable.searchForPlaylist(scanner));
                break;
            case 5:
                Account.checkProfileInformation(account);
                break;
            case 6:
                Account.modifyProfileInformation(account);
                break;
            case 7:
                System.out.println("Goodbye!");
                System.exit(0);
                break;
            default:
                System.out.println("Invalid choice. Please try again.");
                break;
        }
        return choice;
    }
}
