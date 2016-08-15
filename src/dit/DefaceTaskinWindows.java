package dit;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Vector;
import java.util.regex.Pattern;
import javax.swing.JProgressBar;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 *
 * @author Christian Prescott This is the windows version, but need modification
 * in the future.
 */
public class DefaceTaskinWindows extends DefaceTask implements Runnable, IDefaceTask {   
    @Override
    protected void setDefacerName(){
        defacerName = "bet.exe";
    }
    
    public DefaceTaskinWindows(NIHImage image) throws RuntimeException {
        super(image);
    }
}
