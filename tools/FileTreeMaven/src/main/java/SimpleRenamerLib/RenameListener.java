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
public interface RenameListener{
    public void onInit(final ArrayList<RenameFilePair>pairs);
    ErrorAction onError(final RenameFilePair pair,final RenameError errorType,final String errorStr);
    void onRenameStart(final RenameFilePair pair,int index);
    void onRenameSuccess(final RenameFilePair pair,int index);
    void onRenameEnd(final RenameFilePair pair,int index);    
    void onEnd(final ArrayList<RenameFilePair>pairs,int numSuccess);
}
