package ugent.bagger.tables;

import com.anearalone.mets.MdSec;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.JTable;
import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.command.CommandGroup;
import org.springframework.richclient.table.support.AbstractObjectTable;
import ugent.bagger.properties.MdSecProperties;

/**
 *
 * @author nicolas
 */
public class MdSecPropertiesTable extends AbstractObjectTable{        
    ArrayList<MdSec>data;    
    HashMap<String,ArrayList<PropertyChangeListener>>listeners = new HashMap<String,ArrayList<PropertyChangeListener>>();
    
    public static ArrayList<MdSecProperties> toProperties(final ArrayList<MdSec>data){
        ArrayList<MdSecProperties>props = new ArrayList<MdSecProperties>();
        for(MdSec m:data){
            props.add(new MdSecProperties(m));
        }        
        return props;
    }
    @Override
    protected void configureTable(JTable table) {
        table.setFillsViewportHeight(true);        
        table.setCellSelectionEnabled(true);
    }
    public MdSecPropertiesTable(final ArrayList<MdSec>data,final String [] cols,final String id){
        super(id,cols);         
        setData(data);           
        ActionCommand testCommand = new ActionCommand("copyCommand"){            
            @Override
            protected void doExecuteCommand() {                
                JTable table = MdSecPropertiesTable.this.getTable();
                int row = table.getSelectedRow();
                int column = table.getSelectedColumn();
                if(row >=0 && column >= 0){                                                            
                    StringSelection transfer = new StringSelection(table.getValueAt(row,column).toString());
                    Toolkit.getDefaultToolkit().getSystemClipboard().setContents(transfer,transfer);                    
                }
            }
        };        
        CommandGroup commandGroup = CommandGroup.createCommandGroup(testCommand);
        setPopupCommandGroup(commandGroup);
    }        
    @Override
    protected Object[] getDefaultInitialData(){                       
        return toProperties(getData()).toArray();         
    }
    protected ArrayList<MdSec> getData() {
        return data;
    }
    protected void setData(final ArrayList<MdSec> data) {      
        firePropertyChange("setData",this.data,data);
        this.data = data;        
    }             
    public MdSecProperties [] getSelections(){
        int[] selected = getTable().getSelectedRows();
        if(selected == null){
            return null;
        }
        MdSecProperties [] mds = new MdSecProperties[selected.length];
        for (int i = 0; i < selected.length; i++) {
            mds[i] = (MdSecProperties) getTableModel().getElementAt(selected[i]);
        }
        return mds;
    }
    public MdSecProperties getSelected(){
        MdSecProperties [] mds = getSelections();
        if(mds == null || mds.length == 0){
            return null;
        }else{
            return mds[0];
        }
    }
    public void addPropertyChangeListener(String key,PropertyChangeListener l){
        if(!listeners.containsKey(key)){
            listeners.put(key,new ArrayList<PropertyChangeListener>());
        }
        listeners.get(key).add(l);
    }
    public void firePropertyChange(String key,Object oldValue,Object newValue){
        if(listeners.containsKey(key)){            
            PropertyChangeEvent event = new PropertyChangeEvent(this,key,oldValue,newValue);
            for(PropertyChangeListener l:listeners.get(key)){
                l.propertyChange(event);
            }
        }
    }
}