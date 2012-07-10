/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package test;

import java.io.*;
import au.edu.apsr.mtk.base.*;
import au.edu.apsr.mtk.ch.*;
import org.w3c.dom.Document;

/**
 *
 * @author nicolas
 */
public class Mets1 {
    public static void main(String []args){
        if(args.length < 1)System.exit(1);
        try{
            System.out.println("file => "+args[0]);
            //input
            InputStream in = new FileInputStream(args[0]);            
            METSReader mr = new METSReader();
            mr.mapToDOM(in);
            Document doc = mr.getMETSDocument();
            
            METSWrapper mw = new METSWrapper(doc);
            //valideer tegen nieuwste schema
            mw.validate("file:///home/nicolas/mets/mets.xsd");
            METS mets = mw.getMETSObject();
            System.out.println("Package Type of " + mets.getType() + ", using profile: " + mets.getProfile());
            
        }catch(Exception e){
            e.printStackTrace();           
        }
    }
}
