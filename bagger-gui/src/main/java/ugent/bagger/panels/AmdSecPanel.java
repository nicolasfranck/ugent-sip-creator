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
    
    private RightsMdSecPropertiesPanel rightsMdSecPropertiesPanel;
    private TechMdSecPropertiesPanel techMdSecPropertiesPanel;
    private DigiprovMdSecPropertiesPanel digiprovMdSecPropertiesPanel;
    private SourceMdSecPropertiesPanel sourceMdSecPropertiesPanel;
    
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
    public RightsMdSecPropertiesPanel getRightsMdSecPropertiesPanel() {
        if(rightsMdSecPropertiesPanel == null){
            rightsMdSecPropertiesPanel = new RightsMdSecPropertiesPanel(
                (ArrayList<MdSec>)getAmdSec().getRightsMD(),
                getAmdSec().getID()
            );
        }
        return rightsMdSecPropertiesPanel;
    }
    public TechMdSecPropertiesPanel getTechMdSecPropertiesPanel() {
        if(techMdSecPropertiesPanel == null){
            techMdSecPropertiesPanel = new TechMdSecPropertiesPanel(
                (ArrayList<MdSec>)getAmdSec().getTechMD(),
                getAmdSec().getID()
            );
        }
        return techMdSecPropertiesPanel;
    }
    public DigiprovMdSecPropertiesPanel getDigiprovMdSecPropertiesPanel() {
        if(digiprovMdSecPropertiesPanel == null){
            digiprovMdSecPropertiesPanel = new DigiprovMdSecPropertiesPanel(
                (ArrayList<MdSec>)getAmdSec().getDigiprovMD(),
                getAmdSec().getID()
            );
        }
        return digiprovMdSecPropertiesPanel;
    }
    public SourceMdSecPropertiesPanel getSourceMdSecPropertiesPanel() {
        if(sourceMdSecPropertiesPanel == null){
            sourceMdSecPropertiesPanel = new SourceMdSecPropertiesPanel(
                (ArrayList<MdSec>)getAmdSec().getSourceMD(),
                getAmdSec().getID()
            );
        }
        return sourceMdSecPropertiesPanel;
    }    
}