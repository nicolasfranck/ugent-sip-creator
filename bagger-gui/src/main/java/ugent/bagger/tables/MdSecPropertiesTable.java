package ugent.bagger.tables;

import ca.odell.glazedlists.EventList;
import com.anearalone.mets.MdSec;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import javax.swing.AbstractAction;
import javax.swing.JDialog;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import org.springframework.richclient.command.ActionCommandExecutor;
import org.springframework.richclient.table.support.AbstractObjectTable;
import ugent.bagger.dialogs.EditMdSecDialog;
import ugent.bagger.helper.SwingUtils;
import ugent.bagger.properties.MdSecProperties;

/**
 *
 * @author nicolas
 */
public class MdSecPropertiesTable extends AbstractObjectTable{        
    private ArrayList<MdSec>data;
    private ActionCommandExecutor openDialogExecutor;
    
    public static ArrayList<MdSecProperties> toProperties(final ArrayList<MdSec>data){
        ArrayList<MdSecProperties>props = new ArrayList<MdSecProperties>();
        for(MdSec m:data){
            props.add(new MdSecProperties(m));
        }
        return props;
    }
    public MdSecPropertiesTable(final ArrayList<MdSec>data,String [] cols,String id){
        super(id,cols);         
        setData(data);             
        setDoubleClickHandler(getOpenDialogExecutor());
    }    

    public ActionCommandExecutor getOpenDialogExecutor() {
        if(openDialogExecutor == null){
            openDialogExecutor = new ActionCommandExecutor(){
                @Override
                public void execute() {
                    try{                        
                        JDialog dialog = new EditMdSecDialog(
                            SwingUtils.getFrame(),
                            true,
                            getSelected().getMdSec()
                        );                    
                        dialog.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
                        dialog.setPreferredSize(new Dimension(400,500));   
                        dialog.setResizable(false);
                        dialog.setLocationRelativeTo(MdSecPropertiesTable.this.getTable());
                        dialog.pack();
                        
                        dialog.setVisible(true);                        
                    }catch(Exception e){
                        logger.debug(e.getMessage());                    
                    }
                }
            };
        }
        return openDialogExecutor;
    }
    public void setOpenDialogExecutor(ActionCommandExecutor openDialogExecutor) {
        this.openDialogExecutor = openDialogExecutor;
    }    
    @Override
    protected void configureTable(JTable table){          
        //replace default action on enter
        table.getActionMap().put("selectNextRowCell",new AbstractAction(){
            @Override
            public void actionPerformed(ActionEvent ae) {                
            }
        });
        
        table.addKeyListener(new KeyAdapter(){           
            @Override
            public void keyReleased(KeyEvent ke) {                               
                switch(ke.getKeyCode()){
                    //press enter
                    case 10:
                        getOpenDialogExecutor().execute();
                        break;
                    //press delete
                    case 127:
                        deleteSelectedMdSec(); 
                        refresh();
                        break;
                }                
            }            
        });                 
    }
    @Override
    protected Object[] getDefaultInitialData(){                       
        return toProperties(getData()).toArray();         
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
            rows.addAll(toProperties(getData()));                       
        } finally {
           rows.getReadWriteLock().writeLock().unlock();
           //belangrijk!
           ((AbstractTableModel)getTable().getModel()).fireTableDataChanged();
        }
    }
    public void addMdSec(MdSec mdSec){        
        getData().add(mdSec);        
    }   
    public void deleteSelectedMdSec(){
        if(getTable().getSelectedRows().length > 0){
            for(MdSecProperties mdSecProperties:getSelections()){
                deleteMdSec(mdSecProperties.getMdSec());
            }            
        }
    }  
    
    public void deleteMdSec(MdSec mdSec){
        getData().remove(mdSec);
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
}