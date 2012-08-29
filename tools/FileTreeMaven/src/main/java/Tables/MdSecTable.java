/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Tables;

import Dialogs.EditMdSecDialog;
import ca.odell.glazedlists.EventList;
import com.anearalone.mets.MdSec;
import helper.SwingUtils;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import javax.swing.JDialog;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import org.springframework.richclient.command.ActionCommandExecutor;
import org.springframework.richclient.table.support.AbstractObjectTable;

/**
 *
 * @author nicolas
 */
public class MdSecTable extends AbstractObjectTable{    
    private ArrayList<MdSec>data;
    
    public MdSecTable(final ArrayList<MdSec>data,String [] cols,String id){
        super(id,cols);         
        setData(data);             
        setDoubleClickHandler(new ActionCommandExecutor(){
            @Override
            public void execute() {
                try{
                    MdSec mdSec = getSelected(); 
                    JDialog dialog = new EditMdSecDialog(
                        SwingUtils.getFrame(),
                        getSelected()
                    );
                    dialog.pack();
                    dialog.setVisible(true);                    
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        });
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
                    refresh();
                }
            }            
        });                 
    }
    @Override
    protected Object[] getDefaultInitialData(){               
        //filter op data met mdWrap? => nee, want gevaarlijk wanneer je dingen eruit verwijdert..
        return getData().toArray();         
    }
    protected ArrayList<MdSec> getData() {
        return data;
    }
    protected void setData(final ArrayList<MdSec> data) {        
        this.data = data;        
    }     
    public void reset(final ArrayList<MdSec>data){                
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
    public void addMdSec(MdSec mdSec){        
        getData().add(mdSec);        
    }   
    public void deleteSelectedMdSec(){
        if(getTable().getSelectedRows().length > 0){
            for(MdSec mdSec:getSelections()){
                deleteMdSec(mdSec);
            }            
        }
    }  
    
    public void deleteMdSec(MdSec mdSec){
        getData().remove(mdSec);
    }
    public MdSec [] getSelections(){
        int[] selected = getTable().getSelectedRows();
        if(selected == null){
            return null;
        }
        MdSec [] mds = new MdSec[selected.length];
        for (int i = 0; i < selected.length; i++) {
            mds[i] = (MdSec) getTableModel().getElementAt(selected[i]);
        }
        return mds;
    }
    public MdSec getSelected(){
        MdSec [] mds = getSelections();
        if(mds == null || mds.length == 0){
            return null;
        }else{
            return mds[0];
        }
    }
}