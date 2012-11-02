package ugent.bagger.wizards;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import org.springframework.richclient.wizard.AbstractWizardPage;
import ugent.bagger.panels.CSV2Panel;

/**
 *
 * @author nicolas
 */
public class CSVWizardPage2 extends AbstractWizardPage {        
    private CSV2Panel csv2Panel;
    
    public CSVWizardPage2(String pageId){
        super(pageId);
        System.out.println("CSVWizardPage2()");
    }
    
    
    public CSV2Panel getCsv2Panel() {
        if(csv2Panel == null){
            System.out.println("CSVWizardPage2::getCsv2Panel()");
            csv2Panel = new CSV2Panel();
            csv2Panel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        }
        return csv2Panel;
    }
    public void setCsv2Panel(CSV2Panel csv2Panel) {
        this.csv2Panel = csv2Panel;
    }    
    @Override
    protected JComponent createControl() {                
        System.out.println("CSVWizardPage2::createControl()");
        return getCsv2Panel();
    }   
    @Override
    public boolean canFlipToNextPage(){           
        System.out.println("CSVWizardPage2::canFlipToNextPage()");
        return true;
    }
}