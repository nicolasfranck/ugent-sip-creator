/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package simple.views;

import Exceptions.IllegalNamespaceException;
import Exceptions.NoNamespaceException;
import Filters.ExtensionFilter;
import METS.MdSecWrapper;
import bindings.MdSecTable;
import com.anearalone.mets.MdSec;
import com.anearalone.mets.MdSec.MDTYPE;
import com.anearalone.mets.MdSec.MdWrap;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileFilter;
import javax.xml.parsers.ParserConfigurationException;
import org.springframework.richclient.application.support.AbstractView;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 *
 * @author nicolas
 */
public class MdSecView extends AbstractView{
    private MdSecTable mdSecTable;    
    private JFileChooser fileChooser;
    private HashMap<String,String> xsdMap = new HashMap<String,String>();
    private HashMap<String,String> namespaceMap = new HashMap<String,String>();
    private ArrayList<String>forbiddenNamespaces = new ArrayList<String>();
    private FileFilter xmlFilter = new ExtensionFilter("xml","xml files only");

    public ArrayList<String> getForbiddenNamespaces() {
        return forbiddenNamespaces;
    }
    public void setForbiddenNamespaces(ArrayList<String> forbiddenNamespaces) {
        this.forbiddenNamespaces = forbiddenNamespaces;
    }
    public HashMap getXsdMap() {
        return xsdMap;
    }
    public void setXsdMap(HashMap<String,String> xsdMap) {
        this.xsdMap = xsdMap;
    }
    public HashMap<String,String> getNamespaceMap() {
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
        return new MdSecTable(new ArrayList<MdSec>(),new String [] {"ID","mdWrap.xmlData[0].namespaceURI","mdWrap.MDTYPE"},"mdSecTable");
    }
    public void setMdSecTable(MdSecTable mdSecTable) {
        this.mdSecTable = mdSecTable;
    }
    @Override
    protected JComponent createControl() {
        JPanel panel = new JPanel(new BorderLayout());
        
        panel.add(new JScrollPane(getMdSecTable().getControl()));
        
        panel.add(createButtonPanel(),BorderLayout.NORTH);
        
        return panel;
    }
    public JComponent createButtonPanel(){
        final JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton addButton = new JButton("add..");
        JButton removeButton = new JButton("remove");
        JButton importButton = new JButton("import..");
        
        buttonPanel.add(addButton);
        buttonPanel.add(removeButton);        
        buttonPanel.add(importButton);        
        
        removeButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent ae) {
                getMdSecTable().deleteSelectedMdSec();
            }        
        });
        addButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent ae) {                               
                helper.JComponentUtils.setEnabled(buttonPanel,false);
                SwingUtilities.invokeLater(new Runnable(){
                    @Override
                    public void run() {
                        File [] files = chooseFiles();
                        int succeeded = 0;
                        getStatusBar().clear();                        
                        getStatusBar().getProgressMonitor().taskStarted("loading xml files",100);
                        getStatusBar().setVisible(true);
                        
                        for(int i = 0;i<files.length;i++){                            
                            
                            File file = files[i];
                            try{
                                MdSec mdSec = createMdSec(file);                        
                                getMdSecTable().addMdSec(mdSec);
                                succeeded++;
                            }catch(IOException e){
                                e.printStackTrace();
                                logger.error(e.getMessage());                        
                                JOptionPane.showMessageDialog(null,helper.Context.getMessage("mdSecTable.addMdSec.IOException",new Object []{
                                    file,e.getMessage()
                                }));
                            }catch(SAXException e){
                                e.printStackTrace();
                                logger.error(e.getMessage());                        
                                JOptionPane.showMessageDialog(null,helper.Context.getMessage("mdSecTable.addMdSec.SAXException",new Object []{
                                    file,e.getMessage()
                                }));
                            }catch(ParserConfigurationException e){
                                e.printStackTrace();
                                logger.error(e.getMessage());                        
                                JOptionPane.showMessageDialog(null,helper.Context.getMessage("mdSecTable.addMdSec.ParserConfigurationException",new Object []{
                                    e.getMessage()
                                }));
                            }catch(IllegalNamespaceException e){
                                e.printStackTrace();
                                logger.error(e.getMessage());
                                JOptionPane.showMessageDialog(null,helper.Context.getMessage("mdSecTable.addMdSec.illegalNamespaceException",new Object []{
                                    file,e.getNamespace()
                                }));
                            }
                            catch(NoNamespaceException e){
                                e.printStackTrace();
                                logger.error(e.getMessage());
                                JOptionPane.showMessageDialog(null,helper.Context.getMessage("mdSecTable.addMdSec.noNamespaceException",new Object []{
                                    file
                                }));
                            }            
                            int percent = (int)Math.floor( ((i+1) / ((float)files.length))*100);                            
                            getStatusBar().getProgressMonitor().worked(percent);
                        }
                        if(succeeded > 0){
                            getMdSecTable().refresh();
                        }    
                        getStatusBar().getProgressMonitor().done(); 
                        helper.JComponentUtils.setEnabled(buttonPanel,true);
                    }                
                });                               
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
            helper.XML.validate(
                doc,
                helper.XML.createSchema(
                    new URL(new URL("file:"),(String)getXsdMap().get(namespace))
                )
            );
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
}
