/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dit;

//import static dit.ManualMatchTableModel.displayIntoTable;
import dit.panels.MatchDataPanel;
import java.awt.Component;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.Vector;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellRenderer;

/**
 *
 * @author QM
 */
public class HeaderFileTableModel extends AbstractTableModel {

    private String[] columnNames = new String[]{"Attribute", "Value"};
    private Object[][] data;
    private Object Description;
    private LinkedHashMap headerFile;
    private HashMap<Object, Object> changedAttr;
    private NIHImage curimage;
    
    public HeaderFileTableModel()
    {
        data = new Object[0][0];
        Description = new Object();
        changedAttr = new HashMap<Object, Object>();
    }
    
    public HeaderFileTableModel(NIHImage image)
    {
        curimage = image;
        headerFile = curimage.getHeader();
        Description = new Object();
        changedAttr = new HashMap<Object, Object>();
        readHeader();
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
    
    /*
        for now let it only be cahnged when the row is "Description"
    */
    @Override
    public boolean isCellEditable(int row, int col) {
        if (col == 0) return false;
        String attribute = data[row][0].toString();
        return attribute.equals("Description")||
                attribute.equals("Generated")||
                attribute.equals("Scannum")||
                attribute.equals("Patient ID")||
                attribute.equals("Exp Date")||
                attribute.equals("Exp Time")||
                attribute.equals("db_name") ||
                attribute.equals("Data type string") ||
                attribute.equals("History");
    }
    
    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex)
    {
        if (this.isCellEditable(rowIndex, columnIndex)) {
            data[rowIndex][columnIndex] = aValue;
            changedAttr.put(data[rowIndex][0], aValue); //save data in changed Map
            fireTableDataChanged();
        }
    }
    
    public void revert(){
        readHeader();
        changedAttr.clear();
        fireTableDataChanged();
    }
    
    /*
        only use when press the "Done" button and info has changed.
    */
    public void changeHeaderFile(){
        if ( changedAttr.isEmpty() ) return;
        else {
            Set<Object> tmpAttr =  changedAttr.keySet();
            Object[] attributes = tmpAttr.toArray(new Object[tmpAttr.size()]);
            for (int i = 0; i < attributes.length; i++){
                if ( headerFile.containsKey(attributes[i]) ) {
                    headerFile.put(attributes[i], changedAttr.get(attributes[i]));
                   // DeidData.imageHandler.findImageByDisplayName(curimage.getImageDisplayName())
                   // .getHeader().put(attributes[i], changedAttr.get(attributes[i]));
                    curimage.getHeader().put(attributes[i], changedAttr.get(attributes[i]));
                    //System.out.println(curimage.getHeader().get(attributes[i]));
                }
            }
            curimage.changeHeader();
       
        }
    }
    
    /*
    read from header, used in init and revert. 
    */
    
    private void readHeader(){ 
        Set<Object> tmpAttr =  headerFile.keySet();
        Object[] attributes = tmpAttr.toArray(new Object[tmpAttr.size()]);
        
        Object[][] dataArray = new Object[attributes.length][2];
        for (int ndx = 0; ndx < attributes.length; ndx++) {
            dataArray[ndx][0] = attributes[ndx];
            dataArray[ndx][1] = headerFile.get(attributes[ndx]);
            if (attributes[ndx].toString().equals("Description"))
                Description = headerFile.get(attributes[ndx]);
        }
        data = dataArray;
    }
    
    
    
    
    public boolean isHeaderChanged(){
        Set<Object> tmpAttr =  changedAttr.keySet();
        Object[] attributes = tmpAttr.toArray(new Object[tmpAttr.size()]);
        
        for (int i = 0; i < attributes.length; i++){
            if ( headerFile.get(attributes[i]) != null ){
                /*
                String old = headerFile.get(attributes[i]).toString();
                String newS = changedAttr.get(attributes[i]).toString();
                StringTokenizer ostn = new StringTokenizer(old);
                int onum = ostn.countTokens();
                StringTokenizer nstn = new StringTokenizer(newS);
                int nnum = nstn.countTokens();

                if (onum != nnum){
                    return true;
                }
                else {
                    while ( ostn.hasMoreTokens() ){
                        //System.out.println("aaaaaa");
                        if (! ostn.nextToken().toString().equals(nstn.nextToken().toString())) {
                            //System.out.println("hehehehele");
                            return true;
                        }
                    }
                }
                */
                if ( !headerFile.get(attributes[i]).equals(changedAttr.get(attributes[i]))) {
                    return true;
                }
            } 
        }
        return false;
    }
}
