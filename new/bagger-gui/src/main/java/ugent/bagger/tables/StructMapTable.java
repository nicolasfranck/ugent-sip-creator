/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ugent.bagger.tables;

import ca.odell.glazedlists.EventList;
import com.anearalone.mets.StructMap;
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
public class StructMapTable extends AbstractObjectTable{    
    private ArrayList<StructMap>data;
    
    public StructMapTable(final ArrayList<StructMap>data,String [] cols,String id){
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
                    deleteSelectedStructMap(); 
                    refresh();
                }
            }            
        });                 
    }
    @Override
    protected Object[] getDefaultInitialData(){               
        return getData().toArray();         
    }
    protected ArrayList<StructMap> getData() {
        return data;
    }
    protected void setData(final ArrayList<StructMap> data) {        
        this.data = data;        
    }     
    public void reset(final ArrayList<StructMap>data){        
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
    public void addStructMap(StructMap mdSec){        
        getData().add(mdSec);        
    }   
    public void deleteSelectedStructMap(){
        if(getTable().getSelectedRows().length > 0){
            for(StructMap sm:getSelections()){
                deleteStructMap(sm);
            }            
        }
    }  
    
    public void deleteStructMap(StructMap structMap){
        getData().remove(structMap);
    }
    public StructMap [] getSelections(){
        int[] selected = getTable().getSelectedRows();
        if(selected == null){
            return null;
        }
        StructMap [] stms = new StructMap[selected.length];
        for (int i = 0; i < selected.length; i++) {
            stms[i] = (StructMap) getTableModel().getElementAt(selected[i]);
        }
        return stms;
    }
    public StructMap getSelected(){
        StructMap [] stms = getSelections();
        if(stms == null || stms.length == 0){
            return null;
        }else{
            return stms[0];
        }
    }
}