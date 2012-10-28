package ugent.bagger.wizards;

import org.springframework.richclient.wizard.Wizard;
import org.springframework.richclient.wizard.WizardDialog;

/**
 *
 * @author nicolas
 */
public class FTPWizardDialog extends WizardDialog{
    public FTPWizardDialog(){
        this(new FTPWizard());
    }
    public FTPWizardDialog(Wizard wizard){
        super(wizard);
        setTitle(getApplicationName());
        setResizable(false);        
    }
}
