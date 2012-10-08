package ugent.bagger.dialogs;

import com.anearalone.mets.MdSec;
import gov.loc.repository.bagger.ui.BagView;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.net.URL;
import java.util.HashMap;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import org.springframework.richclient.progress.BusyIndicator;
import org.w3c.dom.Document;
import ugent.bagger.exceptions.IllegalNamespaceException;
import ugent.bagger.exceptions.NoNamespaceException;
import ugent.bagger.filters.FileExtensionFilter;
import ugent.bagger.helper.Context;
import ugent.bagger.helper.MetsUtils;
import ugent.bagger.helper.SwingUtils;
import ugent.bagger.helper.XML;
import ugent.bagger.helper.XSLT;
import ugent.bagger.tables.MdSecPropertiesTable;

/**
 *
 * @author nicolas
 */
public final class XMLCrosswalkDialog extends JDialog{
    private File file;        
    private JTextField fileField;
    private String transformFromNamespace;
    private String transformToNamespace;
    
    public XMLCrosswalkDialog(){
        getContentPane().add(createContentPane());
        setTitle("Crosswalk");
    }
    public JComponent createContentPane(){
        
        JPanel mainPanel = new JPanel(new GridLayout(0,1));        
        final MdSecPropertiesTable mdSecPropertiesTable = BagView.getInstance().getInfoFormsPane().getInfoInputPane().getMetsPanel().getDmdSecPropertiesPanel().getDmdSecPropertiesTable();
        
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
                if(file == null || transformFromNamespace == null || transformToNamespace == null){
                    return;
                }
                BusyIndicator.showAt(SwingUtils.getFrame());
                try{
                    String filename = MetsUtils.getCrosswalk().get(transformFromNamespace).get(transformToNamespace);
                    Document xsltDoc = XML.XMLToDocument(Context.getResource(filename));
                    Document sourceDoc = XML.XMLToDocument(file);
                    Document transformedDoc = XSLT.transform(sourceDoc,xsltDoc); 
                    
                    XML.DocumentToXML(transformedDoc,System.out);
                    
                    MdSec mdSec = MetsUtils.createMdSec(transformedDoc);                     
                    mdSecPropertiesTable.addMdSec(mdSec);
                    mdSecPropertiesTable.refresh();                    
                    
                }catch(Exception e){              
                    JOptionPane.showMessageDialog(null,e.getMessage());
                    e.printStackTrace();
                }
                XMLCrosswalkDialog.this.dispose();
                BusyIndicator.clearAt(SwingUtils.getFrame());
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
                    true
                );
                BusyIndicator.showAt(SwingUtils.getFrame());
                if(selectedFiles.length > 0){
                    try{                       
                        Document document = XML.XMLToDocument(selectedFiles[0]);
                        transformFromNamespace = document.getDocumentElement().getNamespaceURI();                    
                        //elke xml moet namespace bevatten (geen oude DOCTYPE!)
                        if(transformFromNamespace == null || transformFromNamespace.isEmpty()){
                            throw new NoNamespaceException("no namespace could be found");
                        } 
                        //sommige xml mag niet in mdWrap: vermijd METS binnen METS!
                        if(MetsUtils.getForbiddenNamespaces().contains(transformFromNamespace)){
                            throw new IllegalNamespaceException("namespace "+transformFromNamespace+" is forbidden in mdWrap",transformFromNamespace);
                        }
                        
                        //indien XSD bekend, dan validatie hierop       
                        if(MetsUtils.getXsdMap().containsKey(transformFromNamespace)){            
                            URL schemaURL = Context.getResource((String)MetsUtils.getXsdMap().get(transformFromNamespace));                                                                                                           
                            XML.validate(document,XML.createSchema(schemaURL));            
                        }
                        //zoek mapping
                        if(!MetsUtils.getCrosswalk().containsKey(transformFromNamespace)){
                            throw new Exception("no crosswalk found!");
                        }
                        
                        HashMap<String,String> crosswalk = MetsUtils.getCrosswalk().get(transformFromNamespace);
                        
                        //vul combobox           
                        transformComboBox.removeAllItems();
                        for(String key:crosswalk.keySet().toArray(new String [] {})){
                            transformComboBox.addItem(MetsUtils.getNamespaceMap().get(key));
                        }
                        
                        //set combobox
                        transformComboBox.setEnabled(true);                        
                        
                        getFileField().setText(selectedFiles[0].getAbsolutePath());
                        setFile(selectedFiles[0]);
                        okButton.setEnabled(true);                        
                        
                        invalidate();                                               
                        
                    }catch(Exception e){
                        JOptionPane.showMessageDialog(XMLCrosswalkDialog.this,e.getMessage());                                               
                    }                    
                }
                BusyIndicator.clearAt(SwingUtils.getFrame());
            }            
        });
        filePanel.add(fileLabel);
        filePanel.add(getFileField());
        filePanel.add(fileButton);
        
        //voeg alles samen
        mainPanel.add(filePanel);
        mainPanel.add(transformPanel);
        mainPanel.add(buttonPanel);
        
        
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
    
}