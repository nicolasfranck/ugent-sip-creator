/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package SimpleRenamerLib;

import RenameWandLib.ErrorAction;
import RenameWandLib.RenameError;
import RenameWandLib.RenameFilePair;
import java.util.ArrayList;

/**
 *
 * @author nicolas
 */
public class RenameListenerAdapter implements RenameListener{
    @Override
    public void onInit(ArrayList<RenameFilePair> pairs) {
    }
    @Override
    public ErrorAction onError(RenameFilePair pair, RenameError errorType, String errorStr) {
        return ErrorAction.ignore;
    }
    @Override
    public void onRenameStart(RenameFilePair pair) {
    }
    @Override
    public void onRenameSuccess(RenameFilePair pair){
    }
    @Override
    public void onRenameEnd(RenameFilePair pair){
    }
    @Override
    public void onEnd(ArrayList<RenameFilePair> pairs, int numSuccess){
    }

}
