/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package bindings;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.AbstractButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import org.springframework.binding.form.FormModel;
import org.springframework.richclient.form.binding.support.AbstractBinding;

/**
 *
 * @author nicolas
 */
public class FileChooserBinding extends AbstractBinding{
    private JFileChooser fileChooser;
    private AbstractButton button;
    public FileChooserBinding(FormModel formModel,String property,JFileChooser fileChooser,AbstractButton button){
        super(formModel,property,String.class);
        setFileChooser(fileChooser);
        setButton(button);
    }
    @Override
    protected JComponent doBindControl(){
        button.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent ae) {                                
                File file;
                if(
                    getFileChooser().showOpenDialog(null) == JFileChooser.APPROVE_OPTION
                )file = getFileChooser().getSelectedFile();
                else file = null;
                if(file != null)
                    getFormModel().getValueModel(getProperty()).setValue(file);
            }
        });
        return button;
    }
    @Override
    protected void readOnlyChanged() {        
    }
    @Override
    protected void enabledChanged() {        
    }
    public JFileChooser getFileChooser() {
        return fileChooser;
    }
    public void setFileChooser(JFileChooser fileChooser) {
        this.fileChooser = fileChooser;
    }
    public AbstractButton getButton() {
        return button;
    }
    public void setButton(AbstractButton button) {
        this.button = button;
    }
}
