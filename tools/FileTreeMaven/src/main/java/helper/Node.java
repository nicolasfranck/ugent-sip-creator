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
    public static void writeNode(Node node) throws NodeException{
        writeNode(node,'\t',0);
    }
    public static void writeNode(Node node,char whiteSpaceChar) throws NodeException{
        writeNode(node,whiteSpaceChar,0);
    }
    public static void writeNode(Node node,char whiteSpaceChar,int tabs) throws NodeException{
        if(!Character.isWhitespace(whiteSpaceChar))throw new NodeException("invalid whitespace character '"+whiteSpaceChar+"'");

        for(int i = 0;i < Math.abs(tabs);i++)
            System.out.print(whiteSpaceChar);
        System.out.println(node.getObject());
        if(node.getChildren().size() > 0){
            for(Node n:node.getChildren()){
                writeNode(n,whiteSpaceChar,tabs + 1);
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
    
    private static boolean hasLeadingWhiteSpace(String line){
        return Character.isWhitespace(line.charAt(0));
    }    
    public static ArrayList<Path> readStructure(File file) throws FileNotFoundException, IOException, NodeException{
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
        String line = null;
        String parent = null;
        int prevNumTabs = 0;
        ArrayList<String>paths = new ArrayList<String>();
        ArrayList<Path>list = new ArrayList<Path>();
        int lineNo = 0;
        char whiteSpaceChar = 0;
        while((line = reader.readLine()) != null){

            int numTabs = 0;

            if(lineNo == 0 && hasLeadingWhiteSpace(line)){
                throw new NodeException("Node structure cannot start with a whitespace!");
            }else if(lineNo > 0 && whiteSpaceChar == 0 && hasLeadingWhiteSpace(line)){
                whiteSpaceChar = line.charAt(0);
                numTabs = numLeadingChars(line,whiteSpaceChar);
            }else{
                numTabs = numLeadingChars(line,whiteSpaceChar);
            }            
            
            /*
             *  first (0 tabs)
             *          second (2 tabs)
             */
            if(
                numTabs > prevNumTabs &&
                Math.abs(numTabs - prevNumTabs) > 1
            ){
                throw new NodeException("invalid indentation at line "+lineNo);
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
            list.add(new Path(copy.toArray()));
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
    @SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
    public boolean equals(Object o){        
        return object.toString().compareTo(o.toString()) == 0;
    }
    /*
     * Belangrijk voor unieke key in set..
     */
    @Override
    public int hashCode(){
        return object.toString().hashCode();
    }
    /*
    public static void main(String [] args) throws FileNotFoundException{
        if(args.length < 1)return;
        try{
            if(args[0].equals("-i")){
                if(args.length < 2)return;
                if(args.length >= 3)
                    System.setOut(new PrintStream(new File(args[2])));

                //read file structure and write out
                Node node = listFiles(new File(args[1]));
                writeNode(node,'\t');
            }else if(args[0].equals("-r")){
                if(args.length < 2)return;
                if(args.length >= 3)
                    System.setOut(new PrintStream(new File(args[2])));

                //write code
                ArrayList<Path>list = readStructure(new File(args[1]));
                Node node = null;
                for(Path path:list){
                    System.out.println(join(path.getComponents(),"/"));
                }
                walkNode(node);
            }

        }catch(Exception e){
            e.printStackTrace();
        }
    }
     *
     */
}
