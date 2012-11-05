package ugent.bagger.dialogs;

import com.anearalone.mets.MdSec;
import gov.loc.repository.bagger.ui.BagView;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.net.URL;
import java.util.HashMap;
import javax.swing.*;
import org.w3c.dom.Document;
import ugent.bagger.exceptions.IllegalNamespaceException;
import ugent.bagger.exceptions.NoNamespaceException;
import ugent.bagger.filters.FileExtensionFilter;
import ugent.bagger.helper.Context;
import ugent.bagger.helper.MetsUtils;
import ugent.bagger.helper.SwingUtils;
import ugent.bagger.helper.XML;
import ugent.bagger.helper.XSLT;
import ugent.bagger.tables.EditMdSecPropertiesTable;

/**
 *
 * @author nicolas
 */
public final class XMLCrosswalkDialog extends JDialog{
    private File file;        
    private JTextField fileField;
    private JTextField transformFromNameSpaceField;
    private String transformFromNamespace;
    private String transformToNamespace;
    
    public XMLCrosswalkDialog(JFrame frame,boolean isModal){
        super(frame,isModal);
        getContentPane().add(createContentPane());
        setTitle("Crosswalk");
    }
    public JComponent createContentPane(){
        
        JPanel mainPanel = new JPanel();        
        mainPanel.setLayout(new BoxLayout(mainPanel,BoxLayout.Y_AXIS));
        final EditMdSecPropertiesTable mdSecPropertiesTable = BagView.getInstance().getInfoFormsPane().getInfoInputPane().getMetsPanel().getDmdSecPropertiesPanel().getEditDmdSecPropertiesTable();
        
        //buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        final JButton okButton = new JButton("ok");
        okButton.setEnabled(false);
        JButton cancelButton = new JButton("cancel");        
        buttonPanel.add(okButton);        
        buttonPanel.add(cancelButton);
        cancelButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent ae) {
                XMLCrosswalkDialog.this.dispose();
            }            
        });
        okButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent ae) {
                System.out.println("XMLCrosswalkDialog::okButton::actionPerformed start");
                System.out.println("file: "+file);
                System.out.println("transformFromNamespace: "+transformFromNamespace);
                System.out.println("transformToNamespace: "+transformToNamespace);
                if(file == null || transformFromNamespace == null || transformToNamespace == null){
                    return;
                }
                
                System.out.println("XMLCrosswalkDialog::okButton::actionPerformed through the gates");
                
                SwingUtils.ShowBusy();
                
                try{
                    
                    Document sourceDoc = XML.XMLToDocument(file);
                    System.out.println("sourceDoc: "+sourceDoc);                    
                    
                    //String filename = MetsUtils.getCrosswalk().get(transformFromNamespace).get(transformToNamespace);
                    String filename = MetsUtils.getXsltPath(sourceDoc.getDocumentElement(),transformToNamespace);
                    
                    System.out.println("filename: "+filename);
                    
                    Document xsltDoc = XML.XMLToDocument(Context.getResource(filename));
                    
                    System.out.println("xsltDoc"+xsltDoc);
                    
                    
                    Document transformedDoc = XSLT.transform(sourceDoc,xsltDoc); 
                    
                    System.out.println("transformedDoc: "+transformedDoc);
                    
                    XML.DocumentToXML(transformedDoc,System.out);
                    
                    MdSec mdSec = MetsUtils.createMdSec(transformedDoc);                     
                    mdSecPropertiesTable.addMdSec(mdSec);
                    mdSecPropertiesTable.refresh();                    
                    
                }catch(Exception e){              
                    JOptionPane.showMessageDialog(null,e.getMessage());
                    e.printStackTrace();
                }
                
                XMLCrosswalkDialog.this.dispose();
                
                SwingUtils.ShowDone();
                
                System.out.println("XMLCrosswalkDialog::okButton::actionPerformed end");
            }            
        });
        
        //transform input
        JPanel transformPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel transformLabel = new JLabel("transform to:");
        final JComboBox transformComboBox = new JComboBox();
        transformComboBox.addItemListener(new ItemListener(){
            @Override
            public void itemStateChanged(ItemEvent ie) {
                if(ie.getStateChange() == ItemEvent.SELECTED){
                    transformToNamespace = MetsUtils.getTypeMap().get((String)ie.getItem());                                        
                }
            }
        });
        transformComboBox.setEnabled(false);
        transformPanel.add(transformLabel);
        transformPanel.add(transformComboBox);        
        
        //file input
        JPanel filePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel fileLabel = new JLabel("File:");
        JButton fileButton = new JButton("browse");
        fileButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent ae) {
                transformComboBox.setEnabled(false);
                okButton.setEnabled(false);                
                
                File [] selectedFiles = SwingUtils.chooseFiles(
                    "Select xml file",
                    new FileExtensionFilter(new String [] {"xml"},"xml files only",true),
                    JFileChooser.FILES_ONLY,
                    false
                );
                
                SwingUtils.ShowBusy();
                
                if(selectedFiles.length > 0){
                    try{                       
                        Document document = XML.XMLToDocument(selectedFiles[0]);
                        transformFromNamespace = document.getDocumentElement().getNamespaceURI();                    
                        
                        System.out.println("namespace found: "+transformFromNamespace);
                        
                        //elke xml moet namespace bevatten (geen oude DOCTYPE!)
                        if(transformFromNamespace == null || transformFromNamespace.isEmpty()){
                            throw new NoNamespaceException("no namespace could be found");
                        } 
                        //sommige xml mag niet in mdWrap: vermijd METS binnen METS!
                        if(MetsUtils.getForbiddenNamespaces().contains(transformFromNamespace)){
                            throw new IllegalNamespaceException("namespace "+transformFromNamespace+" is forbidden in mdWrap",transformFromNamespace);
                        }
                        
                        //indien XSD bekend, dan validatie hierop       
                        String schemaPath = MetsUtils.getSchemaPath(document);
                        if(schemaPath != null){            
                            URL schemaURL = Context.getResource(schemaPath);                                                                                                           
                            XML.validate(document,XML.createSchema(schemaURL));            
                        }
                        //zoek mapping
                        if(!MetsUtils.getCrosswalk().containsKey(transformFromNamespace)){
                            throw new Exception("no crosswalk found!");
                        }
                        
                        HashMap<String,Object> crosswalk = MetsUtils.getCrosswalk().get(transformFromNamespace);
                        
                        //vul combobox           
                        transformComboBox.removeAllItems();
                        String [] keys = crosswalk.keySet().toArray(new String [] {});
                        for(String key:keys){
                            transformComboBox.addItem(MetsUtils.getNamespaceMap().get(key));
                        }
                        //stel 1ste waarde in!
                        transformToNamespace = keys[0];
                        
                        //set combobox
                        transformComboBox.setEnabled(true);                        
                        
                        getTransformFromNameSpaceField().setText(MetsUtils.getNamespaceMap().get(transformFromNamespace));
                        getFileField().setText(selectedFiles[0].getAbsolutePath());
                        setFile(selectedFiles[0]);
                        okButton.setEnabled(true);                        
                        
                        invalidate();                                               
                        
                    }catch(Exception e){
                        e.printStackTrace();
                        JOptionPane.showMessageDialog(XMLCrosswalkDialog.this,e.getMessage());                                               
                    }                    
                }
                
                SwingUtils.ShowDone();
                
            }            
        });
        filePanel.add(fileLabel);
        filePanel.add(getFileField());
        filePanel.add(fileButton);
        
        JPanel transformFromPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        transformFromPanel.add(new JLabel("transforming from:"));
        transformFromPanel.add(getTransformFromNameSpaceField());
        
        //voeg alles samen
        mainPanel.add(filePanel);
        mainPanel.add(transformFromPanel);
        mainPanel.add(transformPanel);
        mainPanel.add(buttonPanel);
        
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        
        return mainPanel;
    }
    public File getFile() {
        return file;
    }
    public void setFile(File file) {
        this.file = file;
    }    
    public JTextField getFileField() {
        if(fileField == null){
            fileField = new JTextField();
            fileField.setColumns(20);
            fileField.setEditable(false);
            fileField.setEnabled(false);
        }
        return fileField;
    }
    public void setFileField(JTextField fileField) {
        this.fileField = fileField;
    }
    public String getTransformFromNamespace() {
        return transformFromNamespace;
    }

    public void setTransformFromNamespace(String transformFromNamespace) {
        this.transformFromNamespace = transformFromNamespace;
    }

    public String getTransformToNamespace() {
        return transformToNamespace;
    }
    public void setTransformToNamespace(String transformToNamespace) {
        this.transformToNamespace = transformToNamespace;
    }
    public JTextField getTransformFromNameSpaceField() {
        if(transformFromNameSpaceField == null){
            transformFromNameSpaceField = new JTextField();            
            transformFromNameSpaceField.setEditable(false);
        }
        return transformFromNameSpaceField;        
    }
    public void setTransformFromNameSpaceField(JTextField transformFromNameSpaceField) {
        this.transformFromNameSpaceField = transformFromNameSpaceField;
    }
}