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
import ugent.bagger.dialogs.EditMdSecDialog;
import ugent.bagger.helper.SwingUtils;
import ugent.bagger.properties.MdSecProperties;

/**
 *
 * @author nicolas
 */
public class EditMdSecPropertiesTable extends MdSecPropertiesTable{            
    private ActionCommandExecutor openDialogExecutor;    
    
    public EditMdSecPropertiesTable(final ArrayList<MdSec>data,String [] cols,String id){
        super(data,cols,id);               
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
                        dialog.setLocationRelativeTo(EditMdSecPropertiesTable.this.getTable());
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
    public void add(MdSec mdSec){        
        getData().add(mdSec);        
    }   
    public void deleteSelected(){
        if(getTable().getSelectedRows().length > 0){
            for(MdSecProperties mdSecProperties:getSelections()){
                deleteMdSec(mdSecProperties.getMdSec());
            }            
        }
    }
    public void deleteMdSec(MdSec mdSec){
        getData().remove(mdSec);
    }    
}