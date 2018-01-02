package main;

import javafx.beans.property.SimpleStringProperty;

public class Library {
    private final SimpleStringProperty id = new SimpleStringProperty("");
    private final SimpleStringProperty name = new SimpleStringProperty("");
    private final SimpleStringProperty artist = new SimpleStringProperty("");
    private final SimpleStringProperty album = new SimpleStringProperty("");
    private final SimpleStringProperty year = new SimpleStringProperty("");
    private final SimpleStringProperty number = new SimpleStringProperty("");
    private final SimpleStringProperty genres = new SimpleStringProperty("");
    private final SimpleStringProperty path = new SimpleStringProperty("");
    private final SimpleStringProperty filename = new SimpleStringProperty("");

    public Library() {
        this("", "", "", "", "", "", "", "", "");
    }

    public Library(String sId, String sName, String sArtist, String sAlbum, String sYear, String sNumber, String sGenres, String sPath, String sFilename) {
        setId(sId);
        setName(sName);
        setArtist(sArtist);
        setAlbum(sAlbum);
        setYear(sYear);
        setGenres(sGenres);
        setNumber(sNumber);
        setFilename(sFilename);
        setPath(sPath);
    }

    public String getId() {
        return id.get();
    }

    public void setId(String sId) {
        id.set(sId);
    }

    public String getName() {
        return name.get();
    }

    public void setName(String fName) {
        name.set(fName);
    }

    public String getArtist() {
        return artist.get();
    }

    public void setArtist(String fName) {
        artist.set(fName);
    }

    public String getAlbum() {
        return album.get();
    }

    public void setAlbum(String fName) {
        album.set(fName);
    }

    public String getPath() {
        return path.get();
    }

    public void setPath(String fName) {
        path.set(fName);
    }

    public String getYear() {
        return year.get();
    }

    public void setYear(String sYear) {
        year.set(sYear);
    }

    public String getNumber() {
        return number.get();
    }

    public void setNumber(String sNumber) {
        number.set(sNumber);
    }

    public String getGenres() {
        return genres.get();
    }

    public void setGenres(String sGenres) {
        genres.set(sGenres);
    }

    public String getFilename() {
        return filename.get();
    }

    public void setFilename(String sFilename) {
        filename.set(sFilename);
    }
}
