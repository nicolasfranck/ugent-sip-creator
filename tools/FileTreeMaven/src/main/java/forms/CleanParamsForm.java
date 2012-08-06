/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package forms;

import RenameWandLib.OnErrorAction;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import org.springframework.binding.form.FormModel;
import org.springframework.richclient.form.AbstractForm;
import org.springframework.richclient.form.FormModelHelper;
import org.springframework.richclient.form.binding.Binding;
import org.springframework.richclient.form.binding.swing.SwingBindingFactory;
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
        SwingBindingFactory bf = (SwingBindingFactory) getBindingFactory();
        TableFormBuilder builder = new TableFormBuilder(bf);
        
        builder.add("cleanDirectories");
        builder.row();
        builder.add("copy");
        builder.row();
        builder.add("overWrite");
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

        JTable table = new JTable(new DefaultTableModel(
            new Object [] [] {},
            new String [] {"van","naar"}
        ));


        return builder.getForm();
    }
}
