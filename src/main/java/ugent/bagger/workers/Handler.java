package ugent.bagger.workers;

import gov.loc.repository.bagger.ui.Progress;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;

/**
 *
 * @author nicolas
 */
public abstract class Handler extends AbstractAction implements Progress{  
    @Override
    public void actionPerformed(ActionEvent ae) {
        execute();
    }
}