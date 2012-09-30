/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ugent.bagger.helper;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author nicolas
 */
public class NodeUtils {
    public static List<FNode> getStructureFromList(List<String>list){        
        ArrayList<FNode>structuredList = new ArrayList<FNode>();
        
        for(String entry:list){
            int pos = entry.indexOf(File.separator);
            if(pos >= 0){
                String dnodeName = entry.substring(0,pos);
                DNode dnode = new DNode(dnodeName);
            }else{
                structuredList.add(new FNode(entry));
            }
        }
        
        return structuredList;
    }
}
