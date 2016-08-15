package dit;

import java.awt.Color;
import java.awt.Component;
import java.io.File;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;
import java.util.*;

/**
 *
 * @author Christian Prescott
 * @modified Qingping Meng
 * GUI class
 */
public class AuditJTableFileRenderer extends JLabel implements TableCellRenderer {
    private static boolean firstCall = true;
    @Override
    public Component getTableCellRendererComponent(JTable jtable, Object o, boolean isSelected, boolean cellHasFocus, int i, int i1) {
        setText((DeidData.imageHandler.getInputFiles().get(i).getImageNewName()));
        if(isSelected){
            if(cellHasFocus){
                setBackground(new Color(51, 102, 203));
                setForeground(Color.white);
            } else {
                setBackground(new Color(202, 202, 202));
                setForeground(Color.black);
            }
        } else {
            setBackground(jtable.getBackground());
            setForeground(Color.black);
        }
        setOpaque(true);
        return this;
    }
  
}
