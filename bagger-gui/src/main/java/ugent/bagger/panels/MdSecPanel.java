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

    public ArrayList<MdSec> getMdSec() {        
        return mdSec;
    }
    public void setMdSec(ArrayList<MdSec> mdSec) {
        this.mdSec = mdSec;
    }
    public MdSecPanel(ArrayList<MdSec>mdSec){
        assert(mdSec != null);
        init(mdSec);
    }
    private void init(ArrayList<MdSec>mdSec){
        setLayout(new BorderLayout());                                
        setMdSec(mdSec);                        
        setDmdSecPropertiesPanel(new DmdSecPropertiesPanel(mdSec));        
        getDmdSecPropertiesPanel().setBorder(BorderFactory.createEmptyBorder(10,0,10,0));
        add(getDmdSecPropertiesPanel());
    }    
    public DmdSecPropertiesPanel getDmdSecPropertiesPanel() {
        if(dmdSecPropertiesPanel == null){
            dmdSecPropertiesPanel = new DmdSecPropertiesPanel((getMdSec()));
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
}