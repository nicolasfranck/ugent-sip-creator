package ugent.bagger.panels;

import com.anearalone.mets.MdSec;
import java.awt.BorderLayout;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.JPanel;

/**
 *
 * @author nicolas
 */
public class MdSecPanel extends JPanel{    
    private DmdSecPropertiesPanel dmdSecPropertiesPanel;
    private ArrayList<MdSec>mdSec;    
    private String id;

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public ArrayList<MdSec> getMdSec() {        
        return mdSec;
    }
    public void setMdSec(ArrayList<MdSec> mdSec) {
        this.mdSec = mdSec;
    }
    public MdSecPanel(ArrayList<MdSec>mdSec,String id){
        assert(mdSec != null);
        init(mdSec);
        this.id = id;
    }
    private void init(ArrayList<MdSec>mdSec){
        setLayout(new BorderLayout());                                
        setMdSec(mdSec);                        
        setDmdSecPropertiesPanel(new DmdSecPropertiesPanel(mdSec,getId()));        
        getDmdSecPropertiesPanel().setBorder(BorderFactory.createEmptyBorder(10,0,10,0));
        add(getDmdSecPropertiesPanel());
    }    
    public DmdSecPropertiesPanel getDmdSecPropertiesPanel() {
        if(dmdSecPropertiesPanel == null){
            dmdSecPropertiesPanel = new DmdSecPropertiesPanel(getMdSec(),getId());
        }
        return dmdSecPropertiesPanel;
    }
    public void setDmdSecPropertiesPanel(DmdSecPropertiesPanel dmdSecPanel) {
        this.dmdSecPropertiesPanel = dmdSecPanel;
    }    
   
    public void reset(ArrayList<MdSec>mdSec){             
        getDmdSecPropertiesPanel().reset(mdSec);
        setMdSec(mdSec);        
    }   
    @Override
    public void setEnabled(boolean enabled){
        super.setEnabled(enabled);
        getDmdSecPropertiesPanel().setEnabled(enabled);
    }
}