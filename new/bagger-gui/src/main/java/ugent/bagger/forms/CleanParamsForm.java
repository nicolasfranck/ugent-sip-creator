/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ugent.bagger.forms;

import ugent.bagger.RenameWandLib.ErrorAction;
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
import ugent.bagger.params.CleanParams;

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
        
        builder.setLabelAttributes("colSpan=1 align=left");
        
        String [] fields = {"cleanDirectories","copy","overWrite"};
        for(String field:fields){
            builder.add(field);
            builder.row();
        }
     
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

        JTable table = new JTable(new DefaultTableModel(
            new Object [] [] {},
            new String [] {"van","naar"}
        ));
        return builder.getForm();
    }
}
