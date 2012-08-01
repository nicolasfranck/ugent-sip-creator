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
public class CleanListenerAdapter implements CleanListener{
    @Override
    public boolean doClean(File from, File to) {
        return true;
    }
    @Override
    public OnErrorAction onError(File from, File to, RenameError errorType, String errorStr) {
        return OnErrorAction.skip;
    }
    @Override
    public void onRenameStart(File from, File to) {        
    }
    @Override
    public void onRenameSuccess(File from, File to) {        
    }
    @Override
    public void onRenameEnd(File from, File to) {        
    }
    @Override
    public void onEnd(File [] files) {
    }
    @Override
    public void onInit(File [] files) {
    }
}
