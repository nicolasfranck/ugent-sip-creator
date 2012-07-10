/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package test;

/**
 *
 * @author nicolas
 */
public class Tester {
    public static void main(String []args){        
        try{
            XSLTExample3.main(
                new String [] {
                    "/home/nicolas/mods.xml",
                    "/home/nicolas/mods2dc_pp.xsl"
                }
            );
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
