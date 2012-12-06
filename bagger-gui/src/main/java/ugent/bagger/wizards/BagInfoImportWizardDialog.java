package ugent.bagger.wizards;

import org.springframework.richclient.wizard.Wizard;
import org.springframework.richclient.wizard.WizardDialog;
import ugent.bagger.helper.Context;

/**
 *
 * @author nicolas
 */
public class BagInfoImportWizardDialog extends WizardDialog{
    public BagInfoImportWizardDialog(String wizardId){
        this(new BagInfoImportWizard(wizardId));
    }
    public BagInfoImportWizardDialog(Wizard wizard){
        super(wizard);
        setTitle(Context.getMessage("BagInfoImportWizardDialog.title"));
    }    
}