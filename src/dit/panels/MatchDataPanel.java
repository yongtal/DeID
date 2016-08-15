package dit.panels;

import dit.*;
import dit.DEIDGUI;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.logging.Level;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;

/**
 *
 * @author christianprescott and angelo
 */
public class MatchDataPanel extends javax.swing.JPanel implements WizardPanel {

    private ManualMatchTableModel mmodel;
    private DummyFileTableModel dmodel;
    private ManuallyCorrectFrame mcp;
    private removeImageFrame rif;
    private ManualCorrectTableModel cmodel;
    public static HashMap<String, String> displayTofile;

    /**
     * Creates new form MatchDataPanel
     */
    public MatchDataPanel() {
       /*
        for(NIHImage image : DeidData.imageHandler.getInputFiles()){
          System.out.println("wahahaahah:  "+image.getIdInDataFile());
        }
        */
        initComponents();
        DEIDGUI.title = "Data Matching";
        DEIDGUI.helpButton.setEnabled(true);
        DEIDGUI.jButtonMisHelp.setVisible(false);
        lblDummy.setVisible(false);
        boolean isSearchByPath = cbxSearchByPath.isSelected();
        boolean isMultipleLink = cbxMultiMatch.isSelected();

        displayTofile = new HashMap<>();

        jTable2.getTableHeader().setReorderingAllowed(false);

        InitModel(); 
        //get 2nd-4th information show in QC and Montage.pdf.

        DEIDGUI.log("MatchDataPanel initialized");

    }
    
