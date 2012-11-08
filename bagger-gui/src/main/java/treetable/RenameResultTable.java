package treetable;

import javax.swing.JTable;
import javax.swing.table.TableColumnModel;
import org.springframework.richclient.table.support.AbstractObjectTable;

/**
 *
 * @author nicolas
 */
public class RenameResultTable extends AbstractObjectTable {
    public RenameResultTable() {
        super("resultTable", new String[]{
            "nr",
            "from",
            "to",
            "status"
        });
    }

    @Override
    protected void configureTable(JTable table) {
        TableColumnModel tcm = table.getColumnModel();
        tcm.getColumn(0).setPreferredWidth(100);
        tcm.getColumn(1).setPreferredWidth(100);
        tcm.getColumn(2).setPreferredWidth(200);
        tcm.getColumn(3).setPreferredWidth(50);
        tcm.getColumn(4).setPreferredWidth(10);
        tcm.getColumn(5).setPreferredWidth(50);        
        table.setFillsViewportHeight(true);
    }

    @Override
    protected Object[] getDefaultInitialData() {
        return new Object [] {};
    }
}
