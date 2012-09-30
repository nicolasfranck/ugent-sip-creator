/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ugent.bagger.helper;

import java.util.ArrayList;

/**
 *
 * @author nicolas
 */
public class DNode extends FNode{
    private ArrayList<FNode>children = new ArrayList<FNode>();
    public DNode(String name){
        super(name);
    }
    @Override
    public ArrayList<FNode>getChildren(){
        return children;
    }
    @Override
    public boolean hasChildren(){
        return true;
    }
}