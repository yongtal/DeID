/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dit;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JLabel;
import javax.swing.border.EmptyBorder;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultCaret;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;

/**
 *
 * @author angelo
 */
public class HelpManualFrame extends javax.swing.JFrame  {
    
    /**
     * Creates new form HelpManualFrame
     */
    public HelpManualFrame(String page) {
        initComponents();
        jTextPane1.setEditable(false);
        String[] initString = new String[3];
        String[] initStyles = new String[3];
        initStyles[0] = "regular";
        int initStrArrSize =1;
        //make the jTextPane jScroll position start at top
        DefaultCaret caret = (DefaultCaret)jTextPane1.getCaret();
        caret.setUpdatePolicy(DefaultCaret.NEVER_UPDATE);
        
        switch (page) {
            case "User Information":
                initString[0] = "Please type your name and your institution in the boxes.  This information will be included in a log file so that data recipients can determine who created the de-identified dataset."
                        +"\n\n"
                        +"Click the ‘Next’ button to load your images on the next page.";
                break;
            case "Load Images":
                initString[0] = "You can add images that are stored in one directory or select the root directory under which all of your images are stored if they are in subject-specific directories."
                        + "\n\n"
                        + "You can remove an image from the list by selecting a path for one image and then clicking the Remove Selected button. Similarly, all images can be removed from the list by clicking the Select All button and then the Remove Selected button."
                        + "\n\n"
                        + "Please indicate if your structural images have already been skull-stripped."
                        + "\n\n"
                        + "Click the ‘Next’ button to include a data file for your images on the next page.";
                break;
            case "Load Demographic Data":
                initString[0] = "Click the 'Choose Data File' button to select the data file containing participant/patient IDs and data (e.g., age) that are associated with the images that you plan to share."
                        + "\n\n"
                        + "DeID can read .txt, .xls and .xlsx data files."
                        + "\n\n"
                        + "DeID will highlight any problems in the data file (e.g., missing values). You can edit each cell to correct any problems."
                        + "\n\n"
                        + "By default, the first column in the data file is used for mapping the cases in your data file to the image file names. If the ID is not in the first column of  your data file, click on the variable name at the top of the column for your matching variable."
                        + "\n\n"
                        + "Click the 'Next' button to ensure that your image filenames are matched to IDs and data in the data file on the next page. ";
                break;
            case "Data Matching":
                initStrArrSize = 2;
                initString[0] = "DeID automatically tries to match the image file name or the directory path with the ID variable in your data file. DeID will report \"MISMATCH\" when it cannot identify matching values. "
                        + "\n\n"
                        + "Solutions for mismatched IDs and image filenames:"
                        + "\n\n"
                        + "1) If DeID cannot find a matching ID for an image, try selecting the Search The Path for Matching Information box."
                        + "\n\n" 
                        + "2) If you have more than one image for each subject, select the 'Multiple Images Linking to One Subject' box."
                        + "\n\n"
                        + "3) If you still have mismatches, try using the Matching Pattern function."
                        + "\n\n"
                        + "The Matching Pattern function allows you to define a string in your image file name that may match an ID.  For example, if you type IXI### in the Matching Pattern box, DeID  will map the variable values in the data file to that format (e.g., one subject's ID is '12', then its ID will be mapped to an image with 'IXI012' in the filename).  If you have strings (%) and numbers (#) in your ID and image filename, then type IXI%## and DeID will map the variable in demographic file to that format (e.g., an ID may be 'ab12' that matches with a 'IXIab12' string in the filename.)"
                        + "\n\n"
                        + "You should correct any mismatches between image filenames and IDs before continuing to the next step."
                        + "\n\n"
                        + "Click the 'Next' button to inspect your data file and remove any variables that may contain personal health information or one of the 18 HIPAA identifiers: \n";
                initString[1] = "" + "\n"; //label
                initStyles[1] = "label";
                break;
            case "Deidentification Options":
                initStrArrSize =3;
                initString[0] = "Move variable names into the right-side window if they contain personal health information (PHI) that should be removed to de-identify your data. For example, this should include the original ID that can be linked to PHI. For example, names, addresses, contact information, birth dates, test dates, medical record numbers, and social security numbers should be removed. More information about variables that should be removed to de-identify your data can be found here: \n"; //regular
                initString[1] = "" + "\n";   //label
                initString[2] = "\n"+"Click the 'Next' button to start de-identification process, including a skull-stripping step.  You will next be asked to confirm that your images havebeen mapped to the correct variable values. " ;//regular
                initStyles[0] = "regular";
                initStyles[1] = "label";
                initStyles[2] = "regular";
                break;
            case "Quality Control":
                initString[0] = "The display window allows you to confirm that variable values shown text with each image are the correct values and image for that case.  This is a sanity check for contributors to ensure that the data have been properly mapped.\n"
                        + "\n"
                        + "Click the 'Next' button to evaluate the skull-stripping and re-skull-strip images when necessary";
                break;
            case "Auditing":
                initString[0] = "Click on a file name to view the image after skull-stripping.\n"
                        + "\n"
                        + "The check box is used to select images that you plan to share. Please do not share images that still have voxels representing the face in the skull-stripped image.\n"
                        + "\n"
                        + "The View Image Montage button will allow you to quickly view all of the skull-stripped images.  You can more carefully inspect the images for the quality of skull-stripping by viewing selected image file names in the left-side window and scrolling through each image in the right-side window or by clicking the View Image button to display the image in MRIcron. \n"
                        + "\n"
                        + "If voxels representing the face remain after skull-stripping or if voxels representing the brain have been removed, the 'Redo Skull-Stripping' button allows you to skull-strip the images again with a different BET threshold.  Lower values will remove fewer voxels representing tissue.\n"
                        + "\n"
                        + "You can also use the View Image Header button to view all header information and confirm that there is no identifying information in the header.\n"
                        + "\n"
                        + "The 'View Demographic Data' allows you to inspect the de-identified data file.\n"
                        + "\n"
                        + "Click the 'Next' button to save the de-identified data set.";
                break;
            case "Transfer Options":
                initString[0] = "A single gzipped file containing your images, data file, and a log file will be created in this step.   Please indicate for the log file whether and how the recipient of your data can share or not share your data.\n"
                        + "\n"
                        + "1) No share: indicates that you and your IRB are allowing only the recipient to use the data.\n"
                        + "\n"
                        + "2) Enclave: indicates that other research groups can use the data, but only in a protected computing enclave that is maintained by the recipient.\n"
                        + "\n"
                        + "3) All share: indicates that you are providing the data as an open access data set.\n"
                        + "\n"
                        + "You can save the result to your local computer by choosing a path and/or you can you can upload the file to a remote location by providing the server address, port, user name, and password.\n"
                        + "\n"
                        + "Please select the 'I agree' box to confirm that the data have been de-identified.\n"
                        + "\n"
                        + "Click 'Next' button to save and/or send your de-identified data set.";
                break;
            case "Complete":
                initString[0] = "This is the last page, you have completely finish the job. "
                        +   "\nYou can find your tar file in the path displayed."
                        +   "\nYou cannot get back to previous step because the temporary files are"
                        +   "\ncleaned up automatically."
                        +   "\nClick 'Done' to finish and exit.";
                break;
            default:
                initStrArrSize = 0;
                break;
        }
        StyledDocument doc = jTextPane1.getStyledDocument();
        addStylesToDocument(doc);                               
            try {
                for (int i=0; i < initStrArrSize; i++) {
                    doc.insertString(doc.getLength(), initString[i],
                                     doc.getStyle(initStyles[i]));
                }
            } catch (BadLocationException ble) {
                System.err.println("Couldn't insert initial text into text pane.");
            }
    }
    protected void addStylesToDocument(StyledDocument doc) {
     
     //Initialize some styles.
        Style def = StyleContext.getDefaultStyleContext().getStyle(StyleContext.DEFAULT_STYLE);
        Style regular = doc.addStyle("regular", def);
        StyleConstants.setFontFamily(def, "Tahoma");
        StyleConstants.setFontSize(def, 13);
        
        Style s = doc.addStyle("italic", regular);
        StyleConstants.setItalic(s, true);

        s = doc.addStyle("bold", regular);
        StyleConstants.setBold(s, true);

        s = doc.addStyle("small", regular);
        StyleConstants.setFontSize(s, 11);

        s = doc.addStyle("large", regular);
        StyleConstants.setFontSize(s, 16);
        /*
        s = doc.addStyle("icon", regular);
        StyleConstants.setAlignment(s, StyleConstants.ALIGN_CENTER);
        ImageIcon pigIcon = createImageIcon("images/Pig.gif",
                                            "a cute pig");
        if (pigIcon != null) {
            StyleConstants.setIcon(s, pigIcon);
        }
        */        
        
        s = doc.addStyle("label", regular);        
        StyleConstants.setAlignment(s, StyleConstants.ALIGN_CENTER); 
        JLabel label = new JLabel();
        label.setText("https://privacyruleandresearch.nih.gov/pr_08.asp");  
        label.setForeground(Color.BLUE);
        label.setFont(new Font("Tahoma", Font.PLAIN, 11)); 
        label.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        label.addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent arg0)
            {
                URI uri;
                try {
                    uri = new URI("https://privacyruleandresearch.nih.gov/pr_08.asp");
                    openWebpage(uri);
                } catch (URISyntaxException ex) {
                    Logger.getLogger(HelpManualFrame.class.getName()).log(Level.SEVERE, "uri parsing failed!", ex);
                }
            }
        });
        StyleConstants.setComponent(s, label);
    }
    
    private static void openWebpage(URI uri){
         Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
        if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
            try {
                desktop.browse(uri);
            } catch (Exception e) {
                Logger.getLogger(HelpManualFrame.class.getName()).log(Level.SEVERE, "open broswer failed!", e);
            }
        } 
    }
    
    private static void openWebpage(URL url) {
        try {
            openWebpage(url.toURI());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }
    
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        CloseButton = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTextPane1 = new javax.swing.JTextPane();

        setTitle("Help");

        CloseButton.setText("Close");
        CloseButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CloseButtonActionPerformed(evt);
            }
        });

        jTextPane1.setEditable(false);
        jTextPane1.setFont(new java.awt.Font("Tahoma", 0, 13)); // NOI18N
        jScrollPane2.setViewportView(jTextPane1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(CloseButton, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(23, 23, 23))
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 716, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 341, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(CloseButton)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void CloseButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CloseButtonActionPerformed
        // TODO add your handling code here:
        this.setVisible(false);
    }//GEN-LAST:event_CloseButtonActionPerformed

    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton CloseButton;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextPane jTextPane1;
    // End of variables declaration//GEN-END:variables
}
