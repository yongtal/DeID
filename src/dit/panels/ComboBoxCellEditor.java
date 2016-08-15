/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dit.panels;

import dit.DEIDGUI;
import dit.DeidData;
import dit.FileUtils;
import dit.MatchStatusRenderer;
import java.awt.Component;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.util.Collection;
import javax.swing.AbstractCellEditor;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.table.TableCellEditor;

public class ComboBoxCellEditor extends AbstractCellEditor implements TableCellEditor {

    private JComboBox jComboBox = new JComboBox();
    boolean cellEditingStopped = false;
    private JLabel lblMatchStat;

    public ComboBoxCellEditor(JLabel lbl) {
        initBox("");
        this.lblMatchStat = lbl;
    }

    private void initBox(String currentSelected) {
        DefaultComboBoxModel model = new DefaultComboBoxModel(new String[]{"None"});
        Object[] demoIDs = DeidData.demographicData.getColumn(DeidData.IdColumn);
        int demoIDNdx = 0;
        while (demoIDNdx < demoIDs.length) {
            model.addElement((String) demoIDs[demoIDNdx]);
            demoIDNdx++;
        }
        jComboBox = new JComboBox();
        jComboBox.setModel(model);
        if (model.getIndexOf(currentSelected) != -1) {
            jComboBox.setSelectedIndex(model.getIndexOf(currentSelected));
        }
        jComboBox.setSelectedIndex(0);

    }

    @Override
    public Object getCellEditorValue() {
        return jComboBox.getSelectedItem().toString();
    }

    @Override
    public Component getTableCellEditorComponent(final JTable table, final Object value, boolean isSelected, final int row, int column) {
        String oldValue = "";
        // System.out.println("SI:"+table.getSelectedRow()+"\tR:"+row);
        if (DeidData.data[row][1] != null) {
            oldValue = DeidData.data[row][1].toString();
        }

        initBox(oldValue);
        jComboBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    fireEditingStopped();
                    int i = row;
                    if (i >= 0) {
                        String filename = (String) DeidData.data[i][0];
                        //System.out.println(i);
                        if (e.getStateChange() == ItemEvent.SELECTED) {
                            // Item was just selected
                            if (DeidData.data[i][0] != null) {
                                if (jComboBox.getSelectedItem().toString().equals("None")) {
                                    DeidData.data[i][1] = jComboBox.getSelectedItem().toString();
                                    DeidData.data[i][2] = new Boolean(false);
                                } else {
                                    DeidData.data[i][1] = (jComboBox.getSelectedItem().toString());
                                    DeidData.data[i][2] = new Boolean(true);

                                    DeidData.imageHandler.findImageByDisplayName(filename).setIdInDataFile(jComboBox.getSelectedItem().toString());
                                    // DeidData.IdFilename.put(MatchDataPanel.displayTofile.get(filename), jComboBox.getSelectedItem().toString() );
                                }
                                Object[][] data = DeidData.data;
                                int checkFlag = 0;
                                for (int ii = 0; ii < data.length; ii++) {
                                    if (ii != i && (Boolean) data[ii][2] == true && data[ii][1].toString().equals(jComboBox.getSelectedItem().toString())) {
                                    }
                                    if (!(Boolean) data[ii][2]) {
                                        checkFlag = 1;
                                    }
                                }
                                if (checkFlag == 0) {
                                    DEIDGUI.continueButton.setEnabled(true);
                                } else {
                                    DEIDGUI.continueButton.setEnabled(false);
                                }
                                // System.out.println(comboBox.getSelectedItem().toString());                                
                                table.setValueAt(value, i, 1);
                                table.setValueAt("true", i, 2);
                                table.getColumnModel().getColumn(2).setCellRenderer(new MatchStatusRenderer());
                                //cb.setSelectedItem(cb.getSelectedItem());
                                table.clearSelection();

                            }

                        }
                        findUnmatchCount();
                    }
                }

            }
        });
        jComboBox.addPopupMenuListener(new PopupMenuListener() {
            @Override
            public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
                cellEditingStopped = false;
            }

            @Override
            public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
                cellEditingStopped = true;
                fireEditingCanceled();
            }

            @Override
            public void popupMenuCanceled(PopupMenuEvent e) {
            }
        });
        return jComboBox;
    }

    @Override
    public boolean stopCellEditing() {
        return cellEditingStopped;
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
}