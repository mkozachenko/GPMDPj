package Database;

import main.CommonData;

import java.nio.file.FileSystems;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GPM {
    /*
     * INSERT INTO LIB_LOCAL(ID, NAME, ARTIST, ALBUM, YEAR, NUMBER, GENRES)
     */
    private static final String ABSOLUTE_LOCATION = FileSystems.getDefault().getPath(CommonData.lib_location).normalize().toAbsolutePath().toString();
    private static Connection con = null;
    private static List song_id = new ArrayList();
    private static List song_name = new ArrayList();
    private static List song_artist = new ArrayList();
    private static List song_album = new ArrayList();
    private static List song_year = new ArrayList();
    private static List song_number = new ArrayList();
    private static List song_genres = new ArrayList();
    private static List song_path = new ArrayList();
    private static List song_filename = new ArrayList();
    private static long startTime, endTime;
    public static void saveBackupData(){
        try {
            Class.forName("org.h2.Driver").newInstance();
            con = DriverManager.getConnection("jdbc:h2:"+CommonData.lib_location+CommonData.locLib_name);
            System.out.println("Connected to H2: "+CommonData.locLib_name);
            Statement st = con.createStatement();
            st.executeQuery("SCRIPT TO '"+ABSOLUTE_LOCATION+"/"+CommonData.backup+"'");
            System.out.println("Backup created:"+ABSOLUTE_LOCATION+"/"+CommonData.backup);
            /*while (rs2.next()) {
                String result1 = rs2.getString("number");
                System.out.println(result1);
            }*/
        } catch(Exception e) {
            System.out.println("Unable to connect: "+CommonData.locLib_name);
            e.printStackTrace();
        }
    }

    public static void insertDataLocal(String[] values) {
        try {
            Class.forName("org.h2.Driver");
            con = DriverManager.getConnection("jdbc:h2:"+CommonData.lib_location+CommonData.locLib_name);
            System.out.println("Connected to H2: "+CommonData.locLib_name);
            String sql = "INSERT INTO LIB_GMP ("+CommonData.locLib_columns+") VALUES (null,?,?,?,?,?,?,?,?)";
            PreparedStatement prepStmt = con.prepareStatement(sql);
            for(int i=1; i<values.length+1;i++){
                prepStmt.setString(i, values[i-1]);
            }
            prepStmt.execute();
        }catch(SQLException se){
            se.printStackTrace();
            se.getNextException();
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            try{
                if(con!=null)
                    con.close();
            }catch(SQLException se){
                se.printStackTrace();
            }
        }
    }

    public static void getAllPaths(){
        try {
            Class.forName("org.h2.Driver");
            con = DriverManager.getConnection("jdbc:h2:"+CommonData.lib_location+CommonData.locLib_name);
            System.out.println("Connected to H2: "+CommonData.locLib_name);
            String sql = "SELECT * FROM LIB_GMP";
            PreparedStatement prepStmt = con.prepareStatement(sql);
            ResultSet results = prepStmt.executeQuery();
            while (results.next())
            {
                song_id.add(results.getString("ID"));
                song_name.add(results.getString("NAME"));
                song_artist.add(results.getString("ARTIST"));
                song_album.add(results.getString("ALBUM"));
                song_year.add(results.getString("YEAR"));
                song_number.add(results.getString("NUMBER"));
                song_genres.add(results.getString("GENRES"));
                song_path.add(results.getString("PATH"));
                song_filename.add(results.getString("FILENAME"));
            }
        }catch(SQLException se){
            se.printStackTrace();
            se.getNextException();
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            /*System.out.println(song_id.size());
            System.out.println(song_name.get(1));*/
            try{
                if(con!=null)
                    con.close();
            }catch(SQLException se){
                se.printStackTrace();
            }
        }
    }

    public static void getAllAfterIndex(String index){
        try {
            Class.forName("org.h2.Driver");
            con = DriverManager.getConnection("jdbc:h2:"+CommonData.lib_location+CommonData.locLib_name);
            System.out.println("Connected to H2: "+CommonData.locLib_name);
            String sql = "SELECT * from LIB_GMP WHERE ID NOT BETWEEN 0 and "+index;
            PreparedStatement prepStmt = con.prepareStatement(sql);
            ResultSet results = prepStmt.executeQuery();
            while (results.next())
            {
                song_id.add(results.getString("ID"));
                song_name.add(results.getString("NAME"));
                song_artist.add(results.getString("ARTIST"));
                song_album.add(results.getString("ALBUM"));
                song_year.add(results.getString("YEAR"));
                song_number.add(results.getString("NUMBER"));
                song_genres.add(results.getString("GENRES"));
                song_path.add(results.getString("PATH"));
                song_filename.add(results.getString("FILENAME"));
            }
        }catch(SQLException se){
            se.printStackTrace();
            se.getNextException();
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            /*System.out.println(song_id.size());
            System.out.println(song_name.get(1));*/
            try{
                if(con!=null)
                    con.close();
            }catch(SQLException se){
                se.printStackTrace();
            }
        }
    }

    public static void insertData(List<String> name, List<String> artist, List<String> album, List<String> year, List<String> number, List<String> genres, List<String> path, List<String> filename){
        int [] updateCounts;
        try {
            Class.forName("org.h2.Driver");
            con = DriverManager.getConnection("jdbc:h2:"+CommonData.lib_location+CommonData.locLib_name);
            con.setAutoCommit(false);
            PreparedStatement prepStmt = con.prepareStatement("INSERT INTO LIB_GMP VALUES (null,?,?,?,?,?,?,?,?)");
            for(int i=0; i<path.size();i++) {
                prepStmt.setString(1, name.get(i));
                prepStmt.setString(2, artist.get(i));
                prepStmt.setString(3, album.get(i));
                prepStmt.setString(4, year.get(i));
                prepStmt.setString(5, number.get(i));
                prepStmt.setString(6, genres.get(i));
                prepStmt.setString(7, path.get(i));
                prepStmt.setString(8, filename.get(i));
                prepStmt.addBatch();

            }
            System.out.println(path.size());
            startTime = System.currentTimeMillis();
            updateCounts = prepStmt.executeBatch(); // Execute every 1000 items.
            endTime = System.currentTimeMillis();
            System.out.println("Выполнение запроса: " + (endTime-startTime) + " ms");
            System.out.println(updateCounts.length);
            con.commit();
            prepStmt.close();
        }catch(SQLException se){
            se.printStackTrace();
            se.getNextException();
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            try{
                if(con!=null)
                    con.close();
            }catch(SQLException se){
                se.printStackTrace();
            }
        }
    }

    public static void insertData2(String name, String artist, String album, String year, String number, String genres, String path, String filename){
        int [] updateCounts;
        try {
            Class.forName("org.h2.Driver");
            con = DriverManager.getConnection("jdbc:h2:"+CommonData.lib_location+CommonData.locLib_name);
            con.setAutoCommit(false);
            PreparedStatement prepStmt = con.prepareStatement("INSERT INTO LIB_GMP VALUES (null,?,?,?,?,?,?,?,?)");
            prepStmt.setString(1, name);
            prepStmt.setString(2, artist);
            prepStmt.setString(3, album);
            prepStmt.setString(4, year);
            prepStmt.setString(5, number);
            prepStmt.setString(6, genres);
            prepStmt.setString(7, path);
            prepStmt.setString(8, filename);
            prepStmt.addBatch();

            updateCounts = prepStmt.executeBatch(); // Execute every 1000 items.
            System.out.println(updateCounts.length);
            con.commit();
            prepStmt.close();
        }catch(SQLException se){
            se.printStackTrace();
            se.getNextException();
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            try{
                if(con!=null)
                    con.close();
            }catch(SQLException se){
                se.printStackTrace();
            }
        }
    }

    public static List getFilename() {
        return song_filename;
    }

    public static List getId() {
        return song_id;
    }

    public static List getName() {
        return song_name;
    }

    public static List getArtist() {
        return song_artist;
    }

    public static List getAlbum() {
        return song_album;
    }

    public static List getYear() {
        return song_year;
    }

    public static List getNumber() {
        return song_number;
    }

    public static List getGenres() {
        return song_genres;
    }

    public static List getSong_path() {
        return song_path;
    }
}
