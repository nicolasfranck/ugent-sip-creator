/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Workers;

import Exceptions.IllegalNamespaceException;
import Exceptions.NoNamespaceException;
import Importers.Importer;
import Importers.ImporterFactory;
import com.anearalone.mets.MdSec;
import helper.Context;
import helper.XML;
import java.io.File;
import java.io.IOException;
import javax.swing.JFileChooser;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 *
 * @author nicolas
 */
public class TaskAddMdSecFromImport extends DefaultWorker {
    @Override
    protected Void doInBackground() throws Exception {
        File [] files = helper.SwingUtils.chooseFiles(
            "Select file",
            null,
            JFileChooser.FILES_ONLY,
            true
        );
        int succeeded = 0;

        for(int i = 0;i<files.length;i++){
            File file = files[i];               
            try{
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
                XML.DocumentToXML(doc,new java.io.FileOutputStream(new File("/tmp/dc.xml")),true);


                MdSec mdSec = helper.MetsUtils.createMdSec(doc);                        
                
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