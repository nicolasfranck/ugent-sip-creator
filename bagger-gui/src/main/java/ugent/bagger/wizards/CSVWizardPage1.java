package ugent.bagger.wizards;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;
import org.springframework.richclient.wizard.AbstractWizardPage;
import ugent.bagger.panels.CSVPanel;

/**
 *
 * @author nicolas
 */
public class CSVWizardPage1 extends AbstractWizardPage {    
    public CSVWizardPage1(String pageId){
        super(pageId);
    }
    @Override
    protected JComponent createControl() {
        JPanel panel = new CSVPanel();
        panel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        return panel;
    }
   
}
