package dit;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

/**
 *
 * @author Christian Prescott
 */
public class MatchStatusRenderer extends JLabel implements TableCellRenderer{
//    private boolean Matched;
//    
//    public MatchStatusRenderer(boolean matched){
//        Matched = matched;
//    }
    
    @Override
    public Component getTableCellRendererComponent(JTable jtable, Object value, 
        boolean isSelected, boolean hasFocus, int rowIndex, int vColIndex) {
        boolean boolVal = ((Boolean)value).booleanValue();
        setHorizontalAlignment(CENTER);
        if(boolVal){
            setText("MATCH");
            setForeground(new Color(0, 174, 0));
        } else {
            setText("MISMATCH");
            setForeground(new Color(191, 0, 0));
        }
        return this;
    }
    
}
