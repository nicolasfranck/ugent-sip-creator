package ugent.bagger.wizards;

import java.awt.Dimension;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import org.springframework.binding.validation.ValidationListener;
import org.springframework.binding.validation.ValidationResults;
import org.springframework.richclient.wizard.AbstractWizardPage;
import ugent.bagger.panels.CSV1Panel;

/**
 *
 * @author nicolas
 */
public class CSVWizardPage1 extends AbstractWizardPage {        
    CSV1Panel csv1Panel;    
    public CSVWizardPage1(String pageId){
        super(pageId);
    }
    public CSV1Panel getCsv1Panel() {
        if(csv1Panel == null){
            csv1Panel = new CSV1Panel();
            csv1Panel.setPreferredSize(new Dimension(500,400));
            csv1Panel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
            csv1Panel.getCsvParseParamsForm().addValidationListener(new ValidationListener() {
                @Override
                public void validationResultsChanged(ValidationResults results) {                    
                    if(!results.getHasErrors()){
                        setPageComplete(false);
                        setPageComplete(true);
                    }
                }
            });
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
        return super.canFlipToNextPage() && !getCsv1Panel().getCsvParseParamsForm().hasErrors();
    }
    
}