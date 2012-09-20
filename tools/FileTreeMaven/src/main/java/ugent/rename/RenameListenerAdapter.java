/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ugent.rename;

import java.util.ArrayList;



/**
 *
 * @author nicolas
 */
public class RenameListenerAdapter implements RenameListener{  
    @Override
    public boolean approveList(ArrayList<RenameFilePair> list) {
        return true;
    }
    @Override
    public ErrorAction onError(RenameFilePair pair, RenameError errorType, String errorStr,int index) {
        return ErrorAction.ignore;
    }
    @Override
    public void onRenameStart(RenameFilePair pair,int index){
    }
    @Override
    public void onRenameEnd(RenameFilePair pair,int index) {
    }
    @Override
    public void onRenameSuccess(RenameFilePair pair,int index) {
    }
    @Override
    public void onEnd(final ArrayList<RenameFilePair> list,int numSuccess){
    }
   
}
