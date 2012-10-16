package ugent.bagger.dialogs;

import java.awt.Frame;
import javax.swing.JDialog;
import org.springframework.util.Assert;
import ugent.bagger.panels.CreateBagsPanel;
import ugent.bagger.params.CreateBagsParams;

/**
 *
 * @author nicolas
 */
public class CreateBagsDialog extends JDialog {      
    public CreateBagsDialog(Frame parentFrame,CreateBagsParams createBagsParams){
        super(parentFrame,true);
        Assert.notNull(createBagsParams);
        setContentPane(new CreateBagsPanel(createBagsParams));
    }    
}