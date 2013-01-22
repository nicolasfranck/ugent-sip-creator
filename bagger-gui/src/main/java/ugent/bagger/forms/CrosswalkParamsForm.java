package ugent.bagger.forms;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.SwingUtilities;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.binding.form.FormModel;
import org.springframework.richclient.form.AbstractForm;
import org.springframework.richclient.form.FormModelHelper;
import org.springframework.richclient.form.binding.Binding;
import org.springframework.richclient.form.binding.swing.SwingBindingFactory;
import org.springframework.richclient.form.builder.TableFormBuilder;
import org.springframework.richclient.list.DynamicComboBoxListModel;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;
import ugent.bagger.bindings.FileSelectBinding;
import ugent.bagger.exceptions.DtdNoFixFoundException;
import ugent.bagger.exceptions.IllegalNamespaceException;
import ugent.bagger.exceptions.NoNamespaceException;
import ugent.bagger.exceptions.NoTransformationFoundException;
import ugent.bagger.filters.FileExtensionFilter;
import ugent.bagger.helper.Context;
import ugent.bagger.helper.MetsUtils;
import ugent.bagger.helper.SwingUtils;
import ugent.bagger.helper.XML;
import ugent.bagger.params.CrosswalkParams;



/**
 *
 * @author nicolas
 */
public class CrosswalkParamsForm extends AbstractForm{
    static final Log log = LogFactory.getLog(CrosswalkParamsForm.class);
    
    public CrosswalkParamsForm(CrosswalkParams crosswalkParams){
        this(FormModelHelper.createFormModel(crosswalkParams,"crosswalkParamsForm"));
    }
    public CrosswalkParamsForm(FormModel formModel){
        super(formModel);       
    }    
    @Override
    protected JComponent createFormControl() {
        
        final SwingBindingFactory bf = (SwingBindingFactory) getBindingFactory();
        TableFormBuilder builder = new TableFormBuilder(bf);                
        builder.setLabelAttributes("colSpan=1 align=left");   
        
        CrosswalkParams crosswalkParams = (CrosswalkParams) getFormObject();               
        
        //file        
        JFileChooser fileChooser = SwingUtils.createFileChooser(
            null,
            new FileExtensionFilter(new String [] {"xml"},Context.getMessage("addXMLMenuItem.fileFilter.label"),true),
            JFileChooser.FILES_ONLY,
            false,
            JFileChooser.OPEN_DIALOG
        );        
        final Binding fileBinding = new FileSelectBinding(
            getFormModel(),
            "file",
            fileChooser,            
            SwingUtils.getFrame()
        );                                
        builder.add(fileBinding);
        builder.row();
        
        //transformFromNamespace
        final JComponent [] componentsTransformFromNamespace = builder.add("transformFromNamespace");        
        componentsTransformFromNamespace[1].setEnabled(false);
        builder.row();
        
        //transformToNamespace                   
        final Binding transformToNamespaceBinding = bf.createBoundComboBox("transformToNamespace",new Object [] {});
        final JComboBox transformComboBox = (JComboBox) transformToNamespaceBinding.getControl();        
        transformComboBox.setModel(new DynamicComboBoxListModel(getValueModel("transformToNamespace")));
        transformComboBox.setEnabled(false);
        builder.add(transformToNamespaceBinding);
        builder.row();       
        
        
        //change file
        getValueModel("file").addValueChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent pce) {
                final ArrayList<File>files = (ArrayList<File>) pce.getNewValue();
                if(files == null || files.isEmpty()){
                    return;
                }
                
                SwingUtilities.invokeLater(new Runnable(){
                    @Override
                    public void run() {
                        SwingUtils.ShowBusy();
                
                        String transformFromNamespace = null;
                        String transformToNamespace = null;
                        
                        String error = null;

                        try{                       
                            Document document = XML.XMLToDocument(files.get(0));
                            String namespace = document.getDocumentElement().getNamespaceURI();

                            if(namespace == null || namespace.isEmpty()){                
                                document = MetsUtils.fixNamespace(document);
                                namespace = document.getDocumentElement().getNamespaceURI();                            
                            }

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
                            String [] keys = crosswalk.keySet().toArray(new String [] {});

                            DynamicComboBoxListModel model = (DynamicComboBoxListModel) transformComboBox.getModel();
                            model.replaceWith(Arrays.asList(keys));

                            //stel 1ste waarde in!
                            transformToNamespace = keys[0];                    

                            getFormModel().getValueModel("transformFromNamespace").setValue(transformFromNamespace);
                            componentsTransformFromNamespace[1].setEnabled(false);
                            getFormModel().getValueModel("transformToNamespace").setValue(transformToNamespace);                                        

                            //enable combobox
                            transformComboBox.setEnabled(true);                      

                        } catch (ParserConfigurationException e) {                            
                            error =   Context.getMessage(
                                "XMLCrosswalkDialog.import.ParserConfigurationException.description",
                                new Object [] {files.get(0),e.getMessage()}                                
                            );
                            log.error(e.getMessage());
                        } catch (SAXException e) {                            
                            error = Context.getMessage(
                                "XMLCrosswalkDialog.import.SAXException.description",
                                new Object [] {files.get(0),e.getMessage()}                                
                            );
                            log.error(e.getMessage());
                        } catch (IOException e) {                            
                            error = Context.getMessage(
                                "XMLCrosswalkDialog.import.IOException.description",
                                new Object [] {files.get(0),e.getMessage()}                                
                            );
                            log.error(e.getMessage());
                        }catch(NoNamespaceException e){
                            error = Context.getMessage(
                                "XMLCrosswalkDialog.import.NoNamespaceException.description",
                                new Object [] {files.get(0)}                                
                            );                        
                            log.error(e.getMessage());
                        }catch(IllegalNamespaceException e){
                            error = Context.getMessage(
                                "XMLCrosswalkDialog.import.IllegalNamespaceException.description",
                                new Object [] {files.get(0),transformFromNamespace}                                
                            );
                            log.error(e.getMessage());
                        }catch(NoTransformationFoundException e){
                            error = Context.getMessage(
                                "XMLCrosswalkDialog.import.NoTransformationFoundException.description",
                                new Object [] {files.get(0)}                                
                            );
                            log.error(e.getMessage());
                        }catch(DtdNoFixFoundException e){     
                            error = Context.getMessage(
                                "XMLCrosswalkDialog.import.DtdNoFixFoundException.description",
                                new Object []{
                                    files.get(0).getAbsolutePath(),e.getMessage()
                                }                                                         
                            );     
                            log.error(e.getMessage());
                        }catch(Exception e){
                            error = Context.getMessage(
                                "XMLCrosswalkDialog.import.Exception.description",
                                new Object []{
                                    files.get(0).getAbsolutePath(),e.getMessage()
                                }                                                         
                            );  
                            log.error(e.getMessage());
                        }
                        
                        if(error != null){
                            SwingUtils.ShowError(
                                Context.getMessage("XMLCrosswalkDialog.Exception.title"),
                                error                            
                            );       
                            log.error(error);
                        }
                        
                        SwingUtils.ShowDone();
                    }                    
                });               
                                
            }
        });
        
        
        getFormModel().validate();
       
        return builder.getForm();
    }
}