package ugent.bagger.tables;

import ca.odell.glazedlists.EventList;
import java.util.ArrayList;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import org.springframework.richclient.table.support.AbstractObjectTable;
import ugent.bagger.params.CreateBagResult;

/**
 *
 * @author nicolas
 */
public class CreateBagResultTable extends AbstractObjectTable{

    private ArrayList<CreateBagResult>data;
    private EventList eventList;
    
    public CreateBagResultTable(final ArrayList<CreateBagResult>data,String [] cols,String id){
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
    protected ArrayList<CreateBagResult> getData() {
        return data;
    }
    public void reset(final ArrayList<CreateBagResult>data){        
        setData(data);
        refresh();
    }
    protected void setData(final ArrayList<CreateBagResult> data) {                
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
    public CreateBagResult [] getSelections(){
        int[] selected = getTable().getSelectedRows();
        if(selected == null){
            return null;
        }
        CreateBagResult[]items = new CreateBagResult[selected.length];
        for (int i = 0; i < selected.length; i++) {
            items[i] = (CreateBagResult) getTableModel().getElementAt(selected[i]);
        }
        return items;
    }
    public CreateBagResult getSelected(){
        CreateBagResult [] items = getSelections();        
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