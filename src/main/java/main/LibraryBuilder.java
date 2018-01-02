package main;

import javafx.beans.property.SimpleStringProperty;

public class LibraryBuilder {

    private SimpleStringProperty name=null;
    private SimpleStringProperty artist=null;
    private SimpleStringProperty album=null;
    private SimpleStringProperty path=null;

    private void LibraryBuilder(String sName, String sAartist, String sAlbum, String sPath) {
        this.name = new SimpleStringProperty(sName);
        this.artist = new SimpleStringProperty(sAartist);
        this.album = new SimpleStringProperty(sAlbum);
        this.path = new SimpleStringProperty(sPath);
    }

    public String getName() {
        return name.get();
    }

    public String getArtist() {
        return artist.get();
    }

    public String getAlbum() {
        return album.get();
    }

    public String getPath() {
        return path.get();
    }
}