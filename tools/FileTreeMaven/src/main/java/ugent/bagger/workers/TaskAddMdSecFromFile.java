/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ugent.bagger.workers;

import ugent.bagger.exceptions.IllegalNamespaceException;
import ugent.bagger.exceptions.NoNamespaceException;
import ugent.bagger.filters.FileExtensionFilter;
import com.anearalone.mets.MdSec;
import ugent.bagger.helper.Context;
import java.io.File;
import java.io.IOException;
import javax.swing.JFileChooser;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;

/**
 *
 * @author nicolas
 */
public class TaskAddMdSecFromFile extends DefaultWorker {    
    @Override
    @SuppressWarnings("empty-statement")
    protected Void doInBackground() throws Exception {
        File [] files = ugent.bagger.helper.SwingUtils.chooseFiles(
            "Select xml file",
            new FileExtensionFilter(new String [] {"xml"},"xml files only",true),
            JFileChooser.FILES_ONLY,
            true
        );
        int succeeded = 0;

        for(int i = 0;i<files.length;i++){                                                        
            File file = files[i];
            try{
                MdSec mdSec = ugent.bagger.helper.MetsUtils.createMdSec(file);                        
                send(mdSec);                    
                succeeded++;
            }catch(IOException e){  
                e.printStackTrace();
                log(Context.getMessage("mdSecTable.addMdSec.IOException",new Object []{
                    file,e.getMessage()
                }));                    
            }catch(SAXException e){  
                e.printStackTrace();
                log(ugent.bagger.helper.Context.getMessage("mdSecTable.addMdSec.SAXException",new Object []{
                    file,e.getMessage()
                })+"\n");                                                    
            }catch(ParserConfigurationException e){  
                e.printStackTrace();
                log(ugent.bagger.helper.Context.getMessage("mdSecTable.addMdSec.ParserConfigurationException",new Object []{
                    e.getMessage()
                })+"\n");                    
            }catch(IllegalNamespaceException e){
                e.printStackTrace();
                log(ugent.bagger.helper.Context.getMessage("mdSecTable.addMdSec.IllegalNamespaceException",new Object []{
                    file,e.getNamespace()
                })+"\n");
            }
            catch(NoNamespaceException e){          
                e.printStackTrace();
                log(ugent.bagger.helper.Context.getMessage("mdSecTable.addMdSec.noNamespaceException",new Object []{
                    file
                })+"\n");                                
            }                            
            int percent = (int)Math.floor( ((i+1) / ((float)files.length))*100);                                                                        
            setProgress(percent);                
        }
        success(succeeded > 0);        
        return null;
    }    
}