/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package simple.views;

import ActionListeners.editMdSecListener;
import Filters.FileExtensionFilter;
import Swing.SimpleFormBuilder;
import com.anearalone.mets.MdSec;
import com.anearalone.mets.Mets;
import Forms.AgentForm;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.validation.Schema;
import org.springframework.richclient.application.support.AbstractView;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 *
 * @author nicolas
 */
public class MetsView extends AbstractView implements ActionListener{
    private Mets mets;    
    private JButton importMetsButton;
    private JButton editDmdSecButton;
    private JButton editSourceMdSecButton;
    private JButton editTechMdSecButton;
    private JButton editDigiprovMdSecButton;
    private JButton editRightsMdSecButton;
    private String schemaURL;
    private Schema schema;
    
    private JPanel panel;
    private JPanel topPanel;
    private JPanel middlePanel;
    
    
    @Override
    protected JComponent createControl() {
        return createForm();
    }
    private JComponent createForm(){   
        
        //create
        topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));        
        
        importMetsButton = new JButton("import mets");  
        editDmdSecButton = new JButton("edit..");
        editSourceMdSecButton = new JButton("edit..");
        editTechMdSecButton = new JButton("edit..");
        editDigiprovMdSecButton = new JButton("edit..");
        editRightsMdSecButton = new JButton("edit..");        
        
        SimpleFormBuilder builder = new SimpleFormBuilder(5,5,5,5,new Dimension(100,25),new Dimension(100,25));               
        builder.add(new JLabel("DmdSec:"),editDmdSecButton);  
        builder.add(new JLabel("AmdSec: sourceMdSec"),editSourceMdSecButton);
        builder.add(new JLabel("AmdSec: techMdSec"),editTechMdSecButton);
        builder.add(new JLabel("AmdSec: digiprovMdSec"),editDigiprovMdSecButton);
        builder.add(new JLabel("AmdSec: rightsMdSec"),editRightsMdSecButton);                            
        
        middlePanel = builder.createForm();
        
        helper.SwingUtils.setJComponentEnabled(middlePanel,false);
        
        //register listeners
        importMetsButton.addActionListener(this);         
        
        //samenstellen
        topPanel.add(importMetsButton);
                
        panel = new JPanel(new BorderLayout());
        panel.add(topPanel,BorderLayout.NORTH);
        panel.add(middlePanel,BorderLayout.CENTER);
        
        return panel;
    }    
    protected Schema getSchema() throws MalformedURLException, SAXException, IOException{
        if(schema ==  null){
            schema = helper.XML.createSchema(new URL(schemaURL));
        }
        return schema;
    }

    public String getSchemaURL() {
        return schemaURL;
    }

    public void setSchemaURL(String schemaURL) {
        this.schemaURL = schemaURL;
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        try{                    
            File [] files = helper.SwingUtils.chooseFiles(
                "Select Mets document (xml)",
                new FileExtensionFilter(new String [] {"xml"},"xml files only",true),
                JFileChooser.FILES_ONLY,
            false);           
            if(files.length == 0)return;
            Document doc = helper.XML.XMLToDocument(files[0]);            
            //valideer doc tegen schema mets (Mets api doet dit niet)
            helper.XML.validate(doc,getSchema());                                
            setMets(helper.MetsUtils.documentToMets(doc));              
            helper.SwingUtils.removeAllActionListeners(editDmdSecButton);            
            editDmdSecButton.addActionListener(new editMdSecListener((Frame)getWindowControl(),(ArrayList<MdSec>)getMets().getDmdSec()));            
            helper.SwingUtils.setJComponentEnabled(middlePanel,true);
            panel.add(new AgentForm(mets.getMetsHdr().getAgent().get(0)).getControl(),BorderLayout.SOUTH);
            panel.repaint();
            
            
        }catch(ParserConfigurationException e){
           e.printStackTrace();
        }catch(SAXException e){
           e.printStackTrace();
        }catch(IOException e){
           e.printStackTrace();
        } 
    }    
    protected Mets getMets(){
        if(mets == null){
            mets = new Mets();
        }
        return mets;
    }
    protected void setMets(Mets mets){
        this.mets = mets;
    }   
}

