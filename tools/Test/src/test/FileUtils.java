/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package test;

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
        public int compare(File f1, File f2){
            return f1.getName().compareToIgnoreCase(f2.getName());
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
        return getTreeNode(file,false,new int [] {0,0});
    }
    public static DefaultMutableTreeNode getTreeNode(File file,boolean sort,int [] stats){
        if(file == null || !file.exists()){
            return null;
        }
        DefaultMutableTreeNode root = new DefaultMutableTreeNode(new FileSource(file));
        if(file.isDirectory()){
            root.setAllowsChildren(true);
            if(stats != null && stats.length == 2)stats[0]++;
            File [] listFiles = file.listFiles();
            if(sort){                
                Arrays.sort(listFiles,defaultFileSorter);               
            }
            for(File f:listFiles){
                root.add(getTreeNode(f,sort,stats));
            }
        }else{
            if(stats != null && stats.length == 2)stats[1]++;
            //prevents the expand sign '+' to appear
            root.setAllowsChildren(false);
        }
        return root;
    }
    public static void walkTree(DefaultMutableTreeNode node,IteratorListener il){
        System.out.println("in tree");
        if(node == null)return;
        System.out.println("after return");
        if(node.isLeaf())il.execute(node.getUserObject());
        else{
            System.out.println("is not a leaf");
            int cc = node.getChildCount();
            for(int i = 0;i < cc;i++){
                walkTree((DefaultMutableTreeNode)node.getChildAt(i),il);
            }
        }
    }
}
