/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Tables;

import ca.odell.glazedlists.EventList;
import com.anearalone.mets.MetsHdr.Agent;
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
public final class AgentTable extends AbstractObjectTable{    
    private ArrayList<Agent>data;
    
    public AgentTable(final ArrayList<Agent>data,String [] cols,String id){
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
                    refresh();
                }
            }            
        });        
    }
    @Override
    protected Object[] getDefaultInitialData(){               
       return getData().toArray();
    }
    protected ArrayList<Agent> getData() {
        return data;
    }
    public void reset(final ArrayList<Agent>data){        
        setData(data);
        refresh();
    }
    protected void setData(final ArrayList<Agent> data) {        
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
            for(Agent agent:getSelections()){
                getData().remove(agent);
            }            
        }
    }      
    public Agent [] getSelections(){
        int[] selected = getTable().getSelectedRows();
        if(selected == null){
            return null;
        }
        Agent[]agents = new Agent[selected.length];
        for (int i = 0; i < selected.length; i++) {
            agents[i] = (Agent) getTableModel().getElementAt(selected[i]);
        }
        return agents;
    }
    public Agent getSelected(){
        Agent [] agents = getSelections();
        if(agents == null || agents.length == 0){
            return null;
        }else{
            return agents[0];
        }
    }
}