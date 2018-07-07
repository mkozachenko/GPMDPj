package Controllers;

import Database.Local;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.MediaPlayer;
import main.Library;

import java.util.Random;

public class FileTable {
    @FXML
    protected TableView<Library> libraryTable;

    private static MediaPlayer mp = Player.mp;

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
                        new Player().getFileToPlay(libraryTable.getSelectionModel().getSelectedIndex(), true);
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

    protected void init(){
//        new Player().localData = libraryTable.getItems();
    }
}
