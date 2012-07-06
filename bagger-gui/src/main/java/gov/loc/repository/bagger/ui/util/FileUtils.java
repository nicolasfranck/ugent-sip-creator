/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.loc.repository.bagger.ui.util;

import java.io.File;
import java.util.ArrayList;
import javax.swing.tree.DefaultMutableTreeNode;
import eu.medsea.mimeutil.MimeUtil;
import java.util.Arrays;
import java.util.Comparator;
/**
 *
 * @author nicolas
 */
public class FileUtils {
    private static Comparator defaultFileSorter =  new Comparator<File>(){
        @Override
        public int compare(File f1, File f2){
            return Long.valueOf(f1.lastModified()).compareTo(f2.lastModified());
        }
    };
    static {
        MimeUtil.registerMimeDetector("eu.medsea.mimeutil.detector.MagicMimeMimeDetector");
    }
    public static ArrayList<File> listFiles(String fn){
        return listFiles(new File(fn));
    }
    public static ArrayList<File> listFiles(File f){
        final ArrayList<File> files = new ArrayList<File>();
        listFiles(
            f,new IteratorListener(){
            @Override
                public void execute(Object o){                   
                    files.add((File)o);
                }
            }
        );
        return files;
    }
    public static void listFiles(String filename,IteratorListener l){
        listFiles(new File(filename),l);
    }
    public static void listFiles(File f,IteratorListener l){
        //must be directory that can be read
        if(!f.isDirectory() || f.listFiles() == null)return;
        File [] listFiles = f.listFiles();
        
        Arrays.sort(listFiles,defaultFileSorter);

        for(File sb:listFiles){
            if(sb.isDirectory()){
                listFiles(sb,l);
            }else if(sb.isFile()){
                l.execute((Object)sb);
            }
        }        
    }
    public static DefaultMutableTreeNode getTreeNode(File file){
        if(file == null || !file.exists()){
            return null;
        }
        DefaultMutableTreeNode root = new DefaultMutableTreeNode(new FileSource(file));
        if(file.isDirectory() /*&& file.listFiles() != null*/){
            root.setAllowsChildren(true);
            for(File f:file.listFiles()){                
                root.add(getTreeNode(f));
            }
        }else{
            //prevents the expand sign '+' to appear
            root.setAllowsChildren(false);
        }
        return root;
    }
    public static void walkTree(DefaultMutableTreeNode node,IteratorListener il){
        if(node == null)return;
        if(node.isLeaf())il.execute(node.getUserObject());
        else{
            int cc = 0;
            for(int i = 0;i < cc;i++){
                walkTree((DefaultMutableTreeNode)node.getChildAt(i),il);
            }
        }
    }
}
