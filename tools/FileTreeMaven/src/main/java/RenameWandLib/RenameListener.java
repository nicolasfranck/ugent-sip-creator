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
public interface RenameListener{
    public void onInit(final ArrayList<FileUnit> matchCandidates,final ArrayList<FileUnit> matches);
    /*
     * before operation starts: list of fileunits (source and target file)
     * return boolean to indicate your approval
     */
    boolean approveList(final ArrayList<FileUnit> list);
    /*
     * rename operation failed
     * @param pair
     *      object that contains source and target file
     * @param errorType
     *      kind of error that happened
     * @param errorStr
     *      error message (sometimes null)
     */
    OnErrorAction onError(final RenameFilePair pair,final RenameError errorType,final String errorStr);
    /*
     * @param pair
     *  object that contains source and target file
     */
    void onRenameStart(final RenameFilePair pair);
    void onRenameSuccess(final RenameFilePair pair);
    void onRenameEnd(final RenameFilePair pair);
    /*
     *  after end of operation
     */
    void onEnd(final ArrayList<RenameFilePair> list,int numSuccess);
}
