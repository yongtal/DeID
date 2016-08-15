package dit;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;



/**
 *
 * @author Xuebo
 */
public class LoggerWrapper {
   
    public static final Logger myLogger = Logger.getLogger(LoggerWrapper.class.getName());
 
    private static LoggerWrapper instance = null;
    
    public static LoggerWrapper getInstance() {
        if(instance == null) {
            prepareLogger();
            instance = new LoggerWrapper();
        }
        return instance;
    }
  
    private static void prepareLogger() {
        FileHandler myFileHandler = null;
        try {
            myFileHandler = new FileHandler("tmp_log.txt", false);
        } catch (IOException ex) {
            Logger.getLogger(LoggerWrapper.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SecurityException ex) {
            Logger.getLogger(LoggerWrapper.class.getName()).log(Level.SEVERE, null, ex);
        }
        myFileHandler.setFormatter(new SimpleFormatter());
        myLogger.addHandler(myFileHandler);
        myLogger.setUseParentHandlers(false);
        myLogger.setLevel(Level.FINER);
    }
  
 
}

