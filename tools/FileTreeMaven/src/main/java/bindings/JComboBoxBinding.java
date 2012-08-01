/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package bindings;

import java.awt.Component;
import javax.swing.JComboBox;
import org.springframework.binding.form.FormModel;
import org.springframework.richclient.form.binding.support.CustomBinding;

/**
 *
 * @author nicolas
 */
public class JComboBoxBinding extends CustomBinding{
    JComboBox comboBox;

    public JComboBoxBinding(JComboBox comboBox,FormModel formModel, String formPropertyPath){
        super(formModel,formPropertyPath,Object.class);
        this.comboBox = comboBox;
    }
    @Override
    protected void valueModelChanged(Object newValue) {        
    }
    @Override
    protected JComboBox doBindControl() {
        return comboBox;
    }
    @Override
    protected void readOnlyChanged() {        
    }
    @Override
    protected void enabledChanged() {        
    }
}
