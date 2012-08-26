/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Dialogs;

import java.awt.BorderLayout;
import java.awt.Frame;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTextArea;

/**
 *
 * @author nicolas
 */
public class TextViewDialog extends JDialog {      
    private String [] data;
    public TextViewDialog(Frame parentFrame,String [] data){
        super(parentFrame,true);        
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
        panel.add(textArea);
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
