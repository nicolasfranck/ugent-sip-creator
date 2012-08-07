/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package forms;

import RenameWandLib.ErrorAction;
import helper.Context;
import helper.JComponentUtils;
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
import Params.SimpleRenameParams;

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

        final ArrayList<JComponent>list = new ArrayList<JComponent>();

        builder.addSeparator(Context.getMessage("common"));
        builder.row();
        builder.add("prefix","colSpan=1 align=left");
        builder.row();
        builder.add("suffix","colSpan=1 align=left");
        builder.row();
        builder.add("name","colSpan=1 align=left");
        builder.row();
        builder.add("preserveExtension","colSpan=1 align=left");
        builder.row();

        builder.add("number","colSpan=1 align=left");
        builder.row();        
        list.add(builder.add("startNumber","colSpan=1 align=left")[1]);
        builder.row();
        list.add(builder.add("jumpNumber","colSpan=1 align=left")[1]);
        builder.row();
        list.add(builder.add("numPadding","colSpan=1 align=left")[1]);
        builder.row();

        builder.addSeparator(Context.getMessage("detail"));
        builder.row();
        builder.add("overWrite","colSpan=1 align=left");
        builder.row();

        getFormModel().getValueModel("number").addValueChangeListener(new PropertyChangeListener(){
            @Override
            public void propertyChange(PropertyChangeEvent pce) {
                Boolean numberChecked = (Boolean) pce.getNewValue();
                for(JComponent component:list){
                    JComponentUtils.setEnabled(component,numberChecked.booleanValue());
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
