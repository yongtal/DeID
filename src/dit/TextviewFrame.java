/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dit;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Scanner;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 *
 * @author angelo
 */
public class TextviewFrame extends javax.swing.JFrame {

    /**
     * Creates new form TextviewFrame
     */
    public TextviewFrame(File textFile) {
        initComponents();
        jTable1.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        jTable1.setColumnSelectionAllowed(true);
        jTable1.setRowSelectionAllowed(false);
        jTable1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
         jTable1.getColumnModel().getSelectionModel().addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent lse) {
                if (lse.getValueIsAdjusting()) {
                    DeidData.IdColumn = ((DefaultListSelectionModel) lse.getSource()).getLeadSelectionIndex();
                    // Sort the table by the new ID
                    ArrayList rowList = new ArrayList(Arrays.asList(DeidData.demographicData.getData()));
                    Collections.sort(rowList, new DemoRowComparator());
                    Object[][] rows = new Object[rowList.size()][];
                    rowList.toArray(rows);
                    DeidData.demographicData = new DemographicTableModel(
                            DeidData.demographicData.getDataFieldNames(), rows);
                    jTable1.getColumnModel().getSelectionModel().removeListSelectionListener(this);
                    jTable1.setModel(DeidData.demographicData);
                    jTable1.setColumnSelectionInterval(DeidData.IdColumn, DeidData.IdColumn);
                    jTable1.getColumnModel().getSelectionModel().addListSelectionListener(this);
                }
            }
        });
         if (textFile.canRead()) {
                ReadDemographicFile(textFile);
                DEIDGUI.log("Demographic Data Displayed.");
            } else {
         DEIDGUI.log("Open File Error!");
         } 
        
    }
    
    private void ReadDemographicFile(File demoFile) {
        Scanner inputStream = null;
        try {
            inputStream = new Scanner(demoFile);
        } catch (FileNotFoundException ex) {
            JOptionPane.showMessageDialog(this, "This file could not "
                    + "be opened.", "Invalid Demographic File",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        String[] fields;
        if (inputStream.hasNextLine()) {
            // Read data field headings from first line
            fields = inputStream.nextLine().split("\t");
            //DITGUI.log("Demographic header row: " + StringUtils.join(fields, ','));
        } else {
            // File is empty
            inputStream.close();
            JOptionPane.showMessageDialog(this, "This file does not contain "
                    + "any demographic data.", "Invalid Demographic File",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Read the data and fill a 2D array
        ArrayList<Object[]> rowList = new ArrayList<Object[]>();
        int lineIndex = 1;
        while (inputStream.hasNextLine()) {
            lineIndex++;
            String line = inputStream.nextLine().trim();
            if(!line.isEmpty()){
                Object[] rowData = line.split("\t");

                if (rowData.length != fields.length) {
                    DEIDGUI.log("Mismatched data in demographic file line " + 
                            lineIndex + " (" + rowData.length + "/" + 
                            fields.length + "), some data " + "may be missing",
                            DEIDGUI.LOG_LEVEL.WARNING);
                } else {
                    rowList.add(rowData);
                }
            }
        }
        inputStream.close();

        if (rowList.isEmpty()) {
            JOptionPane.showMessageDialog(this, "This file does not contain "
                    + "any demographic data.", "Invalid Demographic File",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Sort the data by ID
        // The data may be alphanumeric, and the user may change the ID data later.
        Collections.sort(rowList, new DemoRowComparator());

        Object[][] rows = new Object[rowList.size()][];
        rowList.toArray(rows);
        DeidData.demographicData = new DemographicTableModel(fields, rows);
        jTable1.setModel(DeidData.demographicData);
        DEIDGUI.continueButton.setEnabled(true);
        int idColumn = DeidData.demographicData.getColumnNdx("id");
        if (idColumn < 0) {
            idColumn = 0;
        }
        DeidData.IdColumn = idColumn;
        jTable1.setColumnSelectionInterval(idColumn, idColumn);
        DeidData.selectedIdentifyingFields = null;
        DeidData.deselectedIdentifyingFields = null;
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
        jTable1 = new javax.swing.JTable();

        setTitle("Deidentified Demographic Data");
        setPreferredSize(new java.awt.Dimension(600, 400));

        jTable1.setModel(new DemographicTableModel(new String[]{"No data"}, new Object[1][1]));
        jScrollPane1.setViewportView(jTable1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 558, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 344, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    // End of variables declaration//GEN-END:variables
}
