/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package test;

import com.anearalone.mets.*;
import com.anearalone.mets.FileSec.*;
import com.anearalone.mets.FileSec.FileGrp.*;
import com.anearalone.mets.FileSec.FileGrp.File.*;
import com.anearalone.mets.StructMap;
import com.anearalone.mets.StructMap.Div.Fptr;
import java.io.FileInputStream;
import java.io.InputStream;

/**
 *
 * @author nicolas
 */
public class MetsApiGit1 {
    public static void main(String []args){
        if(args.length < 1)System.exit(1);
        try{
            InputStream in = new FileInputStream(args[0]);
            MetsReader mr = new MetsReader();
            Mets mets = mr.read(in);
            
            FileSec fileSec = mets.getFileSec();
            for(FileGrp fileGrp:fileSec.getFileGrp()){
                for(File metsFile:fileGrp.getFile()){     
                    for(FLocat floc:metsFile.getFLocat()){
                        /*System.out.println(
                            fileGrp.getUse()+
                            " "
                            +
                            metsFile.getSEQ()
                            +
                            " "
                            +
                            floc.getXlinkHREF()
                            +
                            "( FILEID = '"
                            +
                            metsFile.getID()
                            +
                            "')"
                        );*/
                    }                    
                }
            }
            StructMap sm = mets.getStructMap().get(0);
            for(Fptr p:sm.getDiv().getFptr()){
               p.setFILEID("test");
            }
            /*MetsWriter mw = new MetsWriter();
            mw.writeToOutputStream(mets,System.out);*/
            
        }catch(Exception e){
            e.printStackTrace();
        }

    }
}
