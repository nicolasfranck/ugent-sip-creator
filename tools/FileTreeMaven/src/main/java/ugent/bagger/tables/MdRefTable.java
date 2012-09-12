/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ugent.bagger.tables;

import ca.odell.glazedlists.EventList;
import com.anearalone.mets.MdSec.MdRef;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import org.springframework.richclient.table.support.AbstractObjectTable;

/**
 *
 * @author nicolas
 */
public class MdRefTable extends AbstractObjectTable{    
    private ArrayList<MdRef>data;
    
    public MdRefTable(final ArrayList<MdRef>data,String [] cols,String id){
        super(id,cols);         
        setData(data);                
    }    
    @Override
    protected void configureTable(JTable table){        
        table.addKeyListener(new KeyListener(){
            @Override
            public void keyTyped(KeyEvent ke) {                             
            }
            @Override
            public void keyPressed(KeyEvent ke) {                
            }
            @Override
            public void keyReleased(KeyEvent ke) {               
                if(ke.getKeyCode() == 127){                    
                    deleteSelectedMdRef(); 
                    refresh();
                }
            }            
        });                 
    }
    @Override
    protected Object[] getDefaultInitialData(){                       
        return getData().toArray();         
    }
    protected ArrayList<MdRef> getData() {
        return data;
    }
    protected void setData(final ArrayList<MdRef> data) {        
        this.data = data;        
    }     
    public void reset(final ArrayList<MdRef>data){                
        setData(data);
        refresh();
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
           ((AbstractTableModel)this.getTable().getModel()).fireTableDataChanged();
        }
    }
    public void addMdRef(MdRef mdRef){        
        getData().add(mdRef);        
    }   
    public void deleteSelectedMdRef(){
        if(getTable().getSelectedRows().length > 0){
            for(MdRef mdRef:getSelections()){
                deleteMdRef(mdRef);
            }            
        }
    }      
    public void deleteMdRef(MdRef mdRef){
        getData().remove(mdRef);
    }
    public MdRef [] getSelections(){
        int[] selected = getTable().getSelectedRows();
        if(selected == null){
            return null;
        }
        MdRef [] mds = new MdRef[selected.length];
        for (int i = 0; i < selected.length; i++) {
            mds[i] = (MdRef) getTableModel().getElementAt(selected[i]);
        }
        return mds;
    }
    public MdRef getSelected(){
        MdRef [] mds = getSelections();
        if( mds == null || mds.length == 0){
            return null;
        }else{
            return mds[0];
        }
    }
}