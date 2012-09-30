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
public class FNode {
    private String name;
    public FNode(String name){
        this.name = name;
    }
    public ArrayList<FNode>getChildren(){
        return null;
    }
    public boolean hasChildren(){
        return false;
    }
}
