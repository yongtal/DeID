package dit;

import java.io.File;

/**
 *
 * @author Christian Prescott
 */
public class ImageFilter extends javax.swing.filechooser.FileFilter implements java.io.FileFilter{

    @Override
    public boolean accept(File file) {
        boolean isAccepted = false;
        if(file.isDirectory()){
            isAccepted = true;
        } else {
            String extension = FileUtils.getExtension(file);
            if(extension != null && 
                    // NIfTi extension
                    (extension.equals(FileUtils.Extensions.nifti) ||
                    //NIfTi gz file
                    extension.equals(FileUtils.Extensions.niftigz) ||
                    // Analyze extensions
                    extension.equals(FileUtils.Extensions.analyzehdr) || 
                    extension.equals(FileUtils.Extensions.analyzeimg) ||
                    // DICOM extension
                    extension.equals(FileUtils.Extensions.dicom))){
                isAccepted = true;
            }
        }
        
        return isAccepted;
    }

    @Override
    public String getDescription() {
        return "Images (*.nii, *.nii.gz, *.dcm, *.img, *.hdr)";
    }

}
