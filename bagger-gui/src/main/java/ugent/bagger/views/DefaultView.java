package ugent.bagger.views;

import javax.swing.SwingUtilities;
import org.springframework.richclient.application.support.AbstractView;
import ugent.bagger.helper.SwingUtils;

/**
 *
 * @author nicolas
 */
abstract public class DefaultView extends AbstractView{        
    @Override
    public void componentFocusGained(){                        
        //Nicolas Franck: van belang dat dit LATER gebeurd (anders klapt frame dicht)        
        SwingUtilities.invokeLater(new Runnable(){
            @Override
            public void run() {
                SwingUtils.getFrame().pack();        
            }            
        });
    }
}
