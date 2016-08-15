/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dit;

import javax.swing.JComboBox;
import javax.swing.DefaultCellEditor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.*;
/**
 *
 * @author angelo
 */
public class ManuallyCorrectFrame extends javax.swing.JFrame {
    private ManualCorrectTableModel model;
    private WarningJdialog wjd;
    /**
     * Creates new form ManuallyCorrectFrame
     */
    public ManuallyCorrectFrame() {
        initComponents();
       
        this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        model = new ManualCorrectTableModel();       
       
        jTable1.setModel(model);
        jTable1.getColumnModel().getColumn(2).setCellRenderer(new MatchStatusRenderer());
        final JComboBox comboBox = new JComboBox();
        Object[] demoIDs = DeidData.demographicData.getColumn(DeidData.IdColumn);
        int demoIDNdx = 0;
        while(demoIDNdx < demoIDs.length){
            comboBox.addItem((String)demoIDs[demoIDNdx]);
            demoIDNdx++;
        
        }        
        comboBox.addItem("None");
        jTable1.getColumnModel().getColumn(1).setCellEditor(new DefaultCellEditor(comboBox));
        jTable1.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        //System.out.println(model.getMismatchedImageCount());
        comboBox.setEditable(true);
        
        /*comboBox.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
        int i = jTable1.getSelectedRow();
        System.out.println(i);
        if (i >= 0)
        {
        //DeidData.data[i][1] = ((JComboBox) e.getSource()).getSelectedItem(); 
        //DeidData.data[i][2] = new Boolean(true);
        //String filename = (String)DeidData.data[i][0];
        //filename = filename.replace(".nii", "");
        //DeidData.IdFilename.put(filename, (String)((JComboBox) e.getSource()).getSelectedItem() );
        jTable1.setValueAt((String)((JComboBox) e.getSource()).getSelectedItem(), i, 1);    
        jTable1.setValueAt("true", i, 2);
        jTable1.getColumnModel().getColumn(2).setCellRenderer(new MatchStatusRenderer());
        
        }      
        //jTable1.getColumnModel().getColumn(2).setCellRenderer(new MatchStatusRenderer());
        //System.out.println("You chose " + ((JComboBox) e.getSource()).getSelectedItem() + "!");
      }
        
    });*/
        comboBox.addItemListener(new ItemListener(){
        @Override
        public void itemStateChanged(ItemEvent e){
            int i = jTable1.getSelectedRow();
            if (i>=0){
            JComboBox cb = (JComboBox)e.getSource();
            Object item = e.getItem();
            String filename = (String)DeidData.data[i][0];
        System.out.println(i);
        if (e.getStateChange() == ItemEvent.SELECTED) {
            // Item was just selected
            if (DeidData.data[i][0]!=null)
            {
                if (cb.getSelectedItem().toString().equals("None")) {
                    DeidData.data[i][1] = null; 
                    DeidData.data[i][2] = new Boolean(false);
                }
                else {
                    DeidData.data[i][1] = (cb.getSelectedItem()); 
                    DeidData.data[i][2] = new Boolean(true);
                   /* filename = filename.replace(".nii", "");
                    try {filename = filename.replace(".gz","");}
                    catch (Exception ex){
                    System.out.println("Fail to add filename and id pair.");
                    }*/
                    DeidData.IdFilename.put(filename, cb.getSelectedItem().toString() );
                   Object[][] data = DeidData.data;
            for(int ii = 0; ii < data.length; ii++)
            {
            if (ii != i && (Boolean)data[ii][2] == true && data[ii][1].toString().equals(cb.getSelectedItem().toString())) 
            { 
                wjd = new WarningJdialog(new JFrame(), "Warning", "There exists another row where has the same ID matched with a different image.");                
                break;
            }
            }
                    DeidData.correctflag = 1;
                }
            
            
               // System.out.println(comboBox.getSelectedItem().toString());
            jTable1.setValueAt(cb.getSelectedItem().toString(), i, 1);    
            jTable1.setValueAt("true", i, 2);
            jTable1.getColumnModel().getColumn(2).setCellRenderer(new MatchStatusRenderer());
            //cb.setSelectedItem(cb.getSelectedItem());
            jTable1.clearSelection();
            }   
        }
           
       
            
        
        
        }
        
        }
        });
        
        
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
        jButton1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(jTable1);

        jButton1.setLabel("Close");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 492, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(28, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 299, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButton1)
                .addContainerGap(25, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
            
        
        this.setVisible(false);
    }//GEN-LAST:event_jButton1ActionPerformed

    /**
     * @param args the command line arguments
     */
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    // End of variables declaration//GEN-END:variables
}
