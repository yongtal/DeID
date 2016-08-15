package dit;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JProgressBar;

/**
 *
 * @author Christian Prescott & Angelo
 * May not in use.
 */
public class DicomConverterTask implements Runnable {

   // private String outputDir = DeidData.outputPath + "dcm2niiOut/";
    private ArrayList<String> outputDir; 
    private JProgressBar progressBar = null;
    private ArrayList<String> inputDirs;
    private String[] command;

    public DicomConverterTask() throws RuntimeException{
        super();
        
        // Establish OS dependent paths
        if(FileUtils.OS.isMac()){
            //System.out.println("I'm a mac");
        } else if(FileUtils.OS.isWindows()){
        } else if(FileUtils.OS.isUnix()){
        } else {
            throw new RuntimeException("Platform (" + FileUtils.OS.getOS() + ") not "
                    + "supported by dcm2nii.");
        }
        
      /*  File outDir = new File(outputDir);
        if(!outDir.exists()){
            outDir.mkdirs();
        } else {
            // Empty the directory of old files - they cause naming issues with dcm2nii.
            File[] fileList = outDir.listFiles();
            for(File f : fileList){
                try{
                    if(!f.delete()){
                        DEIDGUI.log("Failed to remove temporary files from previous "
                                + "conversion result. If images are named incorrectly,"
                                + " delete all files in " + outDir.getAbsolutePath(), DEIDGUI.LOG_LEVEL.WARNING);
                    }
                } catch (SecurityException e){
                    DEIDGUI.log("Could not remove temporary files from previous "
                            + "conversion result: " + e.getMessage() + "If "
                            + "images are named incorrectly, delete all files "
                            + "in " + outDir.getAbsolutePath(), DEIDGUI.LOG_LEVEL.WARNING);
                }
            }
        }*/
        
        inputDirs = new ArrayList<String>();
        outputDir = new ArrayList<String>();
        command = new String[]{
            DeidData.unpackedFileLocation.get("dcm2nii").getAbsolutePath(),
            // TODO: Are any of these features necessary? Present Dr. Eckert with this list
//            -a Anonymize [remove identifying information]: Y,N = Y
//            -b load settings from specified inifile, e.g. '-b C:\set\t1.ini'  
//            -c Collapse input folders: Y,N = Y
//            -d Date in filename [filename.dcm -> 20061230122032.nii]: Y,N = Y
//            -e events (series/acq) in filename [filename.dcm -> s002a003.nii]: Y,N = Y
//            -f Source filename [e.g. filename.par -> filename.nii]: Y,N = N
//            -g gzip output, filename.nii.gz [ignored if '-n n']: Y,N = Y
//            -i ID  in filename [filename.dcm -> johndoe.nii]: Y,N = N
//            -m manually prompt user to specify output format [NIfTI input only]: Y,N = Y
//            -n output .nii file [if no, create .hdr/.img pair]: Y,N = Y
//            -o Output Directory, e.g. 'C:\TEMP' (if unspecified, source directory is used)
//            -p Protocol in filename [filename.dcm -> TFE_T1.nii]: Y,N = Y
//            -r Reorient image to nearest orthogonal: Y,N 
//            -s SPM2/Analyze not SPM5/NIfTI [ignored if '-n y']: Y,N = N
//            -v Convert every image in the directory: Y,N = Y
//            -x Reorient and crop 3D NIfTI images: Y,N = N
            "-a", "n", "-d", "n", "-e", "n", "-f", "y", "-g", "n",
            "-p", "n", "-r", "y", "-o", "outputDir", "inputDirectory"};
    }

    /**
     * Assign a progress bar to be updated by this conversion task.
     *
     * @param bar The JProgressBar that will display the conversion's progress
     */
    public void setProgressBar(JProgressBar bar) {
        progressBar = bar;
    }

    /**
     * Add directories with DICOM images to be converted by this task.
     *
     * @param file File object representing a directory that contains .dcm
     * images
     */
    public void addInputDirectory(File file) {
        inputDirs.add(file.getAbsolutePath());
        //System.out.println("houhou"+file.getAbsolutePath());
    }
    
    public void addOutputDirectory(File file) {
        outputDir.add(file.getAbsolutePath()+"/");
        //System.out.println("houhou"+file.getAbsolutePath());
    }

