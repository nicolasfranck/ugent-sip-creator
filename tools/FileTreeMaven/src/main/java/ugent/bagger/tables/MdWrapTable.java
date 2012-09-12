/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ugent.bagger.tables;

import ca.odell.glazedlists.EventList;
import com.anearalone.mets.MdSec.MdWrap;
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
public class MdWrapTable extends AbstractObjectTable{    
    private ArrayList<MdWrap>data;
    
    public MdWrapTable(final ArrayList<MdWrap>data,String [] cols,String id){
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
                    deleteSelectedMdWrap(); 
                    refresh();
                }
            }            
        });                 
    }
    @Override
    protected Object[] getDefaultInitialData(){                       
        return getData().toArray();         
    }
    protected ArrayList<MdWrap> getData() {
        return data;
    }
    protected void setData(final ArrayList<MdWrap> data) {        
        this.data = data;        
    }     
    public void reset(final ArrayList<MdWrap>data){                
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
    public void addMdWrap(MdWrap mdWrap){        
        getData().add(mdWrap);        
    }   
    public void deleteSelectedMdWrap(){
        if(getTable().getSelectedRows().length > 0){
            for(MdWrap mdWrap:getSelections()){
                deleteMdWrap(mdWrap);
            }            
        }
    }      
    public void deleteMdWrap(MdWrap mdWrap){
        getData().remove(mdWrap);
    }
    public MdWrap [] getSelections(){
        int[] selected = getTable().getSelectedRows();
        if(selected == null){
            return null;
        }
        MdWrap [] mds = new MdWrap[selected.length];
        for (int i = 0; i < selected.length; i++) {
            mds[i] = (MdWrap) getTableModel().getElementAt(selected[i]);
        }
        return mds;
    }
    public MdWrap getSelected(){
        MdWrap [] mds = getSelections();
        if( mds == null || mds.length == 0){
            return null;
        }else{
            return mds[0];
        }
    }
}