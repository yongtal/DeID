package dit;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JProgressBar;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 *
 * @author Christian Prescott This class executed the task that running bet from
 * command line. Level:Top
 */
public class DefaceTask implements Runnable, IDefaceTask {

//test
    protected String outputDir = DeidData.outputPath + "betOut" + File.separator;
    protected JProgressBar progressBar = null;
    protected JTextArea detailText = null;
    protected static Vector<NIHImage> inputImages;
    protected int images;
    protected NIHImage currentImage;
    protected String[] command;
    public static int count = 0;
    protected String defacerName;

    public DefaceTask(NIHImage image) throws RuntimeException {
        super();

        setDefacerName();
        
        this.currentImage = image;
        File outDir = new File(outputDir);
        if (!outDir.exists()) {
            outDir.mkdirs();
        }

        inputImages = new Vector<>();
        String defacerLocation = null;
        if(defacerName.equals("bet.exe")){
            defacerLocation = DeidData.unpackedFileLocation.get(defacerName).getAbsolutePath();
        } else if(defacerName.equals("runROBEX.bat")){
            defacerLocation = DeidData.unpackedFileLocation.get(defacerName).getParentFile().getAbsolutePath() + "\\runROBEX.bat";
        }
        command = new String[]{
            defacerLocation,
            // Depends on imtest and 
            // Requires fsl config file, change default output to NIFTI
            //Usage: 
            //bet2 <input_fileroot> <output_fileroot> [options]
            //Optional arguments (You may optionally specify one or more of):
            //-o,--outline	generate brain surface outline overlaid onto original image
            //-m,--mask	generate binary brain mask
            //-s,--skull	generate approximate skull image
            //-n,--nooutput	don't generate segmented brain image output
            //-f <f>		fractional intensity threshold (0->1); default=0.5; smaller values give larger brain outline estimates
            //-g <g>		vertical gradient in fractional intensity threshold (-1->1); default=0; positive values give larger brain outline at bottom, smaller at top
            //-r,--radius <r>	head radius (mm not voxels); initial surface sphere is set to half of this
            //-w,--smooth <r>	smoothness factor; default=1; values smaller than 1 produce more detailed brain surface, values larger than one produce smoother, less detailed surface
            //-c <x y z>	centre-of-gravity (voxels not mm) of initial mesh surface.
            //-t,--threshold	-apply thresholding to segmented brain image and mask
            //-e,--mesh	generates brain surface as mesh in vtk format
            //-v,--verbose	switch on diagnostic messages
            //-h,--help	displays this help, then exits
            "input", outputDir + "filename", "-f", DeidData.defaceThreshold};
    }
    
    protected void setDefacerName(){
        defacerName = "bet.exe";
    }

    /**
     * Assign a progress bar to be updated by this conversion task.
     *
     * @param bar The JProgressBar that will display the conversion's progress
     */
    public void setProgressBar(JProgressBar bar) {
        progressBar = bar;
    }

    public void setTextfield(JTextArea field) {
        detailText = field;
    }
    
//    public void appendTextfield(String toAppend){
//        detailText.append(toAppend);
//    }

    /**
     * Add directories with DICOM images to be converted by this task.
     *
     * @param file File object representing a directory that contains .dcm
     * images
     */
    public void setInputImages(Vector<NIHImage> files) {
        inputImages = files;
        images = files.size();
    }

    @Override
    public void run() {

        String errorPatternStr = "ERROR";
        Pattern errorPattern = Pattern.compile(errorPatternStr);

        // Set input directory for bet
        detailText.append("Begin defacing #" + currentImage.getImageName() + " image:\n");
        command[1] = currentImage.getStoredPotistion().getAbsolutePath();
  
        String outFilename = outputDir + currentImage.getImageNewName() + ".hdr";
        System.out.println(outFilename);
        command[2] = outFilename;
        // Overwrites existing files

        System.out.print("Command " + currentImage.getImageName() + " :");
        for (int i = 0; i < command.length; i++) {
            if(i != command.length - 1){
                System.out.println(command[i] + " ");
            }
        }
        System.out.println();

        // Capture output of the bet process
        java.lang.ProcessBuilder pb = new ProcessBuilder(command);
        pb.directory(DeidData.unpackedFileLocation.get(defacerName).getParentFile());
        pb.redirectErrorStream(true);
        Process defaceProc = null;
        boolean fileValid = true;
        try {
            defaceProc = pb.start();
        } catch (IOException ex) {
            DEIDGUI.log(defacerName + " couldn't be started: " + ex.getMessage(), DEIDGUI.LOG_LEVEL.ERROR);
        }
        BufferedReader br = new BufferedReader(new InputStreamReader(defaceProc.getInputStream()));
        String line;
        
      try {
           while ((line = br.readLine()) != null && !line.isEmpty()) {
                //System.out.println(line); // Print bet output
                //Matcher errorMatcher = errorPattern.matcher(line);
                /*if(!defacerName.equals("bet.exe")){
                    if (errorMatcher.find()) {
                       
                        // Sample bet errors:
                        // ** ERROR (nifti_image_read): failed to find header file for './input2'
                        //** ERROR: nifti_image_open(./input2): bad header info
                        //ERROR: failed to open file ./input2
                        //ERROR: Could not open image ./input2
                        //Image Exception : #22 :: Failed to read volume ./input2.nii
                        //terminate called after throwing an instance of 'RBD_COMMON::BaseException'
                        //Abort trap

                        DEIDGUI.log(defacerName + " error occurred: " + line, DEIDGUI.LOG_LEVEL.ERROR);
                        fileValid = false;
                    } else {
                        DEIDGUI.log("unexpected bet output: " + line, DEIDGUI.LOG_LEVEL.WARNING);
                    }
                }
               **/
          }
       } catch (IOException ex) {
       }
        
        if (fileValid) {
            File newFile = new File(outFilename); 
            // File defaceSource = inputImages.get(ndx);
            currentImage.setTempPotision(newFile);
                // If the image was associated wit h a dicom, pair the 
            // new file with that source.
              /*
             if(DeidData.NiftiConversionSourceTable.containsKey(defaceSource)){
             DeidData.NiftiConversionSourceTable.put(newFile, 
             DeidData.NiftiConversionSourceTable.get(defaceSource));
             DeidData.NiftiConversionSourceTable.remove(defaceSource);
             }
             */
            // Add the image to deientified files list.
            System.out.println("Deface File:" + newFile.getAbsolutePath());
            currentImage.setIsDefaced(true);
            //  DeidData.deidentifiedFiles.add(newFile);
            count++;
            //if (count == inputImages.size()){
            //    count = 0;
            //}
        }
        

        float processProgress = (float) count / (float) images;
        if (progressBar != null) {
            progressBar.setValue((int) (100 * processProgress));
        }
        
        detailText.append("Success!\n\n");
        

        // Advance to next panel
        synchronized (this) {
            this.notify();
        }
    }
}
