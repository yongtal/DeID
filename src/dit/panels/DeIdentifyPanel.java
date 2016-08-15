package dit.panels;

import dit.DEIDGUI;
import dit.DeidData;
import dit.DemographicTableModel;
import dit.LoggerWrapper;
import java.util.Arrays;
import java.util.logging.Level;
import javax.swing.DefaultListModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 *
 * @author christianprescott & angelo
 */
public class DeIdentifyPanel extends javax.swing.JPanel implements WizardPanel {

    private DefaultListModel selectedModel, deselectedModel;

    /**
     * Creates new form DeIdentifyPanel
     */
    public DeIdentifyPanel() {
        
        LoggerWrapper.myLogger.log(Level.INFO, "After match, there are totally {0} input images", DeidData.imageHandler.getInputFilesSize());
        
        
        DEIDGUI.title = "Deidentification Options";
        DEIDGUI.helpButton.setEnabled(true);
        // Populate fields box with data from demographic file   
        initComponents();
        if(DeidData.demographicData== DemographicTableModel.dummyModel)
            return;
        
        if(DeidData.demographicData!= DemographicTableModel.dummyModel)
        {
        selectedModel = new DefaultListModel();
        deselectedModel = new DefaultListModel();
        String[] fields = DeidData.demographicData.getDataFieldNames();
        if (DeidData.selectedIdentifyingFields == null) {
            selectedModel.addElement("Filename and " + 
                    DeidData.demographicData.getColumnName(DeidData.IdColumn));
            ///////////////////////////////////////////////
            ////2.20
            //add 
            /*
            for (int ndx = 0; ndx < fields.length; ndx++) {
                System.out.println(fields[ndx]);
                if (ndx != DeidData.IdColumn) {
                    if (DeidData.demographicData.getValueAt(0, ndx) != null) {
                        String date = DeidData.demographicData.getValueAt(0, ndx).toString();
                        if (date.indexOf('-') >= 0 || date.indexOf('/') >= 0) {
                            selectedModel.addElement(DeidData.demographicData.getColumnName(ndx));
                        }
                    }
                }           
            }     
           */ 
            
            for (int ndx = 0; ndx < fields.length; ndx++) {
                if (ndx != DeidData.IdColumn) {
                    if (fields[ndx].equalsIgnoreCase("name") ||
                          fields[ndx].equalsIgnoreCase("date") ||
                            fields[ndx].equalsIgnoreCase("SSN") ||
                            fields[ndx].equalsIgnoreCase("social") ||
                            fields[ndx].equalsIgnoreCase("address") ||
                            fields[ndx].equalsIgnoreCase("phone") ||
                            fields[ndx].equalsIgnoreCase("email") ||
                            fields[ndx].equalsIgnoreCase("beneficiary") ||
                            fields[ndx].equalsIgnoreCase("license") ||
                            fields[ndx].equalsIgnoreCase("record")  )
                    {
                        selectedModel.addElement(fields[ndx]);
                    }
                    /////////////////////////////////////
                    ///////2.20
                    //add the name of fields which contains date to selectedModel
                    else {
                        if (DeidData.demographicData.getValueAt(0, ndx) != null) {
                            String date = DeidData.demographicData.getValueAt(0, ndx).toString();
                            if (date.indexOf('-') >= 0 || date.indexOf('/') >= 0) {
                                selectedModel.addElement(fields[ndx]);
                            }
                            else {
                                deselectedModel.addElement(fields[ndx]);
                            }
                        }
                    }
                    /*
                    else{ //original
                        deselectedModel.addElement(fields[ndx]);
                    }
                    */
                }
            }
        } else {
            for (String s : DeidData.selectedIdentifyingFields) {
                selectedModel.addElement(s);
            }
            for (String s : DeidData.deselectedIdentifyingFields) {
                deselectedModel.addElement(s);
            }
        }

        DEIDGUI.log("User selected \""
                + DeidData.demographicData.getColumnName(DeidData.IdColumn)
                + "\" as the ID column");
        }

        jListDeselectedFields.setModel(deselectedModel);
        jListSelectedFields.setModel(selectedModel);

        // Allow only one listbox to be active at a time
        jListDeselectedFields.addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent lse) {
                ListSelectionListener listener = jListSelectedFields.getListSelectionListeners()[0];
                jListSelectedFields.removeListSelectionListener(listener);
                jListSelectedFields.getSelectionModel().clearSelection();
                jListSelectedFields.addListSelectionListener(listener);
            }
        });
        jListSelectedFields.addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent lse) {
                ListSelectionListener listener = jListDeselectedFields.getListSelectionListeners()[0];
                jListDeselectedFields.removeListSelectionListener(listener);
                jListDeselectedFields.getSelectionModel().clearSelection();
                jListDeselectedFields.addListSelectionListener(listener);
            }
        });

        DEIDGUI.log("DeIdentifyPanel initialized");
    }
    

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jListDeselectedFields = new javax.swing.JList();
        jScrollPane2 = new javax.swing.JScrollPane();
        jListSelectedFields = new javax.swing.JList();
        jButtonAdd = new javax.swing.JButton();
        jButtonRemove = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();

        jListDeselectedFields.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Name", "Gender", "Handedness" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jListDeselectedFields.setPreferredSize(null);
        jScrollPane1.setViewportView(jListDeselectedFields);

        jListSelectedFields.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "ID", "Name", " " };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jListSelectedFields.setMaximumSize(new java.awt.Dimension(74, 51));
        jListSelectedFields.setMinimumSize(new java.awt.Dimension(74, 51));
        jListSelectedFields.setPreferredSize(null);
        jScrollPane2.setViewportView(jListSelectedFields);

        jButtonAdd.setText("Remove>>");
        jButtonAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAddActionPerformed(evt);
            }
        });

        jButtonRemove.setText("<< Add");
        jButtonRemove.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonRemoveActionPerformed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        jLabel2.setText("<html><p>Remove any variable that might be personal health information, including: <br/> Names, locations (address), dates (and ages for people > 90 years), phone or fax numbers, <br/>email address, social security or medical record numbers, health plan or license numbers, vehicle identifiers, URLs, or IP addresses</p></html>");

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(layout.createSequentialGroup()
                        .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 263, Short.MAX_VALUE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                            .add(jButtonRemove, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .add(jButtonAdd, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 117, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jScrollPane2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 263, Short.MAX_VALUE))
                    .add(jLabel2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                .add(jLabel2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jScrollPane2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 244, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jScrollPane1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 244, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(layout.createSequentialGroup()
                        .add(jButtonAdd)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jButtonRemove)))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        layout.linkSize(new java.awt.Component[] {jScrollPane1, jScrollPane2}, org.jdesktop.layout.GroupLayout.VERTICAL);

    }// </editor-fold>//GEN-END:initComponents

    private void jButtonAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAddActionPerformed
        int[] selection = jListDeselectedFields.getSelectedIndices();

        for (int ndx = selection.length - 1; ndx >= 0; ndx--) {
            selectedModel.add(0, deselectedModel.remove(selection[ndx]));
        }

        // Carry the selection over
        jListDeselectedFields.getSelectionModel().clearSelection();
        jListSelectedFields.getSelectionModel().clearSelection();
        jListSelectedFields.getSelectionModel().addSelectionInterval(
                0, selection.length - 1);
    }//GEN-LAST:event_jButtonAddActionPerformed

    private void jButtonRemoveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonRemoveActionPerformed
        int[] selection = jListSelectedFields.getSelectedIndices();

        for (int ndx = selection.length - 1; ndx >= 0; ndx--) {
            deselectedModel.add(0, selectedModel.remove(selection[ndx]));
        }

        // Carry the selection over
        jListSelectedFields.getSelectionModel().clearSelection();
        jListDeselectedFields.getSelectionModel().clearSelection();
        jListDeselectedFields.getSelectionModel().addSelectionInterval(
                0, selection.length - 1);
    }//GEN-LAST:event_jButtonRemoveActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonAdd;
    private javax.swing.JButton jButtonRemove;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JList jListDeselectedFields;
    private javax.swing.JList jListSelectedFields;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    // End of variables declaration//GEN-END:variables

    @Override
    public WizardPanel getNextPanel() {
        saveSelectedFields();
        return new DeidentifyProgressPanel(DeidData.doDeface);
    }

    @Override
    public WizardPanel getPreviousPanel() {
        saveSelectedFields();
        DeidData.demographicData = DeidData.demographicDataforBack;
        
        return new MatchDataPanel();
    }

    private void saveSelectedFields() {
        Object[] fields = selectedModel.toArray();
        DeidData.selectedIdentifyingFields = Arrays.copyOf(fields, fields.length, String[].class);
        LoggerWrapper.myLogger.log(Level.INFO, "selected fields {0}", Arrays.toString(DeidData.selectedIdentifyingFields));

        fields = deselectedModel.toArray();
        DeidData.deselectedIdentifyingFields = Arrays.copyOf(fields, fields.length, String[].class);
        LoggerWrapper.myLogger.log(Level.INFO, "deselected fields {0}", Arrays.toString(DeidData.deselectedIdentifyingFields));

    }
}
