package ugent.bagger.panels;


import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import ugent.bagger.helper.Beans;
import ugent.bagger.helper.VelocityUtils;

/**
 *
 * @author nicolas
 */
public class CSV2Panel extends JPanel{    
    
    private JButton okButton;
    private JButton testButton;   
    private HashMap<String,String>record = new HashMap<String,String>();
    private JTextArea textArea;
    private JComboBox templateComboBox;
    private HashMap<String,HashMap<String,String>>velocityTemplates;

    public HashMap<String, HashMap<String, String>> getVelocityTemplates() {
        if(velocityTemplates == null){
            velocityTemplates = (HashMap<String,HashMap<String,String>>) Beans.getBean("velocityTemplates");
        }
        return velocityTemplates;
    }
    
    public JComboBox getTemplateComboBox() {
        if(templateComboBox == null){           
            ArrayList<VelocityTemplate>data = new ArrayList<VelocityTemplate>();
            for(Entry<String,HashMap<String,String>> entry:getVelocityTemplates().entrySet()){
                HashMap<String,String> value = entry.getValue();
                data.add(new VelocityTemplate(value.get("name"),value.get("path")));
            }
            templateComboBox = new JComboBox(data.toArray());
        }
        return templateComboBox;
    }
    public JTextArea getTextArea() {
        if(textArea == null){
            textArea = new JTextArea();
        }
        return textArea;
    }    
    
    public HashMap<String, String> getRecord() {
        return record;
    }    
    public CSV2Panel(){        
        init();
    }
    public void init(){
        setLayout(new BoxLayout(this,BoxLayout.PAGE_AXIS));        
        add(getTemplateComboBox());
        add(getTextArea());
        add(createButtonPanel());              
    } 
      
    public JPanel createButtonPanel(){
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        okButton = new JButton("ok");        
        testButton = new JButton("test");
        panel.add(okButton);        
        panel.add(testButton);
        
        okButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent ae) {
                CSV2Panel.this.firePropertyChange("ok",null,null);
            }        
        });        
        testButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent ae) {
                showResult();
            }        
        });
        return panel;
    } 
    protected void showResult() {
        try{
            VelocityTemplate vt = (VelocityTemplate) getTemplateComboBox().getSelectedItem();
            VelocityEngine ve = VelocityUtils.getVelocityEngine();
            Template template = ve.getTemplate(vt.getPath());
            VelocityContext vcontext = new VelocityContext(getRecord());
            StringWriter writer = new StringWriter();
            template.merge(vcontext,writer);
            getTextArea().setText(writer.toString());
        }catch(Exception e){
            e.printStackTrace();
        }
        
    }
    private class VelocityTemplate {
        private String name;
        private String path;
        public VelocityTemplate(String name,String path){
            this.name = name;
            this.path = path;
        }
        public String getName() {
            return name;
        }
        public String getPath() {
            return path;
        }        
        @Override
        public String toString(){
            return name;
        }
    }
}