package ugent.bagger.wizards;

import org.springframework.richclient.wizard.Wizard;
import org.springframework.richclient.wizard.WizardDialog;
import ugent.bagger.helper.Context;

/**
 *
 * @author nicolas
 */
public class ExportWizardDialog extends WizardDialog{
    public ExportWizardDialog(String wizardId){          
        this(new ExportWizard(wizardId));         
    }
    public ExportWizardDialog(Wizard wizard){
        super(wizard);                         
        setTitle(Context.getMessage("ExportWizardDialog.title"));                          
    }        
}