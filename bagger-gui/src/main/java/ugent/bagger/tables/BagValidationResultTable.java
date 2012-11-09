package ugent.bagger.tables;

import ca.odell.glazedlists.EventList;
import java.util.ArrayList;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import org.springframework.richclient.table.support.AbstractObjectTable;
import ugent.bagger.params.BagValidationResult;

/**
 *
 * @author nicolas
 */
public final class BagValidationResultTable extends AbstractObjectTable{    
    private ArrayList<BagValidationResult>data;
    private EventList eventList;
    
    public BagValidationResultTable(final ArrayList<BagValidationResult>data,String [] cols,String id){
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
    protected ArrayList<BagValidationResult> getData() {
        return data;
    }
    public void reset(final ArrayList<BagValidationResult>data){        
        setData(data);
        refresh();
    }
    protected void setData(final ArrayList<BagValidationResult> data) {                
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
    public BagValidationResult [] getSelections(){
        int[] selected = getTable().getSelectedRows();
        if(selected == null){
            return null;
        }
        BagValidationResult[]items = new BagValidationResult[selected.length];
        for (int i = 0; i < selected.length; i++) {
            items[i] = (BagValidationResult) getTableModel().getElementAt(selected[i]);
        }
        return items;
    }
    public BagValidationResult getSelected(){
        BagValidationResult [] items = getSelections();        
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