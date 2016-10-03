/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dit;
import dit.panels.AuditPanel;
import dit.panels.DeidentifyProgressPanel;
import static dit.panels.LoadImagesPanel.model;
import dit.panels.QCPanel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Icon;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.plaf.ColorUIResource;
import javax.swing.table.AbstractTableModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

/**
 *
 * @author angelo
 */
public class ReSkullStrippingFrame extends javax.swing.JFrame {

    private WarningJdialog wjd;
    private ArrayList<NIHImage> selection = new ArrayList<>();
    private NIHImage selectedFile = null;
    private String gzFileOutputDir = DeidData.outputPath + "betOut" + File.separator;
    
    /**
     * Creates new form ReSkullStrippingFrame
     */
    public ReSkullStrippingFrame() {
        initComponents();
        
        CheckNode croot = new CheckNode();
        DefaultMutableTreeNode oldroot = (DefaultMutableTreeNode) AuditPanel.model.getRoot();
        copyTreeNodes(croot, oldroot);
        DefaultTreeModel model = new DefaultTreeModel(croot);
        jTree1.setModel(model);
        jTree1.setRootVisible(false);
        jTree1.setCellRenderer(new CheckRenderer());
        jTree1.getSelectionModel().setSelectionMode(
          TreeSelectionModel.SINGLE_TREE_SELECTION
        );
        jTree1.addMouseListener(new NodeSelectionListener(jTree1));
        jTree1.addTreeSelectionListener(new TreeSelectionListener() { 
            // <editor-fold defaultstate="collapsed" desc="jTreeSelectionListener">
            @Override
            public void valueChanged(TreeSelectionEvent e) {

                if (jTree1.getSelectionCount() > 0 ) {
                    TreePath path = jTree1.getSelectionPath();
                    CheckNode node = (CheckNode) (path.getLastPathComponent()); 
                    if (node.isLeaf()) {    
                        if (e.getNewLeadSelectionPath() != e.getOldLeadSelectionPath()) {
                            if(selectedFile != null){
                                selectedFile.emptySet();
                            }
                            /*NIHImage*/ 
                            selectedFile = (NIHImage)node.getUserObject();
                            //System.out.println("cursor is  fffff: " + currCursor);
                            try {    
                                ((NiftiDisplayPanel)jPanel1).setImage(selectedFile);
                            } catch (Exception le){
                                LoggerWrapper.myLogger.log(Level.SEVERE, "Error is {0}", le);
                                DEIDGUI.log("Failed to load image", DEIDGUI.LOG_LEVEL.ERROR);                        
                            }
                        }
                    }
                }                        
            } // </editor-fold>
        });
        // <editor-fold defaultstate="collapsed" desc="AuditTableModel--Not in use">
        /*
        jTableImages.setModel(new AbstractTableModel() {
            
            private String[] columnNames = new String[]{"Selected", "Image"};

            @Override
            public int getRowCount() {
                return DeidData.imageHandler.getInputFilesSize();
            }

            @Override
            public int getColumnCount() {
                return columnNames.length;
            }

            @Override
            public String getColumnName(int i) {
                return columnNames[i];
            }

            @Override
            public Class getColumnClass(int i) {
                Class colClass;
                switch (i) {
                    case 0:
                        colClass = Boolean.class;
                        break;
                    case 1:
                        colClass = NIHImage.class;
                        break;
                    default:
                        colClass = Object.class;
                }
                return colClass;
            }

            @Override
            public boolean isCellEditable(int row, int col) {
                return (col == 0 ? true : false);
            }

            @Override
            public Object getValueAt(int row, int col) {
                Object value;
                switch (col) {
                    case 0:
                        value = DeidData.imageHandler.getInputFiles().get(row).isNeedRedefaced();
                        break;
                    case 1:
                        value = DeidData.imageHandler.getInputFiles().get(row);
                        break;
                    default:
                        value = "Error";
                        break;
                }
                return value;
            }

            @Override
            public void setValueAt(Object o, int row, int col) {
                if (col == 0) {
                    DeidData.imageHandler.getInputFiles().get(row).setNeedRedefaced((Boolean) o);
                }
            }
            
        });
                 */
        // </editor-fold> 

        // <editor-fold defaultstate="collapsed" desc="AuditTableSelectionListener">
        /*
        // Add a selection listener for enabling/disabling the "View Header" button
        jTableImages.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        jTableImages.getSelectionModel().addListSelectionListener(
                new ListSelectionListener() {
                    
                    @Override
                    public void valueChanged(ListSelectionEvent lse) {
                        if (lse.getValueIsAdjusting()) {
                            if(selectedFile != null){
                                selectedFile.emptySet();
                            }
                            //NIHImage [] selection = jTableImages.getSelectedRows();
                           // NIHImage [] selection = DeidData.imageHandler.getInputFiles().get();
                    //jButtonViewHeader.setEnabled(
                            //   DeidData.ConvertedDicomHeaderTable.containsKey(selectedFile)
                            //  ? true : false);
                            selectedFile = DeidData.imageHandler.getInputFiles().get(jTableImages.getSelectedRow());
                           
                                ((NiftiDisplayPanel) jPanel1).setImage(selectedFile);
                        
                            
                            
                        }
                    }
                    
        });
        */
        // </editor-fold>

        
    }

