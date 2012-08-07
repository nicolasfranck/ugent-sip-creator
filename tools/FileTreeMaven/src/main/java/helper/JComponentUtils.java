/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package helper;

import java.awt.Component;
import javax.swing.JComponent;

/**
 *
 * @author nicolas
 */
public class JComponentUtils {
    public static void setEnabled(JComponent component,boolean enabled){
        component.setEnabled(enabled);
        for(Component c:component.getComponents())
            c.setEnabled(enabled);
    }
}
