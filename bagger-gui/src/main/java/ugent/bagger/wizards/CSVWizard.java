package ugent.bagger.wizards;

import org.springframework.richclient.wizard.AbstractWizard;

/**
 *
 * @author nicolas
 */
public class CSVWizard extends AbstractWizard{
    public CSVWizard(){
        addPage(new CSVWizardPage1("page1"));
    }
    @Override
    protected boolean onFinish() {
        return true;
    }    
}
