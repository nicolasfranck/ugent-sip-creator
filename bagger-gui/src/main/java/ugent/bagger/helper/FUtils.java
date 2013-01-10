package ugent.bagger.helper;

import eu.medsea.mimeutil.MimeType;
import eu.medsea.mimeutil.MimeUtil;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.MessageDigest;
import java.util.*;
import java.util.logging.Level;
import java.util.regex.Pattern;
import javax.swing.tree.DefaultMutableTreeNode;
import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemException;
import org.apache.commons.vfs2.FileSystemManager;
import org.apache.commons.vfs2.VFS;
import org.apache.log4j.Logger;
import ugent.bagger.exceptions.FileNameNotPortableException;
import ugent.bagger.exceptions.FileNotReadableException;
import ugent.bagger.exceptions.FileNotWritableException;

/**
 *
 * @author nicolas
 */
public class FUtils {
    static final Logger log = Logger.getLogger(FUtils.class);
    static final HashMap<String,Double> sizes;
    static MessageDigest md5DigestInstance;
    static final String [] sizeNames = {        
      "TB","GB","MB","KB","B"
    };   
    static Pattern safeFileNamePattern = Pattern.compile("^[a-zA-Z0-9\\.\\-_]+$");
    
    public static Comparator defaultFileSorter =  new Comparator<File>(){
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
                if(n > 0) {
                    return ""+n+sizeNames[i];
                }
            }
        }       
        return ((long)size)+"B";
    }
    public static boolean hasChildren(File file){        
        return file != null && file.listFiles() != null && file.listFiles().length > 0;
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
    public static void sort(List<File>files){
        Collections.sort(files,defaultFileSorter);
    }
    public static void arrangeDirectoryEntries(File dir,ArrayList<File>files,ArrayList<File>directories){
        File [] list = dir.listFiles();
        list = list != null ? list: new File [] {};                
        for(File file:list){
            if(file.isDirectory()) {
                directories.add(file);
            }
            else {
                files.add(file);
            }         
        }            
        sort(directories);
        sort(files);
    }
    public static void listFiles(File f,IteratorListener l,boolean sort){
       
        if(!f.isDirectory() || f.listFiles() == null) {
            return;
        }       
        
        if(sort){

            ArrayList<File>files = new ArrayList<File>();
            ArrayList<File>directories = new ArrayList<File>();
            
            arrangeDirectoryEntries(f,files,directories);
            
            for(File dir:directories){           
                listFiles(dir,l,sort);
            }
            
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
                
        DefaultMutableTreeNode root = new DefaultMutableTreeNode(file.getName());       
        
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
    public static void walkTree(DefaultMutableTreeNode node,IteratorHierarchyListener il){
        walkTree(node,il,0);
    }
    public static void walkTree(DefaultMutableTreeNode node,IteratorHierarchyListener il,int deep){
        if(node == null) {            
            return;
        }
        if(node.isLeaf()) {            
            il.execute(node,deep);
        }else{                                  
            for(int i = 0;i < node.getChildCount();i++){             
                walkTree((DefaultMutableTreeNode)node.getChildAt(i),il,deep + 1);
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
        return resolveFile(str).getContent().getInputStream();                            
    }
    public static OutputStream getOutputStreamFor(String str) throws FileSystemException{           
        return resolveFile(str).getContent().getOutputStream();
    }
    public static String getMimeType(File file){                
        Iterator it = MimeUtil.getMimeTypes(file).iterator();
        //steeds application/octet-stream indien bestand niet gevonden
        while(it.hasNext()){                          
            return ((MimeType)it.next()).toString();            
        }
        return "application/octet-stream";
    }    
    public static String getMimeType(InputStream in){
        String mimeType = "application/octet-stream";        
        try{            
            //buffering is belangrijk: enkel mimetype herkenning van inputstream indien deze mark ondersteund!              
            if(!in.markSupported()){
                byte [] buf = new byte[8192];
                int bytesRead = in.read(buf);
                in = new ByteArrayInputStream(buf,0,bytesRead);                              
            }                       
            Iterator it = MimeUtil.getMimeTypes(in).iterator();
            while(it.hasNext()){                          
                mimeType = ((MimeType)it.next()).toString();
                break;
            }            
        }catch(Exception e){
            //als inputstream geen mark ondersteund, dan wordt een Exception geworpen
            log.debug(e.getMessage());            
        }
        return mimeType;        
    }    
    public static String getEntryStringFor(String source,String entry){
        String entryString = null;        
        if(source.endsWith(".tar.gz")){
            entryString = "tgz:file://"+source+"!/"+entry;
        }else if(source.endsWith(".tar")){
            entryString = "tar:file://"+source+"!/"+entry;
        }else if(source.endsWith(".tar.bz2")){
            entryString = "tbz2:file://"+source+"!/"+entry;
        }else if(source.endsWith(".zip")){
            entryString = "zip:file://"+source+"!/"+entry;
        }else if(source.endsWith(".tgz")){
            entryString = "tgz:file://"+source+"!/"+entry;
        }else if(source.endsWith(".tbz2")){
            entryString = "tbz2:file://"+source+"!/"+entry;
        }else if(source.endsWith(".bz2")){
            entryString = "bz2:file://"+source+"!/"+entry;
        }else if(source.endsWith(".gz")){
            entryString = "gz:file://"+source+"!/"+entry;
        }else{
            entryString = "file://"+source+"/"+entry;
        }        
        return entryString;        
    }   
    public static List<DefaultMutableTreeNode>listToStructure(String [] list){
        List<DefaultMutableTreeNode>structuredList = new ArrayList<DefaultMutableTreeNode>();
        
        for(String entry:list){            
            //steeds / gebruikt als separator in bagit!
            String [] components = entry.split("/");           
          
            if(structuredList.isEmpty()){                
                structuredList.add(componentsToTreeNode(components));                
            }else{
                //zoek entry in list
                DefaultMutableTreeNode existingNode = null;
                for(DefaultMutableTreeNode n:structuredList){
                    if(hasUserObject(n,components[0])){
                        existingNode = n;                        
                        break;
                    }
                }
                //vul aan of voeg toe
                if(existingNode != null){        
                    addComponents(existingNode,components,1);
                }else{        
                    DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(components[0]);
                    if(components.length > 1){
                        addComponents(newNode,components,1);
                    }
                    structuredList.add(newNode); 
                }
            }
                        
        }        
        
        return structuredList;
    }
    public static int indexUserObjectChild(DefaultMutableTreeNode node,Object o){
        for(int i = 0;i < node.getChildCount();i++){
            DefaultMutableTreeNode tnode = (DefaultMutableTreeNode) node.getChildAt(i);        
            if(tnode.getUserObject().equals(o)){
                return i;
            }
        }
        return -1;
    }
    public static boolean hasUserObject(DefaultMutableTreeNode node,Object o){
        return node.getUserObject().equals(o);
    }
    public static void addComponents(DefaultMutableTreeNode node,Object [] components){
        addComponents(node,components,0);
    }
    public static void addComponents(DefaultMutableTreeNode node,Object [] components,int offset){
        if(offset > (components.length - 1) || components == null || components.length == 0){
            return;
        }
        
        int indexChild = indexUserObjectChild(node,components[offset]);
        if(indexChild < 0){            
            DefaultMutableTreeNode newNode = componentsToTreeNode(components,offset);            
            node.add(newNode);
        }else{
            addComponents((DefaultMutableTreeNode)node.getChildAt(indexChild),components,offset + 1);
        }
    }
    public static DefaultMutableTreeNode componentsToTreeNode(Object [] components){
        return componentsToTreeNode(components,0);
    }
    public static DefaultMutableTreeNode componentsToTreeNode(Object [] components,int offset){
        if(components == null || components.length == 0 || offset > (components.length - 1)){
            return null;
        }
        DefaultMutableTreeNode node = new DefaultMutableTreeNode(components[offset]);        
        if((offset + 1) <= (components.length - 1)){            
            node.add(componentsToTreeNode(components,offset + 1));
        }
        return node;
    }  
    
    public static void recurseTree(DefaultMutableTreeNode node){
        recurseTree(node,0);
    }
    public static void recurseTree(DefaultMutableTreeNode node,int tab){
        for(int i = 0;i<tab;i++){
            System.out.print(" ");
        }
        System.out.println(node.getUserObject());                
        for(int i = 0;i<node.getChildCount();i++){                       
            recurseTree((DefaultMutableTreeNode)node.getChildAt(i),tab+1);
        }
    }
    public static ArrayList<File> getBadDirs(File file){
        ArrayList<File>badDirs = new ArrayList<File>();
        
        if(file.isDirectory()){
            if(!file.canRead() || !file.canWrite()){
                badDirs.add(file);
            }else{
                for(File f:file.listFiles()){  
                    badDirs.addAll(getBadDirs(f));
                }
            }
        }
        return badDirs;
    }
    public static ArrayList<File> getNotReadableFiles(File file){
        ArrayList<File>notReadableFiles = new ArrayList<File>();
       
        if(!file.canRead()){
            notReadableFiles.add(file);
        }else{
            for(File f:file.listFiles()){  
                notReadableFiles.addAll(getBadDirs(f));
            }
        }
        
        return notReadableFiles;
    }
    public static void checkFile(File file) throws FileNotReadableException, FileNotWritableException, FileNotFoundException{
        checkFile(file,false);
    }
    public static void checkFile(File file,boolean recurse) throws FileNotReadableException, FileNotWritableException, FileNotFoundException{        
        
        if(!file.exists()){
            throw new FileNotFoundException();
        }else if(!file.canRead()){
            throw new FileNotReadableException(file);
        }else if(!file.canWrite()){
            throw new FileNotWritableException(file);
        }
        if(recurse && file.isDirectory()){
            for(File f:file.listFiles()){
                checkFile(f,recurse);
            }
        }
    }
    public static boolean isDescendant(File parent,File child){        
        child = child.getParentFile();
        while(child != null){
            if(child.equals(parent)){
                return true;
            }
            child = child.getParentFile();
        }
        return false;             
    }
    public static void checkSafeFiles(File file)throws FileNameNotPortableException{
        if(!isSafeFile(file)){
            throw new FileNameNotPortableException(file);
        }
        if(file.isDirectory()){
            for(File child:file.listFiles()){
                checkSafeFiles(child);
            }
        }
    }
    public static boolean isSafeFile(File file){
        return isSafeFileName(file.getName());
    }
    public static boolean isSafeFileName(String name){        
        return safeFileNamePattern.matcher(name).find();        
    }
    public static void main(String...args){
        try{
            checkSafeFiles(new File("/home/njfranck/test/bag-a"));
        }catch(FileNameNotPortableException e){
            e.printStackTrace();
        }
    }
}
