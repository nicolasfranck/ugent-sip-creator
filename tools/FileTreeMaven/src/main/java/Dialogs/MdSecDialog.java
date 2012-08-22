/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Dialogs;

import Exceptions.IllegalNamespaceException;
import Exceptions.NoNamespaceException;
import Filters.ExtensionFilter;
import METS.MdSecWrapper;
import bindings.MdSecTable;
import com.anearalone.mets.MdSec;
import com.anearalone.mets.MdSec.MDTYPE;
import com.anearalone.mets.MdSec.MdWrap;
import helper.Context;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingWorker;
import javax.swing.filechooser.FileFilter;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 *
 * @author nicolas
 */
public class MdSecDialog extends JDialog {
    private JComponent buttonPanel;
    private MdSecTable mdSecTable;    
    private JFileChooser fileChooser;
    private HashMap<String,String> xsdMap = null;
    private HashMap<String,String> namespaceMap = null;
    private ArrayList<String>forbiddenNamespaces = null;
    private FileFilter xmlFilter = new ExtensionFilter(new String [] {"xml"},"xml files only");
    private JTextArea console;
    private ArrayList<MdSec>data;
    private JProgressBar progressBar;    
    
    public MdSecDialog(Frame parentFrame,ArrayList<MdSec>data){
        super(parentFrame,true);
        assert(data != null);
        this.data = data;          
        setContentPane(createContentPane());        
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
    public JProgressBar getProgressBar() {
        if(progressBar == null){
            progressBar = new JProgressBar(0,100);
            progressBar.setValue(0);
            progressBar.setStringPainted(true);
            progressBar.setVisible(false);
        }
        return progressBar;
    }
    public void setProgressBar(JProgressBar progressBar) {
        this.progressBar = progressBar;
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
            System.out.println("getting namespaceMap");
            namespaceMap = (HashMap<String,String>)helper.Beans.getBean("namespaceMap");
            System.out.println("namespaceMap: "+namespaceMap);
        }
        return namespaceMap;
    }
    public void setNamespaceMap(HashMap namespaceMap) {
        this.namespaceMap = namespaceMap;
    } 
    public MdSecTable getMdSecTable() {
        if(mdSecTable == null){
            mdSecTable = createMdSecTable();
        }
        return mdSecTable;
    }
    public MdSecTable createMdSecTable(){
        return new MdSecTable(data,new String [] {"ID","mdWrap.xmlData[0].namespaceURI","mdWrap.MDTYPE"},"mdSecTable");
    }
    public void setMdSecTable(MdSecTable mdSecTable) {
        this.mdSecTable = mdSecTable;
    }  
    protected JComponent createContentPane() {
        JPanel panel = new JPanel(new BorderLayout());        
        panel.add(new JScrollPane(getMdSecTable().getControl()));        
        panel.add(getButtonPanel(),BorderLayout.NORTH);        
        JPanel consolePanel = new JPanel(new BorderLayout());
        consolePanel.add(getProgressBar(),BorderLayout.NORTH);
        consolePanel.add(new JLabel("log:"));
        consolePanel.add(new JScrollPane(getConsole()),BorderLayout.SOUTH);        
        panel.add(consolePanel,BorderLayout.SOUTH);        
        return panel;
    }
    public JComponent createButtonPanel(){
        final JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton addButton = new JButton("add xml file..");
        JButton importButton = new JButton("import non xml file..");       
        JButton removeButton = new JButton("remove");       
        
        buttonPanel.add(addButton);                
        buttonPanel.add(importButton);        
        buttonPanel.add(removeButton);
        
        removeButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent ae) {
                getMdSecTable().deleteSelectedMdSec();
            }        
        });       
        addButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent ae) {
                helper.JComponentUtils.setEnabled(getButtonPanel(),false);
                TaskAddMdSec task = new TaskAddMdSec();
                task.addPropertyChangeListener(new PropertyChangeListener() {
                    @Override
                    public void propertyChange(PropertyChangeEvent evt) {
                        System.out.println("progress called!");
                        if("progress".compareTo(evt.getPropertyName())==0){
                            int progress = (Integer) evt.getNewValue();
                            getProgressBar().setValue(progress);    
                        }
                    }
                });                
                task.execute();
            }
        });
        return buttonPanel;
    }
    public JFileChooser getFileChooser(){
        if(fileChooser == null){
            fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("choose a xml file");            
            fileChooser.setFileFilter(xmlFilter);
            fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            fileChooser.setMultiSelectionEnabled(true);
        }
        return fileChooser;
    }
    public void setFileChooser(JFileChooser fileChooser) {
        this.fileChooser = fileChooser;
    }
    public File [] chooseFiles(){
        JFileChooser fchooser = getFileChooser();
        int freturn = fchooser.showOpenDialog(null);
        File [] files = {};
        if(freturn == JFileChooser.APPROVE_OPTION) {
            files = fchooser.getSelectedFiles();
        }
        return files;
    }
    private MdSec createMdSec(File file) throws IOException, SAXException, ParserConfigurationException, IllegalNamespaceException, NoNamespaceException{        
        MdSec mdSec = new MdSecWrapper();        
        mdSec.setID(file.getName());        
        mdSec.setMdWrap(createMdWrap(file));                
        mdSec.setGROUPID(mdSec.getMdWrap().getMDTYPE().toString()); 
        return mdSec;
    }
    private MdWrap createMdWrap(File file) throws ParserConfigurationException, SAXException, IOException, IllegalNamespaceException, NoNamespaceException{
        
       
        
        //Valideer xml, en geef W3C-document terug
        Document doc = helper.XML.XMLToDocument(file);
        
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
            URL schemaURL = new URL((String)getXsdMap().get(namespace));
            helper.XML.validate(doc,helper.XML.createSchema(schemaURL));            
        } 
        MDTYPE mdType = null;
        try{                     
            mdType = MDTYPE.fromValue(getNamespaceMap().get(namespace));                              
        }catch(IllegalArgumentException e){
            mdType = MDTYPE.OTHER;                        
        }
        MdWrap mdWrap = new MdWrap(mdType);                                                            
        if(mdType == MDTYPE.OTHER){
            mdWrap.setOTHERMDTYPE(namespace);
        } 
        mdWrap.setMIMETYPE("text/xml");
        mdWrap.getXmlData().add(doc.getDocumentElement());                
        return mdWrap;
    }
    
    private class TaskAddMdSec extends SwingWorker<Void, Void> {
        @Override
        protected Void doInBackground() throws Exception {
            File [] files = chooseFiles();
            int succeeded = 0;
            
            setProgress(getProgressBar().getMinimum());
            getProgressBar().setVisible(true);
            
            console.setText("");
            for(int i = 0;i<files.length;i++){                                                        
                File file = files[i];
               
                try{
                    MdSec mdSec = createMdSec(file);                        
                    getMdSecTable().addMdSec(mdSec);
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
                getMdSecTable().refresh();                            
            }
            setProgress(getProgressBar().getMinimum());            
            getProgressBar().setVisible(false);                  
            helper.JComponentUtils.setEnabled(getButtonPanel(),true);
            return null;
        }    
    }
}
