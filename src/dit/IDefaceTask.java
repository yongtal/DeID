/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dit;

import java.io.File;
import java.util.Vector;
import javax.swing.JProgressBar;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 *
 * @author QM
 * The interface of deface task. Any DefaceTask need to implement from this interface.
 */
public interface IDefaceTask {
    public void setProgressBar(JProgressBar bar);
    public void setTextfield(JTextArea text);
//    public void appendTextfield(String toAppend);
    public void setInputImages(Vector<NIHImage> file);
    public void run();
    
}
