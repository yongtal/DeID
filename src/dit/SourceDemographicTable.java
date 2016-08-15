package dit;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
//import javax.swing.table.AbstractTableModel;

public class SourceDemographicTable {
    private static String[] columnNames;
    private static Object[][] data;
    
    //public static SourceDemographicTable sourcedummy=new SourceDemographicTable(null, null);

    public SourceDemographicTable(String[] columns, Object[][] rows) {
        columnNames = new String[columns.length];
        for(int i = 0; i < columns.length; i++){
            columnNames[i] = new String(columns[i]);
        }
        //columnNames = Arrays.copyOf(columns,columns.length);
        data = new Object[rows.length][];
        for (int i=0; i <rows.length; i++) {
            data[i] = Arrays.copyOf(rows[i], rows[i].length);
        }          
    }
    
    
    public int getRowCount() {
        return data.length;
    }

    
    public int getColumnCount() {
        return columnNames.length;
    }
    
    public String[] getDataFieldNames(){
        return columnNames;
    }
    
    
    public String getColumnName(int col){
        return columnNames[col];
    }

    
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
