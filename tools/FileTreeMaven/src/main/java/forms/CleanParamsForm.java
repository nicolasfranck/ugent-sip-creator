/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package forms;

import RenameWandLib.OnErrorAction;
import bindings.JComboBoxBinding;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import org.springframework.binding.form.FormModel;
import org.springframework.richclient.form.AbstractForm;
import org.springframework.richclient.form.FormModelHelper;
import org.springframework.richclient.form.builder.TableFormBuilder;
import renaming.CleanParams;

/**
 *
 * @author nicolas
 */
public class CleanParamsForm extends AbstractForm{
    
    public CleanParamsForm(CleanParams cleanParams){
        this(FormModelHelper.createFormModel(cleanParams,"cleanParamsForm"));
    }
    public CleanParamsForm(FormModel formModel){
        super(formModel);
    }    
    @Override
    protected JComponent createFormControl() {
        TableFormBuilder builder = new TableFormBuilder(getBindingFactory());
        builder.addSeparator("opkuisen naamgeving");
        builder.row();
        /*builder.add("substitutes","colSpan=1 align=left");
        builder.row();*/
        builder.add("cleanDirectories");
        builder.row();
        builder.add("copy");
        builder.row();

        return builder.getForm();
    }
}
