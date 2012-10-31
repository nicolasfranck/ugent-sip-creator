package ugent.bagger.wizards;

import org.springframework.richclient.wizard.Wizard;
import org.springframework.richclient.wizard.WizardDialog;

/**
 *
 * @author nicolas
 */
public class CSVWizardDialog extends WizardDialog{
    public CSVWizardDialog(){
        this(new CSVWizard());
    }
    public CSVWizardDialog(Wizard wizard){
        super(wizard);
        setTitle(getApplicationName());               
    }
}
