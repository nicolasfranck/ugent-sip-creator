package ugent.bagger.wizards;

import org.springframework.richclient.wizard.Wizard;
import org.springframework.richclient.wizard.WizardDialog;
import ugent.bagger.helper.Context;

/**
 *
 * @author nicolas
 */
public class CrosswalkParamsDialog extends WizardDialog{
    public CrosswalkParamsDialog(String wizardId){          
        this(new CrosswalkParamsWizard(wizardId));         
    }
    public CrosswalkParamsDialog(Wizard wizard){
        super(wizard);                         
        setTitle(Context.getMessage("CrosswalkParamsWizard.title"));                          
    }        
}