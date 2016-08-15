/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package dit;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
 
/**
 *
 * @author zderose
 */
public class GZipFile {
    private String inGzFile;
    private String outFile;
    
    public void setInGzFile(String in) {
        this.inGzFile = in;
    }
    
    public void setOutFile(String out) {
        this.outFile = out;
    }
    
    public String getInGzFile(){
        return this.inGzFile;
    }
    
    public String getOutFile(){
        return this.outFile;
    }
    
    public GZipFile(String input, String output) {
        System.out.println("Passing in " + input + " as incoming .gz file.");
        this.inGzFile = input;
        System.out.println("Passing in " + output + " as outgoing file.");
        this.outFile = output;
    }
 
    //public static void main( String[] args )
    //{
    //	GZipFile gZip = new GZipFile();
    //	gZip.gunzipIt();
    //}
 
    /**
     * GunZip it
     */
    public void gunzipIt(){
        System.out.println("Unzipping .gz file : " + inGzFile);
        
        byte[] buffer = new byte[10240000];
 
        try{
            GZIPInputStream gzis = 
            new GZIPInputStream(new FileInputStream(inGzFile));
            
            FileOutputStream out = new FileOutputStream(outFile);
 
            int len;
            while ((len = gzis.read(buffer)) > 0) {
                out.write(buffer, 0, len);
            }
            System.out.println(inGzFile + " unzipped.");
            
            gzis.close();
            out.close();
 
            
        } catch(IOException ex) {
            ex.printStackTrace();   
        }
    }
}
