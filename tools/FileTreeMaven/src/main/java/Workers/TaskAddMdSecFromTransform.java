/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Workers;

import Exceptions.IllegalNamespaceException;
import Exceptions.NoNamespaceException;
import Filters.FileExtensionFilter;
import com.anearalone.mets.MdSec;
import helper.Context;
import helper.XML;
import helper.XSLT;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import javax.swing.JFileChooser;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 *
 * @author nicolas
 */
public class TaskAddMdSecFromTransform extends DefaultWorker {
    @Override
    protected Void doInBackground() throws Exception {
        File [] files = helper.SwingUtils.chooseFiles(
            "Select xml file",
            new FileExtensionFilter(new String [] {"xml"},"xml files only",true),
            JFileChooser.FILES_ONLY,
            true
        );
        int succeeded = 0;

        for(int i = 0;i<files.length;i++){
            File file = files[i];

            try{
                Document inputDoc = XML.XMLToDocument(file);
                String namespace = inputDoc.getDocumentElement().getNamespaceURI();

                if(!helper.MetsUtils.getXsltMap().containsKey(namespace)){
                    System.out.println("no transformation found for "+file);
                    continue;
                }
                Document xsltDoc = XML.XMLToDocument(new URL(helper.MetsUtils.getXsltMap().get(namespace)));
                if(xsltDoc == null){
                    continue;
                }
                Document outDoc = XSLT.transform(inputDoc,xsltDoc);

                if(outDoc == null){
                    continue;
                }
                MdSec mdSec = helper.MetsUtils.createMdSec(outDoc);                        

                send(mdSec);                    

                succeeded++;
            }catch(IOException e){                                                    
                log(Context.getMessage("mdSecTable.addMdSec.IOException",new Object []{
                    file,e.getMessage()
                })+"\n");                    
            }catch(SAXException e){                                                    
                log(helper.Context.getMessage("mdSecTable.addMdSec.SAXException",new Object []{
                    file,e.getMessage()
                })+"\n");                        
            }catch(IllegalNamespaceException e){                    
                log(helper.Context.getMessage("mdSecTable.addMdSec.IllegalNamespaceException",new Object []{
                    file,e.getNamespace()
                })+"\n");                    
            }
            catch(NoNamespaceException e){                                                   
                log(helper.Context.getMessage("mdSecTable.addMdSec.noNamespaceException",new Object []{
                    file
                })+"\n");                 
            }

            int percent = (int)Math.floor( ((i+1) / ((float)files.length))*100);                                                                        
            setProgress(percent);                 
        }     
        if(succeeded > 0){
            success(true);
        }
        return null;
    }        
}