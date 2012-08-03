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
public class CleanListenerAdapter implements CleanListener{
    @Override
    public boolean doClean(final RenameFilePair pair) {
        return true;
    }
    @Override
    public OnErrorAction onError(final RenameFilePair pair, RenameError errorType, String errorStr) {
        return OnErrorAction.skip;
    }
    @Override
    public void onCleanStart(final RenameFilePair pair) {
    }
    @Override
    public void onCleanSuccess(final RenameFilePair pair) {
    }
    @Override
    public void onCleanEnd(final RenameFilePair pair) {
    }
    @Override
    public void onEnd(final ArrayList<RenameFilePair> pairs,int numSuccess) {
    }
    @Override
    public void onInit(final ArrayList<RenameFilePair> pairs) {
    }
}
