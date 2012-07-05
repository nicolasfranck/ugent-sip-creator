/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package test;

import java.net.URL;

/**
 *
 * @author nicolas
 */
public class XSLTExample2 {
    public static void main(String []args) throws Exception{
        if(args.length < 2)return;        
        helper.XSLT.transform(new URL("file://"+args[0]),new URL("file://"+args[1]),System.out);
    }
}
