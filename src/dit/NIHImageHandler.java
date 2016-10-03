package dit;

import dit.panels.LoadImagesPanel;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.List;
import java.util.Random;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import niftijlib.Nifti1Dataset;

/**
 *
 * @author brain This class provide all the operation to NIHImages such like
 * look up by name, return files as list
 */
public class NIHImageHandler {

    private Vector<NIHImage> _inputFiles;

    public NIHImageHandler() {
        _inputFiles = new Vector<NIHImage>();
    }

    /**
     * @return the _inputFiles
     */
    public Vector<NIHImage> getInputFiles() {
        return _inputFiles;
    }
    LoggerWrapper loggerWrapper = LoggerWrapper.getInstance();
    
      public boolean addFile(File file) {
        NIHImage image = new NIHImage(file);
        if (image.getImageFormat().equals("hdr")) {
            File secondPart = new File(image.getImageName() + ".img");
            if (!secondPart.exists()) {
                return false;
            }
        }

        if (image.getImageFormat().equals("img")) {
            File secondPart = new File(image.getImageName() + ".hdr");
            if (!secondPart.exists()) {
                return false;
            }
        }

        if (!isExistInputFile(file)) {
            _inputFiles.add(image);
            return true;
        }
        else {
            return false;
        }
    }

    public int getInputFilesSize() {
        return _inputFiles.size();
    }

