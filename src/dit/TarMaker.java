package dit;

import java.io.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import org.xeustechnologies.jtar.TarEntry;
import org.xeustechnologies.jtar.TarOutputStream;

/**
 *
 * @author Christian Prescott
 */
public class TarMaker {
    private TarOutputStream outStream;
    private String fsprt = File.separator;
    
    public TarMaker(File tarFile) throws IOException {
        File parentDir = tarFile.getParentFile();
        if(parentDir != null && !parentDir.exists()){
            parentDir.mkdirs();
        }
        tarFile.createNewFile();
        FileOutputStream fos = new FileOutputStream(tarFile);
        outStream = new TarOutputStream(new BufferedOutputStream(fos));
    }
    
    public void addFile(File entryFile) throws IOException{
        addFile(entryFile, entryFile.getName());
    }
        
    public void addFile(File entryFile, String entryName) throws IOException{
        if(outStream == null){
        }
        try {
            outStream.putNextEntry(new TarEntry(entryFile, entryName));
            BufferedInputStream source = new BufferedInputStream(new FileInputStream(entryFile));
            
            int count;
            byte data[] = new byte[2048];
            while((count = source.read(data)) != -1){
                outStream.write(data, 0, count);
            }
            
            outStream.flush();
            source.close();
        } catch (IOException ex) {
            DEIDGUI.log("Error adding "+entryFile.getPath() + 
                    " to tarball, it may not have been included: " + 
                    ex.getMessage(), DEIDGUI.LOG_LEVEL.WARNING);
            throw new IOException("Unable to add file to tarball");
        }
    }
    
    public void tarTree (DefaultTreeModel model) throws IOException{
        tarTree(null, "", (DefaultMutableTreeNode)model.getRoot());
    }
    
    private void tarTree (TreeNode parent, String parDir, DefaultMutableTreeNode node) throws IOException {
        NIHImage image;
        try {
            image = (NIHImage)(node.getUserObject());
            File file = image.getTempPotision();
            if (image.isSeletecInJarFile()) {
                if (!image.getImageNewName().equals("")) {
                    String outputEntry = parDir+file.getName();
                    addFile(file, outputEntry);    
                    //System.out.println("-------------------------");
                    //System.out.println("Dir: " + parDir);
                    //System.out.println("File: " + file);
                    if (outputEntry.endsWith("hdr")) {
                        String newEntry = outputEntry.substring(0, outputEntry.lastIndexOf('.'))+".img";
                        addFile(new File(file.toString().substring(0, file.toString().lastIndexOf('.')) + ".img"), newEntry);
                       // System.out.println("hdrrrrrrrrrr");
                    }
                    else if (outputEntry.endsWith("img")) {
                        String newEntry = outputEntry.substring(0, outputEntry.lastIndexOf('.'))+".hdr";
                        addFile(new File(file.toString().substring(0, file.toString().lastIndexOf('.')) + ".hdr"), newEntry);
                        //System.out.println("imgggggggggggg");
                    }
                } else {
                    DEIDGUI.log("No randomized ID was created for "
                            + image.getImageName() + ", it may not be "
                            + "deidentified", DEIDGUI.LOG_LEVEL.WARNING);
                    addFile(file,parDir+file.getName());  
                }
            // Include header variables file for valid DICOM source files
            // Header files were created using randomized names, so no 
            // need to rename here.
            }
        } catch (java.lang.ClassCastException e) {

            int chcnt = node.getChildCount();
            if (parent != null) { //check for root
                File file = new File(node.getUserObject().toString());
                parDir += file.getName() + fsprt ;       
            }
            for (int i = 0 ; i < chcnt; i++) {
                tarTree(node, parDir, (DefaultMutableTreeNode)node.getChildAt(i));
            }
        }
    }
    
    public void close(){
        try {
            if(outStream != null){
                outStream.close();
            }
        } catch (IOException ex) {
            DEIDGUI.log("Couldn't close tarball output stream, file may be "
                    + "damaged or incomplete", DEIDGUI.LOG_LEVEL.WARNING);
        }
    }
}
