package ugent.bagger.wizards;

import org.springframework.richclient.wizard.Wizard;
import org.springframework.richclient.wizard.WizardDialog;
import ugent.bagger.helper.Context;

/**
 *
 * @author nicolas
 */
public class SaveBagWizardDialog extends WizardDialog{
    public SaveBagWizardDialog(String wizardId){          
        this(new SaveBagWizard(wizardId));         
    }
    public SaveBagWizardDialog(Wizard wizard){
        super(wizard);                         
        setTitle(Context.getMessage("SaveBagWizardDialog.title"));                          
    }        
}