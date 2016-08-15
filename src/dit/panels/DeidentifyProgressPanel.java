package dit.panels;

import dit.DEIDGUI;
import dit.DEIDGUI.LOG_LEVEL;
import dit.DefaceTask;
import dit.DefaceTaskinWindows;
import dit.DeidData;
import dit.DemographicTableModel;
import dit.FileUtils;
import dit.GZipFile;
import dit.IDefaceTask;
import dit.LoggerWrapper;
import dit.NIHImage;
import dit.OrientationState;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.GZIPInputStream;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import niftijlib.Nifti1Dataset;
import org.dcm4che2.data.DicomElement;
import org.dcm4che2.data.DicomObject;
import org.dcm4che2.io.DicomInputStream;
import org.dcm4che2.io.DicomOutputStream;
import org.dcm4che2.util.StringUtils;
import org.dcm4che2.util.TagUtils;

/**
 *
 * @author christianprescott & angelo
 */
public class DeidentifyProgressPanel extends javax.swing.JPanel implements WizardPanel {

    private String outputPath = DeidData.outputPath;
    private String gzFileOutputDir = DeidData.outputPath + "betOut" + File.separator;
    private boolean doDeface;
    private static int count = 1;
    private static HashMap<String, Integer> idOccurrences = new HashMap<String, Integer>();
    private static HashMap<String, NIHImage> idOriginalFile = new HashMap<String, NIHImage>();
    private Vector<NIHImage> fullNiiImages;
    
