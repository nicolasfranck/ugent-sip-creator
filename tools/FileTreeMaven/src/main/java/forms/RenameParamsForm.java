/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package forms;

import RenameWandLib.OnErrorAction;
import bindings.JComboBoxBinding;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;
import org.springframework.binding.form.FormModel;
import org.springframework.richclient.form.AbstractForm;
import org.springframework.richclient.form.FormModelHelper;
import org.springframework.richclient.form.binding.Binding;
import org.springframework.richclient.form.binding.swing.SwingBindingFactory;
import org.springframework.richclient.form.builder.TableFormBuilder;
import renaming.RenameParams;

/**
 *
 * @author nicolas
 */
public class RenameParamsForm extends AbstractForm{
    
    public RenameParamsForm(RenameParams renameParams){
        this(FormModelHelper.createFormModel(renameParams,"renamePairForm"));
    }
    public RenameParamsForm(FormModel formModel){
        super(formModel);       
    }    
    @Override
    protected JComponent createFormControl() {

        SwingBindingFactory bf = (SwingBindingFactory) getBindingFactory();

        TableFormBuilder builder = new TableFormBuilder(bf);
        builder.addSeparator("hernoemen");
        builder.row();
        builder.add("sourcePattern","colSpan=1 align=left");
        builder.row();
        builder.add("destinationPattern","colSpan=1 align=left");
        builder.row();
        builder.add("recurseIntoSubdirectories");
        builder.row();
        builder.add("copy");
        builder.row();

        Binding b = bf.createBoundComboBox("onErrorAction",OnErrorAction.values());
        final JComboBox comboBox = ((JComboBox)b.getControl());

        SwingUtilities.invokeLater(new Runnable(){
            @Override
            public void run() {
                comboBox.setEditable(false)                ;
                comboBox.setSelectedItem(OnErrorAction.ignore);
            }        
        });
        builder.add(b);
        builder.row();

        return builder.getForm();
    }
}
