package ugent.bagger.bindings;

import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import javax.swing.*;
import org.springframework.binding.form.FormModel;
import org.springframework.richclient.form.binding.support.CustomBinding;

/**
 *
 * @author nicolas
 */
public final class FileSelectBinding extends CustomBinding{
    
    private JFileChooser fileChooser;    
    private JTextField field;
    private Component parent;
    private String template;    
    private String buttonText = "select ..";
    
    public FileSelectBinding(FormModel formModel, String formPropertyPath,JFileChooser fileChooser,String template,String buttonText,Component parent){
        super(formModel,formPropertyPath,ArrayList.class);   
        this.fileChooser = fileChooser;
        this.template = template;
        this.buttonText = buttonText;
        this.parent = parent;
    }

    public JTextField getField() {
        if(field == null){
            field = new JTextField(15); 
            
            field.setEditable(false);
            
            field.addMouseListener(new MouseAdapter(){
                @Override
                public void mouseClicked(MouseEvent me) {
                    fieldListener();
                }
            });            
            field.addActionListener(new ActionListener(){
                @Override
                public void actionPerformed(ActionEvent ae) {
                    fieldListener();
                }
                
            });            
        }
        return field;
    }
    private void fieldListener(){
        if(!getControl().isEnabled()){
            return;
        }
        int freturn = fileChooser.showOpenDialog(parent);
        File [] files = {};
        if(freturn == JFileChooser.APPROVE_OPTION) {
            if(fileChooser.isMultiSelectionEnabled()){
                files = fileChooser.getSelectedFiles();                
            }else{
                //when multiSelectionEnabled == false, then getSelectedFiles() returns an empty array!
                files = new File [] {fileChooser.getSelectedFile()};
            }
            if(files == null){
                files = new File [] {};
            }
        }
        if(files.length > 0){                        
            getValueModel().setValue(new ArrayList<File>(Arrays.asList(files)));                        
        }
    }
    public void setField(JTextField field) {
        this.field = field;
    }
    @Override
    protected void valueModelChanged(Object o){
        ArrayList<File>list = (ArrayList<File>)o;        
        
        if(list == null || list.isEmpty()){
            field.setText(String.format(getTemplate(),0));
        }else{            
            String t = list.size() > 1 ? String.format(getTemplate(),list.size()):list.get(0).getAbsolutePath();
            field.setText(t);        
        }
        field.invalidate();        
    }
    @Override
    protected JComponent doBindControl() {
        FlowLayout layout = new FlowLayout(FlowLayout.LEFT,0,0);        
        JPanel panel = new JPanel(layout);
        panel.add(getField());
        JButton browseButton = new JButton(buttonText);
        
        browseButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent ae) {
                fieldListener();
            }
        });
        panel.add(browseButton);        
        return panel;        
    }
    @Override
    protected void readOnlyChanged() {        
    }
    @Override
    protected void enabledChanged(){        
        
    }
    public String getTemplate() {
        if(template == null){
            template = "%s selected";
        }
        return template;
    }
    public void setTemplate(String template) {
        this.template = template;
    }    
}