    ////////////////////////////////////////////////////////////
    ////////2.13
    //private static ArrayList<Image> idMontage;  //image for id montage
    //private static ArrayList<Image> deidMontage;  //image for id montage
    public static int cellHeight, cellWidth;    //height and weight for single montage image, init in Constructor. 
    /**
     * Creates new form DeidentifyProgressPanel
     */
    public DeidentifyProgressPanel(boolean doDeface) {
        initComponents();
        cellHeight = 210;
        cellWidth = 196;
        DEIDGUI.helpButton.setEnabled(false);
        DEIDGUI.continueButton.setEnabled(false);
        DEIDGUI.backButton.setEnabled(false);
        this.doDeface = doDeface;
        //  DeidData.IdTable = new Hashtable<String, String>();
        DEIDGUI.log("DeidentifyProgressPanel initialized");
        fullNiiImages = new Vector<NIHImage>();
        //idMontage = new ArrayList<Image>();
        //deidMontage = new ArrayList<Image>();
        
        startDeidentification();
     
        //writeMontageImage();
        
    }
    /////////////////////////////////////////////////////////////
    /////////2.13
    //Write montage&info to outpath.  the file should contain 
    //two montage,
    //if we have data match, the 2nd-4th col information contains in datafile
   /*
    private void writeMontageImage(){
      int idImageNum = deidMontage.size();
      int wholeWidth = 2*cellWidth;
      int wholeHeight = idImageNum*cellHeight;
      BufferedImage result = new BufferedImage(wholeWidth, wholeHeight,BufferedImage.TYPE_INT_BGR);
      Graphics g = result.getGraphics();
      for(int i = 0; i < idImageNum; i++){
        Image idtmp = getIdMongtage(i);
        g.drawImage(idtmp, 0, i*cellHeight, cellWidth , cellHeight, null);
        Image deidtmp = getDeidMongtage(i);
        g.drawImage(deidtmp, cellWidth, i*cellHeight, cellWidth , cellHeight, null);
      }
      try {
        File targetFile = new File(DeidData.outputPath + "montage.jpg");
        ImageIO.write(result, "jpg", targetFile);
        System.out.println("Suscessful create Montage file!");
        //image.setMontageFile(targetFile);
        } catch (IOException ex) {
            DEIDGUI.log("Unable to write montage.png",
                    DEIDGUI.LOG_LEVEL.ERROR);
        }
    }
    
    public static Image getIdMongtage(int n){
      return idMontage.get(n);
    }
    
    public static Image getDeidMongtage(int n){
      return deidMontage.get(n);
    }
    */
    
    
   
    
    
    
    
    
    
    
    private void filterGZFiles() {
        for (NIHImage image : DeidData.imageHandler.getInputFiles()) {
            txtDetail.append("Preprocessing image " + image.getImageName() + " \n");
            if (image.getImageFormat().equals("nii.gz")) {
                //String newGZFile = new String();
                //newGZFile = gzFileOutputDir + image.getImageFormalName() + "nii";
                ///File in = new File(image.getStoredPotistion().getPath());
                ///File out = new File(image.getStoredPotistion().getPath().replace(".gz",""));
                String gzFileName = image.getStoredPotistion().getPath();
                File in = new File(gzFileName);
                //File out = new File(gzFileName.replace(".gz",""));
                File dir = new File(gzFileOutputDir);
                //if(!dir.exists()){
                    dir.mkdir();
                //}
                //
                File out = new File(gzFileOutputDir + image.getImageFormalName() + ".nii");
                try{
                    GZIPInputStream gzFile = new GZIPInputStream(new FileInputStream(in));
                    FileOutputStream outStream = new FileOutputStream(out);
                    byte[] buf = new byte[102400];
                    int len;
                    while ((len = gzFile.read(buf)) > 0) { 
                        outStream.write(buf, 0, len);
                    }
                } catch(IOException ex) {
                    ex.printStackTrace();   
                }
                System.out.println(out.getPath() + " has been unzipped.");
                System.out.println(in.getPath());
                System.out.println("has been unzipped. New file:");
                System.out.println(out.getPath());
                //image = new NIHImage(out);
                image.setStoredPotistion(out);
                image.setImageDisplayName(image.getStoredPotistion().getAbsolutePath());
                image.setImageFormat(FileUtils.getExtension(out));
                ///System.out.println(image.getImageFormat());
                ///gzFile.gunzipIt();
                //String originalGZZFile = image.getStoredPotistion().getAbsoluteFile().getPath();
                //System.out.println("\noriginal position:\n" + originalGZZFile);
                //String newGZFile = image.getImageName() + "nii";
                //String newGZFile = gzFileOutputDir + FileUtils.getName(new 
                //        File(image.getImageName())) + ".nii";
                //System.out.println("\nnew position:\n" + newGZFile);
                
                    
                //try {
                //    FileInputStream fis = new FileInputStream(originalGZZFile);
                //    GZIPInputStream gis = new GZIPInputStream(fis);
                //    FileOutputStream fos = new FileOutputStream(newGZFile);
                //    byte[] buffer = new byte[1024];
                //    int len;
                //    while ((len = gis.read(buffer))  != -1) {
                //        fos.write(buffer, 0, len);
                //    }
                    //close resources
                    //gis.close();
                //} catch (IOException e) {
                //    DEIDGUI.log("Failed :"+ e.getMessage(), DEIDGUI.LOG_LEVEL.WARNING);
                //}
                //image = new NIHImage();//= new NIHImage(new File(image.getStoredPotistion().getPath().replace(".gz", "")));
                fullNiiImages.add(image);
            } else {
                File dir = new File(gzFileOutputDir);
                dir.mkdir();
                fullNiiImages.add(image);
            }
        }
    }
        
    
    
    
     
    
    /**
     * 
     * convert nii to analyze
     * 
     */
    private void convertToAnalyze(NIHImage image) {
        
       /** final int HEADERSIZE = 348;
        final int SKIPSIZE = 4;      
       
              
            try {
                gzFileOutputDir = DeidData.outputPath + "fuck";
                FileInputStream  fin = new FileInputStream(image.getStoredPotistion());
                File outHeader = new File(gzFileOutputDir + image.getImageFormalName() + ".hdr");
                FileOutputStream foutHeader =  new FileOutputStream(outHeader);
                byte header [] = new byte[HEADERSIZE];
                fin.read(header);
                System.out.println(Arrays.toString(header));
                header[header.length-3] = 'i';
                System.out.println(Arrays.toString(header));
                foutHeader.write(header);
                            
                File outImage = new File(gzFileOutputDir + image.getImageFormalName() + ".img");
                FileOutputStream foutImage =  new FileOutputStream(outImage);
                fin.skip(SKIPSIZE);
              
              //  fin = new FileInputStream(image.getStoredPotistion());
                byte[] buf = new byte[102400];
               
                int len;
                while ((len = fin.read(buf)) > 0) { 
                    foutImage.write(buf, 0, len);
                }
                
                image.setStoredPotistion(outHeader);
                image.setImageDisplayName(image.getStoredPotistion().getAbsolutePath());
                image.setImageFormat(FileUtils.getExtension(outHeader));
                
                
                
            } 
            catch(IOException ex) {
                ex.printStackTrace();   
            }
            */
        
        
           
           
                String outPath = gzFileOutputDir + "tmp" + image.getImageFormalName();
             
          //  image.setAPI.printHeader()
                try {
                    if (image.getStoredPotistion().getName().endsWith(".nii")) {
                        image.initImage();
                        image.setAPI.readHeader();
                        image.setAPI.magic = new StringBuffer("ni1\0");
                   // System.out.println(image.setAPI.magic.length());
                        byte[] data = image.setAPI.readData();
                        image.setAPI.ds_hdrname = outPath + ".hdr";
                        image.setAPI.ds_is_nii = false;
                        image.setAPI.vox_offset = 0;
                        image.setAPI.writeHeader();
                        image.setAPI.ds_datname = outPath + ".img";
                        image.setAPI.writeData(data);
                        image.setStoredPotistion(new File(outPath  + ".hdr"));
                        image.setImageDisplayName(image.getStoredPotistion().getAbsolutePath());
                    }

                   // image.setImageFormat(FileUtils.getExtension());


                } catch (IOException ex) {
                    Logger.getLogger(DeidentifyProgressPanel.class.getName()).log(Level.SEVERE, null, ex);
                }

            
        

    }
    
    
    private void qcGenerate() {
        
            for(NIHImage image : fullNiiImages){
                //if (!image.isIsDefaced()) {
                    image.setTempPotision(new File(image.getStoredPotistion().getAbsolutePath()));
                    image.initNifti();
                    
                    Image tmp = createIdMontage(image);
                //idMontage.add(createIdMontage(image));
                    image.emptySet();
                    System.out.println("suscessful create idM");
                //}
            }
    }
    
         
    
    
    
