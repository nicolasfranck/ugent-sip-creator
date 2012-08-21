/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package helper;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author nicolas
 */
public class Metadata {
    public static Set<Node> loadSiteMap(String filePath) throws FileNotFoundException, IOException{
        return loadSiteMap(new File(filePath));
    }
    public static Set<Node> loadSiteMap(File file) throws FileNotFoundException, IOException{
        //get/set current name of tab position
        HashMap<Integer,String>tabMap = new HashMap<Integer,String>();

        return readSiteMap(
            new BufferedReader(
                new InputStreamReader(new FileInputStream(file))
            )            
        );
    }
    public static Set<Node> readSiteMap(BufferedReader in) throws IOException{
        Node root = new Node("root");
        String line;
        while((line = in.readLine()) != null){

        }
        return root.getChildren();
    }
    private static int countLeadingChar(String line,char c){
        int pos = 0;
        while(line.charAt(pos) == c){
            pos++;
        }
        return pos;
    }
}
