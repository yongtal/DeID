package dit.panels;

import dit.AuditJTable;
import dit.DEIDGUI;
import dit.DeidData;
import dit.FileUtils;
import dit.LoggerWrapper;
import dit.NIHImage;
import dit.NiftiDisplayPanel;
import dit.OpenImagewithMRIcron;
import dit.ReSkullStrippingFrame;
import dit.TextviewFrame;
import dit.ViewHeaderFrame;
import static dit.panels.LoadImagesPanel.model;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.lang.Math;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.DefaultTreeSelectionModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;


/**
 *
 * @author christianprescott
 */
public class AuditPanel extends javax.swing.JPanel implements WizardPanel {
     private class Point{
        public float x;
        public float y;
        public Point(float x,float y)
        {
            this.x=x;
            this.y=y;
        }
    }
 
    Point startPoint;
    Point endPoint;
    /**
     * Creates new form AuditPanel
     */
    public static final DefaultTreeModel model = LoadImagesPanel.model;
    //private javax.swing.JTree jTree;
    
    private ReSkullStrippingFrame redo;
    private NIHImage selectedFile = null;
    private int prevCursor = 0;
    private int bufferNum;
    
///////////////////////////////////////
    ////////////2.27
    private ViewHeaderFrame viewHeader;
    
