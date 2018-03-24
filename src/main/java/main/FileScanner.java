package main;

import Database.Local;
import com.mpatric.mp3agic.ID3v2;
import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.Mp3File;
import com.mpatric.mp3agic.UnsupportedTagException;
import org.apache.commons.io.FileUtils;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.parser.mp3.Mp3Parser;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.io.*;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class FileScanner {
    private static ContentHandler handler = new DefaultHandler();
    private static Metadata metadata = new Metadata();
    private static Parser parser = new Mp3Parser();
    private static ParseContext parseCtx = new ParseContext();
    private static final int PAD_NAME_TO = 17;

    public static void scanForFiles(String path){
        File root = new File(path);
        try {
            boolean recursive = true;
            Collection files = FileUtils.listFiles(root, null, recursive);
            for (Iterator iterator = files.iterator(); iterator.hasNext();) {
                File file = (File) iterator.next();
                extract(file.getAbsolutePath());
                }
            Local.insertData2(CommonData.song_name, CommonData.song_artist, CommonData.song_album, CommonData.song_year, CommonData.song_number, CommonData.song_genres, CommonData.song_path, CommonData.song_filename);
            CommonData.song_name.clear();
            CommonData.song_artist.clear();
            CommonData.song_album.clear();
            CommonData.song_year.clear();
            CommonData.song_number.clear();
            CommonData.song_genres.clear();
            CommonData.song_path.clear();
            CommonData.song_filename.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Extract with apache-tika
    @Deprecated
    public static void extract_(String filePath){
        String fileName = filePath.substring(filePath.lastIndexOf("\\")+1);
        String[] metadataNames = metadata.names();
        try {
            InputStream input = new FileInputStream(new File(filePath));
            /*for (String name : metadataNames) {
                    System.out.println(name + ": " + metadata.get(name));
                }
            System.out.println("//////////////////////////////////////////////");*/
            if (fileName.matches(CommonData.supportedFormats)) {
                new Profiler().startCount();
                parser.parse(input, handler, metadata, parseCtx);
                input.close();
                CommonData.song_name.add(metadata.get("title"));
                CommonData.song_artist.add(metadata.get("xmpDM:artist"));
                CommonData.song_album.add(metadata.get("xmpDM:album"));
                CommonData.song_year.add(metadata.get("xmpDM:releaseDate"));
                CommonData.song_number.add(metadata.get("xmpDM:trackNumber"));
                CommonData.song_genres.add(metadata.get("xmpDM:genre"));
                CommonData.song_filename.add(fileName);
                CommonData.song_path.add(filePath);
                new Profiler().stopCount("Extract time file " +filePath+ " is ");
            }
        } catch(IOException | SAXException | TikaException e){
            e.printStackTrace();
        }
    }

    //Extract with mp3agic
    public static void extract(String filePath){
        String fileName = filePath.substring(filePath.lastIndexOf("\\")+1);
        Mp3File mp3file;
        try {
            if (fileName.matches(CommonData.supportedFormats)) {
                new Profiler().startCount();
                mp3file = new Mp3File(filePath);
                ID3v2 id3v2tag = mp3file.getId3v2Tag();
                if (id3v2tag != null) {
                    CommonData.song_name.add(id3v2tag.getTitle());
                    CommonData.song_artist.add(id3v2tag.getArtist());
                    CommonData.song_album.add(id3v2tag.getAlbum());
                    CommonData.song_year.add(id3v2tag.getYear());
                    CommonData.song_number.add(id3v2tag.getTrack());
                    CommonData.song_genres.add(id3v2tag.getGenreDescription());
                    CommonData.song_filename.add(fileName);
                    CommonData.song_path.add(filePath);
                }
                else{
                    String name = fileName.substring(0, fileName.lastIndexOf("."));
                    CommonData.song_name.add(name);
                    CommonData.song_artist.add("");
                    CommonData.song_album.add("");
                    CommonData.song_year.add("");
                    CommonData.song_number.add("");
                    CommonData.song_genres.add("");
                    CommonData.song_filename.add(fileName);
                    CommonData.song_path.add(filePath);
                }
                new Profiler().stopCount("Extract time file " +filePath+ " is ");
            }
        } catch (IOException | UnsupportedTagException | InvalidDataException | IllegalArgumentException e) {
            e.printStackTrace();
            System.out.println(filePath);
        }catch (NullPointerException npe){
            npe.printStackTrace();
            System.out.println(filePath);
        }
    }
}
