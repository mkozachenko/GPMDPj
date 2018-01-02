package main;


import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.io.FileUtils;

import java.io.*;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * Created by symph on 20.07.2017.
 */
public class GetPropetries {

    private String lastDirectory, lastFile;
    private static String propFileName = "rnd.properties", extFolder="./data/";
    private static Logger logger = Logger.getLogger(GetPropetries.class.getName());
    private static FileHandler fh;

    private void getUserValues(){
        try {
            PropertiesConfiguration config = new PropertiesConfiguration(extFolder+propFileName);
            lastDirectory = config.getString("lastDirectory");
            lastFile = config.getString("lastFile");
            /*logger.info(config.getPath());*/
        } catch (Exception e) {
            System.out.println("Exception: " + e);
        }
    }

    private void setUserValues(String propName, String propValue){
        try {
            PropertiesConfiguration config = new PropertiesConfiguration(extFolder+propFileName);
            config.setProperty(propName, propValue);
            config.save();
            //logger.info(config.getPath());
        } catch (Exception e) {
            logger(logger,"error", e.getMessage());
        }
    }

    public static void logger(Logger logger, String level, String message) {
        try {
            fh = new FileHandler("./logging.log", true);
            logger.addHandler(fh);
            SimpleFormatter formatter = new SimpleFormatter();
            fh.setFormatter(formatter);
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        switch (level) {
            case "info":
                logger.info("\n"+message+"\n");
                fh.close();
                break;
            case "error":
                logger.severe("\n"+message+"\n");
                fh.close();
                break;
        }
    }

    //getters
    public String getLastDirectory(){
        getUserValues();
        return this.lastDirectory;
    }
    public String getLastFile(){
        getUserValues();
        return this.lastFile;
    }

    /**
     SETTERS
     **/
    public void setLastDirectory(String propValue){
        setUserValues("lastDirectory", propValue);
    }
    public void setLastFile(String propValue){
        setUserValues("lastFile", propValue);
    }
}
