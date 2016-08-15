/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dit;

import javax.swing.table.AbstractTableModel;


/**
 *
 * @author angelo
 */
public class ManualCorrectTableModel extends AbstractTableModel{
    private String[] columnNames = new String[]{"Image File", "Date File ID", "Status"};
    private Object[][] data;
    private int mismatchCount, matchCount;
    public ManualCorrectTableModel() {
        mismatchCount = 0;
        matchCount = 0;
        data = DeidData.data;
        for(int i = 0; i < data.length; i++)
        {
            if ((Boolean)data[i][2])  matchCount++;
            else if (data[i][0] != null && data[i][1] == null && (Boolean)data[i][2] == false) mismatchCount++;
        }
    }
    
     @Override
    public int getRowCount() {
        return data.length;
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public String getColumnName(int col) {
        return columnNames[col];
    }

    @Override
    public Object getValueAt(int row, int col) {
        return data[row][col];
    }
    @Override
    public boolean isCellEditable(int row, int col)
    { 
       
        return true; 
        
    
    }
    public int getMismatchedImageCount() {
        return mismatchCount;
    }
    
    public int getMatchedImageCount(){
        return matchCount;
    }
}
