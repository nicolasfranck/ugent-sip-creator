/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Bindings;

import ca.odell.glazedlists.EventList;
import com.anearalone.mets.MetsHdr.Agent;
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
public final class AgentTable extends AbstractObjectTable{    
    private ArrayList<Agent>data;
    
    public AgentTable(ArrayList<Agent>data,String [] cols,String id){
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
                    deleteSelectedAgent();                    
                }
            }            
        });          
        TableColumnModel tcm = table.getColumnModel();
        tcm.getColumn(0).setPreferredWidth(100);
        tcm.getColumn(1).setPreferredWidth(100);                                      
    }
    @Override
    protected Object[] getDefaultInitialData(){               
       return getData().toArray();
    }
    protected ArrayList<Agent> getData() {
        return data;
    }
    protected void setData(ArrayList<Agent> data) {        
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
           ((AbstractTableModel)this.getTable().getModel()).fireTableDataChanged();
        }
    }
    public void addAgent(Agent agent){        
        getData().add(agent);        
    }   
    public void deleteSelectedAgent(){
        if(getTable().getSelectedRows().length > 0){
            //indexes van geselecteerde rijen (pas op: indien gesorteerd, niet gelijk aan data in model)
            int [] indexes = getTable().getSelectedRows();
            //mapping naar indexes in model
            for(int i = 0;i<indexes.length;i++){
                indexes[i] = getTable().convertRowIndexToModel(indexes[i]);
            }            
            deleteAgent(indexes);
            refresh();
        }
    }   
    public void deleteAgent(int i){
        deleteAgent(new int[] {i});
    }
    public void deleteAgent(int [] indexes){
        
        //PROBLEEM: de data wordt door Model gesorteerd: de orde van de rijen in de table
        //is dus niet gelijk aan de orde in data!!       
        
        //onderstaande werkt enkel indien lijst gesorteerd is
        Arrays.sort(indexes);       
        for(int i = 0;i < indexes.length;i++){           
            getData().remove(indexes[i] - i);
        }
    }
}