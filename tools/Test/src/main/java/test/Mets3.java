/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package test;
//group-id: org.dspace
//artifact-id:mets
import edu.harvard.hul.ois.mets.*;
import edu.harvard.hul.ois.mets.helper.*;
import edu.harvard.hul.ois.mets.helper.MetsReader;
import java.io.*;

/**
 *
 * @author nicolas
 */
public class Mets3 {
    public static void main(String []args){
        for(String file:args){
            try{
                FileInputStream in = new FileInputStream (file);
                Mets mets = Mets.reader(new MetsReader (in, false));
                in.close ();
                mets.validate (new MetsValidator ());
                System.out.println(file+" validates successfully");
            }catch(Exception e){
                System.out.println(file+" does not validate");
                e.printStackTrace();
            }
        }
    }
}
