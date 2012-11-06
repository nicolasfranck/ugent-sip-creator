package ugent.bagger.wizards;

import java.awt.Dimension;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;
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
    }
    public CSV2Panel getCsv2Panel() {
        if(csv2Panel == null){            
            csv2Panel = new CSV2Panel();
            csv2Panel.setPreferredSize(new Dimension(500,400));
            csv2Panel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        }
        return csv2Panel;
    }
    public void setCsv2Panel(CSV2Panel csv2Panel) {
        this.csv2Panel = csv2Panel;
    }    
    @Override
    protected JComponent createControl() {                                
        return getCsv2Panel();
    }   
    @Override
    public boolean canFlipToNextPage(){                   
        return false;
    }
    @Override
    public void onAboutToShow(){
        SwingUtilities.invokeLater(new Runnable(){
            @Override
            public void run() {                
                getCsv2Panel().getShowListener().actionPerformed(null);                
            }            
        });
    }
}