    class NodeSelectionListener extends MouseAdapter {
        // <editor-fold defaultstate="collapsed" desc="NodeSelectionListener">
        JTree tree;

        NodeSelectionListener(JTree tree) {
          this.tree = tree;
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            int x = e.getX();
            int y = e.getY();
            int row = tree.getRowForLocation(x, y);
            TreePath  path = tree.getPathForRow(row);
            //TreePath  path = tree.getSelectionPath();
            if (path != null) {
                CheckNode node = (CheckNode)path.getLastPathComponent();
                boolean isSelected = ! (node.isSelected());
                node.setSelected(isSelected);
                if (node.getSelectionMode() == CheckNode.DIG_IN_SELECTION) {
                    if ( isSelected) {
                      tree.expandPath(path);   
                    } else { //unselect one file
                      tree.collapsePath(path);
                    }
                }
                ((DefaultTreeModel) tree.getModel()).nodeChanged(node);
                // I need revalidate if node is root.  but why?             
                tree.revalidate();
                tree.repaint();

            }
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

        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jButton3 = new javax.swing.JButton();
        jProgressBar1 = new javax.swing.JProgressBar();
        jPanel1 = new NiftiDisplayPanel();
        jSliderSlice = new javax.swing.JSlider();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTree1 = new javax.swing.JTree();

        setTitle("Re-Skull-Stripping");

        jButton1.setText("Select All");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("Unselect All");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jLabel1.setText("Threshold Option (0.0~1.0)");

        jTextField1.setText("0.1");

        jButton3.setText("Start");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jPanel1.setMinimumSize(new java.awt.Dimension(0, 32));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 164, Short.MAX_VALUE)
        );

