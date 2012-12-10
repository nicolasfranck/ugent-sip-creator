package ugent.bagger.wizards;

import java.awt.Dimension;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import org.springframework.binding.validation.ValidationListener;
import org.springframework.binding.validation.ValidationResults;
import org.springframework.richclient.wizard.AbstractWizardPage;
import ugent.bagger.panels.ExportParamsPanel;

/**
 *
 * @author nicolas
 */
public class ExportWizardPage1 extends AbstractWizardPage {        
    private ExportParamsPanel exportParamsPanel;    
    
    public ExportWizardPage1(String pageId){
        super(pageId);
    }
    public ExportParamsPanel getExportParamsPanel() {
        if(exportParamsPanel == null){
            exportParamsPanel = new ExportParamsPanel();
            exportParamsPanel.setPreferredSize(new Dimension(500,400));
            exportParamsPanel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));            
            exportParamsPanel.getExportParamsForm().addValidationListener(new ValidationListener() {
                @Override
                public void validationResultsChanged(ValidationResults results) {                    
                    if(!results.getHasErrors()){
                        setPageComplete(false);
                        setPageComplete(true);                        
                    }
                }
            });
        }
        return exportParamsPanel;
    }
    public void setExportParamsPanel(ExportParamsPanel exportParamsPanel) {
        this.exportParamsPanel = exportParamsPanel;
    }    
    @Override
    protected JComponent createControl() {                        
        return getExportParamsPanel();
    }   
    @Override
    public boolean canFlipToNextPage(){                
        return super.canFlipToNextPage() && !getExportParamsPanel().getExportParamsForm().hasErrors();
    }    
}