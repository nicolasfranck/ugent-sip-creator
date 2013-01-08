package ugent.bagger.wizards;

import java.awt.Dimension;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import org.springframework.binding.validation.ValidationListener;
import org.springframework.binding.validation.ValidationResults;
import org.springframework.richclient.wizard.AbstractWizardPage;
import ugent.bagger.panels.CrosswalkParamsPanel;

/**
 *
 * @author nicolas
 */
public class CrosswalkParamsPage1 extends AbstractWizardPage {        
    private CrosswalkParamsPanel crosswalkParamsPanel;    
    
    public CrosswalkParamsPage1(String pageId){
        super(pageId);
    }

    public CrosswalkParamsPanel getCrosswalkParamsPanel() {
        if(crosswalkParamsPanel == null){
            crosswalkParamsPanel = new CrosswalkParamsPanel();
            Dimension dim = crosswalkParamsPanel.getPreferredSize();
            crosswalkParamsPanel.setMinimumSize(dim);
            crosswalkParamsPanel.setMaximumSize(dim);            
            crosswalkParamsPanel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));            
            crosswalkParamsPanel.getCrosswalkParamsForm().addValidationListener(new ValidationListener() {
                @Override
                public void validationResultsChanged(ValidationResults results) {                    
                    if(!results.getHasErrors()){
                        setPageComplete(false);
                        setPageComplete(true);                        
                    }
                }
            });
        }
        return crosswalkParamsPanel;
    }         
    @Override
    protected JComponent createControl() {                        
        return getCrosswalkParamsPanel();
    }   
    @Override
    public boolean canFlipToNextPage(){                
        return super.canFlipToNextPage() && !getCrosswalkParamsPanel().getCrosswalkParamsForm().hasErrors();
    }    
}