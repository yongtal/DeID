/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dit;

import static dit.ManualMatchTableModel.displayIntoTable;
import dit.panels.MatchDataPanel;
import java.io.File;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Vector;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author QM
 */
public class DummyFileTableModel extends AbstractTableModel {

    private String[] columnNames = new String[]{"Image File", "Date File ID", "Status"};
    private Object[][] data;
    
    public DummyFileTableModel()
    {
        data = new Object[0][0];
    }
    
    public DummyFileTableModel(Vector<NIHImage> images)
    {
        ArrayList<Object[]> dataList = new ArrayList<>();
       // DeidData.IdFilename = new Hashtable<String, String>();
        Iterator<NIHImage> imageIt = images.iterator();
        NIHImage curFile = null;
        try {
            curFile = imageIt.next();
        } catch (NoSuchElementException e) {
            curFile = null;
        }
        while(curFile!=null)
        {
            dataList.add(new Object[]{curFile.getImageDisplayName(),"missing" , new Boolean(true)});
           // DeidData.IdFilename.put(fileintoTable(curFile), "Missing ID" );
            curFile.setIdInDataFile("Missing ID");
            try {
                curFile = imageIt.next();
            } catch (NoSuchElementException e) {
                curFile = null;
            }
        }
        Object[][] dataArray = new Object[dataList.size()][3];
        for (int ndx = 0; ndx < dataList.size(); ndx++) {
            dataArray[ndx] = dataList.get(ndx);
        }
        data = dataArray;
        DeidData.data = data;
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
   
}
