/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package RenameWandLib;

import java.io.File;

/**
 *
 * @author nicolas
 */
public interface CleanListener{
    void onInit(File [] files);
    boolean doClean(final File from,final File to);
    OnErrorAction onError(final File from,final File to,RenameError errorType,final String errorStr);    
    void onRenameStart(final File from,final File to);
    void onRenameSuccess(final File from,final File to);
    void onRenameEnd(final File from,final File to);
    void onEnd(File [] files);
}
