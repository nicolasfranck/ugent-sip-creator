package ugent.bagger.bindings;

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
    
    JFileChooser fileChooser;    
    JTextField field;
    JButton browseButton;
    Component parent;
    String template;    
    String buttonText;
    
    public FileSelectBinding(FormModel formModel, String formPropertyPath,JFileChooser fileChooser,Component parent){
        super(formModel,formPropertyPath,ArrayList.class);   
        this.fileChooser = fileChooser;        
        this.parent = parent;       
        ArrayList<File>files = (ArrayList<File>)formModel.getValueModel(formPropertyPath).getValue();
        
        if(files != null && !files.isEmpty()){            
            valueModelChanged(files);
        }        
    }

    protected JTextField getField() {
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
    void fieldListener(){
        if(!getControl().isEnabled()){
            return;
        }        
        int freturn;
        switch(fileChooser.getDialogType()){            
            case JFileChooser.SAVE_DIALOG:                
                freturn = fileChooser.showSaveDialog(parent);        
                break;
            case JFileChooser.OPEN_DIALOG:
            default:                
                freturn = fileChooser.showOpenDialog(parent);        
                break;
        }        
        File [] files = {};
        
        if(freturn == JFileChooser.APPROVE_OPTION) {                        
            if(
                fileChooser.getDialogType() == JFileChooser.OPEN_DIALOG && 
                fileChooser.isMultiSelectionEnabled()            
            ){                
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
            ArrayList<File>newList = new ArrayList<File>(Arrays.asList(files));
            getValueModel().setValue(newList);
            valueModelChanged(newList);
        }
    }
    protected void setField(JTextField field) {
        this.field = field;
    }
    @Override
    protected void valueModelChanged(Object o){
        ArrayList<File>list = (ArrayList<File>)o;        
        
        if(list == null || list.isEmpty()){
            getField().setText(String.format(getTemplate(),0));
        }else{            
            String t = list.size() > 1 ? String.format(getTemplate(),list.size()):list.get(0).getAbsolutePath();
            getField().setText(t); 
            if(!list.isEmpty()){
                fileChooser.setSelectedFile(list.get(0));
            }
        }
        getField().invalidate();        
    }
    @Override
    protected JComponent doBindControl() {         
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT,0,0)); 
        panel.add(getField());
        panel.add(getBrowseButton());        
        return panel;        
    }
    @Override
    protected void readOnlyChanged() {        
    }
    @Override
    protected void enabledChanged(){     
        boolean enabled = isEnabled();
        fileChooser.setEnabled(enabled);
        getBrowseButton().setEnabled(enabled);
        getField().setEditable(enabled);
    }
    protected String getTemplate() {
        if(template == null){
            String t = (String) UIManager.get("FileSelectBinding.template");
            template = t != null && t != null ? t: "%s selected";
        }
        return template;
    }   

    protected String getButtonText() {
        if(buttonText == null){
            String t = (String) UIManager.get("FileSelectBinding.buttonText");
            buttonText = t != null && t != null ? t: "browse..";
        }
        return buttonText;
    }
    protected JButton getBrowseButton() {
        if(browseButton == null){
            browseButton = new JButton(getButtonText());        
            browseButton.addActionListener(new ActionListener(){
                @Override
                public void actionPerformed(ActionEvent ae) {
                    fieldListener();
                }
            }); 
        }
        return browseButton;
    }
}