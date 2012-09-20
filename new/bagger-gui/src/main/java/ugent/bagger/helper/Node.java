package ugent.bagger.helper;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
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
        node.setParent(this);
    }
    public void addChildren(ArrayList<Node>nodes){
        children.addAll(nodes);
    }
    public void addPath(Path path){
        addPath(path.getComponents());
    }
    public void addPath(Object [] objects){
        addPathToNode(this,objects);
    }    
    public static Node pathToNode(Path path){
        return addPathToNode(null,path.getComponents());
    }
    public static Node pathToNode(Object [] objects){
        return addPathToNode(null,objects);
    }
    public static Node addPathToNode(Node node,Object [] objects){
        return addPathToNode(node,objects,0);
    }
    private static Node addPathToNode(Node node,Object [] objects,int offset){
        if(objects == null || objects.length == 0 || offset >= objects.length){            
            return null;
        }        
        Node newNode = new Node(objects[offset]);
        if(node != null){   
            //contains is implements as nodeA.getObject.equals(nodeB.getObject())            
            Node find = null;
            Iterator<Node>it = node.getChildren().iterator();
            while(it.hasNext()){
                Node n = it.next();
                if(n.equals(newNode)){                    
                    find = n;
                    break;
                }
            }            
            if(find == null){                
                node.addChild(newNode);  
                addPathToNode(newNode,objects,offset+1);
            }else{                
                addPathToNode(find,objects,offset+1);
            }            
        }else{
            addPathToNode(newNode,objects,offset+1);
        }                        
        return newNode;        
    }          
    public Path getPath(){
        ArrayList<Object>objects = new ArrayList<Object>();
        Node parentNode = this;
        do{
            objects.add(parentNode.getObject());
            parentNode = parentNode.getParent();
        }while(parentNode != null);        
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
    public static void writeNode(Node node) throws NodeException, IOException{        
        writeNode(new PrintWriter(System.out),node);
    }
    public static void writeNode(PrintWriter writer,Node node) throws NodeException, IOException{        
        writeNode(writer,node,'\t',0);
    }
    public static void writeNode(Node node,char whiteSpaceChar) throws NodeException, IOException{
        writeNode(new PrintWriter(System.out),node,whiteSpaceChar,0);
    }    
    public static void writeNode(PrintWriter writer,Node node,char whiteSpaceChar) throws NodeException, IOException{
        writeNode(writer,node,whiteSpaceChar,0);
    }
    public static void writeNode(Writer writer,Node node,char whiteSpaceChar,int tabs) throws NodeException, IOException{
        if(!Character.isWhitespace(whiteSpaceChar)) {
            throw new NodeException("invalid whitespace character '"+whiteSpaceChar+"'");
        }        
        for(int i = 0;i < Math.abs(tabs);i++) {
            writer.write(whiteSpaceChar);         
        }        
        writer.write(node.getObject()+"\n");        
        if(node.getChildren().size() > 0){            
            for(Node n:node.getChildren()){
                writeNode(writer,n,whiteSpaceChar,tabs + 1);                
            }
        }       
    }
    private static int numLeadingChars(String line,char c){
        int pos = 0;
        while(line.charAt(pos) == c) {
            pos++;
        }           
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
    public static ArrayList<Path> structureToList(File file) throws FileNotFoundException, IOException, NodeException{
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
        String line;        
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
                if(paths.size() > 0) {
                    paths.remove(paths.size()-1);
                }
                paths.add(name);
            }else if(numTabs > prevNumTabs){
                paths.add(name);
            }else{                
                int numChop = prevNumTabs - numTabs + 1;
                
                for(int i = 0;i<numChop;i++){
                    paths.remove(paths.size() - 1);
                }
                paths.add(name);
            }                      
            ArrayList<String>copy = new ArrayList<String>(paths.size());
            for(String s:paths) {
                copy.add(s);
            }
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
    
    public static void main(String [] args){        
        
         
        PrintWriter writer1 = null;
        PrintWriter writer2 = null;
        try{           
            final Node node = new Node(".");
            //write
            System.out.println("writing..");
            ugent.bagger.helper.FUtils.listFiles(new File("/usr/local/share"),new IteratorListener(){
            @Override
            public void execute(Object o) {
                    File file = (File)o;                    
                    node.addPath(file.getAbsolutePath().substring(1).split("/"));
                }
            });            
            
            System.setErr(new PrintStream(new File("/tmp/error.txt")));
            writer1 = new PrintWriter(new FileWriter(new File("/tmp/output.txt"),true));
            
            for(Node n:node.getChildren()){
                writeNode(writer1,n,' ');
            }      
            
            //and read again
            System.out.print("and rewriting..");
            node.getChildren().clear();
            ArrayList<Path>paths = structureToList(new File("/tmp/output.txt"));
            for(Path path:paths){
                node.addPath(path);
            }
            writer2 = new PrintWriter(new FileWriter(new File("/tmp/output2.txt"),true));
            for(Node n:node.getChildren()){
                writeNode(writer2,n,' ');
            }
            
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            if(writer1 != null){
                try{
                    //important: otherwise the last part will never be flushed
                    writer1.close();
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
            if(writer2 != null){
                try{
                    //important: otherwise the last part will never be flushed
                    writer2.close();
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        }
    }
}