package ugent.bagger.forms;

import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;
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
       
        String [] commonFields = {"source","destination"};
        for(String field:commonFields){
            builder.add(field);
            builder.row();
        }
        String [] detailFields = {
            "copy","overWrite","ignoreCase","regex"
        };
        for(String field:detailFields){
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
        return builder.getForm();
    }
}
