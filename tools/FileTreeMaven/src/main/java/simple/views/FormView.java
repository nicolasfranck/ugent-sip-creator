/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package simple.views;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.*;
import org.springframework.binding.form.ValidatingFormModel;
import org.springframework.binding.validation.ValidationListener;
import org.springframework.binding.validation.ValidationResults;
import org.springframework.richclient.application.support.AbstractView;
import test.Human;
import test.HumanForm;

;
/**
 *
 * @author nicolas
 */
public class FormView extends AbstractView{
   
    private JPanel panel;
    private ValidatingFormModel formModel;
        
    @Override
    protected JComponent createControl() {
        panel = new JPanel();
        panel.setLayout(new BorderLayout());

        HumanForm form = new HumanForm(new Human("Franck","Nicolas"));
        formModel = form.getFormModel();

        final JButton submitButton = new JButton("submit");
        submitButton.setEnabled(false);

        submitButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent ae) {
                System.out.println("SUBMIT: committing changes..");
                formModel.commit();
            }
        });
        form.addValidationListener(new ValidationListener(){
            @Override
            public void validationResultsChanged(ValidationResults results) {
                submitButton.setEnabled(!results.getHasErrors());
            }
        });
        panel.add(submitButton,BorderLayout.SOUTH);
       
        panel.add(form.getControl(),BorderLayout.CENTER);        
        return panel;
    }
    @Override
    public void componentFocusLost(){
        if(!formModel.getHasErrors())
            formModel.commit();
    }
}
