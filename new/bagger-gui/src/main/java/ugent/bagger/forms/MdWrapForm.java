/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ugent.bagger.forms;

import com.anearalone.mets.MdSec.MDTYPE;
import com.anearalone.mets.MdSec.MdWrap;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.JComboBox;
import javax.swing.JComponent;
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
        MdWrap mdWrap = (MdWrap) getFormObject();
        
        SwingBindingFactory bf = (SwingBindingFactory) getBindingFactory();

        TableFormBuilder builder = new TableFormBuilder(bf);       
        System.out.println("label attributes: "+builder.getLabelAttributes());
        
        builder.setLabelAttributes("colSpan=1 align=left");
        
        builder.addSeparator("data");
        builder.row();
        
        builder.add("ID");
        builder.row();
        
        builder.add("label");
        builder.row();
      
        Binding mdTypeBinding = bf.createBoundComboBox("MDTYPE",MDTYPE.values());
        JComboBox box = (JComboBox)mdTypeBinding.getControl();
     
        builder.add(mdTypeBinding);        
        builder.row();           
        
        final JComponent [] componentsOtherMdType = builder.add("OTHERMDTYPE");
        
        ugent.bagger.helper.SwingUtils.setJComponentsEnabled(componentsOtherMdType,mdWrap.getMDTYPE() == MDTYPE.OTHER);
        builder.row();        
        
        getFormModel().getValueModel("MDTYPE").addValueChangeListener(new PropertyChangeListener(){
            @Override
            public void propertyChange(PropertyChangeEvent pce) {
                MDTYPE mdType = (MDTYPE) pce.getNewValue();                   
                ugent.bagger.helper.SwingUtils.setJComponentsEnabled(componentsOtherMdType,mdType == MDTYPE.OTHER);                 
            }
        });        

        return builder.getForm();
    }
}
