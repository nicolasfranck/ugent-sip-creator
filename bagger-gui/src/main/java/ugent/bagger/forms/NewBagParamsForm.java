package ugent.bagger.forms;

import javax.swing.JComponent;
import org.springframework.binding.form.FormModel;
import org.springframework.richclient.form.AbstractForm;
import org.springframework.richclient.form.FormModelHelper;
import org.springframework.richclient.form.binding.swing.SwingBindingFactory;
import org.springframework.richclient.form.builder.TableFormBuilder;
import ugent.bagger.params.NewBagParams;

/**
 *
 * @author nicolas
 */
public class NewBagParamsForm extends AbstractForm{    
    public NewBagParamsForm(NewBagParams newBagParams){
        this(FormModelHelper.createFormModel(newBagParams,"newBagParamsForm"));
    }
    public NewBagParamsForm(FormModel formModel){
        super(formModel);       
    }    
    @Override
    protected JComponent createFormControl() {
        SwingBindingFactory bf = (SwingBindingFactory) getBindingFactory();
        TableFormBuilder builder = new TableFormBuilder(bf);        
        builder.setLabelAttributes("colSpan=1 align=left");                
        //bagId
        builder.add("bagId");        
        builder.row();           
        
        getFormModel().validate();
        
        return builder.getForm();
    }    
}