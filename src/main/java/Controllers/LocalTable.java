package Controllers;

import Database.Local;
import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.value.ChangeListener;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import main.FileScanner;
import main.GetPropetries;
import main.Library;

import java.io.File;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;

public class LocalTable implements Initializable{
    @FXML
    private Button play_btn, pause_btn, fileChooser_btn, directoryChooser_btn;
    @FXML
    private Slider progressBar, volumeSlider;
    @FXML
    private TextArea filesArea;
    @FXML
    protected TableView<Library> libraryTable;
    @FXML
    private Label songLabel, timeLabel, countLabel, volumeLabel, progressLabel;

    private static Media media;
    protected static MediaPlayer mp;
    private final FileChooser fileChooser = new FileChooser();
    private final DirectoryChooser directoryChooser = new DirectoryChooser();
    private ChangeListener<Duration> progressChangeListener;
    private static String lastIndex, file, songString, countString, timeString;
    private double full, current, progress, volume=10;
    private static int currentlyPlayingIndex, bound, runn, observ, ltr;
    private static ObservableList<Library> localData;
    private static Duration duration;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Random rnd = new Random();
        localData = libraryTable.getItems();
        Local.getAllPaths();
        for(int i=0;i<Local.getId().size();i++){
            try{
                localData.add(new Library(Local.getId().get(i).toString(), Local.getName().get(i).toString(), Local.getArtist().get(i).toString(),
                        Local.getAlbum().get(i).toString(), Local.getYear().get(i).toString(), Local.getNumber().get(i).toString(),
                        Local.getGenres().get(i).toString(), Local.getSong_path().get(i).toString(), Local.getFilename().get(i).toString()));
                //lastIndex = localData.get(localData.size()-1).getId();

            }catch (NullPointerException e){
                System.err.println(e);
            }
        }
        bound = rnd.nextInt(localData.size()-1);
        lastIndex = localData.get(localData.size()-1).getId();
        System.out.println(bound+"--"+lastIndex);
        currentlyPlayingIndex = getFileToPlay(bound, true);
        //init volume
        mp.setVolume(volume/100d);
        volumeSlider.setValue(volume);
        volumeLabel.setText(Math.round(volume)+"%");
    }

    /*********************/
    /** MEDIA CONTROLS **/
    /*********************/

    @FXML
    private void PlayButton_clicked(){
        mp.play();
    }

    @FXML
    private void PauseButton_сlicked(){
        mp.pause();
    }

    @FXML
    public void prevSong_btn_clicked() {
        int index = currentlyPlayingIndex-1;
        mp.stop();
        currentlyPlayingIndex = getFileToPlay(index, true);
    }

    @FXML
    public void nextSong_btn_clicked() {
        int index = currentlyPlayingIndex+1;
        mp.stop();
        currentlyPlayingIndex = getFileToPlay(index, true);
    }

    @FXML
    public void volumeChange(){
        volume = volumeSlider.getValue();
        volumeLabel.setText(Math.round(volume)+"%");
        mp.setVolume(volume/100d);
    }

    @FXML
    public void progressSeekDrag(){
        double total;
        int cur_min, cur_sec, min, sec;
        Duration cur, tot;
        //total = mp.getStopTime().toMillis();
        //progressBar.setMax(total);
        cur = new Duration(progressBar.getValue());
        mp.pause();
        mp.setStartTime(duration.multiply(progressBar.getValue() / 100.0));
        mp.play();
//        mp.seek(cur);
//        min = (int)Math.floor((mp.getStopTime().toSeconds() / 60) % 60);
//        sec = (int)Math.floor(mp.getStopTime().toSeconds() % 60);
//        cur_min = (int)Math.floor((progressBar.getValue()/1000/60) % 60);
//        cur_sec = (int)Math.floor(progressBar.getValue()/1000 % 60);
//        System.out.println(min+":"+sec);
//        System.out.println(cur_min+":"+cur_sec+"/"+min+":"+sec);
//        timeLabel.setText(cur_min+":"+cur_sec+"/"+min+":"+sec);
        /** Seek method is very slow so I had to get around it **/
        //mp.seek(cur);
    }

    @FXML
    public void progressSeekClick(){
        Duration cur, tot;
        //cur = new Duration(progressBar.getValue());
//        mp.pause();
//        mp.setStartTime(duration.multiply(progressBar.getValue() / 100.0));
//        mp.play();
        mp.seek(duration.multiply(progressBar.getValue() / 100.0));
    }

    /*************************/
    /** LIBRARY TABLE ITEMS **/
    /*************************/
    @FXML
    public void tableClicked() {
        libraryTable.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if(mouseEvent.getButton().equals(MouseButton.PRIMARY)){
                    if(mouseEvent.getClickCount() == 2){
                        mp.stop();
                        getFileToPlay(libraryTable.getSelectionModel().getSelectedIndex(), true);
                    }
                }
            }
        });
    }

    @FXML
    public void itemMenuClick() {
        Library data = libraryTable.getSelectionModel().getSelectedItem();
        System.out.println("MENU -> "+data.getFilename());
    }
    ////////////////////////////////////////////////
    @FXML
    public void libraryUpdate() {

    }
    /*******************/
    /** FILE CONTROLS **/
    /*******************/

    @FXML
    private void FileChooserClick(){
        new GetPropetries().getLastFile();
        // Set title for FileChooser
        fileChooser.setTitle("Select Some Files");
        // Set Initial Directory
        try {
            fileChooser.setInitialDirectory(new File(System.getProperty(new GetPropetries().getLastFile())));
        }catch(Exception e){
            System.out.println(e);
        }
        fileChooser.getExtensionFilters().addAll(//
                new FileChooser.ExtensionFilter("All Files", "*.*"),
                new FileChooser.ExtensionFilter("mp3", "*.mp3"),
                new FileChooser.ExtensionFilter("wav", "*.wav"));
        Stage stage = (Stage) fileChooser_btn.getScene().getWindow();
        File file = fileChooser.showOpenDialog(stage);
        if (file != null) {
            new GetPropetries().setLastDirectory(file.getAbsolutePath());
            List<File> files = Arrays.asList(file);
        }
    }

    @FXML
    private void DirectoryChooserClick(){
        // Set title for FileChooser
        directoryChooser.setTitle("Select Directory");
        // Set Initial Directory
        try {
            directoryChooser.setInitialDirectory(new File(System.getProperty(new GetPropetries().getLastDirectory())));
        }catch(Exception e){
            System.err.println(e);
        }
        //directoryChooser.setInitialDirectory(new File(System.getProperty("D:")));
        Stage stage = (Stage) directoryChooser_btn.getScene().getWindow();
        File dir = directoryChooser.showDialog(stage);
        if (dir != null) {
            //new GetPropetries().setLastDirectory(dir.toString());
            FileScanner.scanForFiles(dir.getAbsolutePath());
            Local.getAllAfterIndex(lastIndex);
            for(int i=0;i<Local.getId().size();i++){
                try{
                    localData.add(new Library(Local.getId().get(i).toString(), Local.getName().get(i).toString(), Local.getArtist().get(i).toString(),
                            Local.getAlbum().get(i).toString(), Local.getYear().get(i).toString(), Local.getNumber().get(i).toString(),
                            Local.getGenres().get(i).toString(), Local.getSong_path().get(i).toString(), Local.getFilename().get(i).toString()));
                }catch (NullPointerException e){
                    System.err.println(e);
                    System.err.println(localData.get(i).getId());
                }
            }
            lastIndex = localData.get(localData.size()-1).getId();
            System.out.println("AFTER "+lastIndex);
        }
    }

    /*************/
    /** UI INFO **/
    /*************/

    private void setLabels(String count, String songString){
        countLabel.setText(count);
        songLabel.setText(songString);
    }

    public void updates(){

        Platform.runLater(new Runnable() {
            public void run() {
                Duration currentTime = mp.getCurrentTime();
//                    playTime.setText(formatTime(currentTime, duration));
                //progressBar.setDisable(duration.isUnknown());
                if (!progressBar.isDisabled()
                        && duration.greaterThan(Duration.ZERO)
                        && !progressBar.isValueChanging()) {
                    progressLabel.setText(Math.round(currentTime.divide(duration).toMillis()*100)+"%");
                    progressBar.setValue(currentTime.divide(duration).toMillis()
                            * 100.0);
                    timeLabel.setText(String.format("%02d", ((int)Math.floor((currentTime.toSeconds() / 60) % 60)))+":"+(String.format("%02d", (int)Math.floor(currentTime.toSeconds() % 60)))+"/"+timeString);
                }
            }
        });
    }

    /*******************/
    /** MEDIA ACTIONS **/
    /*******************/
    protected int getFileToPlay(int songIndex, boolean autoplay){
        file = localData.get(songIndex).getPath();
        media = new Media(new File(file).toURI().toString());
        mp = new MediaPlayer(media);
        //getting info about current track
        songString = localData.get(songIndex).getName()+" - "+ localData.get(songIndex).getArtist();
        countString = songIndex+"/"+(localData.size()-1);
        libraryTable.getSelectionModel().select(songIndex);
        mp.setVolume(volume/100d);
        new GetPropetries().setLastFilePlayed(file);
        System.out.println(localData.get(songIndex).getPath());

        if(autoplay) {
            mp.play();
        }
        mp.setOnEndOfMedia(new Runnable() {
            @Override
            public void run() {
                mp.stop();
                currentlyPlayingIndex = getFileToPlay(songIndex+1, true); //should depend on selected behaviour - next, random?
            }
        });
        mp.setOnReady(new Runnable() {
            @Override
            public void run() {
                timeString = String.format("%02d", ((int)Math.floor((mp.getStopTime().toSeconds() / 60) % 60)))+":"+(String.format("%02d", (int)Math.floor(mp.getStopTime().toSeconds() % 60)));
//                new Controls().setLabels(countString, songString);
                duration = mp.getMedia().getDuration();
                setLabels(countString, songString);
                updates();
//                new Controls().updates();
            }
        });
        mp.currentTimeProperty().addListener(new InvalidationListener() {
            public void invalidated(Observable ov) {
                updates();
//                new Controls().updates();
            }
        });

        return songIndex;
    }
}