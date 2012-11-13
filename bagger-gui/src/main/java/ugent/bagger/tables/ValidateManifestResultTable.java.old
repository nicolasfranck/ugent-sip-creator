package ugent.bagger.tables;

import ca.odell.glazedlists.EventList;
import java.util.ArrayList;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import org.springframework.richclient.table.support.AbstractObjectTable;
import ugent.bagger.params.ValidateManifestResult;

/**
 *
 * @author nicolas
 */
public final class ValidateManifestResultTable extends AbstractObjectTable{    
    private ArrayList<ValidateManifestResult>data;
    private EventList eventList;
    
    public ValidateManifestResultTable(final ArrayList<ValidateManifestResult>data,String [] cols,String id){
        super(id,cols);                 
        setData(data);
    }            
    @Override
    protected void configureTable(JTable table) {
        table.setFillsViewportHeight(true);
    }
    @Override
    protected Object[] getDefaultInitialData(){               
       return getData().toArray();
    }
    protected ArrayList<ValidateManifestResult> getData() {
        return data;
    }
    public void reset(final ArrayList<ValidateManifestResult>data){        
        setData(data);
        refresh();
    }
    protected void setData(final ArrayList<ValidateManifestResult> data) {                
        this.data = data;    
    }   
    public void refresh(){        
        EventList rows = getFinalEventList();        
        rows.getReadWriteLock().writeLock().lock();        
        try {
            rows.clear();
            rows.addAll(getData());                       
        } finally {
           rows.getReadWriteLock().writeLock().unlock();
           //belangrijk!
           ((AbstractTableModel)getTable().getModel()).fireTableDataChanged();
        }
    }                     
    public ValidateManifestResult [] getSelections(){
        int[] selected = getTable().getSelectedRows();
        if(selected == null){
            return null;
        }
        ValidateManifestResult[]items = new ValidateManifestResult[selected.length];
        for (int i = 0; i < selected.length; i++) {
            items[i] = (ValidateManifestResult) getTableModel().getElementAt(selected[i]);
        }
        return items;
    }
    public ValidateManifestResult getSelected(){
        ValidateManifestResult [] items = getSelections();        
        return items == null || items.length == 0 ? null:items[0];        
    }        
    @Override
    protected boolean isMultipleColumnSort() {
        return false;
    }         
    @Override
    protected JComponent createControl() {
        JComponent comp = super.createControl();
        //schakel sorteerder uit!       
        clearSort();
        return comp;
    }
    public void clearSort(){         
        getTableSorter().clearComparator();
    }    
}