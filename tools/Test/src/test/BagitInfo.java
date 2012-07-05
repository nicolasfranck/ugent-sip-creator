/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package test;

import gov.loc.repository.bagit.BagFactory;
import gov.loc.repository.bagit.Bag;
import gov.loc.repository.bagit.BagFile;

import java.io.File;

/**
 *
 * @author nicolas
 */
public class BagitInfo {
    public static void usage(){
        System.err.println("usage: java Test.BagitExample <source-1> <source-2> .. <source-n>");
        System.exit(1);
    }
    public static void main(String []args){
        try{
            BagFactory bagFactory = new BagFactory();
            System.out.print("<info>");
            for(String filename:args){
                System.out.print("<bagfile>");
                Bag bag = bagFactory.createBag(new File(filename));           
                System.out.print("<payloads>");
                for(BagFile bf:bag.getPayload()){
                    System.out.print("<payload>");
                    System.out.print("<path>"+bf.getFilepath()+"</path>");
                    System.out.print("<payload>");
                }
                System.out.print("</payloads>");
                //SimpleResult result = bag.verifyComplete();

                System.out.print("</bagfile>");
            }
            System.out.println("</info>");
           
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}