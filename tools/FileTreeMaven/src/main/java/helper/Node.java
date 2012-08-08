/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package helper;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author nicolas
 */
public final class Node {
    private Object object;
    private HashSet<Node>children = new HashSet<Node>();
    private Node parent;
   
    public Node(Object object){
        this(object,null);
    }
    public Node(Object object,Node parent){
        setObject(object);
        setParent(parent);
    }
    public Object getObject() {
        return object;
    }
    public void setObject(Object object) {
        this.object = object;
    }
    public Node getParent() {
        return parent;
    }
    public void setParent(Node parent) {
        this.parent = parent;
    }
    public boolean isRoot(){
        return parent == null;
    }
    public boolean isLeaf(){
        return children.isEmpty();
    }
    public Set<Node> getChildren() {
        return children;
    }
    public void setChildren(HashSet<Node> children) {
        this.children = children;
    }
    public void addChild(Node node){
        children.add(node);
    }
    public void addChildren(ArrayList<Node>nodes){
        children.addAll(nodes);
    }
    public void addPath(Object [] objects){
        pathToNode(this,objects);       
    }   
    public static Node pathToNode(Node node,Object [] objects){
        Node lastNode = node;
        for(Object o:objects){            
            if(lastNode == node){
                lastNode = new Node(o,node);
                node = lastNode;
            }else{
                Node currentNode = new Node(o,lastNode);
                lastNode.addChild(currentNode);
                lastNode = currentNode;
            }            
        }        
        return node;
    }
    public ArrayList<Object>getPath(){
        ArrayList<Object>objects = new ArrayList<Object>();
        Node parentNode = this;
        do{
            objects.add(parentNode.getObject());
            parentNode = parentNode.getParent();
        }while(parentNode != null);
        //geef beter omgekeerde lijst terug!!!
        Collections.reverse(objects);
        return objects;
    }
    @Override
    public String toString(){
        return getPath().toString();
    }
    public static void main(String [] args){
        if(args.length < 1)return;
        try{
            ArrayList<ArrayList<String>>list = readStructure(new File(args[0]));
            Node node = null;
            for(ArrayList<String>paths:list){
                node = pathToNode(node,paths.toArray());
            }
            walkNode(node);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public static Node listFiles(File file){
        return listFiles(file,null);
    }
    private static Node listFiles(File file,Node parent){
        Node node = new Node(file.getName(),parent);
        if(file.isDirectory()){
            for(File f:file.listFiles()){                
                Node child = listFiles(f,node);
                node.addChild(child);
            }
        }
        return node;
    }
    public static String join(Object [] objects,String delimiter){
        String result = "";
        for(int i = 0;i < objects.length;i++){
            if(i < objects.length - 1){
                result += objects[i]+delimiter;
            }else{
                result += objects[i];
            }
        }
        return result;
    }
    public static void walkNode(Node node){
        ArrayList<Object> objects = node.getPath();
        System.out.println(join(objects.toArray(),"/"));
        if(!node.isLeaf()){
            for(Node n:node.getChildren()){
                walkNode(n);
            }
        }
    }    
    public static void writeNode(Node node){
        writeNode(node,0);
    }
    public static void writeNode(Node node,int tabs){        
        for(int i = 0;i < Math.abs(tabs);i++)
            System.out.print('\t');
        System.out.println(node.getObject());
        if(node.getChildren().size() > 0){
            for(Node n:node.getChildren()){
                writeNode(n,tabs + 1);
            }
        }
    }
    public static int numLeadingChars(String line,char c){
        int pos = 0;
        while(line.charAt(pos) == c)pos++;           
        return pos;
    }
    public static Node getRootNode(Node node){
        while(!node.isRoot()){
            node = node.getParent();
        }
        return node;
    }
    public static ArrayList<ArrayList<String>> readStructure(File file) throws FileNotFoundException, IOException{
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
        String line = null;
        String parent = null;
        int prevNumTabs = 0;
        ArrayList<String>paths = new ArrayList<String>();
        ArrayList<ArrayList<String>>list = new ArrayList<ArrayList<String>>();
        while((line = reader.readLine()) != null){
            int numTabs = numLeadingChars(line,'\t');
            String name = line.trim();
            if(numTabs == prevNumTabs){
                if(paths.size() > 0)
                    paths.remove(paths.size()-1);
                paths.add(name);
            }else if(numTabs > prevNumTabs){
                paths.add(name);
            }else{
                int numChop = prevNumTabs - numTabs;
                for(int i = 0;i<numChop;i++){
                    paths.remove(paths.size() - 1);
                }
            }
            list.add(paths);
            //System.out.println(list.get(list.size()-1));
            prevNumTabs = numTabs;
        }
        return list;
    }
}
