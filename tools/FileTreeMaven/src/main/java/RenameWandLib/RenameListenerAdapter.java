/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package RenameWandLib;

import java.util.ArrayList;



/**
 *
 * @author nicolas
 */
public class RenameListenerAdapter implements RenameListener{
    @Override
    public void onInit(final ArrayList<FileUnit> matchCandidates,final ArrayList<FileUnit> matches){
    }
    @Override
    public boolean approveList(ArrayList<FileUnit> list) {
        return true;
    }
    @Override
    public OnErrorAction onError(RenameFilePair pair, RenameError errorType, String errorStr) {
        return OnErrorAction.ignore;
    }
    @Override
    public void onRenameStart(RenameFilePair pair){
    }
    @Override
    public void onRenameEnd(RenameFilePair pair) {
    }
    @Override
    public void onRenameSuccess(RenameFilePair pair) {
    }
    @Override
    public void onEnd(final ArrayList<RenameFilePair> list,int numSuccess){
    }
   
}