        ////////////////////////////////////////////////
        ///////2.13
        //add 2nd - 4th data info to NIHimage-montage.
        //NOW The image and data have matched.
    private void getMontageInfo(){
        for(NIHImage image : DeidData.imageHandler.getInputFiles()){
            image.initMontageInfo(); //to empty first
            //System.out.println("wahahaahah:  "+);            
            String imageIDNdx = image.getIdInDataFile();
            //get generalize information
            Object[] demoIDs = DeidData.demographicData.getColumn(DeidData.IdColumn);
            
            LoggerWrapper.myLogger.log(Level.INFO, "totally {0} columns in demo file", DeidData.demographicData.getColumnCount());

            String [] tmp_name = new String [DeidData.demographicData.getColumnCount()];
            
            for (int i = 0; i < DeidData.demographicData.getColumnCount(); i++)
                tmp_name[i] = DeidData.demographicData.getColumnName(i);
            LoggerWrapper.myLogger.log(Level.INFO, "Column names: {0}", Arrays.toString(tmp_name));

            int demoIDNdx = 0;
            while(demoIDNdx < demoIDs.length){
                if(! imageIDNdx.equals( (String)demoIDs[demoIDNdx]))
                    demoIDNdx++;
                else{ //find the match
   
                    int end = Math.min(DeidData.demographicData.getColumnCount(),4);//if the demographic data less than four columns, get min
                    for(int i = 1; i < end; i++){
                        String SecColName = DeidData.demographicData.getColumnName(i);
                        if ( SecColName.equals(""))  //in case
                               SecColName = "EmptyColumn";
                        Object SecColVal =  DeidData.sourcedemographicData.getValueAt(demoIDNdx,i);               
                        if( i == 1) {
                            image.set2ndCol(SecColName, (String) SecColVal);
                            image.set2ndCol(2, (String) SecColVal);
                        }
                        else if( i == 2) {
                            image.set3rdCol(SecColName, (String) SecColVal);
                            image.set3rdCol(2, (String) SecColVal);
                        }
                        else if( i == 3) {
                            image.set4thCol(SecColName, (String) SecColVal);
                            image.set4thCol(2, (String) SecColVal);
                        }
                    }
                  //System.out.println((String) DeidData.demographicData.getValueAt(demoIDNdx,DeidData.IdColumn));
                  break;
                }     
            }
            /*
            //get information before generalization
            int sidRow= DeidData.sourcedemographicData.getColumnNdx(LoadDemoPanel.IdHeaderName);
            //System.out.println("the sid Row is: "+ sidRow);
            if(sidRow == -1) // a potential bug
                sidRow = 0;
            Object[] sdemoIDs = DeidData.sourcedemographicData.getColumn(sidRow);
            int sdemoIDNdx = 0;
            while(sdemoIDNdx < sdemoIDs.length){
                if(! imageIDNdx.equals((String)sdemoIDs[sdemoIDNdx]))
                    sdemoIDNdx++;
                else{
                 int end = Math.min(DeidData.demographicData.getColumnCount(),4);//if the demographic data less than four columns, get min
                 for(int i = 1; i < end; i++){
                        //String SecColName = DeidData.demographicData.getColumnName(i);
                        Object sourceVal =  DeidData.sourcedemographicData.getValueAt(sdemoIDNdx,i);               
                        if( i == 1)
                            image.set2ndCol(2, (String) sourceVal);
                        else if( i == 2)
                            image.set3rdCol(2, (String) sourceVal);
                        else if( i == 3)
                            image.set4thCol(2, (String) sourceVal);
                    }
                  break;               
                }           
            }
            */
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

        jLabel4 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        txtMatchpattern = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        btnMatch = new javax.swing.JButton();
        cbxSearchByPath = new javax.swing.JCheckBox();
        btnRemoveSel = new javax.swing.JButton();
        cbxMultiMatch = new javax.swing.JCheckBox();
        lblMatchStat = new javax.swing.JLabel();
        lblDummy = new javax.swing.JLabel();

        setPreferredSize(new java.awt.Dimension(840, 400));

        jLabel4.setText("<html><p>Please scroll below to ensure that image files and data IDs have been correctly matched.  Mismatched data should be corrected before selecting Continue>.</p><p>&nbsp;</p></html>");

        jTable2.setModel(new MatchTableModel());
        jScrollPane3.setViewportView(jTable2);

        txtMatchpattern.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtMatchpatternActionPerformed(evt);
            }
        });

        jLabel1.setText("Matching pattern");

        btnMatch.setText("Match");
        btnMatch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMatchActionPerformed(evt);
            }
        });

        cbxSearchByPath.setText("Search the path for matching information");
        cbxSearchByPath.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbxSearchByPathActionPerformed(evt);
            }
        });

        btnRemoveSel.setText("Remove Selected Image");
        btnRemoveSel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRemoveSelActionPerformed(evt);
            }
        });

        cbxMultiMatch.setText("Multiple images linking to one subject");
        cbxMultiMatch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbxMultiMatchActionPerformed(evt);
            }
        });

        lblDummy.setForeground(new java.awt.Color(255, 0, 0));
        lblDummy.setText("Some functions are not available since no data file is selected.");

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(layout.createSequentialGroup()
                        .add(jScrollPane3)
                        .addContainerGap())
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                        .add(jLabel4, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                        .add(110, 110, 110))
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                        .add(lblMatchStat, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())
                    .add(layout.createSequentialGroup()
                        .add(15, 15, 15)
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(layout.createSequentialGroup()
                                .add(cbxSearchByPath, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 301, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(jLabel1))
                            .add(cbxMultiMatch))
                        .add(8, 8, 8)
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                            .add(layout.createSequentialGroup()
                                .add(txtMatchpattern, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 121, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(btnMatch, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 66, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                            .add(btnRemoveSel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 171, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap(190, Short.MAX_VALUE))
                    .add(layout.createSequentialGroup()
                        .add(lblDummy)
                        .add(0, 0, Short.MAX_VALUE))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(jLabel4, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(lblDummy)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(cbxSearchByPath)
                    .add(jLabel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 30, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(txtMatchpattern, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(btnMatch))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(cbxMultiMatch)
                    .add(btnRemoveSel))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jScrollPane3, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 231, Short.MAX_VALUE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(lblMatchStat))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnMatchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMatchActionPerformed
        // TODO add your handling code here:
        String matchingkey = txtMatchpattern.getText();
        boolean isSearchByPath = cbxSearchByPath.isSelected();
        boolean isMultipleLink = cbxMultiMatch.isSelected();
        match(matchingkey, isSearchByPath, isMultipleLink);
        findUnmatchCount();

    }//GEN-LAST:event_btnMatchActionPerformed

    private void txtMatchpatternActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtMatchpatternActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtMatchpatternActionPerformed

    private void btnRemoveSelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRemoveSelActionPerformed
        // TODO add your handling code here:

        int[] selection = jTable2.getSelectedRows();
        int selectNum = selection.length;
        if (selectNum > 0) {
            int ii = JOptionPane.showConfirmDialog(null,
                    "Are you sure to remove selected images?", "Confirmation",
                    JOptionPane.YES_NO_OPTION);
            if (ii == JOptionPane.OK_OPTION) {
                for (int s = 0; s < selectNum; s++) {
                    DeidData.imageHandler.getInputFiles().remove(selection[s] - s);
                }
            } else {
                return;
            }

        } else {
            return;
        }

       InitModel();

    }//GEN-LAST:event_btnRemoveSelActionPerformed

    private void cbxSearchByPathActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbxSearchByPathActionPerformed
        String matchingkey = txtMatchpattern.getText();
        boolean isSearchByPath = cbxSearchByPath.isSelected();
        boolean isMultipleLink = cbxMultiMatch.isSelected();
        match(matchingkey, isSearchByPath, isMultipleLink);
        findUnmatchCount();
    }//GEN-LAST:event_cbxSearchByPathActionPerformed

    private void cbxMultiMatchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbxMultiMatchActionPerformed
        String matchingkey = txtMatchpattern.getText();
        boolean isSearchByPath = cbxSearchByPath.isSelected();
        boolean isMultipleLink = cbxMultiMatch.isSelected();
        match(matchingkey, isSearchByPath, isMultipleLink);
        findUnmatchCount();
    }//GEN-LAST:event_cbxMultiMatchActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnMatch;
    private javax.swing.JButton btnRemoveSel;
    private javax.swing.JCheckBox cbxMultiMatch;
    private javax.swing.JCheckBox cbxSearchByPath;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTable jTable2;
    private javax.swing.JLabel lblDummy;
    private javax.swing.JLabel lblMatchStat;
    private javax.swing.JTextField txtMatchpattern;
    // End of variables declaration//GEN-END:variables

    @Override
    public WizardPanel getNextPanel() {
        LoggerWrapper.myLogger.log(Level.INFO, "Before deidentifying:  Max:{0}  Free:{1}  available:{2}", new Object[]{java.lang.Runtime.getRuntime().maxMemory(), java.lang.Runtime.getRuntime().freeMemory(), java.lang.Runtime.getRuntime().totalMemory()});

        DeidData.demographicDataforBack = DeidData.demographicData;
        forceMatch();
        //if(! DeidData.isNoData)
        getMontageInfo(); 
        try {
            DEIDGUI.jButtonMisHelp.setVisible(false);
            mcp.setVisible(false);
            rif.setVisible(false);
        } catch (Exception e) {
            return new DeIdentifyPanel();
        }
        
        return new DeIdentifyPanel();
    }

    @Override
    public WizardPanel getPreviousPanel() {
        //DeidData.demographicData = null;
        DEIDGUI.jButtonMisHelp.setVisible(false);
        return new LoadDemoPanel();
    }

    private void match(String key, boolean isPath, boolean isMultiSearch) {
        String matchingkey = key;
        boolean isSearchByPath = isPath;
        boolean isMultipleLink = isMultiSearch;

        mmodel = new ManualMatchTableModel(DeidData.imageHandler.getInputFiles(), DeidData.demographicData.getColumn(DeidData.IdColumn), matchingkey, isSearchByPath, isMultipleLink);
        jTable2.setModel(mmodel);
        jTable2.getColumnModel().getColumn(2).setCellRenderer(new MatchStatusRenderer());

        if (mmodel.getMismatchedImageCount() > 0 || mmodel.getMatchedImageCount() == 0) {

            DEIDGUI.continueButton.setEnabled(false);
        } else {
            DEIDGUI.continueButton.setEnabled(true);
        }


        cmodel = new ManualCorrectTableModel();

        jTable2.setModel(cmodel);
        jTable2.getColumnModel().getColumn(2).setCellRenderer(new MatchStatusRenderer());

        jTable2.getColumnModel().getColumn(1).setCellEditor((new ComboBoxCellEditor(lblMatchStat)));
        jTable2.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        //System.out.println(model.getMismatchedImageCount());


    }

    private void findUnmatchCount() {
        Collection<String> ids = DeidData.imageHandler.getAllIDs();
        int totalID = DeidData.demographicData.getColumn(DeidData.IdColumn).length;
        for (Object obj : DeidData.demographicData.getColumn(DeidData.IdColumn)) {
            String id = (String) obj;
            if (ids.contains(id)) {
                totalID--;
            }
        }
        lblMatchStat.setText(totalID + " cases have no images");
    }
    
    private void InitModel(){
     if (DeidData.isNoData) {
            lblDummy.setVisible(true);
            btnMatch.setEnabled(false);
            dmodel = new DummyFileTableModel(DeidData.imageHandler.getInputFiles());
            jTable2.setModel(dmodel);
            jTable2.getColumnModel().getColumn(2).setCellRenderer(new MatchStatusRenderer());
            cbxMultiMatch.setEnabled(false);
            cbxSearchByPath.setEnabled(false);
            DEIDGUI.continueButton.setEnabled(true);
        } else {

            String matchingkey = txtMatchpattern.getText();
            boolean isSearchByPath = cbxSearchByPath.isSelected();
            boolean isMultipleLink = cbxMultiMatch.isSelected();


            mmodel = new ManualMatchTableModel(DeidData.imageHandler.getInputFiles(), DeidData.demographicData.getColumn(DeidData.IdColumn), matchingkey, isSearchByPath, isMultipleLink);
            jTable2.setModel(mmodel);
            jTable2.getColumnModel().getColumn(2).setCellRenderer(new MatchStatusRenderer());
            findUnmatchCount();
            if (mmodel.getMismatchedImageCount() > 0 || mmodel.getMatchedImageCount() == 0) {
                // Ensure that there is at least one matched image, and that
                // there are no unmatched images, otherwise OK
                //System.out.println(mmodel.getMismatchedImageCount()+" and " + mmodel.getMatchedImageCount());
                // wjd = new WarningJdialog(new JFrame(), "Warning", "Mismatch for each image must be removed.");
                DEIDGUI.continueButton.setEnabled(false);
            } else {
                DEIDGUI.continueButton.setEnabled(true);
            }


            cmodel = new ManualCorrectTableModel();

            jTable2.setModel(cmodel);
            jTable2.getColumnModel().getColumn(2).setCellRenderer(new MatchStatusRenderer());

            jTable2.getColumnModel().getColumn(1).setCellEditor(new ComboBoxCellEditor(lblMatchStat));
            jTable2.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        }
    }
    
    /////////////////////////////////////////
    /////2.15
    //force match the NIHimage and data before goto next panel;
    private void forceMatch(){
        for(int i = 0; i < jTable2.getRowCount(); i++){
            Object imgDisplayName = jTable2.getValueAt(i, 0);
            Object MatchId = jTable2.getValueAt(i, 1);
            //System.out.println(jTable2.getValueAt(i, 0)+"  "+jTable2.getValueAt(i,1));
            if(DeidData.imageHandler.findImageByDisplayName(imgDisplayName.toString()) != null){
                NIHImage image = DeidData.imageHandler.findImageByDisplayName(imgDisplayName.toString());
                if((!MatchId.equals("missing"))&&(!image.getIdInDataFile().equals(MatchId))){
                    image.setIdInDataFile(MatchId.toString());                
                }
                //System.out.println(image.getIdInDataFile()+"  "+ MatchId);
            }
        }
    }
}
