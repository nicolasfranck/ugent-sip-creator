package ugent.bagger.tables;

import com.anearalone.mets.MdSec;
import java.util.ArrayList;
import javax.swing.JTable;
import org.springframework.richclient.command.ActionCommandExecutor;
import org.springframework.richclient.table.support.AbstractObjectTable;
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
    @Override
    protected void configureTable(JTable table) {
        table.setFillsViewportHeight(true);
    }
    public MdSecPropertiesTable(final ArrayList<MdSec>data,String [] cols,String id){
        super(id,cols);         
        setData(data);                     
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