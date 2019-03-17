package main;

import com.github.felixgail.gplaymusic.api.GPlayMusic;
import com.github.felixgail.gplaymusic.model.Track;
import com.github.felixgail.gplaymusic.model.enums.ResultType;
import com.github.felixgail.gplaymusic.model.enums.StreamQuality;
import com.github.felixgail.gplaymusic.model.requests.PagingRequest;
import com.github.felixgail.gplaymusic.model.requests.SearchTypes;
import com.github.felixgail.gplaymusic.model.responses.ListResult;
import com.github.felixgail.gplaymusic.util.TokenProvider;
import svarzee.gps.gpsoauth.AuthToken;
import svarzee.gps.gpsoauth.Gpsoauth;

import java.io.*;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

import static Database.GPM.*;


public class GPM {
    // authAndroidID="865904035436911"
    static String token, name, artist, album, year, number, genres, f_path, filename;
    static long expire;
    static File folder = new File("D:\\mp3\\fil.mp3");
    static Path path = FileSystems.getDefault().getPath("D:\\mp1\\fil.mp3");
    static Path path2 = FileSystems.getDefault().getPath(folder.getAbsolutePath());
    static GPlayMusic api;


    public static void main (String[] args){
        userAuth(new GetPropetries().getUsername(), new GetPropetries().getPassword(), new GetPropetries().getAndroidID());
        //getUserTracks();
//        getFullLibrary();
        getInfo();
    }

