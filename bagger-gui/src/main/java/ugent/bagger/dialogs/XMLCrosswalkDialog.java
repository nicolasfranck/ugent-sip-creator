package ugent.bagger.dialogs;

import com.anearalone.mets.MdSec;
import gov.loc.repository.bagger.ui.util.ApplicationContextUtil;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;
import ugent.bagger.exceptions.IllegalNamespaceException;
import ugent.bagger.exceptions.NoNamespaceException;
import ugent.bagger.exceptions.NoTransformationFoundException;
import ugent.bagger.filters.FileExtensionFilter;
import ugent.bagger.helper.Context;
import ugent.bagger.helper.MetsUtils;
import ugent.bagger.helper.SwingUtils;
import ugent.bagger.helper.XML;
import ugent.bagger.helper.XSLT;

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
        setTitle(Context.getMessage("XMLCrosswalkDialog.title"));
    }
    public void log(String message){
        ApplicationContextUtil.addConsoleMessage(message);
    }
    public JComponent createContentPane(){
        
        JPanel mainPanel = new JPanel();        
        mainPanel.setLayout(new BoxLayout(mainPanel,BoxLayout.Y_AXIS));        
        
        //buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        final JButton okButton = new JButton(Context.getMessage("XMLCrosswalkDialog.okButton.label"));
        okButton.setEnabled(false);
        JButton cancelButton = new JButton(Context.getMessage("XMLCrosswalkDialog.cancelButton.label"));        
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
                
                XMLCrosswalkDialog.this.setVisible(false);
                
                if(file == null || transformFromNamespace == null || transformToNamespace == null){
                    return;
                }
                
                SwingUtils.ShowBusy();
                
                String error = null;
                try{
                    
                    Document sourceDoc = XML.XMLToDocument(file);                                    
                    String filename = MetsUtils.getXsltPath(sourceDoc.getDocumentElement(),transformToNamespace);                    
                    Document xsltDoc = XML.XMLToDocument(Context.getResource(filename));                    
                    Document transformedDoc = XSLT.transform(sourceDoc,xsltDoc);                                         
                    MdSec mdSec = MetsUtils.createMdSec(transformedDoc);                                         
                    XMLCrosswalkDialog.this.firePropertyChange("mdSec",null,mdSec);
                  
                }catch(ParserConfigurationException e){
                    error = Context.getMessage(
                        "XMLCrosswalkDialog.transform.ParserConfigurationException.description",
                        new Object []{
                            file,e.getMessage()
                        }
                    )+"\n";
                }catch(SAXException e){
                    error = Context.getMessage(
                        "XMLCrosswalkDialog.transform.SAXException.description",
                        new Object []{
                            file,e.getMessage()
                        }
                    )+"\n";
                }catch(IOException e){                    
                    error = Context.getMessage(
                        "XMLCrosswalkDialog.transform.IOException.description",
                        new Object []{
                            file,e.getMessage()
                        }
                    )+"\n"; 
                }catch(TransformerConfigurationException e){
                    error = Context.getMessage(
                        "XMLCrosswalkDialog.transform.TransformerConfigurationException.description",
                        new Object []{
                            file,e.getMessage()
                        }
                    )+"\n";
                }catch(TransformerException e){
                    error = Context.getMessage(
                        "XMLCrosswalkDialog.transform.TransformerException.description",
                        new Object []{
                            file,e.getMessage()
                        }
                    )+"\n";
                }catch(NoNamespaceException e){
                    error = Context.getMessage(
                        "XMLCrosswalkDialog.transform.NoNamespaceException.description",
                        new Object []{
                            file,e.getMessage()
                        }
                    )+"\n";
                }catch(IllegalNamespaceException e){
                    error = Context.getMessage(
                        "XMLCrosswalkDialog.transform.IllegalNamespaceException.description",
                        new Object []{
                            file,e.getMessage()
                        }
                    )+"\n";
                }catch(Exception e){
                    error = Context.getMessage(
                        "XMLCrosswalkDialog.transform.Exception.description",
                        new Object []{
                            file,e.getMessage()
                        }
                    )+"\n";
                }
                
                if(error != null){
                    SwingUtils.ShowError(Context.getMessage("XMLCrosswalkDialog.Exception.title"),error);
                    log(error);
                }
                
                XMLCrosswalkDialog.this.dispose();
                
                SwingUtils.ShowDone();                
            }            
        });
        
        //transform input
        JPanel transformPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel transformLabel = new JLabel(Context.getMessage("XMLCrosswalkDialog.transformLabel.label"));
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
        JLabel fileLabel = new JLabel(Context.getMessage("XMLCrosswalkDialog.fileLabel.label"));
        JButton fileButton = new JButton(Context.getMessage("XMLCrosswalkDialog.fileButton.label"));
        fileButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent ae) {
                transformComboBox.setEnabled(false);
                okButton.setEnabled(false);                
                
                File [] selectedFiles = SwingUtils.chooseFiles(
                    Context.getMessage("addXMLMenuItem.fileChooser.title"),
                    new FileExtensionFilter(new String [] {"xml"},Context.getMessage("addXMLMenuItem.fileFilter.label"),true),
                    JFileChooser.FILES_ONLY,
                    false
                );
                
                SwingUtils.ShowBusy();
                
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
                        String schemaPath = MetsUtils.getSchemaPath(document);
                        if(schemaPath != null){            
                            URL schemaURL = Context.getResource(schemaPath);                                                                                                           
                            XML.validate(document,XML.createSchema(schemaURL));            
                        }
                        //zoek mapping
                        if(!MetsUtils.getCrosswalk().containsKey(transformFromNamespace)){
                            throw new NoTransformationFoundException("no crosswalk found");
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
                        
                    } catch (ParserConfigurationException ex) {
                        SwingUtils.ShowError(
                            Context.getMessage("XMLCrosswalkDialog.Exception.title"),
                            Context.getMessage(
                                "XMLCrosswalkDialog.import.ParserConfigurationException.description",
                                new Object [] {selectedFiles[0],ex.getMessage()}
                            )
                        );
                    } catch (SAXException ex) {
                        SwingUtils.ShowError(
                            Context.getMessage("XMLCrosswalkDialog.Exception.title"),
                            Context.getMessage(
                                "XMLCrosswalkDialog.import.SAXException.description",
                                new Object [] {selectedFiles[0],ex.getMessage()}
                            )
                        );
                    } catch (IOException ex) {
                        SwingUtils.ShowError(
                            Context.getMessage("XMLCrosswalkDialog.Exception.title"),
                            Context.getMessage(
                                "XMLCrosswalkDialog.import.IOException.description",
                                new Object [] {selectedFiles[0],ex.getMessage()}
                            )
                        );
                    }catch(NoNamespaceException e){
                        SwingUtils.ShowError(
                            Context.getMessage("XMLCrosswalkDialog.Exception.title"),
                            Context.getMessage(
                                "XMLCrosswalkDialog.import.NoNamespaceException.description",
                                new Object [] {selectedFiles[0]}
                            )
                        );                                             
                    }catch(IllegalNamespaceException e){
                        SwingUtils.ShowError(
                            Context.getMessage("XMLCrosswalkDialog.Exception.title"),
                            Context.getMessage(
                                "XMLCrosswalkDialog.import.IllegalNamespaceException.description",
                                new Object [] {selectedFiles[0],transformFromNamespace}
                            )
                        );
                    }catch(NoTransformationFoundException e){
                        SwingUtils.ShowError(
                            Context.getMessage("XMLCrosswalkDialog.Exception.title"),
                            Context.getMessage(
                                "XMLCrosswalkDialog.import.NoTransformationFoundException.description",
                                new Object [] {selectedFiles[0]}
                            )
                        );
                    }
                }
                
                SwingUtils.ShowDone();
                
            }            
        });
        filePanel.add(fileLabel);
        filePanel.add(getFileField());
        filePanel.add(fileButton);
        
        JPanel transformFromPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        transformFromPanel.add(new JLabel(Context.getMessage("XMLCrosswalkDialog.transformingFromLabel.label")));
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