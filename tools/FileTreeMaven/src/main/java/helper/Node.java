/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package helper;

import java.util.ArrayList;
import java.util.Collections;

/**
 *
 * @author nicolas
 */
public class Node {
    private Object object;
    private ArrayList<Node>children = new ArrayList<Node>();
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
        return children.size() == 0;
    }
    public ArrayList<Node> getChildren() {
        return children;
    }
    public void setChildren(ArrayList<Node> children) {
        this.children = children;
    }
    public void addChild(Node node){
        children.add(node);
    }
    public void addChildren(ArrayList<Node>nodes){
        children.addAll(nodes);
    }
    public void addPath(Object [] objects){
        Node newNode = Node.pathToNode(objects);
        if(newNode != null)
            children.add(newNode);
    }
    public static Node pathToNode(Object [] objects){
        Node firstNode = null;
        Node lastNode = firstNode;
        for(Object o:objects){            
            if(lastNode == null){
                lastNode = new Node(o);
                firstNode = lastNode;
            }else{
                Node currentNode = new Node(o,lastNode);
                lastNode.addChild(currentNode);
                lastNode = currentNode;
            }            
        }        
        return firstNode;
    }
    public ArrayList<Object>getPath(){
        ArrayList<Object>objects = new ArrayList<Object>();
        Node parentNode = this;
        do{
            objects.add(parentNode.getObject());
            parentNode = parentNode.getParent();
            System.out.println("parentNode now:"+parentNode);
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
        Node node = Node.pathToNode(new String [] {"home","nicolas","brol"});
        System.out.println(node.getChildren().size());
        walkNode(node);
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
        for(Object o:objects){
            System.out.println(" => "+o);
        }
        System.out.println(join(node.getPath().toArray(),"/"));
        if(!node.isLeaf()){
            for(Node n:node.getChildren()){
                System.out.println(join(node.getPath().toArray(),"/"));
                walkNode(n);
            }
        }
    }
}
