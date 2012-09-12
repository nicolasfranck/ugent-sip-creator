/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ugent.bagger.handlers;

import ugent.bagger.helper.FileSource;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.File;
import java.io.IOException;
import java.util.List;
import javax.swing.JComponent;
import javax.swing.JTree;
import javax.swing.TransferHandler;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import org.apache.log4j.Logger;


/**
 *
 * @author nicolas
 */
public class FileTreeTransferHandler extends TransferHandler{

    private static Logger logger = Logger.getLogger(FileTreeTransferHandler.class);
    
    public enum Mode {
        FILES_AND_DIRECTORIES,FILES_ONLY,DIRECTORIES_ONLY
    };   

    private Mode mode = Mode.FILES_AND_DIRECTORIES;

    public FileTreeTransferHandler(){
    }
    public FileTreeTransferHandler(Mode mode){
        setMode(mode);
    }
    public Mode getMode() {
        return mode;
    }
    public void setMode(Mode mode) {
        this.mode = mode;
    }
    @Override
    public boolean canImport(JComponent component,DataFlavor [] dataFlavors){        
        boolean success = false;
        for(DataFlavor dataFlavor:dataFlavors){            
            //Drop from Windows File Explorer
            if(dataFlavor.equals(DataFlavor.javaFileListFlavor)){
                logger.debug("payloadTree: transferHandler: canImport: javaFileListFlavor");
                success = true;
            }
            //Drop from Gnome File Explorer
            else if(dataFlavor.equals(DataFlavor.stringFlavor)){
                logger.debug("payloadTree: transferHandler: canImport: stringFlavor");
                success = true;
            }
        }
        return success;
    }
    @Override
    public boolean importData(JComponent component,Transferable t){
        if(!(component instanceof JTree))return false;

        JTree tree = (JTree)component;
        DefaultMutableTreeNode rootNode = (DefaultMutableTreeNode)tree.getModel().getRoot();

        logger.debug("tree: transferHandler: canImport called");
        boolean success = false;

        File [] files = new File[] {};

        for(DataFlavor dataFlavor:t.getTransferDataFlavors()){
            
            //Drop from Windows File Explorer
            if(dataFlavor.equals(DataFlavor.javaFileListFlavor)){
                logger.debug("tree: transferHandler: importData: javaFileListFlavor");
                try{
                    List<File>list = (List<File>) t.getTransferData(DataFlavor.javaFileListFlavor);
                    files = list.toArray(new File[]{});
                }catch(UnsupportedFlavorException e){
                    e.printStackTrace();
                }catch(IOException e){
                    e.printStackTrace();
                }

                success = true;
            }
            //Drop from Gnome File Explorer
            else if(dataFlavor.equals(DataFlavor.stringFlavor)){
                logger.debug("tree: transferHandler: importData: stringFlavor");
                try{
                    String data = (String) t.getTransferData(DataFlavor.stringFlavor);
                    String [] list = data.substring(0,data.length()-2).split("\r\n");
                    files = new File[list.length];
                    for(int i = 0;i<list.length;i++){
                        String url = list[i];
                        String path = url.replaceAll("file://","");
                        files[i] = new File(path);
                    }
                    success = true;
                }catch(UnsupportedFlavorException e){
                    e.printStackTrace();
                }catch(IOException e){
                    e.printStackTrace();
                }
            }
        }
        //check validity
        for(int i = 0;i<files.length;i++){
            switch(mode){
                case DIRECTORIES_ONLY:
                    if(!files[i].isDirectory())return false;
                    break;
                case FILES_ONLY:
                    if(!files[i].isFile())return false;
                    break;
            }
        }
        //import
        for(int i = 0;i<files.length;i++){
            DefaultMutableTreeNode node = ugent.bagger.helper.FileUtils.toTreeNode(new FileSource(files[i]));
            DefaultMutableTreeNode simularNode = ugent.bagger.helper.TreeUtils.findSimularNode(tree,node);
            if(simularNode != null){
                logger.debug("removing old child");
                rootNode.remove(simularNode);               
            }
            rootNode.add(node);
        }        
        ((DefaultTreeModel)tree.getModel()).reload();        
        return success;
    }
}
