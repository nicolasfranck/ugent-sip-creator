package ugent.bagger.wizards;

import javax.swing.JComponent;
import org.springframework.richclient.wizard.AbstractWizardPage;
import ugent.bagger.panels.SaveBagPanel;

/**
 *
 * @author nicolas
 */
public class SaveBagWizardPage1 extends AbstractWizardPage {        
    SaveBagPanel saveBagPanel;
    
    public SaveBagWizardPage1(String pageId){
        super(pageId);
    }

    public SaveBagPanel getSaveBagPanel() {
        if(saveBagPanel == null){
            saveBagPanel = new SaveBagPanel();
        }
        return saveBagPanel;
    }
    
    @Override
    protected JComponent createControl() {                
        return getSaveBagPanel();
    }   
    @Override
    public boolean canFlipToNextPage(){                   
        return false;
    }
    
}