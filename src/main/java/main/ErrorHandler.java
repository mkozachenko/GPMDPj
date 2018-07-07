package main;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class ErrorHandler {


    private static FileHandler fh;

    public static void handleError(Logger logger, String level, String message) {
        /*CALLING MESSAGE WINDOW TO INFORM USER ABOUT ERROR*/
        logToFile(logger,level, message);
    }

    public static void logToFile(Logger logger, String level, String message) {
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
}
