/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dit;
import java.io.File;
/**
 *
 * @author angelo
 * 
 */
public class DemoFilter extends javax.swing.filechooser.FileFilter implements java.io.FileFilter{
     @Override
    public boolean accept(File file) {
        boolean isAccepted = false;
        if(file.isDirectory()){
        isAccepted = true;
        }else{
         
            String extension = FileUtils.getExtension(file);
            if(extension != null && 
                    // .txt
                    (extension.equals("txt") ||
                    //.xls
                    extension.equals("xls") // removed xlsx file format due to library copyright
                   
                    )){
                isAccepted = true;
            }
        }
        
        return isAccepted;
    }

    @Override
    public String getDescription() {
        return "Files (*.txt, *xls)";
    }
}