    public static void userAuth(String authUsername, String authPassword, String UDID){
        AuthToken auth = null;
        try {
            auth = TokenProvider.provideToken(authUsername, authPassword, UDID);
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
        String rootFolder = new GetPropetries().getGMPfolder();
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
            insertData(CommonData.song_name, CommonData.song_artist, CommonData.song_album, CommonData.song_year, CommonData.song_number, CommonData.song_genres, CommonData.song_path, CommonData.song_filename);
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

    private static void getFullLibrary_(){
        int size=0, l=0, count=1000;
        String npt;
        List<Track> tracks=null, tracks1;
        ListResult<Track> trackBody, tb1, tb2;
/*Prints OUT stream into file*/
        PrintStream out = null, out1 = null, stdout=System.out;
        try {
            out = new PrintStream(new FileOutputStream("output.txt"));
            out1 = new PrintStream(new FileOutputStream("songs.txt"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        /**/
        try {

            System.setOut(out);
            trackBody = api.getService().listTracks().execute().body();
            tracks = trackBody.toList();

            System.out.println("////////////SIZE: "+tracks.size());
            System.out.println("////////////TITLE0: "+tracks.get(0).getTitle());
            System.out.println("////////////TITLE last: "+tracks.get(tracks.size()-1).getTitle());
            for (int i=0;i<8;i++){
                System.out.println("\n\t---"+i);
                npt = trackBody.getNextPageToken();
                trackBody = api.getService().listTracks(new PagingRequest(npt, count)).execute().body();
                tracks = trackBody.toList();
                size = tracks.size();
                System.out.println("SIZE: "+tracks.size());
                System.out.println("TITLE0: "+tracks.get(0).getTitle());
                System.out.println("TITLE last: "+tracks.get(tracks.size()-1).getTitle());
                System.out.println("getNextPageToken: "+npt+"\n");
                System.setOut(out1);
                for (int k=0;k<size;k++){
                    l++;
                    System.out.println("\n #"+l+" ("+i+")");
                    System.out.println(tracks.get(k).getID());
                    System.out.println("ARTIST :: "+tracks.get(k).getArtist()+" - "+tracks.get(k).getTitle());
                }
                System.setOut(out);
                if(tracks.size()<count){ //прерывает сбор треков, если очередной запрос получил меньше треков, чем требовалось. Относительно надежный показатель того, что это были последние треки пользователя.
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.setOut(stdout);
        System.out.println("DONE");
//        System.exit(1);

    }

    public static void getFullLibrary(){
        int size=0, l=0, count=1000;
        String npt;
        List<Track> tracks=null, tracks1;
        ListResult<Track> trackBody, tb1, tb2;
        /*Prints OUT stream into file*/
        PrintStream out = null, out1 = null, stdout=System.out;
        try {
            out = new PrintStream(new FileOutputStream("output.txt"));
            out1 = new PrintStream(new FileOutputStream("songs.txt"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        /**/
        try {

            System.setOut(out);
            trackBody = api.getService().listTracks().execute().body();
            tracks = trackBody.toList();

            System.out.println("////////////SIZE: "+tracks.size());
            System.out.println("////////////TITLE0: "+tracks.get(0).getTitle());
            System.out.println("////////////TITLE last: "+tracks.get(tracks.size()-1).getTitle());
            for (int i=0;i<8;i++){
                System.out.println("\n\t---"+i);
                npt = trackBody.getNextPageToken();
                trackBody = api.getService().listTracks(new PagingRequest(npt, count)).execute().body();
                tracks = trackBody.toList();
                size = tracks.size();
                System.out.println("SIZE: "+tracks.size());
                System.out.println("TITLE0: "+tracks.get(0).getTitle());
                System.out.println("TITLE last: "+tracks.get(tracks.size()-1).getTitle());
                System.out.println("getNextPageToken: "+npt+"\n");
                System.setOut(out1);
                for (int k=0;k<size;k++){
                    l++;
                    System.out.println("\n #"+l+" ("+i+")");
                    System.out.println(tracks.get(k).getID());
                    System.out.println("ARTIST :: "+tracks.get(k).getArtist()+" - "+tracks.get(k).getTitle());
                }
                System.setOut(out);
                if(tracks.size()<count){ //прерывает сбор треков, если очередной запрос получил меньше треков, чем требовалось. Относительно надежный показатель того, что это были последние треки пользователя.
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.setOut(stdout);
        System.out.println("DONE");
//        System.exit(1);

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

    private static void getInfo(){
        int size=0, l=0, count=1000;
        String npt;
        List<Track> tracks=null, tracks1;
        ListResult<Track> trackBody, tb1, tb2;
        /*Prints OUT stream into file*/
        PrintStream out = null, out1 = null, stdout=System.out;
        try {
            out = new PrintStream(new FileOutputStream("output.txt"));
            out1 = new PrintStream(new FileOutputStream("songs.txt"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        /**/
        try {

            System.setOut(out);
            trackBody = api.getService().listTracks().execute().body();
            tracks = trackBody.toList();
            for (int i=0; i<1000; i++) {
                if (tracks.get(i).getRating().isPresent() && tracks.get(i).getRating().get()=="1") {
                    System.out.println("TITLE0: " + tracks.get(i).getTitle());
                    System.out.println("getID: " + tracks.get(i).getID());
                    System.out.println("getArtist: " + tracks.get(i).getArtist());
                    System.out.println("getAlbum: " + tracks.get(i).getAlbum());
                    System.out.println("getAlbumArtist: " + tracks.get(i).getAlbumArtist());
                    System.out.println("getAlbumId: " + tracks.get(i).getAlbumId());
                    System.out.println("getComposer: " + tracks.get(i).getComposer());
                    System.out.println("getRating: " + tracks.get(i).getRating().get());
                    System.out.println("getGenre: " + tracks.get(i).getGenre().get());

                    System.out.println("getTrackNumber: " + tracks.get(i).getTrackNumber());
                    System.out.println("getYear: " + tracks.get(i).getYear());
                    System.out.println("getAlbumArtRef: " + tracks.get(i).getAlbumArtRef().toString());
                    System.out.println("getArtistId: " + tracks.get(i).getArtistId());
                    System.out.println("getBeatsPerMinute: " + tracks.get(i).getBeatsPerMinute());
                    System.out.println("getClientId: " + tracks.get(i).getClientId());
                    System.out.println("getComment: " + tracks.get(i).getComment());
                    System.out.println("getContentType: " + tracks.get(i).getContentType());
                    System.out.println("getCreationTimestamp: " + tracks.get(i).getCreationTimestamp());

                    System.out.println("getDiscNumber: " + tracks.get(i).getDiscNumber());
                    System.out.println("getDurationMillis: " + tracks.get(i).getDurationMillis());
                    System.out.println("getEstimatedSize: " + tracks.get(i).getEstimatedSize());
                    System.out.println("getExplicitType: " + tracks.get(i).getExplicitType());
                    System.out.println("getLastModifiedTimestamp: " + tracks.get(i).getLastModifiedTimestamp());
                    System.out.println("getLastRatingChangeTimestamp: " + tracks.get(i).getLastRatingChangeTimestamp());
                    System.out.println("getPlayCount: " + tracks.get(i).getPlayCount());
                    System.out.println("getVideo: " + tracks.get(i).getVideo());
                    System.out.println("getUuid: " + tracks.get(i).getUuid());

                    System.out.println("getTrackType: " + tracks.get(i).getTrackType());
                    System.out.println("getTotalTrackCount: " + tracks.get(i).getTotalTrackCount());
                    System.out.println("getTotalDiscCount: " + tracks.get(i).getTotalDiscCount());
                    System.out.println("getStreamURL: " + tracks.get(i).getStreamURL(StreamQuality.HIGH));
                    System.out.println("getStoreId: " + tracks.get(i).getStoreId());
                    System.out.println("getSignature: " + tracks.get(i).getSignature());
                    System.out.println("getNid: " + tracks.get(i).getNid());
                    System.out.println("getCreationTimestamp: " + tracks.get(i).getCreationTimestamp());
                    System.out.println("search: " + tracks.get(i).getApi().getTrackApi().search("amiina", 10).iterator().next().getArtist());
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        System.setOut(stdout);
        System.out.println("DONE");
        System.exit(1);

    }

    private static void getPlaylist(){
        int size=0, l=0, count=1000;
        String npt;
        List<Track> tracks=null, tracks1;
        ListResult<Track> trackBody, tb1, tb2;
        /*Prints OUT stream into file*/
        PrintStream out = null, out1 = null, stdout=System.out;
        try {
            out = new PrintStream(new FileOutputStream("output.txt"));
            out1 = new PrintStream(new FileOutputStream("songs.txt"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        /**/
        try {

            System.setOut(out);
            trackBody = api.getService().listTracks().execute().body();
            tracks = trackBody.toList();
            for (int i=0; i<1000; i++) {
                if (tracks.get(i).getRating().isPresent() && tracks.get(i).getRating().get()=="1") {
                    System.out.println("TITLE0: " + tracks.get(i).getTitle());
                    System.out.println("getID: " + tracks.get(i).getID());
                    System.out.println("getArtist: " + tracks.get(i).getArtist());
                    System.out.println("getAlbum: " + tracks.get(i).getAlbum());
                    System.out.println("getAlbumArtist: " + tracks.get(i).getAlbumArtist());
                    System.out.println("getAlbumId: " + tracks.get(i).getAlbumId());
                    System.out.println("getComposer: " + tracks.get(i).getComposer());
                    System.out.println("getRating: " + tracks.get(i).getRating().get());
                    System.out.println("getGenre: " + tracks.get(i).getGenre().get());

                    System.out.println("getTrackNumber: " + tracks.get(i).getTrackNumber());
                    System.out.println("getYear: " + tracks.get(i).getYear());
                    System.out.println("getAlbumArtRef: " + tracks.get(i).getAlbumArtRef().toString());
                    System.out.println("getArtistId: " + tracks.get(i).getArtistId());
                    System.out.println("getBeatsPerMinute: " + tracks.get(i).getBeatsPerMinute());
                    System.out.println("getClientId: " + tracks.get(i).getClientId());
                    System.out.println("getComment: " + tracks.get(i).getComment());
                    System.out.println("getContentType: " + tracks.get(i).getContentType());
                    System.out.println("getCreationTimestamp: " + tracks.get(i).getCreationTimestamp());

                    System.out.println("getDiscNumber: " + tracks.get(i).getDiscNumber());
                    System.out.println("getDurationMillis: " + tracks.get(i).getDurationMillis());
                    System.out.println("getEstimatedSize: " + tracks.get(i).getEstimatedSize());
                    System.out.println("getExplicitType: " + tracks.get(i).getExplicitType());
                    System.out.println("getLastModifiedTimestamp: " + tracks.get(i).getLastModifiedTimestamp());
                    System.out.println("getLastRatingChangeTimestamp: " + tracks.get(i).getLastRatingChangeTimestamp());
                    System.out.println("getPlayCount: " + tracks.get(i).getPlayCount());
                    System.out.println("getVideo: " + tracks.get(i).getVideo());
                    System.out.println("getUuid: " + tracks.get(i).getUuid());

                    System.out.println("getTrackType: " + tracks.get(i).getTrackType());
                    System.out.println("getTotalTrackCount: " + tracks.get(i).getTotalTrackCount());
                    System.out.println("getTotalDiscCount: " + tracks.get(i).getTotalDiscCount());
                    System.out.println("getStreamURL: " + tracks.get(i).getStreamURL(StreamQuality.HIGH));
                    System.out.println("getStoreId: " + tracks.get(i).getStoreId());
                    System.out.println("getSignature: " + tracks.get(i).getSignature());
                    System.out.println("getNid: " + tracks.get(i).getNid());
                    System.out.println("getCreationTimestamp: " + tracks.get(i).getCreationTimestamp());
                    System.out.println("search: " + tracks.get(i).getApi().getTrackApi().search("amiina", 10).iterator().next().getArtist());
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        System.setOut(stdout);
        System.out.println("DONE");
        System.exit(1);

    }
}
