package ugent.bagger.panels;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.richclient.progress.BusyIndicator;
import org.w3c.dom.Document;
import ugent.bagger.helper.Beans;
import ugent.bagger.helper.CSVUtils;
import ugent.bagger.helper.Context;
import ugent.bagger.helper.XML;
import ugent.bagger.params.VelocityTemplate;
import ugent.bagger.swing.JPopupTextArea;

/**
 *
 * @author nicolas
 */
public class CSV2Panel extends JPanel{        
    static Log log = LogFactory.getLog(CSV2Panel.class);
    JButton testButton;   
    HashMap<String,String>record = new HashMap<String,String>();
    JTextArea textArea;
    JComboBox templateComboBox;
    HashMap<String,HashMap<String,String>>velocityTemplates;    
    ActionListener showListener = new ActionListener(){
        @Override
        public void actionPerformed(ActionEvent ae) {
            BusyIndicator.showAt(CSV2Panel.this);                
            showResult();
            BusyIndicator.clearAt(CSV2Panel.this);                
        }        
    };    
    
    public ActionListener getShowListener() {
        return showListener;
    }    
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
                data.add(new VelocityTemplate(value.get("name"),value.get("path"),value.get("xsd")));
            }
            templateComboBox = new JComboBox(data.toArray());            
            Dimension dim = new Dimension(Integer.MAX_VALUE,(int)templateComboBox.getPreferredSize().getHeight());
            templateComboBox.setMaximumSize(dim);            
            templateComboBox.addActionListener(showListener);
        }
        return templateComboBox;
    }
    public JTextArea getTextArea() {
        if(textArea == null){
            textArea = new JPopupTextArea();
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
        setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));    
        add(getTemplateComboBox());
        add(new JScrollPane(getTextArea()));
        add(createButtonPanel());                      
    } 
      
    public JPanel createButtonPanel(){
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));        
        testButton = new JButton(Context.getMessage("test"));        
        panel.add(testButton);
        testButton.addActionListener(showListener);        
        return panel;
    }    
    protected void showResult() {
        try{
            
            //vul record in in template
            VelocityTemplate vt = (VelocityTemplate) getTemplateComboBox().getSelectedItem();
            
            //zet xml om naar w3c.document
            Document doc = CSVUtils.templateToDocument(vt,getRecord());            
            
            //pretty print naar output
            StringWriter out = new StringWriter();
            XML.DocumentToXML(doc,out,true);
            getTextArea().setText(out.toString());
            
        }catch(Exception e){
            log.error(e.getMessage());
        }        
    }    
}