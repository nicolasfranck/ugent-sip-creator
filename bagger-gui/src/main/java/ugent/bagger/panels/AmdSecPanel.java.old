package ugent.bagger.panels;

import com.anearalone.mets.AmdSec;
import com.anearalone.mets.MdSec;
import java.util.ArrayList;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import org.springframework.util.Assert;

/**
 *
 * @author nicolas
 */
public final class AmdSecPanel extends JTabbedPane{
    private AmdSec amdSec;
    
    private MdSecPropertiesPanel rightsMdSecPropertiesPanel;
    private MdSecPropertiesPanel techMdSecPropertiesPanel;
    private MdSecPropertiesPanel digiprovMdSecPropertiesPanel;
    private MdSecPropertiesPanel sourceMdSecPropertiesPanel;
    
    public AmdSecPanel(AmdSec amdSec){
        Assert.notNull(amdSec);
        setAmdSec(amdSec);
        init();
    }
    public AmdSec getAmdSec() {        
        return amdSec;
    }
    public void setAmdSec(AmdSec amdSec) {
        this.amdSec = amdSec;
    }   
    public void resetAmdSec(AmdSec amdSec){
        setAmdSec(amdSec);
        getRightsMdSecPropertiesPanel().reset((ArrayList<MdSec>)amdSec.getRightsMD());
        getSourceMdSecPropertiesPanel().reset((ArrayList<MdSec>)amdSec.getSourceMD());
        getDigiprovMdSecPropertiesPanel().reset((ArrayList<MdSec>)amdSec.getDigiprovMD());
        getTechMdSecPropertiesPanel().reset((ArrayList<MdSec>)amdSec.getTechMD());
    }
    public void init(){
        
        //rightMd       
        addTab("rightsMD",new JScrollPane(getRightsMdSecPropertiesPanel()));
        
        //techMD      
        addTab("techMD",new JScrollPane(getTechMdSecPropertiesPanel()));
        
        //digiprovMD       
        addTab("digiprovMD",new JScrollPane(getDigiprovMdSecPropertiesPanel()));
                
        //sourceMD        
        addTab("sourceMD",new JScrollPane(getSourceMdSecPropertiesPanel()));
        
    }
    public MdSecPropertiesPanel getRightsMdSecPropertiesPanel() {
        if(rightsMdSecPropertiesPanel == null){
            rightsMdSecPropertiesPanel = new MdSecPropertiesPanel((ArrayList<MdSec>)getAmdSec().getRightsMD());
        }
        return rightsMdSecPropertiesPanel;
    }
    public MdSecPropertiesPanel getTechMdSecPropertiesPanel() {
        if(techMdSecPropertiesPanel == null){
            techMdSecPropertiesPanel = new MdSecPropertiesPanel((ArrayList<MdSec>)getAmdSec().getTechMD());
        }
        return techMdSecPropertiesPanel;
    }
    public MdSecPropertiesPanel getDigiprovMdSecPropertiesPanel() {
        if(digiprovMdSecPropertiesPanel == null){
            digiprovMdSecPropertiesPanel = new MdSecPropertiesPanel((ArrayList<MdSec>)getAmdSec().getDigiprovMD());
        }
        return digiprovMdSecPropertiesPanel;
    }
    public MdSecPropertiesPanel getSourceMdSecPropertiesPanel() {
        if(sourceMdSecPropertiesPanel == null){
            sourceMdSecPropertiesPanel = new MdSecPropertiesPanel((ArrayList<MdSec>)getAmdSec().getSourceMD());
        }
        return sourceMdSecPropertiesPanel;
    }    
}