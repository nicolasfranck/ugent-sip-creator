/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ugent.bagger.views;

import org.springframework.richclient.application.support.AbstractView;

/**
 *
 * @author nicolas
 */
abstract public class DefaultView extends AbstractView{
    @Override
    public void componentFocusGained(){
        //ugent.bagger.helper.SwingUtils.getFrame().pack();        
    }
}
