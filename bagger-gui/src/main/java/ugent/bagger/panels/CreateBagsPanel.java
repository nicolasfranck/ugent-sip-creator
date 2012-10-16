package ugent.bagger.panels;

import java.awt.BorderLayout;
import javax.swing.JComponent;
import javax.swing.JPanel;
import org.springframework.util.Assert;
import ugent.bagger.forms.CreateBagsParamsForm;
import ugent.bagger.params.CreateBagsParams;

/**
 *
 * @author nicolas
 */
public final class CreateBagsPanel extends JPanel{
    private CreateBagsParams createBagsParams;
    private CreateBagsParamsForm createBagsParamsForm;
    
    public CreateBagsPanel(CreateBagsParams createBagsParams){        
        Assert.notNull(createBagsParams);        
        this.createBagsParams = createBagsParams;        
        setLayout(new BorderLayout());
        add(createContentPane());        
    }
    public void reset(CreateBagsParams createBagsParams){             
        getCreateBagsParamsForm().setFormObject(createBagsParams);        
        this.createBagsParams = createBagsParams;    
    }
    protected JComponent createContentPane() {
        return getCreateBagsParamsForm().getControl();        
    }
    public CreateBagsParamsForm getCreateBagsParamsForm() {
        if(createBagsParamsForm == null){
            createBagsParamsForm = new CreateBagsParamsForm(createBagsParams);
        }
        return createBagsParamsForm;
    }
    public void setCreateBagsParamsForm(CreateBagsParamsForm createBagsParamsForm) {
        this.createBagsParamsForm = createBagsParamsForm;
    }
}