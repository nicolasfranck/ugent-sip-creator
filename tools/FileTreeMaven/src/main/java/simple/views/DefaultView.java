/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package simple.views;

import org.springframework.richclient.application.support.AbstractView;

/**
 *
 * @author nicolas
 */
abstract public class DefaultView extends AbstractView{
    @Override
    public void componentFocusGained(){
        helper.SwingUtils.getFrame().pack();        
    }
}
