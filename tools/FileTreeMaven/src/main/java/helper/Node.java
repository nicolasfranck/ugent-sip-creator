package helper;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
public final class Node {
    private Object object;
    private HashSet<Node>children = new HashSet<Node>();
    private Node parent;
    private int hashCode = 0;
    
    public static int calculateHashCode(Node node){
        int h = 0;
        String s = node.getObject().toString();
        for(int i = 0;i<s.length();i++)
            h += s.charAt(i);
        return h;
    }
   
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
        this.hashCode = calculateHashCode(this);
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
    public void addPath(Path path){
        addPath(path.getComponents());
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
    public Path getPath(){
        ArrayList<Object>objects = new ArrayList<Object>();
        Node parentNode = this;
        do{
            objects.add(parentNode.getObject());
            parentNode = parentNode.getParent();
        }while(parentNode != null);
        //geef beter omgekeerde lijst terug!!!
        Collections.reverse(objects);        
        return new Path(objects.toArray());
    }    
    public static void main(String [] args){
        if(args.length < 1)return;
        try{
            //write code
            /*
            Node node = listFiles(new File(args[0]));
            writeNode(node);
            */
            //write code
            ArrayList<ArrayList<String>>list = readStructure(new File(args[0]));
            
            Node node = null;
            for(ArrayList<String>paths:list){
                System.out.println(join(paths.toArray(),"/"));
                //node = pathToNode(node,paths.toArray());
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
        if(node == null)return;
        Path path = node.getPath();
        System.out.println(join(path.getComponents(),"/"));
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
    public static ArrayList<ArrayList<String>> readStructure(File file) throws FileNotFoundException, IOException, NodeException{
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
        String line = null;
        String parent = null;
        int prevNumTabs = 0;
        ArrayList<String>paths = new ArrayList<String>();
        ArrayList<ArrayList<String>>list = new ArrayList<ArrayList<String>>();
        int lineNo = 0;
        while((line = reader.readLine()) != null){
            int numTabs = numLeadingChars(line,'\t');
            
            if(Math.abs(numTabs - prevNumTabs) > 1){
                throw new NodeException("invalid tab indentation at line "+lineNo);
            }
            
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
                paths.add(name);
            }
            //list.add(paths);            
            ArrayList<String>copy = new ArrayList<String>(paths.size());
            for(String s:paths)
                copy.add(s);
            list.add(copy);
            prevNumTabs = numTabs;
            lineNo++;
        }
        return list;
    }
    @Override
    public String toString(){
        return object.toString();
    }
    @Override
    public boolean equals(Object o){        
        return object.toString().compareTo(o.toString()) == 0;
    }
    @Override
    public int hashCode(){
        return hashCode;
    } 
}
