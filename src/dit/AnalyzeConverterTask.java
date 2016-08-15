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
 * @author Christian Prescott
 * This class is depressed, may not in use any more
 */
public class AnalyzeConverterTask implements Runnable {

   // private String outputDir = DeidData.outputPath + "fslchfiletypeOut/";
   // private ArrayList<String> outputDir; 
    private JProgressBar progressBar = null;
    private ArrayList<File> inputPairs;
    private String[] command;

    public AnalyzeConverterTask() throws RuntimeException {
        super();
        
        // Establish OS dependent paths
      /*  if(FileUtils.OS.isMac()){
            //System.out.println("I'm a mac");
        } else {
            throw new RuntimeException("Platform (" + FileUtils.OS.getOS() + ") not "
                    + "supported by fslchfiletype.");
        }
        */
      /*  File outDir = new File(outputDir);
        if(!outDir.exists()){
            outDir.mkdirs();
        }*/
        
        inputPairs = new ArrayList<File>();
       // outputDir = new ArrayList<String>();
        command = new String[]{
            DeidData.unpackedFileLocation.get("fslchfiletype").getAbsolutePath(),
            // Depends on fslchfiletype_exe and remove_ext
            //./fslchfiletype <filetype> <filename> [filename2]
            //Changes the file type of the image file, or copies to new file
            //Valid values of filetype are ANALYZE, NIFTI, NIFTI_PAIR,
            //                             ANALYZE_GZ, NIFTI_GZ, NIFTI_PAIR_GZ
            "NIFTI", "input", "outputdir + filename"};
        //System.out.println(command.toString());
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
     * Add directories with Analyze images to be converted by this task.
     *
     * @param file File object representing a directory that contains .hdr and .img
     * images
     */
    public void addInputPair(File file) {
        inputPairs.add(file);
    }
    
    

    @Override
    public void run() {
        ArrayList<File> newFiles = new ArrayList<File>();
        String errorPatternStr = "ERROR";
        Pattern errorPattern = Pattern.compile(errorPatternStr);

        DEIDGUI.log("Converting " + inputPairs.size() + " Analyze pairs");
        for (int ndx = 0; ndx < inputPairs.size(); ndx++) {
            // Set input directory for fslchfiletype
            command[2] = inputPairs.get(ndx).getAbsolutePath();
            String outFilename = inputPairs.get(ndx).getParent()+File.separator + FileUtils.getName(inputPairs.get(ndx)) + ".nii";
            command[3] = outFilename;

            DEIDGUI.log("fslchfiletype command: ", DEIDGUI.LOG_LEVEL.INFO);
            for(int i=0;i<command.length;i++)
            {
                DEIDGUI.log(command[i]+" ", DEIDGUI.LOG_LEVEL.INFO);
                System.out.print(command[i]+" ");
            }
            System.out.println();
            
            // Capture output of the fslchfiletype process
            java.lang.ProcessBuilder pb = new ProcessBuilder(command);
            pb.redirectErrorStream(true);
            pb.directory(DeidData.unpackedFileLocation.get("fslchfiletype").getParentFile());
            Process analyzeConProc = null;
            boolean fileValid = true;
            try {
                analyzeConProc = pb.start();
            } catch (IOException ex) {
                DeidData.niftiFiles.addAll(newFiles);
                // Advance to next panel
                synchronized (this) {
                    this.notify();
                }
                DEIDGUI.log("The fslchfiletype thread couldn't be started: " + ex.getMessage(), DEIDGUI.LOG_LEVEL.ERROR);
                return;
            }
            BufferedReader br = new BufferedReader(new InputStreamReader(analyzeConProc.getInputStream()));
            String line;
            // Update progress bar based on output of fslchfiletype
            try {
                while ((line = br.readLine()) != null  && !line.isEmpty()) {
                    if(Thread.interrupted()){
                        // Kill conversion process
                        analyzeConProc.destroy();
                    }
                    //System.out.println(line); // Print fslchfiletype output
                    Matcher errorMatcher = errorPattern.matcher(line);
                    if (errorMatcher.find()) {
                        // Error sample outputs:
                        // ** ERROR (nifti_image_read): failed to find header file for '../sampledata/set1analyze/suptents'
                        // ** ERROR: nifti_image_open(../sampledata/set1analyze/suptents): bad header info
                        // ERROR: failed to open file ../sampledata/set1analyze/suptents
                        // Cannot open volume ../sampledata/set1analyze/suptents for reading!

                        DEIDGUI.log("fslchfiletype error occurred: " + line, DEIDGUI.LOG_LEVEL.ERROR);
                        fileValid = false;
                    } else {
                        DEIDGUI.log("Unexpected fslchfiletype output: " + line, DEIDGUI.LOG_LEVEL.WARNING);
                    }
                }
            } catch (IOException ex) {}
            
            if(fileValid){
                DEIDGUI.log("Converted " + outFilename);
                newFiles.add(new File(outFilename));
            }
            float processProgress = (float) ndx / (float) inputPairs.size();
            if (progressBar != null) {
                progressBar.setValue((int) (100 * processProgress));
            }
        }

        DeidData.niftiFiles.addAll(newFiles);
        DEIDGUI.log("Added " + newFiles.size() + " new NIfTIs converted from Analyze");

        // Advance to next panel
        synchronized (this) {
            this.notify();
        }
    }
}
