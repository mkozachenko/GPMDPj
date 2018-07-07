package Controllers;

import Database.Local;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;
import main.Library;

import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;

public class Controls implements Initializable {
    @FXML
    private Slider progressBar, volumeSlider;
    @FXML
    private Label songLabel, timeLabel, countLabel, volumeLabel, progressLabel;

    private static MediaPlayer mp = Player.mp;
    private ChangeListener<Duration> progressChangeListener;
    private static String lastIndex, file, songString, countString, timeString;
    private double full, current, progress, volume=10;
    private static Duration duration;
    private static int currentlyPlayingIndex, bound, runn, observ, ltr;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        volumeSlider.setValue(volume);
        volumeLabel.setText(Math.round(volume)+"%");
    }


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
        currentlyPlayingIndex = new Player().getFileToPlay(index, true);
    }

    @FXML
    public void nextSong_btn_clicked() {
        int index = currentlyPlayingIndex+1;
        mp.stop();
        currentlyPlayingIndex = new Player().getFileToPlay(index, true);
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

    protected void updates(){
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

    protected void setLabels(String count, String songString){
        countLabel.setText(count);
        songLabel.setText(songString);
    }

}
