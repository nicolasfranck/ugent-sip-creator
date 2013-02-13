package ugent.bagger.wizards;

import org.springframework.richclient.wizard.Wizard;
import org.springframework.richclient.wizard.WizardDialog;
import ugent.bagger.helper.Context;

/**
 *
 * @author nicolas
 */
public class CSVWizardDialog extends WizardDialog{
    public CSVWizardDialog(String wizardId){          
        this(new CSVWizard(wizardId));         
    }
    public CSVWizardDialog(Wizard wizard){
        super(wizard);                         
        setTitle(Context.getMessage("CSVWizardDialog.title"));                          
    }        
}