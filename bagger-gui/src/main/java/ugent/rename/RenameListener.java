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
public interface RenameListener{   
    boolean approveList(final ArrayList<RenameFilePair> list);
    /*
     * rename operation failed
     * @param pair
     *      object that contains source and target file
     * @param errorType
     *      kind of error that happened
     * @param errorStr
     *      error message (sometimes null)
     */
    ErrorAction onError(final RenameFilePair pair,final RenameError errorType,final String errorStr,int index);
    /*
     * @param pair
     *  object that contains source and target file
     */
    void onRenameStart(final RenameFilePair pair,int index);
    void onRenameSuccess(final RenameFilePair pair,int index);
    void onRenameEnd(final RenameFilePair pair,int index);
    /*
     *  after end of operation
     */
    void onEnd(final ArrayList<RenameFilePair> list,int numSuccess);
}
