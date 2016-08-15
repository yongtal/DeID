package dit;

import java.awt.event.MouseEvent;
import java.io.File;
import javax.swing.JTable;
import javax.swing.table.TableModel;

/**
 *
 * @author Christian Prescott
 * GUI class
 */
public class AuditJTable extends JTable {

    public AuditJTable() {
        super();
        setTableHeader(null); // Remove the table's header
        setDefaultRenderer(NIHImage.class, new AuditJTableFileRenderer());
    }

    @Override
    public void setModel(TableModel tm) {
        super.setModel(tm);
        if (getColumnModel().getColumnCount() > 0) {
            getColumnModel().getColumn(0).setWidth(30);
            getColumnModel().getColumn(0).setMinWidth(30);
            getColumnModel().getColumn(0).setMaxWidth(30);
            getColumnModel().getColumn(0).setResizable(false);
        }
    }

    @Override
    public String getToolTipText(MouseEvent e) {
        int row = rowAtPoint(e.getPoint());
        NIHImage item = (NIHImage) getModel().getValueAt(row, 1);
        return item.getImageName();
    }
}
