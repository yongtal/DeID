package dit;

import java.io.File;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Vector;

/**
 *
 * @author Christian Prescott
 * This class contains all the global variables and methods.
 * Level:Top
 */
public class DeidData {
    public static boolean forward = true;
    public static NIHImageHandler imageHandler = new NIHImageHandler();   // all the image related actions are operated by this handler
    public static Hashtable<String, File> unpackedFileLocation = new Hashtable<String, File>(); //contains all the unpacked tools
    public static Hashtable<String, String> longitudinalIDs = new Hashtable(); 
    public static DemographicTableModel demographicData;
    public static DemographicTableModel demographicDataforBack;
    public static SourceDemographicTable sourcedemographicData; //the origin demo, init in LoadDemoPanel
    public static File deidentifiedDemoFile;
    public static boolean doDeface = true; // Load Image Panel: has been defaced
    public static boolean demoFileModified = false;
    public static boolean isNoData = false;
    public static int IdColumn = 0;
   //public static int[] dateColumn;     //the column number which save date info. add in loadDemoP use in DeIdentifyP
    public static String outputPath = "dit_output/";
    public static String defaceThreshold = "0.1";
    public static String tarfilesavedpath = "";
    public static String mapfilesavedpath = "";
    public static String UserFullName = "", UserInstitution = "";
    public static Object[][] data = new Object[0][0];

    public static void addInputFile(Vector<File> files) {
        for (int i = 0; i < files.size(); i++) {
            File currentFile = files.get(i);
             
            imageHandler.addFile(currentFile);

        }
    }

    public static void addInputFile(File file) {
        imageHandler.addFile(file);
    }

    private static boolean isExistInputFile(File file) {

        String fileName = file.getAbsolutePath();
        if (fileName.endsWith("nii.gz")) {
            fileName = fileName.replace("nii.gz", "");
        } else {
            fileName = fileName.substring(0, fileName.lastIndexOf("."));
        }

        for (int i = 0; i < inputFiles.size(); i++) {
            String existFileName = inputFiles.get(i).getAbsolutePath().substring(0, inputFiles.get(i).getAbsolutePath().lastIndexOf("."));;
            if (existFileName.equals(fileName)) {
                return true;
            }
        }

        return false;

    }
    //
    //the var below this line could be ignored//
    //
    public static Vector<File> inputFiles = new Vector<File>(),
            niftiFiles = new Vector<File>(),
            deidentifiedFiles = new Vector<File>();
    public static Hashtable<File, File> // Nifti result => Dicom source
            NiftiConversionSourceTable = new Hashtable<File, File>(),
            ConvertedDicomHeaderTable = new Hashtable<File, File>();
    // This table will map original filenames to their original IDs. Even
    // if the user chooses not to randomize IDs, it will be filled with the
    // correct key->value pairs. How convenient!
    public static Hashtable<String, String> IdFilename;
    public static int multimatchingFlag = 0;
    public static Vector<Object> errorlog;
    public static int curFlag = 0;
    public static String parentPath = "none";
    public static String anaPath = "/tmp/deid_output/fslchfiletypeOut";
    public static String dicomPath = "tmp/deid_output/dcm2niiOut";
    public static Vector<String> multimatchingNamelist;
    public static Hashtable<String, Integer> multinameSol = new Hashtable();
    public static Hashtable<String, String> multinameSolFile = new Hashtable();
    ;
    public static Hashtable<String, String> IdTable;
    //this hashtable is to make sure all the longitudinal images belong to same subject have a same id.
    public static String[] selectedIdentifyingFields,
            deselectedIdentifyingFields;
    public static Boolean[] includeFileInTar = null;
    public static Boolean[] whetherredoImage = null;
    public static int correctflag = 0;
    public static File demoSourceFile = null;
    // Data declarations
    public static final String[] dicomVarIds = new String[]{
        "(0008,0070)",
        "(0010,1030)",
        "(0018,0020)",
        "(0018,0021)",
        "(0018,0022)",
        "(0018,0023)",
        "(0018,0024)",
        "(0018,0025)",
        "(0018,0050)",
        "(0018,0080)",
        "(0018,0081)",
        "(0018,0082)",
        "(0018,0083)",
        "(0018,0087)",
        "(0018,0089)",
        "(0018,0091)",
        "(0018,0093)",
        "(0018,0094)",
        "(0018,0095)",
        "(0018,1020)",
        "(0018,1251)",
        "(0018,1310)",
        "(0018,1312)",
        "(0018,1314)",
        "(0018,5100)",
        "(0020,0032)",
        "(0020,0037)",
        "(0020,0052)",
        "(0028,0010)",
        "(0028,0011)",};
    public static final String[] dicomVarNames = new String[]{
        "Manufacturer",
        "PatientWeight",
        "ScanningSequence",
        "SequenceVariant",
        "ScanOptions",
        "MrAcquisitionType",
        "SequenceName",
        "AngioFlag",
        "SliceThickness",
        "RepetitionTime",
        "EchoTime",
        "InversionTime",
        "NumberOfAverages",
        "MagneticFieldStrength",
        "PhaseEncodingSteps",
        "EchoTrainLength",
        "PercentSampling",
        "PercentPhaseFov",
        "PixelBandwidth",
        "SoftwareVersion",
        "TransmittingCoil",
        "AcquisitionMatrix",
        "PhaseEncodingDirection",
        "FlipAngle",
        "PatientPosition",
        "ImagePositionPatient",
        "ImageOrientationPatient",
        "FrameOfReferenceUid",
        "ImageRows",
        "ImageColumns",};
}
