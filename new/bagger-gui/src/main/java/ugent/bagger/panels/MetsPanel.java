package ugent.bagger.panels;

import com.anearalone.mets.MdSec;
import com.anearalone.mets.Mets;
import com.anearalone.mets.MetsHdr;
import java.awt.BorderLayout;
import java.util.ArrayList;
import javax.swing.JPanel;

/**
 *
 * @author nicolas
 */
public class MetsPanel extends JPanel{    
    private DmdSecPanel dmdSecPanel;
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
        setDmdSecPanel(new DmdSecPanel(((ArrayList<MdSec>)mets.getDmdSec())));        
        add(getDmdSecPanel());
    }    
    public DmdSecPanel getDmdSecPanel() {
        if(dmdSecPanel == null){
            dmdSecPanel = new DmdSecPanel(((ArrayList<MdSec>)mets.getDmdSec()));
        }
        return dmdSecPanel;
    }
    public void setDmdSecPanel(DmdSecPanel dmdSecPanel) {
        this.dmdSecPanel = dmdSecPanel;
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
        getDmdSecPanel().reset((ArrayList<MdSec>)mets.getDmdSec());
        setMets(mets);
    }    
}