    public AuditPanel() {

        initComponents();
        // if(DeidData.demographicData != DemographicTableModel.dummyModel)
      //  createFakenames();
       
        jButtonViewMontage.setVisible(true);
        DEIDGUI.title = "Auditing";
        DEIDGUI.helpButton.setEnabled(true);
        bufferNum = (int) (java.lang.Runtime.getRuntime().freeMemory() / 20000000 / 2 / 2);
        //System.out.println("# of Buffer isssssssssssss " + bufferNum);
        LoggerWrapper.myLogger.log(Level.INFO, "# of Buffer is {0}", bufferNum);
        jButtonViewHeader.setEnabled(false);
        jButtonViewImage.setEnabled(false);
        //if (! DeidData.doDeface)
        //    jButton1.setEnabled(false);
        
        // Define the AuditJTable model                                                                                                                                                                                                                                                                                                                                   
        jTree.setModel(model);
        FileTreeCellRenderer renderer = new FileTreeCellRenderer();
        renderer.setLeafIcon(UIManager.getIcon("FileView.fileIcon")); // used for leaf nodes
        jTree.setCellRenderer(renderer);
        jTree.setRootVisible(false); 
        jTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        
        // Add a selection listener for enabling/disabling the "View Header" button
        jTree.addTreeSelectionListener(new TreeSelectionListener() { 
            @Override
            public void valueChanged(TreeSelectionEvent e) {

                if (jTree.getSelectionCount() > 0 ) {
                    TreePath path = jTree.getSelectionPath();
                    DefaultMutableTreeNode node = (DefaultMutableTreeNode) (path.getLastPathComponent()); 
                    if (node.isLeaf()) {  
                        jButtonViewHeader.setEnabled(true);
                        jButtonViewImage.setEnabled(true);
                        if (e.getNewLeadSelectionPath() != e.getOldLeadSelectionPath()) {
                            if(selectedFile != null){
                                selectedFile.emptySet();
                            }
                            /*NIHImage*/ 
                            selectedFile = (NIHImage)node.getUserObject();
                            //System.out.println("cursor is  fffff: " + currCursor);
                            try {    
                                ((NiftiDisplayPanel)imagePanel).setImage(selectedFile);
                            } catch (Exception le){
                                LoggerWrapper.myLogger.log(Level.SEVERE, "Error is {0}", le);
                                DEIDGUI.log("Failed to load image", DEIDGUI.LOG_LEVEL.ERROR);                        
                            }
                        }
                    }
                }                        
            }
        });

        DEIDGUI.log("AuditPanel initialized");  
    }
    
    
    public class FileTreeCellRenderer extends DefaultTreeCellRenderer {
        // <editor-fold defaultstate="collapsed">
        @Override
        public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
            if (value instanceof DefaultMutableTreeNode) {
                
                value = ((DefaultMutableTreeNode)value).getUserObject();
                if (value instanceof File && ((File) value).isDirectory()) {
                        value = ((File) value).getName();
                }
                if (value instanceof NIHImage) {
                    String newname = ((NIHImage) value).getTempPotision().toString();
                    newname = newname.substring(newname.lastIndexOf(System.getProperty("file.separator"))+1);
                    value = newname;
                }
            }
            return super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
        }
        // </editor-fold>
    }
             
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane2 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList();
        jLabel1 = new javax.swing.JLabel();
        jButtonViewDemo = new javax.swing.JButton();
        jButtonViewImage = new javax.swing.JButton();
        jButtonViewHeader = new javax.swing.JButton();
        imagePanel = new NiftiDisplayPanel();
        sliceBar = new javax.swing.JSlider();
        jButtonViewMontage = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        leftRotateBtn = new javax.swing.JButton();
        rightRotateBtn = new javax.swing.JButton();
        resetRotateBtn = new javax.swing.JButton();
        orientationLbl = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();

        jList1.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jScrollPane2.setViewportView(jList1);

        jLabel1.setText("<html><p>Ensure the data has been de-identified. Deselect images that have not had identifying information properly removed or will not be transferred.</p><p>&nbsp;</p></html>");

        jButtonViewDemo.setText("View Data File");
        jButtonViewDemo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonViewDemoActionPerformed(evt);
            }
        });

        jButtonViewImage.setText("View Image");
        jButtonViewImage.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonViewImageActionPerformed(evt);
            }
        });

        jButtonViewHeader.setText("View Image Header");
        jButtonViewHeader.setEnabled(false);
        jButtonViewHeader.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonViewHeaderActionPerformed(evt);
            }
        });

        imagePanel.setMinimumSize(new java.awt.Dimension(0, 32));
        imagePanel.setPreferredSize(new java.awt.Dimension(0, 0));
        imagePanel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                imagePanelMouseEntered(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                imagePanelMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                imagePanelMouseReleased(evt);
            }
        });

        org.jdesktop.layout.GroupLayout imagePanelLayout = new org.jdesktop.layout.GroupLayout(imagePanel);
        imagePanel.setLayout(imagePanelLayout);
        imagePanelLayout.setHorizontalGroup(
            imagePanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 228, Short.MAX_VALUE)
        );
        imagePanelLayout.setVerticalGroup(
            imagePanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 111, Short.MAX_VALUE)
        );

        sliceBar.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                sliceBarStateChanged(evt);
            }
        });

        jButtonViewMontage.setText("View Image Montage");
        jButtonViewMontage.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonViewMontageActionPerformed(evt);
            }
        });

        jButton1.setText("Redo Skull-Stripping");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        leftRotateBtn.setText("Left");
        leftRotateBtn.setActionCommand("");
        leftRotateBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                leftRotateBtnActionPerformed(evt);
            }
        });

        rightRotateBtn.setText("Right");
        rightRotateBtn.setActionCommand("");
        rightRotateBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rightRotateBtnActionPerformed(evt);
            }
        });

        resetRotateBtn.setText("Reset");
        resetRotateBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                resetRotateBtnActionPerformed(evt);
            }
        });

        orientationLbl.setText("<html>Drag the image to adjust orientation. <br> Click buttons to rotate image. </html>");

        jScrollPane3.setViewportView(jTree);

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(jLabel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .add(layout.createSequentialGroup()
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING, false)
                            .add(jScrollPane3)
                            .add(org.jdesktop.layout.GroupLayout.LEADING, layout.createSequentialGroup()
                                .add(jButtonViewDemo)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(jButtonViewMontage)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(jButton1)))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 50, Short.MAX_VALUE)
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                                .add(orientationLbl, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap())
                            .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                    .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                                        .add(layout.createSequentialGroup()
                                            .add(45, 45, 45)
                                            .add(leftRotateBtn)
                                            .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                            .add(resetRotateBtn)
                                            .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                            .add(rightRotateBtn))
                                        .add(layout.createSequentialGroup()
                                            .add(jButtonViewImage)
                                            .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                            .add(jButtonViewHeader))
                                        .add(sliceBar, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                    .add(imagePanel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 228, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                                .add(9, 9, 9))))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(jLabel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                        .add(jButtonViewDemo)
                        .add(jButtonViewMontage)
                        .add(jButton1))
                    .add(orientationLbl, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(layout.createSequentialGroup()
                        .add(19, 19, 19)
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                            .add(leftRotateBtn, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 17, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(rightRotateBtn, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 17, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(resetRotateBtn, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 17, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(imagePanel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 111, Short.MAX_VALUE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(sliceBar, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                            .add(jButtonViewImage)
                            .add(jButtonViewHeader)))
                    .add(layout.createSequentialGroup()
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jScrollPane3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents
    
    private void jButtonViewImageActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonViewImageActionPerformed
        /* if(jTableImages.getSelectedRow() >= 0){
         * File selectedFile = (File) DeidData.deidentifiedFiles.get(jTableImages.getSelectedRow());
         * OpenFile(selectedFile);
         * }*/
        
        if(jTree.getSelectionModel().getSelectionCount() > 0){
            TreePath path = jTree.getSelectionPath();
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) (path.getLastPathComponent()); 
            File selectedFile =  ((NIHImage)node.getUserObject()).getTempPotision();
            OpenImagewithMRIcron openImage = new OpenImagewithMRIcron(selectedFile);
            openImage.run();
        }
    }//GEN-LAST:event_jButtonViewImageActionPerformed
    
    private void jButtonViewDemoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonViewDemoActionPerformed
        // OpenFile(DeidData.deidentifiedDemoFile);
        TextviewFrame viewtext = new TextviewFrame(DeidData.deidentifiedDemoFile);
        //try {
        viewtext.pack();
        viewtext.setVisible(true);
        //} catch(IOException ex)
        //{
        //      DEIDGUI.log("Unable to open demographic file.", DEIDGUI.LOG_LEVEL.ERROR);
        //}
        
    }//GEN-LAST:event_jButtonViewDemoActionPerformed
        
    private void sliceBarStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_sliceBarStateChanged
        if(jTree.getSelectionModel().getSelectionCount() > 0){
            ((NiftiDisplayPanel)imagePanel).setSlice((float)sliceBar.getValue()/100f);
        }
    }//GEN-LAST:event_sliceBarStateChanged
    
    private void jButtonViewMontageActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonViewMontageActionPerformed
                                          
        File montageFile = new File(DeidData.outputPath + "montage.jpg");
        final BufferedImage image;
        
        if (montageFile.exists() ) {
            try {
                image = ImageIO.read(montageFile);
                JPanel montagePanel = new JPanel(){
                    @Override
                    protected void paintComponent(Graphics g) {
                        super.paintComponent(g);
                        g.drawImage(image, 0, 0, null);
                    } 
                };
                montagePanel.setPreferredSize(new Dimension(image.getWidth(), image.getHeight()));
                JScrollPane sp = new JScrollPane(montagePanel);
                
                sp.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
                sp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
                setLayout(new BorderLayout());
                add(sp, BorderLayout.CENTER);
                JFrame f = new JFrame("DeID image Montage");
                f.setContentPane(sp);
                int HEIGHT = Math.min(image.getHeight()+40, 630);
                f.setSize(image.getWidth()+35, HEIGHT);
                f.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
                f.setVisible(true);
            
            } catch (IOException ex) {
                Logger.getLogger(AuditPanel.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    
    }//GEN-LAST:event_jButtonViewMontageActionPerformed
    
    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        redo = new ReSkullStrippingFrame();
        redo.pack();
        redo.setVisible(true);       
    }//GEN-LAST:event_jButton1ActionPerformed

    private void rightRotateBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rightRotateBtnActionPerformed
        ((NiftiDisplayPanel)imagePanel).setOrientationState( ((NiftiDisplayPanel)imagePanel).rotateClockwise());
        ((NiftiDisplayPanel)imagePanel).setSlice(0.5f);
    }//GEN-LAST:event_rightRotateBtnActionPerformed

    private void leftRotateBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_leftRotateBtnActionPerformed
         ((NiftiDisplayPanel)imagePanel).setOrientationState( ((NiftiDisplayPanel)imagePanel).rotateAntiClockwise());
         ((NiftiDisplayPanel)imagePanel).setSlice(0.5f);
    }//GEN-LAST:event_leftRotateBtnActionPerformed

    private void resetRotateBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_resetRotateBtnActionPerformed
         ((NiftiDisplayPanel)imagePanel).reset();
    }//GEN-LAST:event_resetRotateBtnActionPerformed

    private void imagePanelMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_imagePanelMouseEntered
        Cursor cursor=Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR); 
        //change cursor appearance to HAND_CURSOR when the mouse pointed on images
        imagePanel.setCursor(cursor);    
    }//GEN-LAST:event_imagePanelMouseEntered

    private void imagePanelMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_imagePanelMousePressed
         startPoint=new Point(evt.getX(),evt.getY());
         //System.out.println("Mouse entered at ("+evt.getX()+","+evt.getY()+")");
    }//GEN-LAST:event_imagePanelMousePressed

    private void imagePanelMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_imagePanelMouseReleased
        if(jTree.getSelectionModel().getSelectionCount() > 0) { 
        
            endPoint=new Point(evt.getX(),evt.getY());
              
            float diffX=endPoint.x-startPoint.x;
            float diffY=endPoint.y-startPoint.y;
            
            if(diffX>0)
            {
                if(Math.abs(diffY)<diffX)
                    ((NiftiDisplayPanel)imagePanel).setOrientationState(((NiftiDisplayPanel)imagePanel).getOrientationState().toRight());
                else if(diffY>0)
                    ((NiftiDisplayPanel)imagePanel).setOrientationState(((NiftiDisplayPanel)imagePanel).getOrientationState().toTop());
                else
                    ((NiftiDisplayPanel)imagePanel).setOrientationState(((NiftiDisplayPanel)imagePanel).getOrientationState().toBottom());
                 
            }
         
            if(diffX<0)
            {
                if(Math.abs(diffY)<(-diffX))
                    ((NiftiDisplayPanel)imagePanel).setOrientationState(((NiftiDisplayPanel)imagePanel).getOrientationState().toLeft());
                else if(diffY>0)
                    ((NiftiDisplayPanel)imagePanel).setOrientationState(((NiftiDisplayPanel)imagePanel).getOrientationState().toTop());
                else
                    ((NiftiDisplayPanel)imagePanel).setOrientationState(((NiftiDisplayPanel)imagePanel).getOrientationState().toBottom());
            }
             
            ((NiftiDisplayPanel)imagePanel).setSlice(0.5f);
        }
    }//GEN-LAST:event_imagePanelMouseReleased

    private void jButtonViewHeaderActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonViewHeaderActionPerformed
        // TODO add your handling code here:
        //if (!ViewHeaderFrame.isExists) {
            TreePath path = jTree.getSelectionPath();
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) (path.getLastPathComponent()); 
            NIHImage image =  (NIHImage)node.getUserObject();
            //NIHImage image = DeidData.imageHandler.getInputFiles().get(imagesTable.getSelectedRow());
            viewHeader = new ViewHeaderFrame(image);      
            viewHeader.pack();
            viewHeader.setVisible(true);
        //}
        //jButtonViewHeader.setEnabled(false);
    }//GEN-LAST:event_jButtonViewHeaderActionPerformed
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    public static javax.swing.JPanel imagePanel;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButtonViewDemo;
    private javax.swing.JButton jButtonViewHeader;
    private javax.swing.JButton jButtonViewImage;
    private javax.swing.JButton jButtonViewMontage;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JList jList1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    public static final javax.swing.JTree jTree = new javax.swing.JTree();
    private javax.swing.JButton leftRotateBtn;
    private javax.swing.JLabel orientationLbl;
    private javax.swing.JButton resetRotateBtn;
    private javax.swing.JButton rightRotateBtn;
    private javax.swing.JSlider sliceBar;
    // End of variables declaration//GEN-END:variables
    
    @Override
    public WizardPanel getNextPanel() {
        try{
            redo.setVisible(false);
            viewHeader.setVisible(false);
            //ViewHeaderFrame.isExists = false
        }catch (Exception e){
            
            return new TransferPanel();
            
        }
        return new TransferPanel();
    }
    
    @Override
    public WizardPanel getPreviousPanel() {
        //DeidData.includeFileInTar = null;
        return new QCPanel(false);
    }
}
