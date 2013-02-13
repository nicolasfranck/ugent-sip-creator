package ugent.bagger.forms;

import javax.swing.JComponent;
import org.springframework.binding.form.FormModel;
import org.springframework.richclient.form.AbstractForm;
import org.springframework.richclient.form.FormModelHelper;
import org.springframework.richclient.form.binding.Binding;
import org.springframework.richclient.form.binding.swing.SwingBindingFactory;
import org.springframework.richclient.form.builder.TableFormBuilder;
import ugent.bagger.params.RenameParams;
import ugent.rename.ErrorAction;

/**
 *
 * @author nicolas
 */
public class RenameParamsForm extends AbstractForm{
    
    public RenameParamsForm(RenameParams renameParams){
        this(FormModelHelper.createFormModel(renameParams,"renameForm"));
    }
    public RenameParamsForm(FormModel formModel){
        super(formModel);       
    }    
    @Override
    protected JComponent createFormControl() {

        SwingBindingFactory bf = (SwingBindingFactory) getBindingFactory();
        TableFormBuilder builder = new TableFormBuilder(bf);        
        builder.setLabelAttributes("colSpan=1 align=left");                
                
        for(String field:new String [] {"source","destination","ignoreCase",/*"renameExtension",*/"recursive","prefix","postfix","regex"}){
            builder.add(field);
            builder.row();
        }                       
        
        Binding b = bf.createBoundComboBox("onErrorAction",ErrorAction.values());        
        builder.add(b);
        builder.row();
        
        getFormModel().validate();
        
        return builder.getForm();
    }
}