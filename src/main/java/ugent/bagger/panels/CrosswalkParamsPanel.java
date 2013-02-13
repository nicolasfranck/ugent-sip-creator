package ugent.bagger.panels;

import java.awt.Dimension;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JPanel;
import ugent.bagger.forms.CrosswalkParamsForm;
import ugent.bagger.params.CrosswalkParams;

/**
 *
 * @author nicolas
 */
public class CrosswalkParamsPanel extends JPanel{
    CrosswalkParams crosswalkParams;
    CrosswalkParamsForm crosswalkParamsForm; 
   
    public CrosswalkParamsPanel(){
        init();
    }
    public void init(){
        setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));                
        JComponent form = getCrosswalkParamsForm().getControl();
        form.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
        Dimension dim = form.getPreferredSize();
        form.setMaximumSize(dim);
        form.setMinimumSize(dim);
        add(form);        
    }

    public CrosswalkParams getCrosswalkParams() {
        if(crosswalkParams == null){
            crosswalkParams = new CrosswalkParams();
        }
        return crosswalkParams;
    }
    public void setCrosswalkParams(CrosswalkParams crosswalkParams) {
        this.crosswalkParams = crosswalkParams;
    }
    public CrosswalkParamsForm getCrosswalkParamsForm() {
        if(crosswalkParamsForm == null){
            crosswalkParamsForm = new CrosswalkParamsForm(getCrosswalkParams());
        }
        return crosswalkParamsForm;
    }

    public void setCrosswalkParamsForm(CrosswalkParamsForm crosswalkParamsForm) {
        this.crosswalkParamsForm = crosswalkParamsForm;
    }      
}
