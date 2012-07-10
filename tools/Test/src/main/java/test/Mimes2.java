/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package test;

/**
 *
 * @author nicolas
 */
import java.io.File;
import java.util.ArrayList;
import net.sf.jmimemagic.Magic;
import net.sf.jmimemagic.MagicException;
import net.sf.jmimemagic.MagicMatch;
import net.sf.jmimemagic.MagicMatchNotFoundException;
import net.sf.jmimemagic.MagicParseException;

public class Mimes2 {
    public static void main(String [] args) throws MagicParseException, MagicMatchNotFoundException, MagicException{

        Magic parser = new Magic();

        for(String filename:args){
           ArrayList<File> list = FileUtils.listFiles(filename);

           for(File file:list){
               try{
                    System.out.print(file.getAbsolutePath()+":");
                    MagicMatch match = parser.getMagicMatch(file, true, true);
                    System.out.println(match.getMimeType());
               }catch(Exception e){
                    System.out.println("<unknown>");
               }
           }
       }
    }
}
