package dit;

import java.io.*;
import org.xeustechnologies.jtar.TarEntry;
import org.xeustechnologies.jtar.TarOutputStream;

/**
 *
 * @author Christian Prescott
 */
public class TarMaker {
    private TarOutputStream outStream;
    
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
