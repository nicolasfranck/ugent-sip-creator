package ugent.bagger.wizards;

import javax.swing.JComponent;
import org.springframework.richclient.wizard.AbstractWizardPage;
import ugent.bagger.forms.BagInfoImportForm;
import ugent.bagger.params.BagInfoImportParams;

/**
 *
 * @author nicolas
 */
public class BagInfoImportWizardPage1 extends AbstractWizardPage {
    private BagInfoImportForm bagInfoImportForm;
    private BagInfoImportParams bagInfoImportParams;
    
    public BagInfoImportWizardPage1(String pageId){
        super(pageId);
    }
    @Override
    protected JComponent createControl() {        
        return getBagInfoImportForm().getControl();
    }    

    public BagInfoImportForm getBagInfoImportForm() {
        if(bagInfoImportForm == null){
            bagInfoImportForm = new BagInfoImportForm(getBagInfoImportParams());
        }
        return bagInfoImportForm;
    }

    public BagInfoImportParams getBagInfoImportParams() {
        if(bagInfoImportParams == null){
            bagInfoImportParams = new BagInfoImportParams();
        }
        return bagInfoImportParams;
    }
    @Override
    public boolean canFlipToNextPage(){
        //geen volgende pagina!
        return false;
    }
    
}
