/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ugent.bagger.forms;

import RenameWandLib.ErrorAction;
import ugent.bagger.helper.Context;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;
import org.springframework.binding.form.FormModel;
import org.springframework.richclient.form.AbstractForm;
import org.springframework.richclient.form.FormModelHelper;
import org.springframework.richclient.form.binding.Binding;
import org.springframework.richclient.form.binding.swing.SwingBindingFactory;
import org.springframework.richclient.form.builder.TableFormBuilder;
import ugent.bagger.params.SimpleRenameParams;

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
        
        builder.setLabelAttributes("colSpan=1 align=left");

        final ArrayList<JComponent>list = new ArrayList<JComponent>();

        builder.addSeparator(Context.getMessage("common"));
        
        String [] commonFields = {"prefix","suffix","name","preserveExtension","number"};
        for(String field:commonFields){
            builder.row();
            builder.add(field);
        }
        
        list.add(builder.add("startNumber")[1]);
        builder.row();
        list.add(builder.add("jumpNumber")[1]);
        builder.row();
        list.add(builder.add("numPadding")[1]);
        builder.row();

        builder.addSeparator(Context.getMessage("detail"));
        builder.row();
        builder.add("overWrite");
        builder.row();

        getFormModel().getValueModel("number").addValueChangeListener(new PropertyChangeListener(){
            @Override
            public void propertyChange(PropertyChangeEvent pce) {
                Boolean numberChecked = (Boolean) pce.getNewValue();
                for(JComponent component:list){
                    ugent.bagger.helper.SwingUtils.setJComponentEnabled(component,numberChecked.booleanValue());
                }
            }
        });       
        
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