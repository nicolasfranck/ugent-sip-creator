/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package forms;

import RenameWandLib.OnErrorAction;
import helper.Context;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;
import org.springframework.binding.form.FormModel;
import org.springframework.richclient.form.AbstractForm;
import org.springframework.richclient.form.FormModelHelper;
import org.springframework.richclient.form.binding.Binding;
import org.springframework.richclient.form.binding.swing.SwingBindingFactory;
import org.springframework.richclient.form.builder.TableFormBuilder;
import renaming.SimpleRenameParams;

/**
 *
 * @author nicolas
 */
public class SimpleRenameParamsForm extends AbstractForm{
    
    public SimpleRenameParamsForm(SimpleRenameParams renameParams){
        this(FormModelHelper.createFormModel(renameParams,"simpleRenameForm"));
    }
    public SimpleRenameParamsForm(FormModel formModel){
        super(formModel);       
    }    
    @Override
    protected JComponent createFormControl() {

        SwingBindingFactory bf = (SwingBindingFactory) getBindingFactory();

        TableFormBuilder builder = new TableFormBuilder(bf);

        builder.addSeparator(Context.getMessage("common"));
        builder.row();

        builder.add("prefix","colSpan=1 align=left");
        builder.row();
        builder.add("suffix","colSpan=1 align=left");
        builder.row();
        builder.add("name","colSpan=1 align=left");
        builder.row();        
        builder.add("renumber","colSpan=1 align=left");
        builder.row();
        builder.add("paddingNum","colSpan=1 align=left");
        builder.row();


        builder.addSeparator(Context.getMessage("detail"));
        builder.row();
        builder.add("copy","colSpan=1 align=left");
        builder.row();
        builder.add("matchLowerCase","colSpan=1 align=left");
        builder.row();
        builder.add("recurseIntoSubdirectories","colSpan=1 align=left");
        builder.row();
        builder.add("overWrite","colSpan=1 align=left");
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
