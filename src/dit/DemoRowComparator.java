package dit;

import java.util.Comparator;

/**
 * Comparator for mixed string and int types
 * @author Christian Prescott
 * for sort
 */
public class DemoRowComparator implements Comparator{
    @Override
    public int compare(Object t, Object t1) {
        int columnNdx = DeidData.IdColumn;
        Object[] a = (Object[]) t;
        Object[] b = (Object[]) t1;
        return ((String)a[columnNdx]).compareToIgnoreCase((String) b[columnNdx]);
    }
}
