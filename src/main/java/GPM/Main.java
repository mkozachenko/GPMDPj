package GPM;

import Database.Local;
import com.github.felixgail.gplaymusic.api.GPlayMusic;
import com.github.felixgail.gplaymusic.api.TrackApi;
import com.github.felixgail.gplaymusic.model.Track;
import com.github.felixgail.gplaymusic.model.enums.ResultType;
import com.github.felixgail.gplaymusic.model.enums.StreamQuality;
import com.github.felixgail.gplaymusic.model.requests.PagingRequest;
import com.github.felixgail.gplaymusic.util.TokenProvider;
import com.github.felixgail.gplaymusic.model.requests.SearchTypes;
import main.CommonData;
import svarzee.gps.gpsoauth.AuthToken;
import svarzee.gps.gpsoauth.Gpsoauth;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.List;
import java.util.function.IntConsumer;

public class Main {
    static String user="symphonicon@gmail.com", pass="Ruo2GE159951", and_id=""; // and_id="865904035436911"
    static String token, name, artist, album, year, number, genres, f_path, filename, rootFolder = "D:\\mus_file\\";
    static long expire;
    static File folder = new File("D:\\mp3\\fil.mp3");
    static Path path = FileSystems.getDefault().getPath("D:\\mp1\\fil.mp3");
    static Path path2 = FileSystems.getDefault().getPath(folder.getAbsolutePath());
    static GPlayMusic api;


    public static void main (String[] args) throws IOException {
        userAuth();
        //getUserTracks();
        getFullLibrary();
    }

    private static void userAuth(){
        AuthToken auth = null;
        try {
            auth = TokenProvider.provideToken(user, pass, and_id);
            //System.out.println(auth);
            token = auth.getToken();
            expire = auth.getExpiry();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Gpsoauth.TokenRequestFailed tokenRequestFailed) {
            //tokenRequestFailed.printStackTrace();
            System.err.println(tokenRequestFailed.getCause());
            System.err.println(tokenRequestFailed.getMessage());
            System.err.println(tokenRequestFailed.getLocalizedMessage());
            tokenRequestFailed.printStackTrace();
        }
        api = new GPlayMusic.Builder().setAuthToken(auth).build(); //.setDebug(true).
    }

