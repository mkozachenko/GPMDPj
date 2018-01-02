package Controllers;

import Database.Local;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import main.CommonData;
import main.FileScanner;
import main.GetPropetries;
import main.Library;

import java.io.File;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

public class Player implements Initializable{
    private static Media media;
    private static MediaPlayer mp;
    private final FileChooser fileChooser = new FileChooser();
    private final DirectoryChooser directoryChooser = new DirectoryChooser();
    private ChangeListener<Duration> progressChangeListener;
    private static String lastIndex="0", file;
    private double full, current, progress;
    private static int currentlyPlayingIndex;

    @FXML
    private Button play_btn, pause_btn, fileChooser_btn, directoryChooser_btn;
    @FXML
    private Slider playProgress, volumeSlider;
    @FXML
    private TextArea filesArea;
    @FXML
    private TableView libraryTable;
    private static ObservableList<Library> data;
    @FXML
    private TableColumn nameCol, artistCol, albumCol;
    private TableRow clickedRow;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        data = libraryTable.getItems();
        Local.getAllPaths();
        for(int i=0;i<Local.getId().size();i++){
            try{
                data.add(new Library(Local.getId().get(i).toString(), Local.getName().get(i).toString(), Local.getArtist().get(i).toString(),
                        Local.getAlbum().get(i).toString(), Local.getYear().get(i).toString(), Local.getNumber().get(i).toString(),
                        Local.getGenres().get(i).toString(), Local.getSong_path().get(i).toString(), Local.getFilename().get(i).toString()));
                lastIndex = data.get(data.size()-1).getId();
            }catch (NullPointerException e){
                System.err.println(e);
            }
        }
        System.out.println(lastIndex);
        currentlyPlayingIndex = getFileToPlay(0);

    }

    @FXML
    private void PlayButton_clicked(){
        mp.play();
        setCurrentlyPlaying(mp);
    }

    @FXML
    private void PauseButton_сlicked(){
        mp.pause();
        System.out.println("Paused");
    }

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
                    data.add(new Library(Local.getId().get(i).toString(), Local.getName().get(i).toString(), Local.getArtist().get(i).toString(),
                            Local.getAlbum().get(i).toString(), Local.getYear().get(i).toString(), Local.getNumber().get(i).toString(),
                            Local.getGenres().get(i).toString(), Local.getSong_path().get(i).toString(), Local.getFilename().get(i).toString()));
                }catch (NullPointerException e){
                    System.err.println(e);
                    System.err.println(data.get(i).getId());
                }
            }
            lastIndex = data.get(data.size()-1).getId();
            System.out.println("AFTER "+lastIndex);
        }
    }

    /** sets the currently playing label to the label of the new media player and updates the progress monitor. */
    private void setCurrentlyPlaying(final MediaPlayer newPlayer) {
        playProgress.setValue(0);
        playProgress.maxProperty().bind(Bindings.createDoubleBinding(
                () -> mp.getTotalDuration().toSeconds(),
                mp.totalDurationProperty())
        );
        progressChangeListener = new ChangeListener<Duration>() {
            @Override public void changed(ObservableValue<? extends Duration> observableValue, Duration oldValue, Duration newValue) {
                playProgress.setValue(1.0 * newPlayer.getCurrentTime().toMillis() / newPlayer.getTotalDuration().toMillis());
            }
        };
        newPlayer.currentTimeProperty().addListener(progressChangeListener);

        String source = newPlayer.getMedia().getSource();
        source = source.substring(0, source.length() - ".mp3".length());
        source = source.substring(source.lastIndexOf("/") + 1).replaceAll("%20", " ");
        //currentlyPlaying.setText("Now Playing: " + source);
    }

    @FXML
    public void prevSong_btn_clicked() {
        int index = currentlyPlayingIndex-1;
        mp.stop();
        currentlyPlayingIndex = getFileToPlay(index);
        System.out.println("PREV: "+index);
    }

    @FXML
    public void nextSong_btn_clicked() {
        int index = currentlyPlayingIndex+1;
        mp.stop();
        currentlyPlayingIndex = getFileToPlay(index);

        System.out.println("NEXT: "+index);
    }

    @FXML
    public void tableClicked() {

    }

    private static int getFileToPlay(int songIndex){
        //String file = "src\\main\\resources\\test.mp3";
        file = data.get(songIndex).getPath();
        media = new Media(new File(file).toURI().toString());
        mp = new MediaPlayer(media);
        System.out.println("PLAYING: "+songIndex);
        return songIndex;
    }

    private void volumeControl(double volume){
        volume = volumeSlider.getValue();
        mp.setVolume(volume);
    }
}
