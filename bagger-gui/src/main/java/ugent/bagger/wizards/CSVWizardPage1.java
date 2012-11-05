package ugent.bagger.wizards;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import org.springframework.richclient.wizard.AbstractWizardPage;
import ugent.bagger.panels.CSV1Panel;

/**
 *
 * @author nicolas
 */
public class CSVWizardPage1 extends AbstractWizardPage {        
    private CSV1Panel csv1Panel;
    public CSVWizardPage1(String pageId){
        super(pageId);
    }
    public CSV1Panel getCsv1Panel() {
        if(csv1Panel == null){
            csv1Panel = new CSV1Panel();
            csv1Panel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        }
        return csv1Panel;
    }
    public void setCsv1Panel(CSV1Panel csv1Panel) {
        this.csv1Panel = csv1Panel;
    }    
    @Override
    protected JComponent createControl() {                
        return getCsv1Panel();
    }   
    @Override
    public boolean canFlipToNextPage(){                   
        return !getCsv1Panel().getCsvParseParamsForm().hasErrors();
    }
}