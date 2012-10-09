package treetable;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import org.apache.log4j.Logger;

/* A FileNode is a derivative of the File class - though we delegate to 
 * the File object rather than subclassing it. It is used to maintain a 
 * cache of a directory's children and therefore avoid repeated access 
 * to the underlying file system during rendering. 
 */
public class FileNode { 
    File     file; 
    Object[] children;

    private static Logger logger = Logger.getLogger(FileNode.class);

    public FileNode(File file) { 
	this.file = file; 
    }

    // Used to sort the file names.
    private static Comparator defaultFileSorter =  new Comparator<File>(){
        @Override
        public int compare(File f1, File f2){
            return f1.getName().compareToIgnoreCase(f2.getName());
        }
    };

    /**
     * Returns the the string to be used to display this leaf in the JTree.
     */
    @Override
    public String toString() {         
	return file.getName();
    }

    public File getFile() {
	return file; 
    }

    /**
     * Loads the children, caching the results in the children ivar.
     */
    public Object[] getChildren() {
	if (children != null) {
	    return children; 
	}
	try{            
            File [] fileEntries = file.listFiles();
            logger.debug("getChildren: fileEntries:"+fileEntries);
            if(fileEntries != null){
                ArrayList<File> files = new ArrayList<File>();
                ArrayList<File> directories = new ArrayList<File>();
                int num = 0;
                for(File fileEntry:fileEntries){
                    if(!fileEntry.canRead()){
                        logger.debug("getChildren: fileEntry "+fileEntry+" not readable ('num' now:"+num+")");
                        continue;
                    }else if(fileEntry.isDirectory()){
                        logger.debug("getChildren: fileEntry "+fileEntry+" is directory ('num' now:"+num+")");
                        directories.add(fileEntry);
                        num++;
                    }else if(fileEntry.isFile()){
                        logger.debug("getChildren: fileEntry "+fileEntry+" is regular file");
                        files.add(fileEntry);
                        num++;
                    }                    
                }
                logger.debug("getChildren: directories:"+directories.size());
                logger.debug("getChildren: files:"+files.size());
                logger.debug("getChildren: total:"+num);
                Collections.sort(directories,defaultFileSorter);
                Collections.sort(files,defaultFileSorter);               
                children = new FileNode[num];
                int i = 0;
                for(i = 0;i < directories.size();i++){                
                    children[i] = new FileNode(directories.get(i));
                }
                for(int j = 0;j < files.size();j++){
                    children[i] = new FileNode(files.get(j));
                    i++;
                }               
            }else{
                children = new FileNode[]{};
            }
	}catch(Exception se){
            se.printStackTrace();
            children = new FileNode[]{};
        }
	return children; 
    }    
}