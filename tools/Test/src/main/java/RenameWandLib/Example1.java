/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package RenameWandLib;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author nicolas
 */
public class Example1 {
    public static void main(String [] args){
        try{
            RenameWand renamer = new RenameWand();
            renamer.setCurrentDirectory(new java.io.File("/home/nicolas/bhsl-pap"));
            renamer.setRecurseIntoSubdirectories(true);
            renamer.setSourcePatternString("<prefix>_<year>_<seq>_AC_thumbnail.jpeg");
            renamer.setTargetPatternString("<prefix>_<year>_<4|seq>_AC_thumbnail.jpg");
            renamer.setSimulateOnly(true);
            renamer.setRenameListener(new RenameListenerAdapter(){
                @Override
                public OnErrorAction onError(RenameFilePair pair, RenameError errorType, String errorStr) {
                    if(errorStr != null)System.err.println(errorStr);
                    System.out.println("mv "+pair.getSource().getAbsolutePath()+" to "+pair.target.getAbsolutePath()+" failed");
                    System.out.println("undoing all operations!");
                    System.out.println(errorType+":'"+errorStr+"'");
                    return OnErrorAction.undoAll;
                }
                @Override
                public void onRenameStart(RenameFilePair pair) {
                    try {
                        System.out.println("renaming " + pair.getSource().getCanonicalPath() + " to " + pair.getTarget().getCanonicalPath());
                    } catch (IOException ex) {
                        Logger.getLogger(RenameWand.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                @Override
                public void onRenameEnd(RenameFilePair pair) {
                    try {
                        System.out.println("renaming " + pair.getSource().getCanonicalPath() + " to " + pair.getTarget().getCanonicalPath()+" DONE!");
                    } catch (IOException ex) {
                        Logger.getLogger(RenameWand.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            });
            renamer.rename();
            System.out.println(renamer.getNumRenameOperationsPerformed()+" rename actions performed!");
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