    ////////////////////////////////////////////////////////////////////////////
    ///////////////////2.13
   //just for get (old)id-image montage. 
   //also save the image in both NIHimage.
    private Image createIdMontage(NIHImage image) {
        
        //int cellWidth = 196, textHeight = 12, cellHeight = 196, rowHeight = cellHeight + textHeight;
        int  textHeight = 12, rowHeight = cellHeight + textHeight;
        int montageWidth = cellWidth;
        int montageHeight = (1 / 4 + 1) * rowHeight;
        BufferedImage i = new BufferedImage(montageWidth, montageHeight, BufferedImage.TYPE_INT_RGB);
        int rowCounter = 0;
        int colCounter = 0;
           
         try {
            DeidData.imageHandler.correctOrientation(image);
            BufferedImage ii = image.imageAt(0.5f, image.getOrientationState());
            Graphics2D g = i.createGraphics();
            g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            //Font f = new Font(Font.MONOSPACED, Font.PLAIN, 10);
            g.setColor(Color.WHITE);
            //g.setFont(f);
            g.drawImage(ii, cellWidth * colCounter, rowCounter / 4 * rowHeight + 12, cellWidth, cellHeight, null);
            //g.drawString(image.getImageNewName(), cellWidth * colCounter + 2, rowCounter / 4 * cellHeight + textHeight);
            rowCounter++;
            colCounter = (colCounter + 1) % 4;
        } catch (IOException ex) {
            Logger.getLogger(DeidentifyProgressPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
        image.setIdMontage(i);
        return i;
    }
   
    private void startDeidentification() {
        
      
        
        new Thread(new Runnable() {
            @Override
            public void run() {
           
                try {   
                        LoggerWrapper.myLogger.log(Level.INFO, "Before deidentifying:  Max:{0}  Free:{1}  available:{2}", new Object[]{java.lang.Runtime.getRuntime().maxMemory(), java.lang.Runtime.getRuntime().freeMemory(), java.lang.Runtime.getRuntime().totalMemory()});
        
                        filterGZFiles();
                       
                         
                        //qcGenerate();
                        /////by
                        count = 0;
                        randomizeIds();
                        System.out.println("after randomizeIDs");
                       
                        for (NIHImage image : fullNiiImages) {
                              // create id montage
                            image.setTempPotision(new File(image.getStoredPotistion().getAbsolutePath()));
                            image.initNifti();

                            Image tmp = createIdMontage(image);
                            txtDetail.append("Create montage for " + image.getImageName() + " \n");
                            
                            //idMontage.add(createIdMontage(image));
                            image.emptySet();
                            System.out.println("suscessful create idM");
      
                            System.out.println("\nprocessing in " + count);
                            System.out.println("Max:"+ java.lang.Runtime.getRuntime().maxMemory()+"  Free:"+java.lang.Runtime.getRuntime().freeMemory()+"  available:"+java.lang.Runtime.getRuntime().totalMemory());
                            System.out.println("randomize id.");
                            
                            System.out.println("deface image."); 
                            if (doDeface) {
                                System.out.println("dodeface is true"); 
                                jLabel2.setText("<html><p>Defacing images...</p><p>&nbsp;</p></html>");

                                if (count % 10 == 0)
                                    LoggerWrapper.myLogger.log(Level.INFO, "Max:{0}  Free:{1}  available:{2}", new Object[]{java.lang.Runtime.getRuntime().maxMemory(), java.lang.Runtime.getRuntime().freeMemory(), java.lang.Runtime.getRuntime().totalMemory()});
                                convertToAnalyze(image);
                                defaceImages(image);
                            

                            } else {
                                System.out.println("dodeface is false"); 
                                DeidData.imageHandler.moveImages();
                            }

                        
                            
                            
                        
                            System.out.println("I died here");

                            //jLabel2.setText("<html><p>Deidentifying demographic file...</p><p>&nbsp;</p></html>");
                            System.out.println("create demographic file.");
                            if(count == 0){
                                createDemographicFile(image);
                            } else{
                                appendDemographicFile(image);
                            }
                            System.out.println("create header data files.");
                            if (DeidData.NiftiConversionSourceTable.size() > 0) {
                                jLabel2.setText("<html><p>Deidentifying DICOM header data...</p><p>&nbsp;</p></html>");
                                createHeaderDataFiles();
                            }

                            System.out.println("create montage.");
                            //montage created here
                            DeidData.imageHandler.correctOrientation(image);

                           // createMontage(image);
                            count++;
                            //if (count == DeidData.imageHandler.getInputFiles().size()) {
                            //    count = 1;
                            //}

                            image.clearData();
                        }
                    }
                catch (OutOfMemoryError E) {
                    LoggerWrapper.myLogger.log(Level.SEVERE, "Out of memory!!!! at images {0}, Max:{1}  Free:{2}  available:{3}", new Object[]{count, java.lang.Runtime.getRuntime().maxMemory(), java.lang.Runtime.getRuntime().freeMemory(), java.lang.Runtime.getRuntime().totalMemory()});
                    
                    DEIDGUI.log("Failed to advance: " + E.getMessage(), LOG_LEVEL.ERROR);
                }
                
                DefaceTask.count = 0;
                DEIDGUI.advance();
            }

        }).start();
    }
    
    
    
    /* Dr. Eckert's Image ID Cipher
     * image id = <site initials as numeric string>_<name initials as numeric string>_<random 4 digits>
     * i.e. 1234_5678_2301 or 1234_5678_4598
     * Utilizes an alphabet with consonants swapped around vowels and then the
     * transposed alphabet is numbered 01-26, and then used with the users site
     * and initials information */
    private final char[] CipherAlphabet = "bacfedgjihklmponqrsvutwxyz".toCharArray();

    private void randomizeIds(){
        // Only randomize if the ID column is selected
        for (NIHImage image : DeidData.imageHandler.getInputFiles()) {
            image.setImageNewName("");
        }

        // Build the base ID using the cipher
        String baseId = "";
        String[] institutionBits = DeidData.UserInstitution.split("\\s");
        for (String s : institutionBits) {
            if (s.length() > 0) {
                // The alphabet is not sorted, so a search will not work.
                char firstLetter = s.toLowerCase().charAt(0);
                for (int ndx = 0; ndx < CipherAlphabet.length; ndx++) {
                    if (CipherAlphabet[ndx] == firstLetter) {
                        baseId += String.format("%02d", ndx);
                    }
                }
            }
        }
        baseId += "_";
        String[] names = DeidData.UserFullName.split("\\s");
        for (String s : names) {
            if (s.length() > 0) {
                // The alphabet is not sorted, so a search will not work.
                char firstLetter = s.toLowerCase().charAt(0);
                for (int ndx = 0; ndx < CipherAlphabet.length; ndx++) {
                    if (CipherAlphabet[ndx] == firstLetter) {
                        baseId += String.format("%02d", ndx);
                    }
                }
            }
        }
        baseId += "_";

        if (DeidData.isNoData) {
            for (NIHImage file : DeidData.imageHandler.getInputFiles()) {
                DeidData.imageHandler.assignID(file, "r" + baseId);
            }
        } else {
            String idIdentifier = "Filename and "
                    + DeidData.demographicData.getColumnName(DeidData.IdColumn);
            String[] omissions = DeidData.selectedIdentifyingFields;
            Arrays.sort(omissions);
            boolean randomizeFilename = (Arrays.binarySearch(
                    omissions, idIdentifier) >= 0);
            //If the id column is selected
            if (randomizeFilename) {
                for (NIHImage image : DeidData.imageHandler.getInputFiles()) {
                    DeidData.imageHandler.assignID(image, baseId);
                }
            }

        }
       
        DEIDGUI.log("Randomized file IDs");
    }

/*    private void defaceImages() {
        try {
            IDefaceTask defaceTask = null;
            if (FileUtils.OS.isWindows()) {
                defaceTask = new DefaceTaskinWindows();
            } else {
                defaceTask = new DefaceTask();
            }
            defaceTask.setProgressBar(jProgressBar1);
            defaceTask.setTextfield(txtDetail);
            defaceTask.setInputImages(DeidData.imageHandler.getInputFiles());
            System.out.println("Total:" + DeidData.imageHandler.getInputFilesSize());
            synchronized (defaceTask) {
                new Thread((Runnable) defaceTask).start();
                try {
                    defaceTask.wait();
                } catch (InterruptedException ex) {
                    DEIDGUI.log("bet was interrupted, the defacing result may "
                            + "be incorrect", DEIDGUI.LOG_LEVEL.WARNING);
                }
            }

            DEIDGUI.log("Defaced images");
        } catch (RuntimeException e) {
            e.printStackTrace();
            DEIDGUI.log("Defacing couldn't be started: " + e.getMessage(),
                    DEIDGUI.LOG_LEVEL.ERROR);
        }
    }    
*/
    private void defaceImages(NIHImage image) {
        try {
            IDefaceTask defaceTask = null;
            if (FileUtils.OS.isWindows()) {
                System.out.println("windows? ");
                defaceTask = new DefaceTaskinWindows(image);
            } else {
                defaceTask = new DefaceTask(image);
            }
//            defaceTask.appendTextfield("Defacing image...");
            defaceTask.setProgressBar(jProgressBar1);
            defaceTask.setTextfield(txtDetail);
            defaceTask.setInputImages(DeidData.imageHandler.getInputFiles());
            System.out.println("Total:" + DeidData.imageHandler.getInputFilesSize());
            synchronized (defaceTask) {
               new Thread((Runnable) defaceTask).start();
                System.out.println("tracepoint 1 "); 
                try {
                
                    defaceTask.wait();
                } catch (InterruptedException ex) {
                    System.out.println("tracepoint 3");
                    DEIDGUI.log("bet was interrupted, the defacing result may "
                            + "be incorrect", DEIDGUI.LOG_LEVEL.WARNING);
                }
                System.out.println("tracepoint 4 ");
            }
            
            /**  synchronized (defaceTask) {
                Thread abc = new Thread((Runnable) defaceTask);
                abc.start();
                System.out.println("tracepoint 1 "); 
                try {
                    defaceTask.wait();
                    abc.join();
                    
                } catch (InterruptedException ex) {
                    System.out.println("tracepoint 3");
                    DEIDGUI.log("bet was interrupted, the defacing result may "
                            + "be incorrect", DEIDGUI.LOG_LEVEL.WARNING);
                }
                System.out.println("tracepoint 4 ");
            }
            
            **/
            
            
            
            
            
            
            
//           defaceTask.appendTextfield(" complete!\n");
            DEIDGUI.log("Defaced images");
        } catch (RuntimeException e) {
            
            e.printStackTrace();
            DEIDGUI.log("Defacing couldn't be started: " + e.getMessage(),
                    DEIDGUI.LOG_LEVEL.ERROR);
        }
    }

    private void appendDemographicFile(NIHImage image){
        String[] headings = DeidData.demographicData.getDataFieldNames();
        String[] omissions = DeidData.selectedIdentifyingFields;
        Arrays.sort(omissions);
        boolean[] omit = new boolean[headings.length];

        // Find set of identifying columns to omit
        int omitCount = 0;
        for (int ndx = 0; ndx < headings.length; ndx++) {
            String fieldName = headings[ndx];
            if (ndx == DeidData.IdColumn) {
                fieldName = "Filename and "
                        + DeidData.demographicData.getColumnName(DeidData.IdColumn);
            }
            if (Arrays.binarySearch(omissions, fieldName) >= 0) {
                omit[ndx] = true;
                omitCount++;
            } else {
                omit[ndx] = false;
            }
        }

        if (omitCount != DeidData.selectedIdentifyingFields.length) {
            DEIDGUI.log("Some identifying fields weren't found (" + omitCount
                    + "/" + DeidData.selectedIdentifyingFields.length + "). "
                    + "The demographic data may not be deidentified properly",
                    DEIDGUI.LOG_LEVEL.WARNING);
        }

        File newDemoFile = new File(outputPath + "Demographics_Behavioral.txt");
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(newDemoFile, true));
            for (int ndx = 0; ndx < DeidData.demographicData.getRowCount(); ndx++) {
                //if(ndx == DeidData.demographicData.getRowCount()-1){
                    Object[] row = DeidData.demographicData.getRow(ndx);
                    String[] rowS = Arrays.copyOf(row, row.length, String[].class);

                    if (!image.getIdInDataFile().equals(rowS[DeidData.IdColumn])) {
                        continue;
                    }
                    for (int colNdx = 0; colNdx < row.length; colNdx++) {
                        if (!omit[colNdx]) {
                            writer.write(rowS[colNdx] + "\t");
                        } else if (colNdx == DeidData.IdColumn) {
                        // The ID is a special case - it must be included in its
                        // randomized form.
                        // writer.write(DeidData.IdTable.get(rowS[colNdx]) + "\t");
                            writer.write(image.getImageNewName() + "\t");
                        }
                    }
                    writer.newLine();
                //}
            }
        } catch (IOException ex) {
            DEIDGUI.log("Couldn't write deidentified demographic file: "
                    + ex.getMessage(), DEIDGUI.LOG_LEVEL.WARNING);
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException ex) {
                }
            }
        }
        DeidData.deidentifiedDemoFile = newDemoFile;

        DEIDGUI.log("Created anonymized demographic file");
    }
    
    private void createDemographicFile(NIHImage image) {
        String[] headings = DeidData.demographicData.getDataFieldNames();
        String[] omissions = DeidData.selectedIdentifyingFields;
        Arrays.sort(omissions);
        boolean[] omit = new boolean[headings.length];

        // Find set of identifying columns to omit
        int omitCount = 0;
        for (int ndx = 0; ndx < headings.length; ndx++) {
            String fieldName = headings[ndx];
            if (ndx == DeidData.IdColumn) {
                fieldName = "Filename and "
                        + DeidData.demographicData.getColumnName(DeidData.IdColumn);
            }
            if (Arrays.binarySearch(omissions, fieldName) >= 0) {
                omit[ndx] = true;
                omitCount++;
            } else {
                omit[ndx] = false;
            }
        }

        if (omitCount != DeidData.selectedIdentifyingFields.length) {
            DEIDGUI.log("Some identifying fields weren't found (" + omitCount
                    + "/" + DeidData.selectedIdentifyingFields.length + "). "
                    + "The demographic data may not be deidentified properly",
                    DEIDGUI.LOG_LEVEL.WARNING);
        }

        File newDemoFile = new File(outputPath + "Demographics_Behavioral.txt");
        BufferedWriter writer = null;
        try {
            newDemoFile.createNewFile();
            writer = new BufferedWriter(new FileWriter(newDemoFile, false));

            // Write headings
            for (int colNdx = 0; colNdx < headings.length; colNdx++) {
                if (!omit[colNdx] || colNdx == DeidData.IdColumn) {
                    // The ID is a special case - heading must be included even
                    // when omitted (in randomized form)
                    writer.write(headings[colNdx] + "\t");
                }
            }
            writer.newLine();

            for (int ndx = 0; ndx < DeidData.demographicData.getRowCount(); ndx++) {

                Object[] row = DeidData.demographicData.getRow(ndx);
                String[] rowS = Arrays.copyOf(row, row.length, String[].class);

                if (!image.getIdInDataFile().equals(rowS[DeidData.IdColumn])) {
                    continue;
                }
                for (int colNdx = 0; colNdx < row.length; colNdx++) {

                    if (!omit[colNdx]) {
                        writer.write(rowS[colNdx] + "\t");
                    } else if (colNdx == DeidData.IdColumn) {
                        // The ID is a special case - it must be included in its
                        // randomized form.
                        // writer.write(DeidData.IdTable.get(rowS[colNdx]) + "\t");

                        writer.write(image.getImageNewName() + "\t");
                    }
                }
                writer.newLine();
            }
        } catch (IOException ex) {
            DEIDGUI.log("Couldn't write deidentified demographic file: "
                    + ex.getMessage(), DEIDGUI.LOG_LEVEL.WARNING);
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException ex) {
                }
            }
        }
        DeidData.deidentifiedDemoFile = newDemoFile;

        DEIDGUI.log("Created anonymized demographic file");
    }

    private void createHeaderDataFiles() {
        Iterator<Entry<File, File>> it = DeidData.NiftiConversionSourceTable.entrySet().iterator();
        String newline = System.getProperty("line.separator");
        DEIDGUI.log("Creating " + DeidData.NiftiConversionSourceTable.size() + " header data files");
        while (it.hasNext()) {
            File hdrFile = null;
            try {
                Entry<File, File> curSet = it.next();
                // Create new file or overwrite existing
                String imageName = FileUtils.getName(curSet.getKey());
                if (DeidData.IdTable.containsKey(imageName)) {
                    imageName = DeidData.IdTable.get(imageName);
                }
                hdrFile = new File(outputPath + imageName + "_hdr_varNames.txt");
                hdrFile.createNewFile();
                DeidData.ConvertedDicomHeaderTable.put(curSet.getKey(), hdrFile);
                BufferedWriter writer = null;
                String[][] metadata = readDicomMetadata(curSet.getValue(), false);
                try {
                    writer = new BufferedWriter(new FileWriter(hdrFile, false));
                    for (int ndx = 0; ndx < metadata.length; ndx++) {
                        int tagNdx = Arrays.binarySearch(DeidData.dicomVarIds, metadata[ndx][0]);
                        if (tagNdx >= 0) {
                            String[] element = metadata[ndx];
                            String[] tagHalves = StringUtils.split(element[0], ',');
                            String formattedTag = "\"" + tagHalves[0] + ",\"\t" + tagHalves[1];
                            // element[1] contains the name as read from the
                            // DICOM. The name as provided by Dr. Mark Eckert
                            // is preferred.
                            String name = DeidData.dicomVarNames[tagNdx],
                                    vr = element[2],
                                    value = element[3];
                            String tab = "\t";
                            writer.write(formattedTag + tab + name + tab
                                    + vr + tab + value + newline);
                        }
                    }
                } catch (IOException e) {
                    DEIDGUI.log("Couldn't write header data to "
                            + hdrFile.getAbsolutePath(), DEIDGUI.LOG_LEVEL.WARNING);
                } finally {
                    if (writer != null) {
                        try {
                            writer.close();
                        } catch (IOException ex) {
                        }
                    }
                }
            } catch (IOException ex) {
                if (hdrFile != null) {
                    DEIDGUI.log("Couldn't write header data to "
                            + hdrFile.getAbsolutePath(), DEIDGUI.LOG_LEVEL.WARNING);
                }
            }
        }
        DEIDGUI.log("Created header files");
    }

    //need modification in the future
    //I help you modify it.
    ///////////////////2.13
    //save in both NIHImage image and the Vector
    /*
    private void createMontage(NIHImage image) {

        //int cellWidth = 196, textHeight = 12, cellHeight = 196, rowHeight = cellHeight + textHeight;
        int textHeight = 12, rowHeight = cellHeight + textHeight;
        int montageWidth = cellWidth;
        int montageHeight = (1 / 4 + 1) * rowHeight;

//        if (DeidData.imageHandler.getInputFiles().isEmpty()) {
//            return;
//        }
        BufferedImage i = new BufferedImage(montageWidth, montageHeight, BufferedImage.TYPE_INT_RGB);
        int rowCounter = 0;
        int colCounter = 0;
        System.out.println("fffffffffffff :   " + image.getTempPotision().getAbsolutePath());
        //Nifti1Dataset set = new Nifti1Dataset(image.getTempPotision().getAbsolutePath());
        //image.inifti();

        try {
            BufferedImage ii = image.imageAt(0.5f, image.getOrientationState());
            Graphics2D g = i.createGraphics();
            g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            Font f = new Font(Font.MONOSPACED, Font.PLAIN, 10);
            g.setColor(Color.WHITE);
            g.setFont(f);
            g.drawImage(ii, cellWidth * colCounter, rowCounter / 4 * rowHeight + 12, cellWidth, cellHeight, null);
            g.drawString(image.getImageNewName(), cellWidth * colCounter + 2, rowCounter / 4 * cellHeight + textHeight);
            rowCounter++;
            colCounter = (colCounter + 1) % 4;
        } catch (IOException ex) {
            Logger.getLogger(DeidentifyProgressPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
        image.setDeidMontage(i);
        deidMontage.add(i);
    }
    */

    private String[][] readDicomMetadata(File dicomFile, boolean anonymizeFile) {
        DicomInputStream dis = null;
        ArrayList<String[]> metadataList = new ArrayList<String[]>();
        try {
            // Open and read the DICOM image
            dis = new DicomInputStream(dicomFile);
            DicomObject dicomObject = dis.readDicomObject();

            // Iterate over metadata elements
            Iterator<DicomElement> metadataIt = dicomObject.iterator();
            while (metadataIt.hasNext()) {
                // Read element data
                DicomElement elem = metadataIt.next();
                String elemName = dicomObject.nameOf(elem.tag()),
                        elemTag = TagUtils.toString(elem.tag()),
                        elemVR = dicomObject.vrOf(elem.tag()).toString(),
                        elemValue = "";
                try {
                    if (dicomObject.vm(elem.tag()) != 1) {
                        String[] elemValues = dicomObject.getStrings(elem.tag());
                        elemValue = StringUtils.join(elemValues, '\\');
                    } else {
                        elemValue = dicomObject.getString(elem.tag());
                    }
                } catch (UnsupportedOperationException e) {
                    // Only alert if the element is one that we want to keep
                    if (Arrays.binarySearch(DeidData.dicomVarIds, elemTag) >= 0) {
                        DEIDGUI.log("Couldn't get value of desired DICOM element "
                                + elemTag + " \"" + elemName + "\". The image "
                                + "header file may be incomplete.", DEIDGUI.LOG_LEVEL.WARNING);
                    }
                }

                metadataList.add(new String[]{
                    elemTag,
                    elemName,
                    elemVR,
                    elemValue});
                if (anonymizeFile && Arrays.binarySearch(DeidData.dicomVarIds, elemTag) < 0) {
                    // Anonymize other elements
                    dicomObject.remove(elem.tag());
                }
            }

            if (anonymizeFile) {
                // Save anonymized file
                // TODO: Save this file in a temporary location - the original
                // file should be left intact.
                // TODO: Missing (0002,0010) Transfer Syntax UID, unable to
                // save file. Find out which data are required to save a DICOM.
                DicomOutputStream dos = new DicomOutputStream(new File("/Users/christianprescott/Desktop/anon.dcm"));//dicomFile);
                dos.writeDicomFile(dicomObject);
            }
        } catch (IOException ex) {
            DEIDGUI.log("Couldn't read DICOM object " + dicomFile.getAbsolutePath()
                    + ". The image header file may be incomplete.", DEIDGUI.LOG_LEVEL.WARNING);
        } finally {
            try {
                if (dis != null) {
                    dis.close();
                }
            } catch (IOException ex) {
            }
        }

        String[][] metadataArray = new String[metadataList.size()][metadataList.get(0).length];
        metadataList.toArray(metadataArray);
        return metadataArray;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel2 = new javax.swing.JLabel();
        jProgressBar1 = new javax.swing.JProgressBar();
        txtSummary = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtDetail = new javax.swing.JTextArea();

        jLabel2.setText("<html><p>Deidentifying image IDs...</p><p>&nbsp;</p></html>");

        txtSummary.setEditable(false);
        txtSummary.setText("This process may take several minutes and  may not be responding. \n");
        txtSummary.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtSummaryActionPerformed(evt);
            }
        });

        txtDetail.setColumns(20);
        txtDetail.setLineWrap(true);
        txtDetail.setRows(5);
        txtDetail.setEnabled(false);
        jScrollPane1.setViewportView(txtDetail);

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jProgressBar1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 388, Short.MAX_VALUE)
                    .add(txtSummary, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .add(layout.createSequentialGroup()
                        .add(jLabel2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(0, 0, Short.MAX_VALUE))
                    .add(jScrollPane1))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(jLabel2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jProgressBar1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(txtSummary, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jScrollPane1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 188, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(18, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void txtSummaryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtSummaryActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtSummaryActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JLabel jLabel2;
    private javax.swing.JProgressBar jProgressBar1;
    private javax.swing.JScrollPane jScrollPane1;
    public static javax.swing.JTextArea txtDetail;
    private javax.swing.JTextField txtSummary;
    // End of variables declaration//GEN-END:variables

    @Override
    public WizardPanel getNextPanel() {
       
        return new QCPanel(true);
    }

    @Override
    public WizardPanel getPreviousPanel() {

        return new DeIdentifyPanel();
    }
    public NIHImage findImageByLongitudinalSubject(String subject) {
        for (NIHImage image : fullNiiImages) {
            if (image.getLongitudinalSuject().equals(subject) && !image.getImageNewName().equals("")) {
                return image;
            }
        }
        return null;
    }
}
