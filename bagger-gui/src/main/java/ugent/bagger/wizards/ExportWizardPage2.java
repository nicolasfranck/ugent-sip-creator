package ugent.bagger.wizards;

import java.awt.Dimension;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import org.springframework.richclient.wizard.AbstractWizardPage;
import ugent.bagger.panels.ExportPanel2;

/**
 *
 * @author nicolas
 */
public class ExportWizardPage2 extends AbstractWizardPage {        
    ExportPanel2 exportPanel2;    
    
    public ExportWizardPage2(String pageId){
        super(pageId);
    }
    public ExportPanel2 getExportPanel2() {
        if(exportPanel2 == null){
            exportPanel2 = new ExportPanel2();
            exportPanel2.setPreferredSize(new Dimension(500,400));
            exportPanel2.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));            
            ExportWizard wizard = (ExportWizard) getWizard();            
            exportPanel2.setExportParams(wizard.getExportWizardPage1().getExportParamsPanel().getExportParams());            
        }
        return exportPanel2;
    }
    public void setExportPanel2(ExportPanel2 exportPanel2) {
        this.exportPanel2 = exportPanel2;
    }    
    @Override
    protected JComponent createControl() {                
        return getExportPanel2();
    }   
    @Override
    public boolean canFlipToNextPage(){                   
        return false;
    }    
}