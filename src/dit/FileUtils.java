package dit;

import java.io.*;
import java.nio.channels.FileChannel;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

/**
 *
 * @author Christian Prescott
 * A auxiliary class to give some file operations and OS detection.  
 */
public class FileUtils {

    public static class Extensions {

        public static final String dicom = "dcm",
                nifti = "nii",
                analyzehdr = "hdr",
                analyzeimg = "img",
                niftigz = "nii.gz";
    }

    // Check the current OS
    public static class OS {

        public static boolean isWindows() {
            String os = System.getProperty("os.name").toLowerCase();
            return (os.indexOf("win") >= 0);
        }

        public static boolean isMac() {
            String os = System.getProperty("os.name").toLowerCase();
            return (os.indexOf("mac") >= 0);
        }

        public static boolean isUnix() {
            String os = System.getProperty("os.name").toLowerCase();
            return (os.indexOf("nix") >= 0 || os.indexOf("nux") >= 0);
        }

        public static boolean isSolaris() {
            String os = System.getProperty("os.name").toLowerCase();
            return (os.indexOf("sunos") >= 0);
        }

        public static String getOS() {
            return System.getProperty("os.name");
        }

        public static boolean isOS64bit() {
            String osarc = System.getProperty("os.arch").toLowerCase();
            return (osarc.indexOf("64") >= 0);
        }
    }

    /**
     * Returns the extension of a file, or null if none is specified.
     */
    public static String getExtension(File file) {
        String ext = null;
        String name = file.getName();
        if (name.endsWith(".nii.gz")) {
            ext = "nii.gz";
            return ext;
        }
        int i = name.lastIndexOf('.');

        if (i > 0 && i < name.length() - 1) {
            ext = name.substring(i + 1).toLowerCase();
        }

        return ext;
    }
    
    public static boolean deleteRecursive(File path) throws FileNotFoundException
    {
        if(!path.exists())
            throw new FileNotFoundException(path.getAbsolutePath());
        boolean ret=true;
        if(path.isDirectory())
        {
            for(File f : path.listFiles())
            {   
                ret=ret&& FileUtils.deleteRecursive(f);
            }
        }
        return ret && path.delete();
    }

    /**
     * Returns the name of a file without the extension.
     */
    public static String getName(File file) {
        
        
           String name = file.getAbsolutePath();
            name=name.replace(System.getProperty("file.separator").toString(), "_");
             name=name.replace(":", "");
            if (name.endsWith(".nii.gz")) {
                int j = name.lastIndexOf(".nii.gz");
                name = name.substring(0, j);
                return name;
            }

            int i = name.lastIndexOf('.');

            if (i > 0) {
                name = name.substring(0, i);
            }
            return name;
       
      
    }

    public static String sizeToString(long size) {
        long[] dividers = new long[]{1073741824, 1048576, 1024, 1};
        String[] units = new String[]{"GB", "MB", "KB", "B"};

        if (size < 0) {
            throw new IllegalArgumentException("Size must be greater than zero.");
        }

        String result = "";
        for (int ndx = 0; ndx < dividers.length; ndx++) {
            if (size >= dividers[ndx]) {
                result = String.format("%.2f", (float) size / (float) dividers[ndx]) + units[ndx];
                break;
            }
        }

        return result;
    }

    public static File stripExtension(File file) {
        String parent = file.getParent();
        String newName = FileUtils.getName(file);

        String newPath = (parent == null ? "" : parent) + File.separator + newName;

        return new File(newPath);
    }

    public static Vector<File> getFileListRecursively(File[] files, int depth) {
        return getFileListRecursively(Arrays.asList(files), depth);
    }

    public static Vector<File> getFileListRecursively(List<File> files, int depth) {
        Vector<File> fileList = new Vector<File>();
        if (depth >= 0) {
            for (int ndx = 0; ndx < files.size(); ndx++) {
                File file = files.get(ndx);
                if (file.isDirectory()) {
                    fileList.addAll(getFileListRecursively(file.listFiles(new ImageFilter()), depth - 1));
                } else {
                    fileList.add(file);
                }
            }
        }
        return fileList;
    }

    // This method: not tested.
    public static void copyFile(File source, File dest) throws
            FileNotFoundException, IOException {
        if (!dest.exists()) {
            dest.createNewFile();            
        }

        FileChannel readStream = null, writeStream = null;
        try {
            readStream = new FileInputStream(source).getChannel();
            writeStream = new FileOutputStream(dest).getChannel();

            readStream.transferTo(0, readStream.size(), writeStream);
        } finally {
            if (readStream != null) {
                readStream.close();
            }
            if (writeStream != null) {
                writeStream.close();
            }
        }
    }
}
