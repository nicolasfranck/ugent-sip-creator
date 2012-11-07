/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ugent.bagger.wizards;

import org.springframework.richclient.wizard.Wizard;
import org.springframework.richclient.wizard.WizardDialog;

/**
 *
 * @author nicolas
 */
public class BagInfoImportWizardDialog extends WizardDialog{
    public BagInfoImportWizardDialog(){
        this(new BagInfoImportWizard());
    }
    public BagInfoImportWizardDialog(Wizard wizard){
        super(wizard);
    }    
}