    private static void getUserTracks(){
        try{
            List<Track> tracks = api.getService().listTracks().execute().body().toList();
            for(int i=0;i<tracks.size();i++) {
                filename = tracks.get(i).getID();
                Path folder = FileSystems.getDefault().getPath(new File(rootFolder + filename).getAbsolutePath());
                //api.getTrackApi().getTrack(tracks.get(i).getID()).download(StreamQuality.LOW, folder);
                //if (new File(rootFolder + filename).exists()) {
                if(true){
                    /*name = tracks.get(i).getTitle();
                    artist = tracks.get(i).getArtist();
                    album = tracks.get(i).getAlbum();
                    year = String.valueOf(tracks.get(i).getYear().orElse(0000));
                    number = String.valueOf(tracks.get(i).getTrackNumber());
                    genres = tracks.get(i).getGenre().get();
                    f_path = folder.toString();*/
                    CommonData.song_name.add(tracks.get(i).getTitle());
                    CommonData.song_artist.add(tracks.get(i).getArtist());
                    CommonData.song_album.add(tracks.get(i).getAlbum());
                    CommonData.song_year.add(String.valueOf(tracks.get(i).getYear().orElse(0000)));
                    CommonData.song_number.add(String.valueOf(tracks.get(i).getTrackNumber()));
                    CommonData.song_genres.add(tracks.get(i).getGenre().orElse("unknown"));
                    CommonData.song_filename.add(filename);
                    CommonData.song_path.add(folder.toString());
                    System.out.println(i+"---"+tracks.get(i).getTitle());
                }
            }
            GPM.Local.insertData(CommonData.song_name, CommonData.song_artist, CommonData.song_album, CommonData.song_year, CommonData.song_number, CommonData.song_genres, CommonData.song_path, CommonData.song_filename);
            CommonData.song_name.clear();
            CommonData.song_artist.clear();
            CommonData.song_album.clear();
            CommonData.song_year.clear();
            CommonData.song_number.clear();
            CommonData.song_genres.clear();
            CommonData.song_path.clear();
            CommonData.song_filename.clear();
        }catch (IllegalArgumentException exc){
            System.err.println(exc);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void getPromotedTracks(){
        try{
            List<Track> tracks = api.getPromotedTracks();
            System.out.println(tracks.get(0).getID());
            System.out.println(tracks.get(0).getArtist());
            System.out.println(tracks.get(0).getTitle());
            api.getTrackApi().getTrack(tracks.get(0).getID()).download(StreamQuality.HIGH, path2);
            api.getTrackApi().getTrack(tracks.get(1).getID()).download(StreamQuality.MEDIUM, path);
            //api.getTrackApi().getTrack(tracks.get(2).getID()).download(StreamQuality.LOW, path2);
            System.out.println(tracks.size());
        }catch (IllegalArgumentException exc){
            System.err.println(exc);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void getFullLibrary(){
        try {
            //List<Track> tracks = api.getService().listTracks().execute().body().toList();
            List<Track> tracks, tracks1;
            //System.out.println(tracks.size()+"---"+tracks.get(0).getTitle()+"---"+tracks.get(tracks.size()-1).getTitle());
            /*tracks = api.getService().listTracks().execute().body().toList();
            System.out.println(tracks.size()+"---"+tracks.get(0).getTitle()+"---"+tracks.get(tracks.size()-1));*/
            String npt;
            for (int i=0;i<5;i++){
                npt = api.getService().listTracks().execute().body().getNextPageToken();
                //tracks = api.getService().listTracks(new PagingRequest(npt, 2000)).execute().body().toList();
                tracks = api.getService().listTracks().execute().body().toList();
                System.out.println(tracks.size()+"---"+tracks.get(0).getTitle()+"---"+tracks.get(tracks.size()-1).getTitle());
                System.out.println(i+"__"+npt);
                //System.out.println(tr);
            }



        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void vote(){

        try {
            List<Track> tracks = api.getService().listTracks().execute().body().toList();

        //System.out.println(tracks.size()+"---"+tracks.get(0).getTitle()+"---"+tracks.get(tracks.size()-1).getTitle());
            /*tracks = api.getService().listTracks().execute().body().toList();
            System.out.println(tracks.size()+"---"+tracks.get(0).getTitle()+"---"+tracks.get(tracks.size()-1));*/
        tracks = api.getService().listTracks(new PagingRequest(api.getService().listTracks().execute().body().getNextPageToken(), 2000)).execute().body().toList();
        System.out.println(tracks.size()+"---"+tracks.get(0).getTitle()+"---"+tracks.get(tracks.size()-1).getTitle());
        System.out.println("1__"+api.getTrackApi().getTrack(tracks.get(0).getID()).getRating());
        System.out.println("1__"+api.getTrackApi().getTrack(tracks.get(109).getID()).getRating());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void search(){
                /*try{
            List<Track> tracks = api.search("smoke water", new SearchTypes().getTypes().);
            System.out.println(tracks.get(0).getID());
            System.out.println(tracks.get(0).getArtist());
            System.out.println(tracks.get(0).getTitle());
            api.getTrackApi().getTrack(tracks.get(0).getID()).download(StreamQuality.HIGH, FileSystems.getDefault().getPath(new File("D:\\mp3\\fi1l.mp3").getAbsolutePath()));
            System.out.println(tracks.size());
        }catch (IllegalArgumentException exc){
            System.err.println(exc);
        }*/

        List<ResultType> results = new SearchTypes().getTypes();
        System.out.println(results.size());
        System.out.println(results.get(0));
        System.out.println(results.get(results.size()));
    }
}
