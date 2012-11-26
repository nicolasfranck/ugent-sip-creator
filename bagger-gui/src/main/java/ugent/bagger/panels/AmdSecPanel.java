package ugent.bagger.panels;

import com.anearalone.mets.AmdSec;
import com.anearalone.mets.MdSec;
import java.util.ArrayList;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import org.springframework.util.Assert;
import ugent.bagger.helper.Context;

/**
 *
 * @author nicolas
 */
public final class AmdSecPanel extends JPanel{
    AmdSec amdSec;
    TechMdSecPropertiesPanel techMdSecPropertiesPanel;
    SourceMdSecPropertiesPanel sourceMdSecPropertiesPanel;
    RightsMdSecPropertiesPanel rightsMdSecPropertiesPanel;
    DigiprovMdSecPropertiesPanel digiprovMdSecPropertiesPanel;

    public TechMdSecPropertiesPanel getTechMdSecPropertiesPanel() {
        if(techMdSecPropertiesPanel == null){
            techMdSecPropertiesPanel = new TechMdSecPropertiesPanel((ArrayList<MdSec>)getAmdSec().getTechMD());
        }
        return techMdSecPropertiesPanel;
    }

    public SourceMdSecPropertiesPanel getSourceMdSecPropertiesPanel() {
        if(sourceMdSecPropertiesPanel == null){
            sourceMdSecPropertiesPanel = new SourceMdSecPropertiesPanel((ArrayList<MdSec>)getAmdSec().getSourceMD());
        }
        return sourceMdSecPropertiesPanel;
    }

    public RightsMdSecPropertiesPanel getRightsMdSecPropertiesPanel() {
        if(rightsMdSecPropertiesPanel == null){
            rightsMdSecPropertiesPanel = new RightsMdSecPropertiesPanel((ArrayList<MdSec>)getAmdSec().getRightsMD());
        }
        return rightsMdSecPropertiesPanel;
    }
    public DigiprovMdSecPropertiesPanel getDigiprovMdSecPropertiesPanel() {
        if(digiprovMdSecPropertiesPanel == null){
            digiprovMdSecPropertiesPanel = new DigiprovMdSecPropertiesPanel((ArrayList<MdSec>)getAmdSec().getDigiprovMD());
        }
        return digiprovMdSecPropertiesPanel;
    }   
    
    public AmdSecPanel(AmdSec amdSec){
        Assert.notNull(amdSec);
        this.amdSec = amdSec;
        init();
    }     
    protected AmdSec getAmdSec(){
        return amdSec;
    }
    
    public void init(){
        setLayout(new BoxLayout(this,BoxLayout.PAGE_AXIS)); 
        
        JLabel techMDLabel = new JLabel(Context.getMessage("amdSecPanel.techMDLabel.label"));        
        techMDLabel.setToolTipText(Context.getMessage("amdSecPanel.techMDLabel.tooltip"));
        add(techMDLabel);                
        add(getTechMdSecPropertiesPanel());
        
        JLabel digiprovMDLabel = new JLabel(Context.getMessage("amdSecPanel.digiprovMDLabel.label"));
        digiprovMDLabel.setToolTipText(Context.getMessage("amdSecPanel.digiprovMDLabel.tooltip"));                
        add(digiprovMDLabel);                
        add(getDigiprovMdSecPropertiesPanel());        
        
        JLabel sourceMDLabel = new JLabel(Context.getMessage("amdSecPanel.sourceMDLabel.label"));
        sourceMDLabel.setToolTipText(Context.getMessage("amdSecPanel.sourceMDLabel.tooltip"));        
        add(sourceMDLabel);                
        add(getSourceMdSecPropertiesPanel());
        
        JLabel rightsMDLabel = new JLabel(Context.getMessage("amdSecPanel.rightsMDLabel.label"));
        rightsMDLabel.setToolTipText(Context.getMessage("amdSecPanel.rightsMDLabel.tooltip"));        
        add(rightsMDLabel);                
        add(getRightsMdSecPropertiesPanel());
    }
    public void reset(AmdSec amdSec){
        getTechMdSecPropertiesPanel().reset((ArrayList<MdSec>)amdSec.getTechMD());
        getDigiprovMdSecPropertiesPanel().reset((ArrayList<MdSec>)amdSec.getDigiprovMD());
        getSourceMdSecPropertiesPanel().reset((ArrayList<MdSec>)amdSec.getSourceMD());
        getRightsMdSecPropertiesPanel().reset((ArrayList<MdSec>)amdSec.getRightsMD());
        this.amdSec = amdSec;        
    }
}
