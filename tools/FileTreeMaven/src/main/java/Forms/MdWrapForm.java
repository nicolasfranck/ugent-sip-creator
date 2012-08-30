/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Forms;

import com.anearalone.mets.MdSec.MDTYPE;
import com.anearalone.mets.MdSec.MdWrap;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JTextField;
import org.springframework.binding.form.FormModel;
import org.springframework.richclient.form.AbstractForm;
import org.springframework.richclient.form.FormModelHelper;
import org.springframework.richclient.form.binding.Binding;
import org.springframework.richclient.form.binding.swing.SwingBindingFactory;
import org.springframework.richclient.form.builder.TableFormBuilder;

/**
 *
 * @author nicolas
 */
public class MdWrapForm extends AbstractForm{
    
    public MdWrapForm(MdWrap mdWrap){        
        this(FormModelHelper.createFormModel(mdWrap,"mdWrapForm"));
    }
    public MdWrapForm(FormModel formModel){
        super(formModel);       
    }    
    @Override
    protected JComponent createFormControl() {

        SwingBindingFactory bf = (SwingBindingFactory) getBindingFactory();

        TableFormBuilder builder = new TableFormBuilder(bf);       
        
        builder.setLabelAttributes("colSpan=1 align=left");
        
        builder.add("ID");
        builder.row();
        
        builder.add("label");
        builder.row();
      
        Binding mdTypeBinding = bf.createBoundComboBox("MDTYPE",MDTYPE.values());
        JComboBox box = (JComboBox)mdTypeBinding.getControl();
     
        builder.add(mdTypeBinding);        
        builder.row();           
        
        final JComponent [] componentsOtherMdType = builder.add("OTHERMDTYPE");
        helper.SwingUtils.setJComponentsEnabled(componentsOtherMdType,false);
        builder.row();        
        
        getFormModel().getValueModel("MDTYPE").addValueChangeListener(new PropertyChangeListener(){
            @Override
            public void propertyChange(PropertyChangeEvent pce) {
                MDTYPE mdType = (MDTYPE) pce.getNewValue();     
                boolean enabled = false;
                if(mdType == MDTYPE.OTHER){
                    enabled = true;                    
                }else{
                    enabled = false;
                    ((JTextField)componentsOtherMdType[1]).setText("");                    
                }
                helper.SwingUtils.setJComponentsEnabled(componentsOtherMdType,enabled);                
            }
        });        

        return builder.getForm();
    }
}
