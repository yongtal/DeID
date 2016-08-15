/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dit;

import java.io.File;
import java.io.IOException;

/**
 *
 * @author angelo
 * Level:Top
 */
public class OpenImagewithMRIcron implements Runnable {

    private String[] command;
    private String imageName;

    public OpenImagewithMRIcron(File imagefile) throws RuntimeException {
        super();
        if (FileUtils.OS.isMac()) {
            //System.out.println("I'm a mac");
        } else if (FileUtils.OS.isWindows()) {
        } else if (FileUtils.OS.isUnix()) {
        } else {
            throw new RuntimeException("Platform (" + FileUtils.OS.getOS() + ") not "
                    + "supported by MRIcron.");
        }

        imageName = imagefile.getAbsolutePath();
        if (FileUtils.OS.isOS64bit() && FileUtils.OS.isUnix()) {
            command = new String[]{
                DeidData.unpackedFileLocation.get("mricron_64").getAbsolutePath(),
                imageName, "-r", DeidData.unpackedFileLocation.get("mricron_64").getParent().toString() + "/default.ini",};
        } else if (FileUtils.OS.isWindows()) {
            command = new String[]{
                DeidData.unpackedFileLocation.get("mricron.exe").getAbsolutePath() + " ", imageName};
        } else if (FileUtils.OS.isMac()) {
            command = new String[]{"/bin/sh",
                    DeidData.unpackedFileLocation.get("Mango.zip").getParentFile().getAbsolutePath() + "/Mango.sh", imageName};
        } else {
            command = new String[]{
                DeidData.unpackedFileLocation.get("mricron").getAbsolutePath(),
                imageName, "-r ", DeidData.unpackedFileLocation.get("mricron").getParent().toString() + "/default.ini",};
        }

        System.out.print("Command View:");
        for (String string : command) {
            System.out.print(string + " ");;
        }
        System.out.println();

    }

    @Override
    public void run() {
        java.lang.ProcessBuilder pb = new ProcessBuilder(command);
        pb.redirectErrorStream(true);
        if (FileUtils.OS.isWindows()) {
            pb.directory(DeidData.unpackedFileLocation.get("mricron.exe").getParentFile());
        } else if (FileUtils.OS.isMac()) {
            pb.directory(DeidData.unpackedFileLocation.get("Mango.zip").getParentFile());
        } else {
            pb.directory(DeidData.unpackedFileLocation.get("mricron").getParentFile());
        }
        Process openImageProc = null;
        try {
            openImageProc = pb.start();
        } catch (IOException ex) {

            DEIDGUI.log("Open image file failed: " + ex.getMessage(), DEIDGUI.LOG_LEVEL.ERROR);

        }

    }
}
