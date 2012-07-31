/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package test;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JComponent;
import org.springframework.binding.form.CommitListener;
import org.springframework.binding.form.FormModel;
import org.springframework.richclient.form.AbstractForm;
import org.springframework.richclient.form.FormModelHelper;
import org.springframework.richclient.form.builder.TableFormBuilder;

/**
 *
 * @author nicolas
 */
public class HumanForm extends AbstractForm{
    
    public HumanForm(Human human){
        this(FormModelHelper.createFormModel(human,"humanForm"));
    }
    public HumanForm(FormModel formModel){
        super(formModel);
    }
    
    @Override
    protected JComponent createFormControl() {
        TableFormBuilder builder = new TableFormBuilder(getBindingFactory());
        builder.addSeparator("General");
        builder.row();
        builder.add("firstName","colSpan=1 align=left");
        builder.row();
        builder.add("name","colSpan=1 align=left");
        builder.row();
        
        builder.addSeparator("address","colSpan=1 align=left");
        builder.row();
        builder.add("address.street","colSpan=1 align=left");
        builder.row();
        builder.add("address.streetNr","colSpan=1 align=left");
        builder.row();
        builder.add("address.postalCode","colSpan=1 align=left");
        builder.row();
        builder.add("address.place","colSpan=1 align=left");
        builder.row();
        builder.add("address.country","colSpan=1 align=left");
        builder.row();

        return builder.getForm();
    }
}
