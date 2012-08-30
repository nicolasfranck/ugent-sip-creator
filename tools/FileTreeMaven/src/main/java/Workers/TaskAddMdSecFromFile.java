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
                MdSec mdSec = helper.MetsUtils.createMdSec(file);                        
                send(mdSec);                    
                succeeded++;
            }catch(IOException e){  
                e.printStackTrace();
                log(Context.getMessage("mdSecTable.addMdSec.IOException",new Object []{
                    file,e.getMessage()
                }));                    
            }catch(SAXException e){  
                e.printStackTrace();
                log(helper.Context.getMessage("mdSecTable.addMdSec.SAXException",new Object []{
                    file,e.getMessage()
                })+"\n");                                                    
            }catch(ParserConfigurationException e){  
                e.printStackTrace();
                log(helper.Context.getMessage("mdSecTable.addMdSec.ParserConfigurationException",new Object []{
                    e.getMessage()
                })+"\n");                    
            }catch(IllegalNamespaceException e){
                e.printStackTrace();
                log(helper.Context.getMessage("mdSecTable.addMdSec.IllegalNamespaceException",new Object []{
                    file,e.getNamespace()
                })+"\n");
            }
            catch(NoNamespaceException e){          
                e.printStackTrace();
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