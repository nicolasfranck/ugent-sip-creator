/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ugent.bagger.workers;

import com.anearalone.mets.MdSec;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import javax.swing.JFileChooser;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;
import ugent.bagger.exceptions.DocumentCreationFailedException;
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
public class TaskAddMdSecFromTransform extends DefaultWorker {
    @Override
    protected Void doInBackground() throws Exception {
        File [] files = ugent.bagger.helper.SwingUtils.chooseFiles(
            Context.getMessage("TaskAddMdSecFromTransform.fileChooser.title"),
            new FileExtensionFilter(new String [] {"xml"},Context.getMessage("TaskAddMdSecFromTransform.fileFilter.label"),true),
            JFileChooser.FILES_ONLY,
            true
        );
        int succeeded = 0;
        int numErrors = 0;

        for(int i = 0;i<files.length;i++){
            File file = files[i];

            try{
                Document inputDoc = XML.XMLToDocument(file);
                String namespace = inputDoc.getDocumentElement().getNamespaceURI();

                if(!MetsUtils.getXsltMap().containsKey(namespace)){                    
                    throw new NoTransformationFoundException();                    
                }
                URL url = Context.getResource(MetsUtils.getXsltMap().get(namespace));                        
                Document xsltDoc = XML.XMLToDocument(url);
                if(xsltDoc == null){
                    throw new DocumentCreationFailedException(); 
                }
                Document outDoc = XSLT.transform(inputDoc,xsltDoc);

                if(outDoc == null){
                    throw new DocumentCreationFailedException(); 
                }
                MdSec mdSec = MetsUtils.createMdSec(outDoc);                        

                send(mdSec);                    

                succeeded++;
            }catch(IOException e){                                                    
                numErrors++;
                log(Context.getMessage("mdSecTable.addMdSec.IOException",new Object []{
                    file,e.getMessage()
                })+"\n");                    
            }catch(SAXException e){ 
                numErrors++;
                log(Context.getMessage("mdSecTable.addMdSec.SAXException",new Object []{
                    file,e.getMessage()
                })+"\n");                        
            }catch(IllegalNamespaceException e){                    
                numErrors++;
                log(Context.getMessage("mdSecTable.addMdSec.IllegalNamespaceException",new Object []{
                    file,e.getNamespace()
                })+"\n");                    
            }
            catch(NoNamespaceException e){                                                   
                numErrors++;
                log(Context.getMessage("mdSecTable.addMdSec.noNamespaceException",new Object []{
                    file
                })+"\n");                 
            }catch(NoTransformationFoundException e){
                numErrors++;
                log(Context.getMessage("mdSecTable.addMdSec.noTransformationFoundException",new Object []{
                    file
                })+"\n");
            }
            catch(DocumentCreationFailedException e){
                numErrors++;
                log(Context.getMessage("mdSecTable.addMdSec.documentCreationFailedException",new Object []{
                    file
                })+"\n");
            }

            int percent = (int)Math.floor( ((i+1) / ((float)files.length))*100);                                                                        
            setProgress(percent);                 
        }     
        
        //report
        String report = Context.getMessage("report.message",new Integer []{
            succeeded,numErrors
        });
        String reportLog = Context.getMessage("report.log");
        SwingUtils.ShowMessage(null,report+"\n"+reportLog);
        
        if(succeeded > 0){
            success(true);
        }
        return null;
    }        
}