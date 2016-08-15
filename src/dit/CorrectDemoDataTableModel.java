/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dit;

import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author angelo
 * GUI class
 */
public class CorrectDemoDataTableModel extends AbstractTableModel{
    private String[]  columnNames;
    private Object[][] data;
    
    public CorrectDemoDataTableModel(DemographicTableModel dtm){
        columnNames = dtm.getDataFieldNames();
        data = dtm.getData();
        
    
    }
     @Override
    public boolean isCellEditable(int row, int col)
    { 
       return true;       
    
    }
     @Override
    public int getRowCount() {
        return data.length;
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }
    
    public String[] getDataFieldNames(){
        return columnNames;
    }
    
    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex)
    {
        data[rowIndex][columnIndex] = aValue;
    }
    @Override
    public String getColumnName(int col){
        return columnNames[col];
    }

    @Override
    public Object getValueAt(int row, int col) {
        return data[row][col];
    }
    
    public Object[][] getData(){
        return data;
    }
    
    
    
    public int getColumnNdx(String heading){
        int columnNdx = -1;
        for(int ndx = 0; ndx < columnNames.length; ndx++){
            if(columnNames[ndx].toLowerCase().equals(heading.toLowerCase())){
                columnNdx = ndx;
                break;
            }
        }
        return columnNdx;
    }
    
    public Object[] getColumn(int i){
        if(i < 0 || i >= columnNames.length){
            return null;
        }
        
        ArrayList<Object> contents = new ArrayList<Object>();
        for(int ndx = 0; ndx < data.length; ndx++){
            contents.add(data[ndx][i]);
        }
        return contents.toArray();
    }
    
    public Object[] getRow(int i){
        Object[] row = new Object[getColumnCount()];
        for(int ndx = 0; ndx < getColumnCount(); ndx++){
            row[ndx] = getValueAt(i, ndx);
        }
        return row;
    }
    
}
