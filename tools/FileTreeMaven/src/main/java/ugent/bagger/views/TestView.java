/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ugent.bagger.views;

import ugent.bagger.filters.FileExtensionFilter;
import ugent.bagger.panels.MetsPanel;
import com.anearalone.mets.Mets;
import com.anearalone.mets.MetsWriter;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import javax.swing.*;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.validation.Schema;
import org.springframework.richclient.application.support.AbstractView;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 *
 * @author nicolas
 */
public class TestView extends AbstractView implements ActionListener{
    private Mets mets;    
    private JButton importMetsButton;
    
    private String schemaURL;
    private Schema schema;
    
    private JPanel panel;
    private JPanel topPanel;
    private JPanel middlePanel;
    
    private MetsPanel metsTab;
    
    
    @Override
    protected JComponent createControl() {
        return createForm();
    }
    private JComponent createForm(){   
        
        //create
        topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));        
        
        importMetsButton = new JButton("import mets");
        
        importMetsButton.addActionListener(this);
        
        JButton dumpButton = new JButton("dump");
        dumpButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent ae) {
                try{
                    MetsWriter mw = new MetsWriter();                    
                    mw.writeToFile(getMets(),new File("/tmp/mets.xml"));
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        });
        
        middlePanel = getMetsTab();
        
        //samenstellen
        topPanel.add(importMetsButton);
        topPanel.add(dumpButton);
                
        panel = new JPanel(new BorderLayout());
        panel.add(topPanel,BorderLayout.NORTH);
        panel.add(middlePanel,BorderLayout.CENTER);
        
        return panel;
    }    
    protected Schema getSchema() throws MalformedURLException, SAXException, IOException{
        if(schema ==  null){
            schema = ugent.bagger.helper.XML.createSchema(new URL(schemaURL));
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
            File [] files = ugent.bagger.helper.SwingUtils.chooseFiles(
                "Select Mets document (xml)",
                new FileExtensionFilter(new String [] {"xml"},"xml files only",true),
                JFileChooser.FILES_ONLY,
            false);           
            if(files.length == 0) {
                return;
            }
            Document doc = ugent.bagger.helper.XML.XMLToDocument(files[0]);            
            //valideer doc tegen schema mets (Mets api doet dit niet)
            ugent.bagger.helper.XML.validate(doc,getSchema());                                
            setMets(ugent.bagger.helper.MetsUtils.documentToMets(doc));
          
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
        getMetsTab().reset(getMets());
    }
    public MetsPanel getMetsTab() {
        if(metsTab == null){
            metsTab = new MetsPanel(getMets());
        }
        return metsTab;
    }
    public void setMetsTab(MetsPanel metsTab) {
        this.metsTab = metsTab;
    }    
}
