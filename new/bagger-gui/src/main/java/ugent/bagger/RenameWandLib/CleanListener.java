/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ugent.bagger.RenameWandLib;

import java.util.ArrayList;

/**
 *
 * @author nicolas
 */
public interface CleanListener{
    void onInit(final ArrayList<RenameFilePair> pairs);
    boolean doClean(final RenameFilePair pair);
    ErrorAction onError(final RenameFilePair pair,RenameError errorType,final String errorStr);
    void onCleanStart(final RenameFilePair pair);
    void onCleanSuccess(final RenameFilePair pair);
    void onCleanEnd(final RenameFilePair pair);
    void onEnd(final ArrayList<RenameFilePair> pairs,int numSuccess);
}
