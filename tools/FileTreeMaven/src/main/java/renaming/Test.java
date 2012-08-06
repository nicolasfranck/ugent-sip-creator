/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package renaming;

import java.util.ArrayList;

/**
 *
 * @author nicolas
 */
public class Test {
    private ArrayList<ArrayList<Object>>data= new ArrayList<ArrayList<Object>>();
    
    public ArrayList<ArrayList<Object>> getData() {
        return data;
    }
    public void setData(ArrayList<ArrayList<Object>> data) {
        System.out.println("setting data!");
        this.data = data;
    }
}
