/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package renamewand;

import java.util.List;

/**
 *
 * @author nicolas
 */
public interface RenameListener {
    /*
     * before operation starts: list of fileunits (source and target file)
     * return boolean to indicate your approval
     */
    boolean approveList(final List<FileUnit> list);
    /*
     * rename operation failed
     * @param pair
     *      object that contains source and target file
     * @param errorType
     *      kind of error that happened
     * @param errorStr
     *      error message (sometimes null)
     */
    OnRenameOperationError onError(final RenameFilePair pair,final RenameError errorType,final String errorStr);
    /*
     * @param pair
     *  object that contains source and target file
     */
    void onRenameStart(final RenameFilePair pair);
    void onRenameEnd(final RenameFilePair pair);
}
