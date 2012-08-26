/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Panels;

import Exceptions.IllegalNamespaceException;
import Exceptions.NoNamespaceException;
import Filters.FileExtensionFilter;
import Importers.Importer;
import Importers.ImporterFactory;
import Mets.MdSecWrapper;
import Tables.DmdSecTable;
import com.anearalone.mets.MdSec;
import com.anearalone.mets.MdSec.MdWrap;
import helper.Context;
import helper.XML;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;
import javax.swing.*;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.validation.Schema;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 *
 * @author nicolas
 */
public class MdSecPanel extends JPanel{
    private JComponent buttonPanel;
    private DmdSecTable dmdSecTable;       
    private HashMap<String,String> xsdMap = null;
    private HashMap<String,String> namespaceMap = null;
    private ArrayList<String>forbiddenNamespaces = null;  
    private JTextArea console;
    private ArrayList<MdSec>data;
    private ProgressMonitor progressMonitor;    
    
    public MdSecPanel(final ArrayList<MdSec>data){        
        assert(data != null);
        this.data = data; 
        setLayout(new BorderLayout());
        add(createContentPane());        
    }    
    public void reset(final ArrayList<MdSec>data){                
        getDmdSecTable().reset(data);
        this.data = data;        
    }
    public JComponent getButtonPanel() {
        if(buttonPanel == null){
            buttonPanel = createButtonPanel();
        }
        return buttonPanel;
    }
    public void setButtonPanel(JPanel buttonPanel) {
        this.buttonPanel = buttonPanel;
    }        
    public JTextArea getConsole() {
        if(console == null){
           console = new JTextArea();
           console.setLineWrap(false);          
           console.setPreferredSize(new Dimension(0,100));
           console.setEditable(false);
        }
        return console;
    }
    public void setConsole(JTextArea console) {
        this.console = console;
    }   
    public ArrayList<String> getForbiddenNamespaces() {
        if(forbiddenNamespaces == null){
            forbiddenNamespaces = (ArrayList<String>)helper.Beans.getBean("forbiddenNamespaces");
        }
        return forbiddenNamespaces;
    }
    public void setForbiddenNamespaces(ArrayList<String> forbiddenNamespaces) {
        this.forbiddenNamespaces = forbiddenNamespaces;
    }
    public HashMap<String,String> getXsdMap() {
        if(xsdMap == null){
            xsdMap = (HashMap<String,String>)helper.Beans.getBean("xsdMap");
        }
        return xsdMap;
    }
    public void setXsdMap(HashMap<String,String> xsdMap) {
        this.xsdMap = xsdMap;
    }
    public HashMap<String,String> getNamespaceMap() {
        if(namespaceMap == null){           
            namespaceMap = (HashMap<String,String>)helper.Beans.getBean("namespaceMap");            
        }
        return namespaceMap;
    }
    public void setNamespaceMap(HashMap namespaceMap) {
        this.namespaceMap = namespaceMap;
    } 
    public DmdSecTable getDmdSecTable() {
        if(dmdSecTable == null){
            dmdSecTable = createMdSecTable();
        }
        return dmdSecTable;
    }
    public DmdSecTable createMdSecTable(){
        return new DmdSecTable(data,new String [] {"ID","mdWrap.xmlData[0].namespaceURI","mdWrap.MDTYPE"},"mdSecTable");
    }
    public void setMdSecTable(DmdSecTable dmdSecTable) {
        this.dmdSecTable = dmdSecTable;
    }  
    protected JComponent createContentPane() {
        JPanel panel = new JPanel(new BorderLayout());        
        panel.add(new JScrollPane(getDmdSecTable().getControl()));        
        panel.add(getButtonPanel(),BorderLayout.NORTH);        
        JPanel consolePanel = new JPanel(new BorderLayout());     
        consolePanel.add(new JLabel("log:"));
        JScrollPane pane = new JScrollPane();
        pane.setViewportView(getConsole());
        pane.setPreferredSize(new Dimension(0,80));
        consolePanel.add(pane,BorderLayout.SOUTH);        
        panel.add(consolePanel,BorderLayout.SOUTH);        
        return panel;
    }
    public JComponent createButtonPanel(){
        final JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton addButton = new JButton("add xml file..");
        JButton importButton = new JButton("import non xml file..");       
        JButton removeButton = new JButton("remove");       
        
        panel.add(addButton);                
        panel.add(importButton);        
        panel.add(removeButton);
        
        removeButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent ae) {
                getDmdSecTable().deleteSelectedMdSec();
            }        
        });     
        
        addButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent ae){                
                
                progressMonitor = new ProgressMonitor(MdSecPanel.this,"importing..","",0,100);
                progressMonitor.setProgress(0);
                progressMonitor.setMillisToDecideToPopup(100);
                progressMonitor.setMillisToPopup(0);              
                
                
                MdSecPanel.TaskAddMdSecFromFile task = new MdSecPanel.TaskAddMdSecFromFile();
                task.addPropertyChangeListener(new PropertyChangeListener() {
                    @Override
                    public void propertyChange(PropertyChangeEvent evt) {                        
                        if("progress".compareTo(evt.getPropertyName())==0){                            
                            int progress = (Integer) evt.getNewValue();                            
                            progressMonitor.setProgress(progress);                              
                            progressMonitor.setNote(progress+"%");
                        }
                    }
                });                
                task.execute();
            }
        });
        importButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent ae) {                
                
                progressMonitor = new ProgressMonitor(MdSecPanel.this,"importing..","",0,100);
                progressMonitor.setProgress(0);
                progressMonitor.setMillisToDecideToPopup(100);
                progressMonitor.setMillisToPopup(0);              
                
                
                MdSecPanel.TaskAddMdSecFromImport task = new MdSecPanel.TaskAddMdSecFromImport();
                task.addPropertyChangeListener(new PropertyChangeListener() {
                    @Override
                    public void propertyChange(PropertyChangeEvent evt) {                        
                        if("progress".compareTo(evt.getPropertyName())==0){                            
                            int progress = (Integer) evt.getNewValue();                            
                            progressMonitor.setProgress(progress);                              
                            progressMonitor.setNote(progress+"%");
                        }
                    }
                });                
                task.execute();
            }
        });
        return panel;
    }   
    private MdSec createMdSec(File file) throws IOException, SAXException, ParserConfigurationException, IllegalNamespaceException, NoNamespaceException{        
        MdSec mdSec = new MdSecWrapper();        
        mdSec.setID(file.getName());
        mdSec.setMdWrap(createMdWrap(file));                
        mdSec.setGROUPID(mdSec.getMdWrap().getMDTYPE().toString()); 
        return mdSec;
    }
    private MdSec createMdSec(Document doc) throws NoNamespaceException, IllegalNamespaceException, MalformedURLException, SAXException, IOException{
        MdSec mdSec = new MdSecWrapper();
        mdSec.setID(UUID.randomUUID().toString());
        mdSec.setGROUPID(UUID.randomUUID().toString()); 
        mdSec.setMdWrap(createMdWrap(doc));
        return mdSec;
    }
    private MdWrap createMdWrap(Document doc) throws NoNamespaceException, IllegalNamespaceException, MalformedURLException, SAXException, IOException{
        String namespace = doc.getDocumentElement().getNamespaceURI();      
        //elke xml moet namespace bevatten (geen oude DOCTYPE!)
        if(namespace == null || namespace.isEmpty()){
            throw new NoNamespaceException("no namespace could be found");
        } 
        //sommige xml mag niet in mdWrap: vermijd METS binnen METS!
        if(getForbiddenNamespaces().contains(namespace)){
            throw new IllegalNamespaceException("namespace "+namespace+" is forbidden in mdWrap",namespace);
        }
        //indien XSD bekend, dan validatie hierop       
        if(getXsdMap().containsKey(namespace)){
            System.out.println("validating against "+(String)getXsdMap().get(namespace));
            URL schemaURL = new URL((String)getXsdMap().get(namespace));
            System.out.println("creating schema");
            Schema schema = helper.XML.createSchema(schemaURL);
            System.out.println("creating schema done!");
            helper.XML.validate(doc,schema);            
        } 
        System.out.println("validation successfull");
        MdSec.MDTYPE mdType = null;
        try{                     
            mdType = MdSec.MDTYPE.fromValue(getNamespaceMap().get(namespace));                              
        }catch(IllegalArgumentException e){
            mdType = MdSec.MDTYPE.OTHER;                        
        }
        MdSec.MdWrap mdWrap = new MdWrap(mdType);                                                            
        if(mdType == MdSec.MDTYPE.OTHER){
            mdWrap.setOTHERMDTYPE(namespace);
        } 
        mdWrap.setMIMETYPE("text/xml");
        mdWrap.getXmlData().add(doc.getDocumentElement());                
        return mdWrap;
    }
    private MdWrap createMdWrap(File file) throws ParserConfigurationException, SAXException, IOException, IllegalNamespaceException, NoNamespaceException{           
        //Valideer xml, en geef W3C-document terug
        return createMdWrap(helper.XML.XMLToDocument(file));
    }
    
    private class TaskAddMdSecFromFile extends SwingWorker<Void, Void> {
        @Override
        @SuppressWarnings("empty-statement")
        protected Void doInBackground() throws Exception {
            File [] files = helper.SwingUtils.chooseFiles(
                "Select xml file",
                new FileExtensionFilter(new String [] {"xml"},"xml files only",true),
                JFileChooser.FILES_ONLY,
                true
            );
            int succeeded = 0;
            
            console.setText("");
            for(int i = 0;i<files.length;i++){                                                        
                File file = files[i];
               
                try{
                    MdSec mdSec = createMdSec(file);                        
                    getDmdSecTable().addMdSec(mdSec);
                    succeeded++;
                }catch(IOException e){                                
                    console.append(Context.getMessage("mdSecTable.addMdSec.IOException",new Object []{
                        file,e.getMessage()
                    }));                                
                }catch(SAXException e){                                
                    console.append(helper.Context.getMessage("mdSecTable.addMdSec.SAXException",new Object []{
                        file,e.getMessage()
                    }));                                
                }catch(ParserConfigurationException e){                                
                    console.append(helper.Context.getMessage("mdSecTable.addMdSec.ParserConfigurationException",new Object []{
                        e.getMessage()
                    }));                               
                }catch(IllegalNamespaceException e){
                    console.append(helper.Context.getMessage("mdSecTable.addMdSec.IllegalNamespaceException",new Object []{
                        file,e.getNamespace()
                    }));
                }
                catch(NoNamespaceException e){                               
                    console.append(helper.Context.getMessage("mdSecTable.addMdSec.noNamespaceException",new Object []{
                        file
                    }));                                
                }            
                console.append("\n");
                int percent = (int)Math.floor( ((i+1) / ((float)files.length))*100);                                                                        
                setProgress(percent);                
            }                        
            if(succeeded > 0){
                getDmdSecTable().refresh();                            
            }
            progressMonitor.close();            
            
            return null;
        }    
    }
    private class TaskAddMdSecFromImport extends SwingWorker<Void, Void> {
        @Override
        protected Void doInBackground() throws Exception {
            File [] files = helper.SwingUtils.chooseFiles(
                "Select file",
                null,
                JFileChooser.FILES_ONLY,
                true
            );
            int succeeded = 0;
            
            console.setText("");
            for(int i = 0;i<files.length;i++){
                File file = files[i];
                Importer importer = ImporterFactory.createImporter(file);
                if(importer == null){
                    System.out.println("no importer found for "+file);
                    continue;
                }
                System.out.println("importing from file "+file);
                Document doc = importer.performImport(file);
                if(doc == null){
                    System.out.println("doc creation failed "+file);
                    continue;
                }
                System.out.println("importing from file "+file+" successfull");
                try{
                    XML.DocumentToXML(doc,new java.io.FileOutputStream(new File("/tmp/dc.xml")),true);
                }catch(Exception e){
                    e.printStackTrace();
                }
                try{
                    MdSec mdSec = createMdSec(doc);                        
                    getDmdSecTable().addMdSec(mdSec);
                    succeeded++;
                }catch(IOException e){                                
                    e.printStackTrace();
                    console.append(Context.getMessage("mdSecTable.addMdSec.IOException",new Object []{
                        file,e.getMessage()
                    }));                                
                }catch(SAXException e){                                
                    e.printStackTrace();
                    console.append(helper.Context.getMessage("mdSecTable.addMdSec.SAXException",new Object []{
                        file,e.getMessage()
                    }));                                
                }catch(IllegalNamespaceException e){
                    e.printStackTrace();
                    console.append(helper.Context.getMessage("mdSecTable.addMdSec.IllegalNamespaceException",new Object []{
                        file,e.getNamespace()
                    }));
                }
                catch(NoNamespaceException e){                               
                    e.printStackTrace();
                    console.append(helper.Context.getMessage("mdSecTable.addMdSec.noNamespaceException",new Object []{
                        file
                    }));                                
                }            
                console.append("\n");
                int percent = (int)Math.floor( ((i+1) / ((float)files.length))*100);                                                                        
                setProgress(percent); 
                
            }
            if(succeeded > 0){
                getDmdSecTable().refresh();                            
            }
            progressMonitor.close();
                        
            return null;
        }
        
    }
}