    private boolean isExistInputFile(File file) {
        String fileName = file.getAbsolutePath();
        if (fileName.endsWith("nii.gz")) {
            fileName = fileName.replace("nii.gz", "");
        } else {
            fileName = fileName.substring(0, fileName.lastIndexOf("."));
        }
        for (NIHImage image : _inputFiles) {
            // System.out.println(image.getImageName()+  "  "+fileName);
            if (image.getImageName().equals(fileName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * @param inputFiles the _inputFiles to set
     */
    public void setInputFiles(Vector<NIHImage> inputFiles) {
        this._inputFiles = inputFiles;
    }

    public void removeAll(List<File> asList) {
        _inputFiles.removeAll(asList);
    }
    
    public boolean remove(NIHImage image) {
        return _inputFiles.remove(image);
    }

    public NIHImage findImageByDisplayName(String displayName) {
        for (NIHImage image : _inputFiles) {
            if (image.getImageDisplayName().equals(displayName)) {
                return image;
            }
        }
        return null;
    }

    public NIHImage findImageByLongitudinalSubject(String subject) {
        for (NIHImage image : _inputFiles) {
            if (image.getLongitudinalSuject().equals(subject) && !image.getImageNewName().equals("")) {
                return image;
            }
        }
        return null;
    }

    public Vector<NIHImage> findImageByIdInDataFile(String id) {
        Vector<NIHImage> images = new Vector<>();
        for (NIHImage image : _inputFiles) {
            if (image.getIdInDataFile().equals(id)) {
                images.add(image);
            }
        }
        return images;
    }

    public Collection<String> getAllIDs() {
        Collection<String> ids = new Vector<>();
        for (NIHImage image : _inputFiles) {
            if (!image.getIdInDataFile().equals("")) {
                ids.add(image.getIdInDataFile());
            }
        }
        return ids;
    }

    public void assignID(NIHImage image, String baseId) {

        if (image.isIsLongitudinal()) {
            NIHImage sameSubjectImage = findImageByLongitudinalSubject(image.getLongitudinalSuject());

            if (sameSubjectImage != null && !sameSubjectImage.getImageNewName().equals("")) {
                System.out.println("Hit");
                String prefix = sameSubjectImage.getImageNewName().substring(0,
                        sameSubjectImage.getImageNewName().lastIndexOf("#"));
                image.setImageNewName(prefix + image.getLongitudinalNumber());
                return;
            }

        }

        String newId;
        do {
            newId = baseId;
            for (int strNdx = 0; strNdx < 6; strNdx++) {
                newId += Integer.toString(new Random().nextInt(9));
            }
        } while (!uniqueID(newId));

        if (image.isIsLongitudinal()) {
            image.setImageNewName(newId + image.getLongitudinalNumber());
        } else {
            image.setImageNewName(newId);
        }

    }
    //Check if the newly randomized name is in the original id list
    private boolean uniqueID(String newId) {
        for (NIHImage image : _inputFiles) {
            if (image.getImageNewName().equals(newId)) {
                return false;
            }
        }
        return true;
    }

    public void correctOrientation(NIHImage image) {
        if (image.isIsCorrectedOrientation()) {
            return;
        }

        Nifti1Dataset set = new Nifti1Dataset(image.getTempPotision().getAbsolutePath());
        float sform = set.sform_code;
        float qform = set.qform_code;
        float quat_x = set.srow_x[3];
        float quat_y = set.srow_y[3];
        float quat_z = set.srow_z[3];
        OrientationState oldState = image.getOrientationState();

        OrientationState newState = null;
        if (sform == 4.0 || sform == 0.0) {
            newState = oldState.toTop().toTop().toRight().toRight();
            //  System.out.println("Case 1:" + sform + "   " + qform);
        } else if (sform == 1.0 && quat_x < 0 && quat_y > 0 && quat_z < 0) {
            newState = oldState.toTop().toTop().toRight().toRight();
            // System.out.println("Case 1:" + sform + "   " + qform);

        } else if (sform == 1.0 && quat_x > 0 && quat_y > 0 && quat_z < 0) {
            System.out.println("Case 3:" + sform + "   " + qform);
            newState = oldState;
        } else {
            System.out.println("Case 4:" + sform + "   " + qform);
            newState = oldState;
        }

        image.setOrientationState(newState);
        image.setIsCorrectedOrientation(true);
    }

    public void moveImages() {
        for (NIHImage image : _inputFiles) {
            if (!image.getImageNewName().equals("")) {
                String newFileDir = DeidData.outputPath + "betOut/";
                File secondPart = null;
                File newSecondFileName = null;
                String newFileName = DeidData.outputPath + "betOut/" + image.getImageNewName() + ".nii";
                File newFile = new File(newFileName);
                File newDir = new File(newFileDir);
                
                if (image.getImageFormat().equals("hdr")) {
                    secondPart = new File(image.getImageName() + ".img");
                    newFile = new File(DeidData.outputPath + "betOut/" + image.getImageNewName() + ".hdr");
                    newSecondFileName = new File(DeidData.outputPath + "betOut/" + image.getImageNewName() + ".img");
                    if (!secondPart.exists()) {
                        System.out.println("The corresponding .img file of " + image.getImageName() + "is missing");
                        return;
                    }
                }
                else if (image.getImageFormat().equals("img")) {
                    secondPart = new File(image.getImageName() + ".hdr");
                    newFile = new File(DeidData.outputPath + "betOut/" + image.getImageNewName() + ".img");
                    newSecondFileName = new File(DeidData.outputPath + "betOut/" + image.getImageNewName() + ".hdr");
                    if (!secondPart.exists()) {
                        System.out.println("The corresponding .hdr file of " + image.getImageName() + "is missing");
                        return;
                    }
                }               
                
                
                
                
                
                try {
                    if (!newDir.exists()) {
                        newDir.mkdir();
                    }
                    FileUtils.copyFile(image.getStoredPotistion(), newFile);
                    if (newSecondFileName != null)
                        FileUtils.copyFile(secondPart, newSecondFileName);
                    image.setTempPotision(newFile);
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(NIHImageHandler.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(NIHImageHandler.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
    
    /**
     * after remove selected images, update TreeModel.
     */
    public void updateTreeModel() {
        DefaultTreeModel model = LoadImagesPanel.model;
        DefaultMutableTreeNode root = (DefaultMutableTreeNode)model.getRoot();
        Enumeration e = root.depthFirstEnumeration();
        while (e.hasMoreElements()) {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) e.nextElement();
            if (node.isLeaf()) {
                try {
                   NIHImage image = (NIHImage)node.getUserObject();
                   if (! _inputFiles.contains(image)) {
                        model.removeNodeFromParent(node);
                    }
                } catch (java.lang.ClassCastException ex) {
                    model.removeNodeFromParent(node);
                }
            }
        }
        model.reload();
    }
}
