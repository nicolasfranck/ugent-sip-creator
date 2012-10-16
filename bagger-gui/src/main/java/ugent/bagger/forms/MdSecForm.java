/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ugent.bagger.forms;

import com.anearalone.mets.MdSec;
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
import ugent.bagger.helper.SwingUtils;
/**
 *
 * @author nicolas
 */
public class MdSecForm extends AbstractForm{
    
    public MdSecForm(MdSec mdSec){                
        this(FormModelHelper.createFormModel(mdSec,"mdSecForm"));
    }
    public MdSecForm(FormModel formModel){
        super(formModel);       
    }    
    @Override
    protected JComponent createFormControl() {
        
        SwingBindingFactory bf = (SwingBindingFactory) getBindingFactory();
        MdSec mdSec = (MdSec) getFormObject();

        TableFormBuilder builder = new TableFormBuilder(bf);
        
        builder.setLabelAttributes("colSpan=1 align=left");
        
        String [] fields = {"GROUPID","STATUS"};
        //CREATED lukt niet want setCREATED vereist een speciale behandeling
        for(String field:fields){
            builder.add(field);
            builder.row();        
        }               
       
        //mdWrap
        builder.add("mdWrap.label");
        builder.row();
      
        Binding mdTypeBinding = bf.createBoundComboBox("mdWrap.MDTYPE",MdSec.MDTYPE.values());
        JComboBox box = (JComboBox)mdTypeBinding.getControl();
     
        builder.add(mdTypeBinding);        
        builder.row();           
        
        final JComponent [] componentsOtherMdType = builder.add("mdWrap.OTHERMDTYPE");
        
        SwingUtils.setJComponentsEnabled(componentsOtherMdType,mdSec.getMdWrap().getMDTYPE() == MdSec.MDTYPE.OTHER);
        builder.row();        
        
        getFormModel().getValueModel("mdWrap.MDTYPE").addValueChangeListener(new PropertyChangeListener(){
            @Override
            public void propertyChange(PropertyChangeEvent pce) {
                MdSec.MDTYPE mdType = (MdSec.MDTYPE) pce.getNewValue();                   
                SwingUtils.setJComponentsEnabled(componentsOtherMdType,mdType == MdSec.MDTYPE.OTHER);                 
            }
        }); 
        
        return builder.getForm();
    }
}