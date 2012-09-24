package ugent.bagger.helper;

import eu.medsea.mimeutil.MimeType;
import eu.medsea.mimeutil.MimeUtil;
import java.io.File;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import javax.swing.tree.DefaultMutableTreeNode;
import org.apache.commons.vfs.FileObject;
import org.apache.commons.vfs.FileSystemException;
import org.apache.commons.vfs.FileSystemManager;
import org.apache.commons.vfs.VFS;
import sun.security.provider.MD5;
/**
 *
 * @author nicolas
 */
public class FUtils {
    private static final HashMap<String,Double> sizes;
    private static MessageDigest md5DigestInstance;
    private static final String [] sizeNames = {        
      "TB","GB","MB","KB","B"
    };
   
    private static Comparator defaultFileSorter =  new Comparator<File>(){
        @Override
        public int compare(File f1, File f2){
            return f1.getName().compareToIgnoreCase(f2.getName());
        }
    };
    static {
        MimeUtil.registerMimeDetector("eu.medsea.mimeutil.detector.MagicMimeMimeDetector");
        sizes = new HashMap<String,Double>();
        sizes.put("KB",new Double(1024));
        sizes.put("MB",new Double(1024*1024));       
        sizes.put("GB",new Double(1024*1024*1024));        
        sizes.put("TB",new Double(1024*1024*1024*1024));        
    }
    public static String sizePretty(double size){       
        for(int i = 0;i < sizeNames.length;i++){
            if(
                sizes.containsKey(sizeNames[i])
            ){                 
                double d = sizes.get(sizeNames[i]).doubleValue();               
                int n = (int) Math.round(size / d);                
                if(n > 0)return ""+n+sizeNames[i];
            }
        }
        return ((long)size)+"B";
    }
    public static ArrayList<File> listFiles(String fn){
        return listFiles(new File(fn));
    }
    public static ArrayList<File> listFiles(String fn,boolean sort){
        return listFiles(new File(fn),sort);
    }
    public static ArrayList<File> listFiles(File f){
        return listFiles(f,false);
    }
    public static ArrayList<File> listFiles(final File f,boolean sort){
        final ArrayList<File> files = new ArrayList<File>();        
        listFiles(
            f,new IteratorListener(){
            @Override
                public void execute(Object o){                   
                    files.add((File)o);          
                }
            },sort
        );       
        return files;
    }
    public static void listFiles(String filename,IteratorListener l){
        listFiles(new File(filename),l);
    }
    public static void listFiles(File f,IteratorListener l){
        listFiles(f,l,false);        
    }
    public static void listFiles(File f,IteratorListener l,boolean sort){
       
        if(!f.isDirectory() || f.listFiles() == null) {
            return;
        }       
        
        if(sort){

            ArrayList<File>files = new ArrayList<File>();
            ArrayList<File>directories = new ArrayList<File>();

            for(File sb:f.listFiles()){
                if(sb.isDirectory()) {
                    directories.add(sb);
                }
                else {
                    files.add(sb);
                }         
            }
            Collections.sort(directories,defaultFileSorter);
            for(File dir:directories){           
                listFiles(dir,l,sort);
            }
            Collections.sort(files,defaultFileSorter);
            for(File file:files){            
                l.execute(file);
            } 
            
        }else{
            
            for(File sb:f.listFiles()){
                if(sb.isDirectory()) {
                    listFiles(sb,l,sort);
                }
                else {
                    l.execute(sb);
                }         
            }
            
        }
    }
    public static DefaultMutableTreeNode toTreeNode(File file){
        return toTreeNode(file,-1);
    }
    public static DefaultMutableTreeNode toTreeNode(File file,int maxdepth){
        if(file == null || !file.exists()){
            return null;
        }        
        DefaultMutableTreeNode root = new DefaultMutableTreeNode(file);       
        
        if(file.isFile()){
            root.setAllowsChildren(false);
            return root;
        }
        if(maxdepth == 0){
            return root;
        }
        
        ArrayList<File>files = new ArrayList<File>();
        ArrayList<File>directories = new ArrayList<File>();
        
        for(File sb:file.listFiles()){
            if(sb.isDirectory()) {
                directories.add(sb);
            }
            else {
                files.add(sb);
            }         
        }
        
        Collections.sort(directories,defaultFileSorter);
        for(File dir:directories){            
            root.add(toTreeNode(dir,maxdepth-1));
        }
        Collections.sort(files,defaultFileSorter);
        for(File f:files){            
           root.add(toTreeNode(f,maxdepth-1));
        }             
        
        return root;
    }
    public static void walkTree(DefaultMutableTreeNode node,IteratorListener il){
        if(node == null) {            
            return;
        }
        if(node.isLeaf()) {            
            il.execute(node.getUserObject());
        }
        else{                                  
            for(int i = 0;i < node.getChildCount();i++){             
                walkTree((DefaultMutableTreeNode)node.getChildAt(i),il);
            }
        }
    }   
    public static FileSystemManager getFileSystemManager() throws FileSystemException{
        return VFS.getManager();
    }
    public static FileObject resolveFile(String str) throws FileSystemException{
        return getFileSystemManager().resolveFile(str);
    }
    public static InputStream getInputStreamFor(String str) throws FileSystemException{                        
        FileObject fileObject = resolveFile(str);        
        InputStream in = fileObject.getContent().getInputStream();                    
        return in;
    }
    public static String getMimeType(File file){
        if(file.isFile()){                    
            Collection mimes = MimeUtil.getMimeTypes(file);
            if(!mimes.isEmpty()){
                Iterator it = mimes.iterator();
                while(it.hasNext()){                          
                    return ((MimeType)it.next()).toString();
                }
            }else{
                return "application/octet-stream";
            }
        }
        return "";
    }    
}