    @Override
    public void run() {
        String progressPatternStr = "Converting\\s*(\\d+)/(\\d+)",
                savingPatternStr = "Saving (.+)",
                dicomNamePatternStr = "(.+)->";
        Pattern progressPattern = Pattern.compile(progressPatternStr),
                savingPattern = Pattern.compile(savingPatternStr),
                dicomNamePattern = Pattern.compile(dicomNamePatternStr),
                validatingPattern = Pattern.compile("Validating \\d+ potential DICOM images"),
                foundPattern = Pattern.compile("Found \\d+ DICOM images");
        ArrayList<File> newFiles = new ArrayList<File>(),
                sourceFiles = new ArrayList<File>();

        DEIDGUI.log("Converting " + inputDirs.size() + " DICOM directories");
        for (int ndx = 0; ndx < inputDirs.size(); ndx++) {
            // Set input directory for dcm2nii
            command[command.length - 1] = inputDirs.get(ndx);
            command[command.length - 2] = outputDir.get(ndx);
            clearOutputdir(outputDir.get(ndx));
            // Capture output of the dcm2nii process
            java.lang.ProcessBuilder pb = new ProcessBuilder(command);
            pb.redirectErrorStream(true);
            pb.directory(DeidData.unpackedFileLocation.get("dcm2nii").getParentFile());
            Process dicomConProc = null;
            try {
                dicomConProc = pb.start();
            } catch (IOException ex) {
                DeidData.niftiFiles.addAll(newFiles);
                DEIDGUI.log("The dcm2nii thread couldn't be started: " + ex.getMessage(), DEIDGUI.LOG_LEVEL.ERROR);
                // Advance to next panel
                synchronized (this) {
                    this.notify();
                }
                return;
            }
            
            BufferedReader br = new BufferedReader(new InputStreamReader(dicomConProc.getInputStream()));
            String line;
            try {
                while ((line = br.readLine()) != null && !line.isEmpty()) {
                    if(Thread.interrupted()){
                        // Kill conversion process
                        dicomConProc.destroy();
                    }
                    Matcher progressMatcher = progressPattern.matcher(line),
                            dicomNameMatcher = dicomNamePattern.matcher(line),
                            validatingMatcher = validatingPattern.matcher(line),
                            foundMatcher = foundPattern.matcher(line);
                    
                    
                    if(line.startsWith("Chris Rorden's dcm2nii")||
                            line.startsWith("reading preferences file") ||
                            line.startsWith("Data will be exported to") ||
                            validatingMatcher.find() ||
                            foundMatcher.find()){
                    } else if (progressMatcher.find()) {
                        float processProgress =
                                (float) Integer.parseInt(progressMatcher.group(1))
                                / (float) Integer.parseInt(progressMatcher.group(2));
                        if (progressBar != null) {
                            progressBar.setValue(Math.min(99,
                                    // Progress of this process adjusted for total
                                    (int) ((100 * (processProgress + ndx)) / inputDirs.size())));
                        }
                    } else if (dicomNameMatcher.find()) {
                        String parentPath = inputDirs.get(ndx);
                        // Seperate the parent directory if it isn't already
                        File originalFile = new File(parentPath
                                + (parentPath.charAt(parentPath.length() - 1) == File.separatorChar
                                ? "" : File.separator) + dicomNameMatcher.group(1));
                        sourceFiles.add(originalFile);

                        //System.out.println("Found original file name: " + originalFile.getAbsolutePath());
                        // Read saved file name from next line
                        String saveLine;
                        if ((saveLine = br.readLine()) != null) {
                            Matcher savingMatcher = savingPattern.matcher(saveLine);
                            if (savingMatcher.find()) {
                                File savedFile = new File(savingMatcher.group(1));
                                DEIDGUI.log("Converted " + savedFile.getName());
                                newFiles.add(savedFile);
                                // Construct map from new NIfTIs to source dicoms for use later
                                DeidData.NiftiConversionSourceTable.put(savedFile, originalFile);

                                //System.out.println("Found new saved file: " + savedFile.getAbsolutePath());
                            } else {
                                DEIDGUI.log("Unexpected output from dcm2nii: " + saveLine, DEIDGUI.LOG_LEVEL.WARNING);
                            }
                        }
                    } else {
                        // Output on file exists:
                        // File already exists /Users/christianprescott/Desktop/dcm2niiTest/Output/1MRhead_DHead41200612140912061560001632817982.nii /Users/christianprescott/Desktop/dcm2niiTest/Output/1MRhead_DHead41200612140912061560001632817982.nii
                        DEIDGUI.log("Unexpected output from dcm2nii: " + line, DEIDGUI.LOG_LEVEL.WARNING);
                    }
                }
            } catch (IOException ex) {}
        }

        DeidData.niftiFiles.addAll(newFiles);
       /* for (int ndx = 0; ndx < outputDir.size(); ndx++) {
            clearOutputdir(outputDir.get(ndx));
        }*/
        DEIDGUI.log("Added " + newFiles.size() + " new NIfTIs converted from DICOMs");
        
        // Advance to next panel
        synchronized (this) {
            this.notify();
        }
    }
    
    private void clearOutputdir(String outDir){
        File outD =  new File(outDir); 
        File[] fileList = outD.listFiles();
            for(File f : fileList){
                if(f.getName().endsWith(".nii")){
                try{
                    
                    if(!f.delete()){
                        DEIDGUI.log("Failed to remove temporary files from previous "
                                + "conversion result. If images are named incorrectly,"
                                + " delete all files in " + outD.getAbsolutePath(), DEIDGUI.LOG_LEVEL.WARNING);
                    }
                } catch (SecurityException e){
                    DEIDGUI.log("Could not remove temporary files from previous "
                            + "conversion result: " + e.getMessage() + "If "
                            + "images are named incorrectly, delete all files "
                            + "in " + outD.getAbsolutePath(), DEIDGUI.LOG_LEVEL.WARNING);
                }
                }
            }
    }
}
