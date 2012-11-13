package ugent.bagger.tables;

import ca.odell.glazedlists.EventList;
import java.util.ArrayList;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import org.springframework.richclient.table.support.AbstractObjectTable;

/**
 *
 * @author nicolas
 */
public final class ClassTable<T> extends AbstractObjectTable {
    private ArrayList<T>data;
    private EventList eventList;
    
    public ClassTable(final ArrayList<T>data,String [] cols,String id){
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
    protected ArrayList<T> getData() {
        return data;
    }
    public void reset(final ArrayList<T>data){        
        setData(data);
        refresh();
    }
    protected void setData(final ArrayList<T> data) {                
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
    public T [] getSelections(){
        int[] selected = getTable().getSelectedRows();
        if(selected == null){
            return null;
        }        
        T[]list = (T[]) new Object[selected.length];
        for (int i = 0; i < selected.length; i++) {
            list[i] = (T) getTableModel().getElementAt(selected[i]);
        }
        return list;
    }
    public T getSelected(){
        T [] list = getSelections();        
        return list == null || list.length == 0 ? null:list[0];        
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