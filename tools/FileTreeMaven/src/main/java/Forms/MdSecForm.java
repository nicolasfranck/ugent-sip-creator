/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Forms;

import com.anearalone.mets.MdSec;
import javax.swing.JComponent;
import org.springframework.binding.form.FormModel;
import org.springframework.richclient.form.AbstractForm;
import org.springframework.richclient.form.FormModelHelper;
import org.springframework.richclient.form.binding.swing.SwingBindingFactory;
import org.springframework.richclient.form.builder.TableFormBuilder;

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

        TableFormBuilder builder = new TableFormBuilder(bf);
        
        builder.setLabelAttributes("colSpan=1 align=left");
        
        String [] fields = {"ID","GROUPID","STATUS","CREATED"};
        
        for(String field:fields){
            builder.add(field);
            builder.row();        
        }        
        
        return builder.getForm();
    }
}
