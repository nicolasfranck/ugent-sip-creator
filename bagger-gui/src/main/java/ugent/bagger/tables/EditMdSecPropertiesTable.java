package ugent.bagger.tables;

import ca.odell.glazedlists.EventList;
import com.anearalone.mets.MdSec;
import gov.loc.repository.bagger.bag.impl.MetsBag;
import gov.loc.repository.bagger.ui.BagView;
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
import ugent.bagger.dialogs.EditMdSecDialog;
import ugent.bagger.helper.Context;
import ugent.bagger.helper.SwingUtils;
import ugent.bagger.properties.MdSecProperties;

/**
 *
 * @author nicolas
 */
public class EditMdSecPropertiesTable extends MdSecPropertiesTable{            
    ActionCommandExecutor openDialogExecutor;        
    ArrayList<MdSec>exceptions;
    
    protected BagView getBagView(){
        return BagView.getInstance();
    }
    protected MetsBag getMetsBag(){
        return getBagView().getBag();
    }
    public EditMdSecPropertiesTable(final ArrayList<MdSec>data,String [] cols,String id){
        this(data,cols,id,new ArrayList<MdSec>());
    }  
    public EditMdSecPropertiesTable(final ArrayList<MdSec>data,final String [] cols,final String id,final ArrayList<MdSec>exceptions){
        super(data,cols,id);          
        setExceptions(exceptions);
        setDoubleClickHandler(getOpenDialogExecutor());                
    }

    public ArrayList<MdSec> getExceptions() {
        if(exceptions == null){
            exceptions = new ArrayList<MdSec>();
        }
        return exceptions;
    }
    public void setExceptions(ArrayList<MdSec> exceptions) {
        this.exceptions = exceptions;
    }    
    public ActionCommandExecutor getOpenDialogExecutor() {
        if(openDialogExecutor == null){
            openDialogExecutor = new ActionCommandExecutor(){
                @Override
                public void execute() {
                    try{                       
                        final MdSecProperties selected = getSelected();
                        if(selected == null || selected.getMdSec() == null){
                            return;
                        }
                        SwingUtils.ShowWhile(new Runnable() {
                            @Override
                            public void run() {
                                JDialog dialog = new EditMdSecDialog(
                                    SwingUtils.getFrame(),
                                    true,
                                    selected.getMdSec()
                                );                    
                                dialog.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
                                dialog.setPreferredSize(new Dimension(600,500));   
                                dialog.setResizable(false);                        
                                SwingUtils.centerOnParent(dialog,true);                                     
                                dialog.setVisible(true);                        
                            }
                        });
                        
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
        super.configureTable(table);
        
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
                        deleteSelected(); 
                        refresh();
                        break;
                }                
            }            
        });               
    }         
    public void reset(final ArrayList<MdSec>data){                
        setData(data);
        firePropertyChange("reset",null,null);
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
        firePropertyChange("refresh",null,null);
    }
    public void add(MdSec mdSec){        
        getData().add(mdSec);     
        getMetsBag().changeToDirty();
        getMetsBag().setChanged(true);
        getBagView().setCompleteExecutor();
        getBagView().setValidateExecutor();
        firePropertyChange("add",null,mdSec);        
    }   
    public void deleteSelected(){
        if(getTable().getSelectedRows().length > 0){
            boolean doDelete = SwingUtils.confirm(
                Context.getMessage("EditMdSecPropertiesTable.delete.confirm.title"),
                Context.getMessage(
                    "EditMdSecPropertiesTable.delete.confirm.description",
                    new Object [] {getTable().getSelectedRows().length}
                )
            );
            if(!doDelete){
                return;
            }
            
            for(MdSecProperties mdSecProperties:getSelections()){                
                if(getExceptions().contains(mdSecProperties.getMdSec())){
                    SwingUtils.ShowError(
                        Context.getMessage("EditMdSecPropertiesTable.deleteSelected.deny.title"),
                        Context.getMessage("EditMdSecPropertiesTable.deleteSelected.deny.description")
                    );
                }else{                    
                    deleteMdSec(mdSecProperties.getMdSec());
                }                                                
            }            
        }
    }
    public void deleteMdSec(MdSec mdSec){        
        getData().remove(mdSec);
        firePropertyChange("remove",null,mdSec);
    }        
}