# Spotify Java Application

## Table of Contents

1. [Project Overview](#project-overview)
2. [Features](#features)
3. [Requirements](#requirements)
4. [Installation](#installation)
5. [Usage](#usage)
6. [Directory Structure](#directory-structure)
7. [Contributing](#contributing)
8. [License](#license)
9. [Contact](#contact)

## Project Overview

This project is a Java application for managing Spotify-like functionalities, including user accounts, artists, albums, songs, and playlists. It features admin capabilities for managing artists, albums, and songs, as well as user functionalities for logging in, signing up, and managing personal account details.

## Features

- User Authentication
    - Log in
    - Sign up
    - View and edit account details
- Admin Capabilities
    - Add, view, and delete artists
    - Add, view, and delete albums
    - Add, view, and delete songs
    - Add, view, and delete playlists
- Data Persistence using JSON

## Requirements

- Java Development Kit (JDK) 8 or higher
- Apache Maven
- Any Java IDE (Eclipse, IntelliJ IDEA, etc.)

## Installation

1. **Clone the repository**

   ```bash
   git clone https://github.com/your-username/spotify-java-app.git
   cd spotify-java-app
Build the project using Maven

```bash
mvn clean install
```
Run the application

```bash
mvn exec:java -Dexec.mainClass="fr.quentin.spotify.Main"
```

## Usage

1.Starting the Application

Upon running the application, you will be presented with a welcome screen where you can choose to log in, sign up, or exit.

2.Logging In

Enter your email and password.

If the credentials are correct, you will be logged in successfully.

3.Signing Up

Enter your email, password, and additional details like username, birthdate, and country.
Ensure your email and password formats are correct.
If the details are valid, your account will be created.

4.Admin Actions

As an admin, you can manage artists, albums, songs, and playlists through the provided menu options.
Each management option will prompt you for the necessary details and perform the requested action.

## Directory Structure

spotify-java-app/

├── src\
│   ├── main\
│   │   ├── java\
│   │   │   ├── fr\
│   │   │   │   ├── quentin\
│   │   │   │   │   ├── spotify\
│   │   │   │   │   │   ├── artistGroup\
│   │   │   │   │   │   │   ├── Album.java\
│   │   │   │   │   │   │   ├── Artist.java\
│   │   │   │   │   │   │   ├── Playlist.java\
│   │   │   │   │   │   │   ├── Song.java\
│   │   │   │   │   │   ├── client\
│   │   │   │   │   │   │   ├── Hub.java\
│   │   │   │   │   │   ├── config\
│   │   │   │   │   │   │   ├── PathsConfig.java\
│   │   │   │   │   │   ├── exceptions\
│   │   │   │   │   │   │   ├── AlbumNotFoundException.java\
│   │   │   │   │   │   │   ├── ArtistNotFoundException.java\
│   │   │   │   │   │   │   ├── DataAccessException.java\
│   │   │   │   │   │   │   ├── PlaylistNotFoundException.java\
│   │   │   │   │   │   │   ├── SongNotFoundException.java\
│   │   │   │   │   │   ├── server\
│   │   │   │   │   │   │   ├── Account.java\
│   │   │   │   │   │   │   ├── AdminAccount.java\
│   │   │   │   │   │   │   ├── Connexion.java\
│   │   │   │   │   │   │   └── Main.java\
│   │   │   │   │   │   ├── utils\
│   │   │   │   │   │   │   └── ANSIColorUtil.java\
│   │   │   │   │   │   ├── mainApplication\
│   │   │   │   │   │   │   └── mainApplication.java\
│   │   └── resources\
│   │       ├── data\
│   │       │   ├── account.json\
│   │       │   ├── albums.json\
│   │       │   ├── artists.json\
│   │       │   ├── playlists.json\
│   │       │   └── songs.json\
├── .gitignore\
├── pom.xml\
└── README.md

## Contributing
Fork the repository

git fork https://github.com/your-username/spotify-java-app.git
Create a feature branch

git checkout -b feature-name
Commit your changes

git commit -m "Add new feature"
Push to the branch

git push origin feature-name
Create a Pull Request

Go to the repository on GitHub and create a pull request from your branch.

## License
This project is licensed under the MIT License - see the LICENSE file for details.

## Contact
Author: ROQUES Louis / DORIGNY Quentin \
Email: louis.roques@ynov.com / quentin.dorigny@ynov.com 
Gitea : rolouis / doquentin 