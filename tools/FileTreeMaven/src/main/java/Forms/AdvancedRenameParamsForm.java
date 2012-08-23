/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Forms;

import RenameWandLib.ErrorAction;
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
import Params.AdvancedRenameParams;

/**
 *
 * @author nicolas
 */
public class AdvancedRenameParamsForm extends AbstractForm{
    
    public AdvancedRenameParamsForm(AdvancedRenameParams renameParams){
        this(FormModelHelper.createFormModel(renameParams,"advancedRenameForm"));
    }
    public AdvancedRenameParamsForm(FormModel formModel){
        super(formModel);       
    }    
    @Override
    protected JComponent createFormControl() {

        SwingBindingFactory bf = (SwingBindingFactory) getBindingFactory();

        TableFormBuilder builder = new TableFormBuilder(bf);

        builder.addSeparator(Context.getMessage("common"));
        builder.row();

        builder.add("sourcePattern","colSpan=1 align=left");
        builder.row();
        builder.add("destinationPattern","colSpan=1 align=left");
        builder.row();
        builder.addSeparator(Context.getMessage("detail"));
        builder.row();
        builder.add("renameDirectories","colSpan=1 align=left");
        builder.row();
        builder.add("copy","colSpan=1 align=left");
        builder.row();
        builder.add("matchLowerCase","colSpan=1 align=left");
        builder.row();
        builder.add("recurseIntoSubdirectories","colSpan=1 align=left");
        builder.row();
        builder.add("overWrite","colSpan=1 align=left");
        builder.row();

        Binding b = bf.createBoundComboBox("onErrorAction",ErrorAction.values());
        final JComboBox comboBox = ((JComboBox)b.getControl());

        SwingUtilities.invokeLater(new Runnable(){
            @Override
            public void run() {
                comboBox.setEditable(false)                ;
                comboBox.setSelectedItem(ErrorAction.ignore);
            }        
        });
        builder.add(b);
        builder.row();


        return builder.getForm();
    }
}
