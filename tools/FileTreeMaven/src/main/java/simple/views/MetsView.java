/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package simple.views;

import Filters.FileExtensionFilter;
import Tabs.MetsTab;
import com.anearalone.mets.Mets;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
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
    
    private String schemaURL;
    private Schema schema;
    
    private JPanel panel;
    private JPanel topPanel;
    private JPanel middlePanel;
    
    private MetsTab metsTab;
    
    
    @Override
    protected JComponent createControl() {
        return createForm();
    }
    private JComponent createForm(){   
        
        //create
        topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));        
        
        importMetsButton = new JButton("import mets");
        
        importMetsButton.addActionListener(this);
        
        middlePanel = getMetsTab();
        
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
            if(files.length == 0) {
                return;
            }
            Document doc = helper.XML.XMLToDocument(files[0]);            
            //valideer doc tegen schema mets (Mets api doet dit niet)
            helper.XML.validate(doc,getSchema());                                
            setMets(helper.MetsUtils.documentToMets(doc));
          
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    reset();
                }
            });
            
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
    protected void reset(){
        System.out.println("resetting from MetsView");
        getMetsTab().reset(getMets());
    }
    public MetsTab getMetsTab() {
        if(metsTab == null){
            metsTab = new MetsTab(getMets());
        }
        return metsTab;
    }
    public void setMetsTab(MetsTab metsTab) {
        this.metsTab = metsTab;
    }    
}

