package main;


import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.io.FileUtils;

import java.io.*;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * Created by symph on 20.07.2017.
 */
public class GetPropetries {
    private static Logger logger = Logger.getLogger(GetPropetries.class.getName());
    private String lastDirectory, lastFile, lastPlayed,
            authUsername, authPassword, authAndroidID, localGMPfolder;
    private static String propFileName = "rnd.properties", extFolder="./props/", settingsFile = "settings.properties", localFoldersFile = "localFolders";


    private void getUserSettings(){
        try {
            PropertiesConfiguration config = new PropertiesConfiguration(extFolder+settingsFile);
            authUsername = config.getString("authUsername");
            authPassword = config.getString("authPassword");
            authAndroidID = config.getString("authAndroidID");
            localGMPfolder = config.getString("localGMPfolder");
        } catch (Exception e) {
            System.out.println("Exception: " + e);
        }
    }

    private void getUserValues(){
        try {
            PropertiesConfiguration config = new PropertiesConfiguration(extFolder+propFileName);
            lastDirectory = config.getString("lastDirectoryAdded");
            lastFile = config.getString("lastFileAdded");
            lastPlayed = config.getString("lastFilePlayed");
            /*logger.info(config.getPath());*/
        } catch (Exception e) {
            System.out.println("Exception: " + e);
        }
    }

    //get list of local folders to search for files
    private List getLocalFolders(){
        List<String> lines = new ArrayList<String>();
        try {
            Scanner sc = new Scanner(new File("./data/"+localFoldersFile));
            while (sc.hasNextLine()) {
                lines.add(sc.nextLine());
            }
        } catch (Exception e) {
            System.err.println("Exception: " + e);
        }
        return lines;
    }

    private void setUserValues(String propFile, String propName, String propValue){
        try {
            PropertiesConfiguration config = new PropertiesConfiguration(propFile);
            config.setProperty(propName, propValue);
            config.save();
            //logger.info(config.getPath());
        } catch (Exception e) {
            main.ErrorHandler.handleError(logger,"error", e.getMessage());
        }
    }

    /**
     GETTERS
     **/
    public String getLastDirectory(){
        getUserValues();
        return lastDirectory;
    }
    public String getLastFile(){
        getUserValues();
        return lastFile;
    }

    public String getLastFilePlayed(){
        getUserValues();
        return lastFile;
    }

    public String getUsername(){
        getUserSettings();
        return authUsername;
    }
    public String getPassword(){
        getUserSettings();
        return authPassword;
    }

    public String getGMPfolder(){
        getUserSettings();
        return localGMPfolder;
    }

    public String getAndroidID(){
        getUserSettings();
        return authAndroidID;
    }

    /**
     SETTERS
     **/
    public void setLastDirectory(String propValue){
        setUserValues(extFolder+propFileName,"lastDirectory", propValue);
    }
    public void setLastFile(String propValue){
        setUserValues(extFolder+propFileName,"lastFile", propValue);
    }
    public void setLastFilePlayed(String propValue){
        setUserValues(extFolder+propFileName,"lastFilePlayed", propValue);
    }

    public void setUsername(String propValue){
        setUserValues(extFolder+settingsFile,"authUsername", propValue);
    }

    public void setPassword(String propValue){
        setUserValues(extFolder+settingsFile,"authPassword", propValue);
    }

    public void setUDID(String propValue){
        setUserValues(extFolder+settingsFile,"authAndroidID", propValue);
    }

    public void setGMPfolder(String propValue){
        setUserValues(extFolder+settingsFile,"localGMPfolder", propValue);
    }
}
