package ugent.bagger.wizards;

import com.anearalone.mets.MdSec;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.SwingUtilities;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.richclient.wizard.AbstractWizard;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;
import ugent.bagger.exceptions.DtdNoFixFoundException;
import ugent.bagger.exceptions.IllegalNamespaceException;
import ugent.bagger.exceptions.NoNamespaceException;
import ugent.bagger.forms.CrosswalkParamsForm;
import ugent.bagger.helper.Context;
import ugent.bagger.helper.MetsUtils;
import ugent.bagger.helper.SwingUtils;
import ugent.bagger.helper.XML;
import ugent.bagger.helper.XSLT;
import ugent.bagger.params.CrosswalkParams;

/**
 *
 * @author nicolas
 */
public class CrosswalkParamsWizard extends AbstractWizard {    
    static final Log log = LogFactory.getLog(CrosswalkParamsWizard.class);
    CrosswalkParamsPage1 crosswalkParamsPage1;        
    HashMap<String,ArrayList<PropertyChangeListener>>propertyChangeListeners = new HashMap<String,ArrayList<PropertyChangeListener>>();    

    public CrosswalkParamsWizard(String wizardId){
        super(wizardId);
    }
    public CrosswalkParamsPage1 getCrosswalkParamsPage1() {
        if(crosswalkParamsPage1 == null){
            crosswalkParamsPage1 = new CrosswalkParamsPage1("CrosswalkParamsPage1");
        }
        return crosswalkParamsPage1;
    }
    @Override
    public void addPages(){     
        addPage(getCrosswalkParamsPage1());           
    }
    public void addPropertyChangeListener(String key,PropertyChangeListener l){        
        if(!propertyChangeListeners.containsKey(key)){
            propertyChangeListeners.put(key,new ArrayList<PropertyChangeListener>());
        }
        propertyChangeListeners.get(key).add(l);       
    }
    public void firePropertyChange(String key,Object oldValue,Object newValue){
        if(propertyChangeListeners.containsKey(key)){
            ArrayList<PropertyChangeListener>list = propertyChangeListeners.get(key);
            for(PropertyChangeListener l:list){
                l.propertyChange(new PropertyChangeEvent(this,key,newValue,newValue));               
            }            
        }
    }
    @Override
    protected boolean onFinish() {        
        final CrosswalkParamsForm crosswalkParamsForm = getCrosswalkParamsPage1().getCrosswalkParamsPanel().getCrosswalkParamsForm();
        final CrosswalkParams crosswalkParams = getCrosswalkParamsPage1().getCrosswalkParamsPanel().getCrosswalkParams();
        if(crosswalkParamsForm.hasErrors()){
            return false;
        }
        
        crosswalkParamsForm.commit();
        
        Runnable run = new Runnable() {
            @Override
            public void run() {
                SwingUtils.ShowBusy();
                
                String error = null;
                File file = crosswalkParams.getFile().get(0);
                String transformFromNamespace = crosswalkParams.getTransformFromNamespace();        
                String transformToNamespace = crosswalkParams.getTransformToNamespace();        

                try{

                    Document sourceDoc = XML.XMLToDocument(file);    
                    String namespace = sourceDoc.getDocumentElement().getNamespaceURI();

                    if(namespace == null || namespace.isEmpty()){                
                        sourceDoc = MetsUtils.fixNamespace(sourceDoc);                        
                        namespace = sourceDoc.getDocumentElement().getNamespaceURI();                        
                    }

                    String filename = MetsUtils.getXsltPath(sourceDoc.getDocumentElement(),transformToNamespace);                    
                    Document xsltDoc = XML.XMLToDocument(Context.getResource(filename));                    
                    Document transformedDoc = XSLT.transform(sourceDoc,xsltDoc);                                         

                    MdSec mdSec = MetsUtils.createMdSec(transformedDoc);                                              
                    firePropertyChange("mdSec",null,mdSec);            

                }catch(ParserConfigurationException e){
                    log.error(e);                    
                    error = Context.getMessage(
                        "XMLCrosswalkDialog.transform.ParserConfigurationException.description",
                        new Object []{
                            file,e.getMessage()
                        }
                    );
                }catch(SAXException e){
                    log.error(e);                    
                    error = Context.getMessage(
                        "XMLCrosswalkDialog.transform.SAXException.description",
                        new Object []{
                            file,e.getMessage()
                        }
                    );
                }catch(IOException e){  
                    log.error(e);                   
                    error = Context.getMessage(
                        "XMLCrosswalkDialog.transform.IOException.description",
                        new Object []{
                            file,e.getMessage()
                        }
                    ); 
                }catch(TransformerConfigurationException e){
                    log.error(e);
                    error = Context.getMessage(
                        "XMLCrosswalkDialog.transform.TransformerConfigurationException.description",
                        new Object []{
                            file,e.getMessage()
                        }
                    );
                }catch(TransformerException e){
                    log.error(e); 
                    error = Context.getMessage(
                        "XMLCrosswalkDialog.transform.TransformerException.description",
                        new Object []{
                            file,e.getMessage()
                        }
                    );
                }catch(NoNamespaceException e){
                    log.error(e);                   
                    error = Context.getMessage(
                        "XMLCrosswalkDialog.transform.NoNamespaceException.description",
                        new Object []{
                            file,e.getMessage()
                        }
                    );
                }catch(IllegalNamespaceException e){
                    log.error(e);                  
                    error = Context.getMessage(
                        "XMLCrosswalkDialog.transform.IllegalNamespaceException.description",
                        new Object []{
                            file,e.getMessage()
                        }
                    )+"\n";
                }catch(DtdNoFixFoundException e){     
                    log.error(e);                                      
                    error = Context.getMessage("XMLCrosswalkDialog.transform.DtdNoFixFoundException.description",new Object []{
                        file
                    });                         
                }catch(Exception e){
                    log.error(e);                                      
                    error = Context.getMessage(
                        "XMLCrosswalkDialog.transform.Exception.description",
                        new Object []{
                            file,e.getMessage()
                        }
                    );
                }
                
                SwingUtils.ShowDone();
                
                if(error != null){
                    SwingUtils.ShowError(
                        Context.getMessage("XMLCrosswalkDialog.Exception.title"),
                        error
                    );            
                }
            }
        };
                
        SwingUtilities.invokeLater(run);
       
        return true;
    }    
} 