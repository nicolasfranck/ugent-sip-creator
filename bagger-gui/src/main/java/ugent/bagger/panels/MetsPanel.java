package ugent.bagger.panels;

import com.anearalone.mets.MdSec;
import com.anearalone.mets.Mets;
import com.anearalone.mets.MetsHdr;
import java.awt.BorderLayout;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.JPanel;

/**
 *
 * @author nicolas
 */
public class MetsPanel extends JPanel{    
    private DmdSecPropertiesPanel dmdSecPropertiesPanel;
    private Mets mets;
    
    public MetsPanel(Mets mets){
        assert(mets != null);
        init(mets);
    }
    private void init(Mets mets){
        setLayout(new BorderLayout());                        
        if(mets.getMetsHdr() == null){            
            mets.setMetsHdr(new MetsHdr());
        }        
        setMets(mets);                
        setDmdSecPropertiesPanel(new DmdSecPropertiesPanel(((ArrayList<MdSec>)mets.getDmdSec())));        
        getDmdSecPropertiesPanel().setBorder(BorderFactory.createEmptyBorder(10,0,10,0));
        add(getDmdSecPropertiesPanel());
    }    
    public DmdSecPropertiesPanel getDmdSecPropertiesPanel() {
        if(dmdSecPropertiesPanel == null){
            dmdSecPropertiesPanel = new DmdSecPropertiesPanel(((ArrayList<MdSec>)mets.getDmdSec()));
        }
        return dmdSecPropertiesPanel;
    }
    public void setDmdSecPropertiesPanel(DmdSecPropertiesPanel dmdSecPanel) {
        this.dmdSecPropertiesPanel = dmdSecPanel;
    }    
    public Mets getMets() {        
        return mets;
    }
    public void setMets(Mets mets) {
        this.mets = mets;
    }
    public void reset(Mets mets){     
        if(mets.getMetsHdr() == null){
            mets.setMetsHdr(new MetsHdr());
        }                
        getDmdSecPropertiesPanel().reset((ArrayList<MdSec>)mets.getDmdSec());
        setMets(mets);
    }    
}