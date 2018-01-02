package Database;

import main.CommonData;
import main.Profiler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.nio.file.FileSystems;
import java.sql.*;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;



public class Local {
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
            String sql = "INSERT INTO LIB_LOCAL ("+CommonData.locLib_columns+") VALUES (null,?,?,?,?,?,?,?,?)";
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
            String sql = "SELECT * FROM LIB_LOCAL";
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
            String sql = "SELECT * from LIB_LOCAL WHERE ID NOT BETWEEN 0 and "+index;
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
        String sql = "INSERT INTO LIB_LOCAL ("+CommonData.locLib_columns+") VALUES (null,?,?,?,?,?,?,?,?)";
        System.out.println("START: "+LocalTime.now());
        try {
            Class.forName("org.h2.Driver");
            con = DriverManager.getConnection("jdbc:h2:"+CommonData.lib_location+CommonData.locLib_name);
            con.setAutoCommit(false);
            PreparedStatement prepStmt = con.prepareStatement
                    ("INSERT INTO LIB_LOCAL ("+CommonData.locLib_columns+") VALUES (null,?,?,?,?,?,?,?,?)");
            //for(int i=0; i<path.size();i++){
                prepStmt.setString(1, name.get(1));
                prepStmt.setString(2, artist.get(1));
                prepStmt.setString(3, album.get(1));
                prepStmt.setString(4, year.get(1));
                prepStmt.setString(5, number.get(1));
                prepStmt.setString(6, genres.get(1));
                prepStmt.setString(7, path.get(1));
                prepStmt.setString(8, filename.get(1));

            prepStmt.setString(1, name.get(2));
            prepStmt.setString(2, artist.get(2));
            prepStmt.setString(3, album.get(2));
            prepStmt.setString(4, year.get(2));
            prepStmt.setString(5, number.get(2));
            prepStmt.setString(6, genres.get(2));
            prepStmt.setString(7, path.get(2));
            prepStmt.setString(8, filename.get(2));

            prepStmt.setString(3, name.get(3));
            prepStmt.setString(2, artist.get(3));
            prepStmt.setString(3, album.get(3));
            prepStmt.setString(4, year.get(3));
            prepStmt.setString(5, number.get(3));
            prepStmt.setString(6, genres.get(3));
            prepStmt.setString(7, path.get(3));
            prepStmt.setString(8, filename.get(3));

            prepStmt.setString(4, name.get(4));
            prepStmt.setString(2, artist.get(4));
            prepStmt.setString(3, album.get(4));
            prepStmt.setString(4, year.get(4));
            prepStmt.setString(5, number.get(4));
            prepStmt.setString(6, genres.get(4));
            prepStmt.setString(7, path.get(4));
            prepStmt.setString(8, filename.get(4));
                prepStmt.addBatch();
               /* if (i % 100 == 0) {
                    System.out.println(i%100);*/
                    updateCounts = prepStmt.executeBatch(); // Execute every 1000 items.
                    //System.out.println(prepStmt);
                    System.out.println(path.size());
                    System.out.println(updateCounts.length);
                //}
            //}
            con.commit();
            prepStmt.close();
/*            con.setAutoCommit(true);*/
            System.out.println("FINISH: "+LocalTime.now());

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

    public static void insertData2(List<String> name, List<String> artist, List<String> album, List<String> year, List<String> number, List<String> genres, List<String> path, List<String> filename){
        int [] updateCounts;
        try {
            Class.forName("org.h2.Driver");
            con = DriverManager.getConnection("jdbc:h2:"+CommonData.lib_location+CommonData.locLib_name);
            con.setAutoCommit(false);
            PreparedStatement prepStmt = con.prepareStatement("INSERT INTO LIB_LOCAL VALUES (null,?,?,?,?,?,?,?,?)");
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
