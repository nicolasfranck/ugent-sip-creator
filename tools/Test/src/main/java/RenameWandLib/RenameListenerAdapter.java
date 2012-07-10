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
    public boolean approveList(List<FileUnit> list) {
        return true;
    }
    public OnErrorAction onError(RenameFilePair pair, RenameError errorType, String errorStr) {
        return OnErrorAction.ignore;
    }
    public void onRenameStart(RenameFilePair pair){
    }
    public void onRenameEnd(RenameFilePair pair) {
    }
    public void onRenameSuccess(RenameFilePair pair) {
    }
}
