/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package RenameWandLib;

import java.util.List;

/**
 *
 * @author nicolas
 */
public class RenameListenerAdapter implements RenameListener{
    @Override
    public void onInit(final List<FileUnit> matchCandidates,final List<FileUnit> matches){
    }
    @Override
    public boolean approveList(List<FileUnit> list) {
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
    public void onEnd(){
    }
   
}
