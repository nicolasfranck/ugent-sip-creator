/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package simple.views;

import Exceptions.MdRefException;
import Filters.FileExtensionFilter;
import Tabs.MetsTab;
import com.anearalone.mets.Mets;
import com.anearalone.mets.MetsWriter;
import helper.Context;
import helper.SwingUtils;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import javax.swing.*;
import javax.xml.validation.Schema;
import org.springframework.richclient.application.support.AbstractView;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 *
 * @author nicolas
 */
public class MetsView extends AbstractView{
    private Mets mets;    
    private JButton importMetsButton;
    
    private List<String>metsSchemaVersions;
    private List<String>metsSchemaURLS;
    private HashMap<String,Schema> metsSchemas = new HashMap<String,Schema>();    
    
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
        
        importMetsButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent ae) {
                monitor(new TaskImportMetsFromFile(),"importing mets..");
            }
        });
        
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

    public List<String> getMetsSchemaURLS() {
        return metsSchemaURLS;
    }
    public void setMetsSchemaURLS(List<String> metsSchemaURLS) {
        this.metsSchemaURLS = metsSchemaURLS;
    }    
    public List<String> getMetsSchemaVersions() {
        return metsSchemaVersions;
    }
    public void setMetsSchemaVersions(List<String> metsSchemaVersions) {
        this.metsSchemaVersions = metsSchemaVersions;
    }    
    protected Schema getSchema(String version) throws MalformedURLException, SAXException, IOException{        
        if(!metsSchemas.containsKey(version)){
            metsSchemas.put(version,helper.XML.createSchema(new URL(version)));
        }
        return metsSchemas.get(version);        
    }
    private void monitor(SwingWorker worker,String title){        
        helper.SwingUtils.monitor(MetsView.this.getControl(),worker,title);        
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
    public MetsTab getMetsTab() {
        if(metsTab == null){
            metsTab = new MetsTab(getMets());
        }
        return metsTab;
    }
    public void setMetsTab(MetsTab metsTab) {
        this.metsTab = metsTab;
    }  
    private class TaskImportMetsFromFile extends SwingWorker<Void, Void> {
        @Override        
        protected Void doInBackground() throws Exception {                        
            try{      
                System.out.println("starting");
                //stap 1
                File [] files = helper.SwingUtils.chooseFiles(
                    Context.getMessage("MetsView.fileChooser.dialog.title"),                
                    new FileExtensionFilter(new String [] {"xml"},Context.getMessage("MetsView.fileChooser.filter.label"),true),
                    JFileChooser.FILES_ONLY,
                false);           
                if(files.length == 0) {
                    return null;
                }       
                
                setProgress( (int) ((1.0 / 5)*100));
                //stap 2
                Document doc = helper.XML.XMLToDocument(files[0]);        
                String namespace = doc.getDocumentElement().getNamespaceURI();                
                
                boolean namespaceFound = false;
                for(String ns:metsSchemaURLS){
                    if(ns.equals(namespace)){
                        namespaceFound = true;
                        break;
                    }
                }
                if(!namespaceFound){
                    System.out.println("xml file has incorrect namespace");
                    JOptionPane.showMessageDialog(SwingUtils.getFrame(),Context.getMessage("MetsView.importMets.incorrectNamespaceException.label"));
                    throw new Exception("xml file has incorrect namespace");
                }                
                
                setProgress( (int) ((2.0 / 5)*100));
                
                //stap 3
                
                //valideer doc tegen schema mets (Mets api doet dit niet)            
                boolean success = false;
                for(String version:metsSchemaVersions){                
                    try{                       
                        System.out.println("validating against "+version);
                        Schema schema = getSchema(version);
                        helper.XML.validate(doc,schema);                    
                        success = true;
                    }catch(Exception e){
                        e.printStackTrace();                    
                    }
                    if(success){
                        break;
                    }
                }                               
                if(!success){                    
                    JOptionPane.showMessageDialog(SwingUtils.getFrame(),Context.getMessage("MetsView.importMets.validationFailed.label"));
                    return null;
                }
                
                setProgress( (int) ((3.0 / 5)*100));
                
                //stap 4
                Mets tempMets = helper.MetsUtils.documentToMets(doc);                        
                      
                //helper.MetsUtils.validate(tempMets);
                
                setProgress( (int) ((4.0 / 5)*100));
                //stap 5
                setMets(tempMets);          
                reset();               
                
                setProgress( (int) ((5.0 / 5)*100));
            }catch(IOException e){
                JOptionPane.showMessageDialog(SwingUtils.getFrame(),"fout bij het lezen van het bestand: "+e.getMessage());
            }catch(SAXException e){
                JOptionPane.showMessageDialog(SwingUtils.getFrame(),"bestand bevat geen geldige mets xml: "+e.getMessage());
            }catch(MdRefException e){
                JOptionPane.showMessageDialog(SwingUtils.getFrame(),e.getMessage());
            }finally{
                System.out.println("DONE called???");
                this.done();                
            }            
            return null;
        }    
    }
}

