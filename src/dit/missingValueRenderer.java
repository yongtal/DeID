/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dit;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

/**
 *
 * @author angelo
 */
public class missingValueRenderer extends JLabel implements TableCellRenderer {
    @Override
    public Component getTableCellRendererComponent(JTable jtable, Object o, 
        boolean isSelected, boolean cellHasFocus, int i, int i1) {
        //System.out.println(o.toString());
        setText(o.toString());
    if(o.toString().trim().equals("misV")){
           
                setBackground(Color.red);
                setForeground(Color.white);
           
           
        } else if(isSelected)
        {
            setBackground(Color.LIGHT_GRAY);
            setForeground(Color.black);
        }
    else
        {
            setBackground(jtable.getBackground());
            setForeground(Color.black);
        }
        setOpaque(true);
    return this;
    }
}
