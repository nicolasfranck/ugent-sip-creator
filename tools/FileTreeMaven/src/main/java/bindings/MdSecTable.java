/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bindings;

import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.event.ListEvent;
import ca.odell.glazedlists.event.ListEventListener;
import com.anearalone.mets.MdSec;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Arrays;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumnModel;
import org.springframework.richclient.table.support.AbstractObjectTable;

/**
 *
 * @author nicolas
 */
public final class MdSecTable extends AbstractObjectTable{
    private MdSec [] data;    
    
    public MdSecTable(MdSec [] data,String [] cols,String id){
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
                    deleteSelectedMdSec();                    
                }
            }            
        });          
        TableColumnModel tcm = table.getColumnModel();
        tcm.getColumn(0).setPreferredWidth(100);
        tcm.getColumn(1).setPreferredWidth(100);                                      
    }
    @Override
    protected Object[] getDefaultInitialData(){               
       return getData();
    }
    public MdSec[] getData() {
        return data;
    }
    public void setData(MdSec [] data) {        
        this.data = data;        
    }   
    public void refreshData(MdSec [] data){
        setData(data);          
        EventList rows = getFinalEventList();        
        rows.getReadWriteLock().writeLock().lock();        
        try {
            rows.clear();
            rows.addAll(Arrays.asList(getData()));                       
        } finally {
           rows.getReadWriteLock().writeLock().unlock();
           //belangrijk!
           ((AbstractTableModel)this.getTable().getModel()).fireTableDataChanged();
        }
    }
    public void addMdSec(MdSec mdSec){
        MdSec [] newData = new MdSec[data.length + 1];
        System.arraycopy(data, 0, newData, 0, data.length);
        newData[data.length] = mdSec;
        refreshData(newData);             
    }   
    public void deleteSelectedMdSec(){
        if(getTable().getSelectedRows().length > 0){
            //indexes van geselecteerde rijen (pas op: indien gesorteerd, niet gelijk aan data in model)
            int [] indexes = getTable().getSelectedRows();
            //mapping naar indexes in model
            for(int i = 0;i<indexes.length;i++){             
                System.out.print("from "+indexes[i]);                                
                indexes[i] = getTable().convertRowIndexToModel(indexes[i]);
                System.out.println(" to "+indexes[i]);
            }            
            deleteMdSec(indexes);
        }
    }   
     public void deleteMdSec(int i){
        deleteMdSec(new int[] {i});
    }
    public void deleteMdSec(int [] indexes){
        
        //PROBLEEM: de data wordt door Model gesorteerd: de orde van de rijen in de table
        //is dus niet gelijk aan de orde in data!!        
      
        ArrayList<Integer>removeIndexes = new ArrayList<Integer>();
        ArrayList<Integer>newIndexes = new ArrayList<Integer>();
        
        //check healthyness
        for(int i = 0;i < indexes.length;i++){
            int c = indexes[i];
            if(c >= 0 && c < data.length){
                removeIndexes.add(c);
            }            
        }
        for(int i = 0;i < data.length;i++){
            newIndexes.add(i);
        }
        if(removeIndexes.isEmpty()) {
            return;
        }  
        //new indexes left
        for(int i = 0;i < removeIndexes.size();i++){
            System.out.println("removing index: "+removeIndexes.get(i)+", value:"+data[removeIndexes.get(i)].getID());
            newIndexes.remove(removeIndexes.get(i));
        }       
        //construct new list
        MdSec [] newData = new MdSec[newIndexes.size()];        
        for(int i = 0;i< newIndexes.size();i++){           
            newData[i] = data[newIndexes.get(i)];
        }            
        refreshData(newData);
    }
}
