/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package forms;

import javax.swing.JComponent;
import org.springframework.binding.form.FormModel;
import org.springframework.richclient.form.AbstractForm;
import org.springframework.richclient.form.FormModelHelper;
import org.springframework.richclient.form.builder.TableFormBuilder;
import renaming.RenamePair;

/**
 *
 * @author nicolas
 */
public class RenamePairForm extends AbstractForm{
    
    public RenamePairForm(RenamePair renamePair){
        this(FormModelHelper.createFormModel(renamePair,"renamePairForm"));
    }
    public RenamePairForm(FormModel formModel){
        super(formModel);
    }    
    @Override
    protected JComponent createFormControl() {
        TableFormBuilder builder = new TableFormBuilder(getBindingFactory());        
        builder.add("sourcePattern","colSpan=1 align=left");
        builder.row();
        builder.add("destinationPattern","colSpan=1 align=left");
        builder.row();
        return builder.getForm();
    }
}
