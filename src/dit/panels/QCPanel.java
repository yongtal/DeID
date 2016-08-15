/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package dit.panels;

import dit.AuditJTable;
import dit.DEIDGUI;
import dit.DeidData;
import dit.FileUtils;
import dit.NIHImage;
import dit.NiftiDisplayPanel;
import dit.OpenImagewithMRIcron;
import dit.ReSkullStrippingFrame;
import dit.TextviewFrame;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.LayoutManager;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.WindowConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;
import niftijlib.Nifti1Dataset;


/**
 *
 * @author yongtal
 */
public class QCPanel extends javax.swing.JPanel implements WizardPanel{

    /**
     * Creates new form QCPanel
     */
    Icon deidtmp;
    Icon idtmp;
    private javax.swing.JPanel imagePanel;
    
    public QCPanel(boolean flag) {
        DEIDGUI.title = "Quality Control";
        DEIDGUI.helpButton.setEnabled(true);
        
        int imageNum = DeidData.imageHandler.getInputFiles().size();
        //imagePanel = new javax.swing.JPanel( new GridLayout(imageNum,2));
        
        imagePanel = new javax.swing.JPanel( );
        imagePanel.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        //c.gridheight = imageNum;
        //c.gridwidth = 2;
        
        //////////////////////////////////////////////
        ///////////2.22
        
        File targetFile = new File(DeidData.outputPath + "montage.jpg");
        //create deface montage image
        for(NIHImage image : DeidData.imageHandler.getInputFiles()) {
            if (flag) {
                DeidentifyProgressPanel.txtDetail.append("Postprocessing image " + image.getImageName() + " \n");
                image.initNifti();
                createMontage(image);
                image.emptySet();
            }
         
        }
        ////////////////////////////////////////
        ////////2.13
        int i = 0;
        for(NIHImage image : DeidData.imageHandler.getInputFiles()){            
            Image deidMontage = image.getDeidMontage();  
            Image idMontage = image.getIdMontage();
            deidtmp = new ImageIcon(deidMontage);
            idtmp = new ImageIcon(idMontage); 
            //JLabel tmplbl = new javax.swing.JLabel();
            
            
            c.fill = GridBagConstraints.NONE;
           
            c.gridx = 0;
            c.gridy = i*2;
            imagePanel.add(new javax.swing.JLabel(idtmp),c);            
            //idmontage and info
            c.gridy = i*2+1;  
            if(! DeidData.isNoData){
                String secValName = image.get2ndCol(0);
                String secIdVal = image.get2ndCol(2);
                String thrValName = image.get3rdCol(0);
                String thrIdVal = image.get3rdCol(2);
                String forValName = image.get4thCol(0);
                String forIdVal = image.get4thCol(2);
            imagePanel.add(new javax.swing.JLabel("<html>"+secValName+"="+secIdVal
                 +"<br>"+thrValName+"="+thrIdVal+"<br>"+forValName+"="+forIdVal+"</html>"),c);        
            }
            //else
            //    imagePanel.add(new javax.swing.JLabel("<html>  <br>No DataFile selected!<br>  </html>"));
           
            //deid montage and info
            c.fill = GridBagConstraints.NONE;
            c.weightx = 0.5;
            c.gridx = 1;
            c.gridy = i*2;
            imagePanel.add(new javax.swing.JLabel(deidtmp),c);  
            c.gridy = i*2+1;
            if(! DeidData.isNoData){
                String secValName = image.get2ndCol(0);
                String secDeidVal = image.get2ndCol(1);
                String thrValName = image.get3rdCol(0);
                String thrDeidVal = image.get3rdCol(1);
                String forValName = image.get4thCol(0);
                String forDeidVal = image.get4thCol(1);
                imagePanel.add(new javax.swing.JLabel("<html>"+secValName+"="+secDeidVal
                +"<br>"+thrValName+"="+thrDeidVal+"<br>"+forValName+"="+forDeidVal+"</html>"),c);
            }
            //else
            //    imagePanel.add(new javax.swing.JLabel("<html>  <br>No DataFile selected!<br>  </html>")); 
            i++;
        }

        initComponents(); 
        if (flag)   //adavance to QC panel 
            writeMontageFile();
       
       
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel3 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jImagePanel = new javax.swing.JScrollPane(imagePanel);

        jLabel3.setText("<html><p>Please confirm that the data has been de-identified.  If there are any problems, press \"back\" to modify. </p></html>");

        jLabel1.setText("Quality Control: ");

        jImagePanel.setBackground(new java.awt.Color(255, 255, 255));
        jImagePanel.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jImagePanel.setName(""); // NOI18N

        /*
        jImagePanel.setVisible(true);
        jImagePanel.setLayout(new GridLayout());
        //for(int i = 0; i < 5; i++){
            JLabel jtmp = new javax.swing.JLabel();
            jtmp.setText("adsfa");
            jImagePanel.add(jLabel3);
            jtmp.setVisible(true);
            */
            //}

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel1)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(112, 112, 112)
                        .addComponent(jImagePanel, javax.swing.GroupLayout.DEFAULT_SIZE, 410, Short.MAX_VALUE)))
                .addGap(60, 60, 60))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(7, 7, 7)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jImagePanel, javax.swing.GroupLayout.DEFAULT_SIZE, 417, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents


    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jImagePanel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    // End of variables declaration//GEN-END:variables

    @Override
    public WizardPanel getNextPanel() {
    //    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        
        return new AuditPanel();
    }

    @Override
    public WizardPanel getPreviousPanel() {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        DeidData.demographicData = DeidData.demographicDataforBack;
        DeidData.includeFileInTar = null;
        return new DeIdentifyPanel();
    }

 public static void writeMontageFile(){
      int cellHeight = DeidentifyProgressPanel.cellHeight;
      int cellWidth = DeidentifyProgressPanel.cellWidth;
      int idImageNum  = DeidData.imageHandler.getInputFiles().size();
      int wholeWidth = 2*cellWidth;
      int wholeHeight = idImageNum*cellHeight;
      BufferedImage result = new BufferedImage(wholeWidth, wholeHeight,BufferedImage.TYPE_INT_BGR);
      Graphics g = result.getGraphics();
      int i = 0;
      for(NIHImage image : DeidData.imageHandler.getInputFiles()){
        Image idtmp = image.getIdMontage();
        g.drawImage(idtmp, 0, i*cellHeight, cellWidth , cellHeight, null);
        Image deidtmp = image.getDeidMontage();
        g.drawImage(deidtmp, cellWidth, i*cellHeight, cellWidth , cellHeight, null);
        i++;
      }
      try {
        File targetFile = new File(DeidData.outputPath + "montage.jpg");
        if ( targetFile.exists() ) {
            targetFile.delete();
            System.out.println("delete the origin montage file and write a new one");
        }
        ImageIO.write(result, "jpg", targetFile);
        System.out.println("Suscessful create Montage file!");
        //image.setMontageFile(targetFile);
        } catch (IOException ex) {
            DEIDGUI.log("Unable to write montage.png",
            DEIDGUI.LOG_LEVEL.ERROR);
        }
    }
 
    //need modification in the future
    //I help you modify it.
    ///////////////////2.13
    //save in both NIHImage image and the Vector
    public static void createMontage(NIHImage image) {

        //int cellWidth = 196, textHeight = 12, cellHeight = 196, rowHeight = cellHeight + textHeight;
        int cellHeight =  DeidentifyProgressPanel.cellHeight;
        int cellWidth =  DeidentifyProgressPanel.cellWidth;
        int textHeight = 12, rowHeight = cellHeight + textHeight;
        int montageWidth = cellWidth;
        int montageHeight = (1 / 4 + 1) * rowHeight;

//        if (DeidData.imageHandler.getInputFiles().isEmpty()) {
//            return;
//        }
        BufferedImage i = new BufferedImage(montageWidth, montageHeight, BufferedImage.TYPE_INT_RGB);
        int rowCounter = 0;
        int colCounter = 0;

       // Nifti1Dataset set = new Nifti1Dataset(image.getTempPotision().getAbsolutePath());

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
        //deidMontage.add(i);
    }

 
 
 
}