        jSliderSlice.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSliderSliceStateChanged(evt);
            }
        });

        jLabel2.setText("Select the images to be re-skull-stripped");

        jLabel3.setText("Press Start to redo skull-stripping");

        jButton4.setText("Reset");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jButton5.setLabel("Done");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jScrollPane1.setViewportView(jTree1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 203, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTextField1)
                            .addComponent(jProgressBar1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(jButton5)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jButton4)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel3)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addComponent(jSliderSlice, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addContainerGap())))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jSliderSlice, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(15, 15, 15)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jProgressBar1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton3)
                    .addComponent(jButton4)
                    .addComponent(jButton5))
                .addGap(28, 28, 28))
            .addGroup(layout.createSequentialGroup()
                .addGap(13, 13, 13)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 314, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(jButton2))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jSliderSliceStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSliderSliceStateChanged
        if (/*jTableImages.getSelectedRow() >= 0 ||*/ ((TreeNode) jTree1.getSelectionPath().getLastPathComponent()).isLeaf()) {
            ((NiftiDisplayPanel) jPanel1).setSlice((float) jSliderSlice.getValue() / 100f);
        }
    }//GEN-LAST:event_jSliderSliceStateChanged

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // select all button
        for (NIHImage image : DeidData.imageHandler.getInputFiles()) {
            image.setNeedRedefaced(true);
        }
        //jTableImages.repaint();
        
        ((CheckNode) jTree1.getModel().getRoot()).setSelected(true);
        for (int i = 0; i < jTree1.getRowCount(); i++) { jTree1.expandRow(i);} //expand all nodes
        jTree1.repaint();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // unselect all button
        for (NIHImage image : DeidData.imageHandler.getInputFiles()) {
            image.setNeedRedefaced(false);
        }
        //jTableImages.repaint();
        
        ((CheckNode) jTree1.getModel().getRoot()).setSelected(false);
        //collapse all row if you want
        //for (int i = jTree.getRowCount(); i>0; i--){jTree.collapseRow(i);}
        jTree1.repaint();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // start button
        //jProgressBar1.setValue(0);
        jButton3.setEnabled(false);
        
        
        if (!redoFlag()) {
            wjd = new WarningJdialog(new JFrame(), "Warning", "No images selected!");
        } else {
            jButton5.setEnabled(false);
            new Thread(new Runnable() {
                @Override
                public void run() {

                    jLabel3.setText("ReDefacing...");

                    double d;
                    if (!jTextField1.getText().trim().equals("")) {
                        try {
                            d = Double.parseDouble(jTextField1.getText());
                            if (d >= 0.0 && d <= 1.0) {
                                DeidData.defaceThreshold = jTextField1.getText();
                            }
                        } catch (Exception e) {
                            DeidData.defaceThreshold = "0.1";
                        }
                    }
                    
                    for (NIHImage image : DeidData.imageHandler.getInputFiles()) {
                        if(image.isNeedRedefaced())
                            selection.add(image);                     
                    }
                    
                    
              
                    
                    defaceImages();

                    jLabel3.setText("<html><p>Finished. Press Reset<br/> to start another reDefacing.</p></html>");
                    jProgressBar1.setValue(100);
                    
                    //jTableImages.repaint();
                    
                    ((CheckNode) jTree1.getModel().getRoot()).setSelected(false);
                    for (NIHImage image : DeidData.imageHandler.getInputFiles()) {
                        if(image.isNeedRedefaced())
                            ((CheckNode)((Object)image)).setSelected(true);
                    }
                    jTree1.repaint();
                   
                    
                   // for (NIHImage image: selection) {
                        
                   //     image.initNifti();
                     
                  //  }
                  //  NIHImage selectedFile = DeidData.imageHandler.getInputFiles().get(jTableImages.getSelectedRow());
                  // ((NiftiDisplayPanel) jPanel1).setImage(selectedFile);
                    
                    
                    /*
                   if (jTableImages.getSelectedRow() >= 0 ) {
                       
                        ((NiftiDisplayPanel) jPanel1).setImage(selectedFile);
                       //((NiftiDisplayPanel) jPanel1).setSlice((float) jSliderSlice.getValue() / 100f);
                   }
                    */
                    if (jTree1.getSelectionCount() > 0 ) {                       
                        ((NiftiDisplayPanel) jPanel1).setImage(selectedFile);
                    }
                              
                    //selectedFile = DeidData.imageHandler.getInputFiles().get(audit.imagesTable.getSelectedRow());             
                    //((NiftiDisplayPanel) imagePanel).setImage(selectedFile);

                    //jTableImages.revalidate();
                    //jTableImages.getSelectionModel().clearSelection();
                    //jTableImages.getColumnModel().getSelectionModel().clearSelection();
                    // int lastSelected = jTableImages.getSelectedRow();
                    //jTableImages.repaint();
                    //change montage in the auditpanel.
                    
                    TreePath path;
                    if ( (path= AuditPanel.jTree.getSelectionPath()) != null){                    
                        DefaultMutableTreeNode node = (DefaultMutableTreeNode) (path.getLastPathComponent()); 
                        if ( node.isLeaf()) { 
                            ((NiftiDisplayPanel)AuditPanel.imagePanel).setImage((NIHImage)node.getUserObject());
                        }
                    }
                    
                    jButton5.setEnabled(true);
                    
                // jTableImages.clearSelection();
                 // AuditPanel.imagesTable.valueChanged(null);
                //TableImages.changeSelection(lastSelected, 1, false, false);
                }
                //this.jButton3.setEnabled(true);
            }).start();
        }


    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // Reset button
        jLabel3.setText("Press Start to redo skull-stripping");
        this.jButton3.setEnabled(true);
        //this.jButton5.setEnabled(true);
        jProgressBar1.setValue(0);

    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        // Done button
        jButton5.setEnabled(false);
        for (NIHImage image : selection) {
          //  if (image.isNeedRedefaced()) {  //////////////////potential bug
            image.initNifti();
            //    System.out.println("fuckkk");
            QCPanel.createMontage(image);
            image.emptySet();
        //    }
            image.setNeedRedefaced(false);
        }
        System.out.println("I am going to write montage file");
        QCPanel.writeMontageFile();
        DeidData.defaceThreshold = "0.1";
        this.setVisible(false);
        jButton5.setEnabled(true);
    }//GEN-LAST:event_jButton5ActionPerformed

    private boolean redoFlag() {
        boolean flag = false;
        for (NIHImage image : DeidData.imageHandler.getInputFiles()) {
            if (image.isNeedRedefaced()) {
                flag = true;
                break;
            }
            //jTableImages.setValueAt(new Boolean(false), ndx, 0);
        }
        return flag;

    }
    
    
    private void convertToAnalyze(NIHImage image) {
      
           
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
    
    
    

    private void defaceImages() {

        try {
            ReDefaceTask defaceTask = new ReDefaceTask();
            defaceTask.setProgressBar(jProgressBar1);
            for (NIHImage image : DeidData.imageHandler.getInputFiles()) {

                if (image.isNeedRedefaced()) {
                    convertToAnalyze(image);
                    defaceTask.addInputImage(image);
                }
            }

            synchronized (defaceTask) {
                new Thread(defaceTask).start();
                try {
                    defaceTask.wait();
                } catch (InterruptedException ex) {
                    DEIDGUI.log("bet was interrupted, the defacing result may "
                            + "be incorrect", DEIDGUI.LOG_LEVEL.WARNING);
                }
            }

            DEIDGUI.log("Defaced images");
        } catch (RuntimeException e) {
            DEIDGUI.log("Defacing couldn't be started: " + e.getMessage(),
                    DEIDGUI.LOG_LEVEL.ERROR);
        }
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JProgressBar jProgressBar1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSlider jSliderSlice;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTree jTree1;
    // End of variables declaration//GEN-END:variables

    
    public class CheckRenderer extends JPanel implements TreeCellRenderer {   
    // <editor-fold defaultstate="collapsed">
        protected TreeLabel label;
        protected JCheckBox check;
        
        public CheckRenderer() {
            setLayout(null);
            add(check = new JCheckBox());
            add(label = new TreeLabel());
            check.setBackground(UIManager.getColor("Tree.textBackground"));
            label.setForeground(UIManager.getColor("Tree.textForeground"));
        }

        @Override
        public Component getTreeCellRendererComponent(JTree tree, Object value,
        boolean isSelected, boolean expanded, boolean leaf, int row,
        boolean hasFocus) {
            String stringValue = tree.convertValueToText(value, isSelected,
                expanded, leaf, row, hasFocus);
            
            setEnabled(tree.isEnabled());
            check.setSelected(((CheckNode) value).isSelected());
            label.setFont(tree.getFont());
            stringValue = stringValue.substring(stringValue.lastIndexOf(System.getProperty("file.separator"))+1);
            label.setText(stringValue);
            label.setSelected(isSelected);
            label.setFocus(hasFocus);
            if (leaf) {
                label.setIcon(UIManager.getIcon("Tree.leafIcon"));
                NIHImage image = (NIHImage)((CheckNode) value).getUserObject();
                String strValue = image.getImageNewName();
                label.setText(strValue);
            } else if (expanded) {
                label.setIcon(UIManager.getIcon("Tree.openIcon"));
            } else {
                label.setIcon(UIManager.getIcon("Tree.closedIcon"));
            }
            return this;
        }

        @Override
        public Dimension getPreferredSize() {
          Dimension d_check = check.getPreferredSize();
          Dimension d_label = label.getPreferredSize();
          return new Dimension(d_check.width + d_label.width,
              (d_check.height < d_label.height ? d_label.height
                  : d_check.height));
        }

        @Override
        public void doLayout() {
          Dimension d_check = check.getPreferredSize();
          Dimension d_label = label.getPreferredSize();
          int y_check = 0;
          int y_label = 0;
          if (d_check.height < d_label.height) {
            y_check = (d_label.height - d_check.height) / 2;
          } else {
            y_label = (d_check.height - d_label.height) / 2;
          }
          check.setLocation(0, y_check);
          check.setBounds(0, y_check, d_check.width, d_check.height);
          label.setLocation(d_check.width, y_label);
          label.setBounds(d_check.width, y_label, d_label.width, d_label.height);
        }

        @Override
        public void setBackground(Color color) {
          if (color instanceof ColorUIResource)
            color = null;
          super.setBackground(color);
        }

        public class TreeLabel extends JLabel {
            boolean isSelected;
            boolean hasFocus;

            public TreeLabel() { }
            @Override
            public void setBackground(Color color) {
              if (color instanceof ColorUIResource)
                color = null;
              super.setBackground(color);
            }

            @Override
            public void paint(Graphics g) {
              String str;
              if ((str = getText()) != null) {
                if (0 < str.length()) {
                  if (isSelected) {
                    g.setColor(UIManager
                        .getColor("Tree.selectionBackground"));
                  } else {
                    g.setColor(UIManager.getColor("Tree.textBackground"));
                  }
                  Dimension d = getPreferredSize();
                  int imageOffset = 0;
                  Icon currentI = getIcon();
                  if (currentI != null) {
                    imageOffset = currentI.getIconWidth()
                        + Math.max(0, getIconTextGap() - 1);
                  }
                  g.fillRect(imageOffset, 0, d.width - 1 - imageOffset,
                      d.height);
                  if (hasFocus) {
                    g.setColor(UIManager
                        .getColor("Tree.selectionBorderColor"));
                    g.drawRect(imageOffset, 0, d.width - 1 - imageOffset,
                        d.height - 1);
                  }
                }
              }
              super.paint(g);
            }

            @Override
            public Dimension getPreferredSize() {
              Dimension retDimension = super.getPreferredSize();
              if (retDimension != null) {
                retDimension = new Dimension(retDimension.width + 3,
                    retDimension.height);
              }
              return retDimension;
            }

            public void setSelected(boolean isSelected) {
              this.isSelected = isSelected;
            }

            public void setFocus(boolean hasFocus) {
              this.hasFocus = hasFocus;
            }
        }
    // </editor-fold>
    }
   
    
    public class CheckNode extends DefaultMutableTreeNode {
        // <editor-fold defaultstate="collapsed">
        public final static int SINGLE_SELECTION = 0;

        public final static int DIG_IN_SELECTION = 4;

        protected int selectionMode;

        protected boolean isSelected;

        public CheckNode() {
            this(null);
        }

        public CheckNode(Object userObject) {
            this(userObject, true, false);
        }

        public CheckNode(Object userObject, boolean allowsChildren,
            boolean isSelected) {
            super(userObject, allowsChildren);
            this.isSelected = isSelected;
            setSelectionMode(DIG_IN_SELECTION);
        }

        public void setSelectionMode(int mode) {
          selectionMode = mode;
        }

        public int getSelectionMode() {
          return selectionMode;
        }

        public void setSelected(boolean isSelected) {
            this.isSelected = isSelected;
            if(isLeaf()) {
                ((NIHImage)getUserObject()).setNeedRedefaced(isSelected);
            }
            if ((selectionMode == DIG_IN_SELECTION) && (children != null)) {
                Enumeration e = children.elements();
                while (e.hasMoreElements()) {
                    CheckNode node = (CheckNode) e.nextElement();
                    node.setSelected(isSelected);
                }
            }
        }

        public boolean isSelected() {
            return isSelected;
        }

    // If you want to change "isSelected" by CellEditor,
    /*
     public void setUserObject(Object obj) { if (obj instanceof Boolean) {
     * setSelected(((Boolean)obj).booleanValue()); } else {
     * super.setUserObject(obj); } }
     */
    // </editor-fold>
    }
    
    private void copyTreeNodes (CheckNode node, DefaultMutableTreeNode onode){
        for (int i = 0; i < onode.getChildCount(); i++){
            DefaultMutableTreeNode chnode = (DefaultMutableTreeNode)onode.getChildAt(i);
            node.add(new CheckNode(chnode.getUserObject()));
            CheckNode nnode = (CheckNode)node.getChildAt(i);                  
            copyTreeNodes(nnode,chnode);
        }
    }
    

}
