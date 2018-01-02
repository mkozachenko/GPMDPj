package main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommonData {
    public static final String lib_location = "./src/main/resources/data/",
            locLib_name = "local_library",
            gpmLib_name = "",
            backup = "data.bkp";
    public static String[] data;
    public static List<String> song_name = new ArrayList<String>();
    public static List<String> song_artist = new ArrayList<String>();
    public static List<String> song_album = new ArrayList<String>();
    public static List<String> song_year = new ArrayList<String>();
    public static List<String> song_number = new ArrayList<String>();
    public static List<String> song_genres = new ArrayList<String>();
    public static List<String> song_path = new ArrayList<String>();
    public static List<String> song_filename = new ArrayList<String>();
    /*public static String[] locLib_columns = {"ID", "NAME", "ARTIST", "ALBUM", "YEAR", "NUMBER", "GENRES", "PATH"};*/
    public static String locLib_columns = "ID, NAME, ARTIST, ALBUM, YEAR, NUMBER, GENRES, PATH, FILENAME";
    public static final String supportedFormats = ".*\\.mp3||.*\\.wav";
    public static int[] lib_id;
    public static String[] lib_name, lib_artist, lib_album, lib_year, lib_number, lib_genres, lib_path, lib_filename;
    //public static ArrayList<Integer> lib_id;
    //public static ArrayList<String> lib_name, lib_artist, lib_album, lib_year, lib_number, lib_genres, lib_path, lib_filename;
    /*public static Map<String, String> locLib_map;
    static {
        locLib_map.put("ID", null);
        locLib_map.put("NAME", null);
        locLib_map.put("ARTIST", null);
        locLib_map.put("ALBUM", null);
        locLib_map.put("YEAR", null);
        locLib_map.put("NUMBER", null);
        locLib_map.put("GENRES", null);
        locLib_map.put("PATH", null);
    }*/
}
