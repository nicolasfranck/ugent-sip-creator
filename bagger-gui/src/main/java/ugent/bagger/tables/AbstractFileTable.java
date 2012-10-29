package ugent.bagger.tables;

import ca.odell.glazedlists.EventList;
import java.util.ArrayList;
import javax.swing.JComponent;
import javax.swing.table.AbstractTableModel;
import org.springframework.richclient.table.support.AbstractObjectTable;
import ugent.bagger.params.AbstractFile;

/**
 *
 * @author nicolas
 */
public final class AbstractFileTable extends AbstractObjectTable{    
    private ArrayList<AbstractFile>data;
    private EventList eventList;
    
    public AbstractFileTable(final ArrayList<AbstractFile>data,String [] cols,String id){
        super(id,cols);                 
        setData(data);
    }            
    @Override
    protected Object[] getDefaultInitialData(){               
       return getData().toArray();
    }
    protected ArrayList<AbstractFile> getData() {
        return data;
    }
    public void reset(final ArrayList<AbstractFile>data){        
        setData(data);
        refresh();
    }
    protected void setData(final ArrayList<AbstractFile> data) {                
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
    public AbstractFile [] getSelections(){
        int[] selected = getTable().getSelectedRows();
        if(selected == null){
            return null;
        }
        AbstractFile[]files = new AbstractFile[selected.length];
        for (int i = 0; i < selected.length; i++) {
            files[i] = (AbstractFile) getTableModel().getElementAt(selected[i]);
        }
        return files;
    }
    public AbstractFile getSelected(){
        AbstractFile [] files = getSelections();        
        return files == null || files.length == 0 ? null:files[0];        
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