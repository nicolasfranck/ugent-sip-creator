/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ugent.bagger.dialogs;

import java.awt.BorderLayout;
import java.awt.Frame;
import javax.swing.*;

/**
 *
 * @author nicolas
 */
public final class TextViewDialog extends JDialog {      
    private String [] data;
    public TextViewDialog(Frame parentFrame,boolean isModal,String [] data){
        super(parentFrame,isModal);        
        setData(data);        
        setContentPane(createPanel());        
    }    
    protected String[] getData() {
        if(data == null){
            data = new String [] {};
        }
        return data;
    }
    protected JComponent createPanel(){
        JPanel panel = new JPanel(new BorderLayout());        
        JTextArea textArea = new JTextArea();       
        textArea.setEditable(false);
        for(String entry:getData()){
            textArea.append(entry);
        }
        panel.add(new JScrollPane(textArea));
        return panel;
    }
    protected void setData(String[] data) {
        this.data = data;
    }
    public void reset(String [] data){
        setData(data);
        setContentPane(createPanel()); 
    }    
}
