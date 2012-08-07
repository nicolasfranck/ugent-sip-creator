/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Params;

import RenameWandLib.ErrorAction;
import java.util.HashMap;

/**
 *
 * @author nicolas
 */
public class CleanParams {
    private HashMap substitutes;
    private boolean cleanDirectories = false;
    private boolean copy = false;
    private ErrorAction onErrorAction;
    private boolean overWrite = false;

    public ErrorAction getOnErrorAction() {
        return onErrorAction;
    }
    public void setOnErrorAction(ErrorAction onErrorAction) {
        this.onErrorAction = onErrorAction;
    }
    public boolean isCleanDirectories() {
        return cleanDirectories;
    }
    public void setCleanDirectories(boolean cleanDirectories) {
        this.cleanDirectories = cleanDirectories;
    }
    public HashMap getSubstitutes() {
        return substitutes;
    }
    public void setSubstitutes(HashMap substitutes) {
        this.substitutes = substitutes;
    }
    public boolean isCopy() {
        return copy;
    }
    public void setCopy(boolean copy) {
        this.copy = copy;
    }
    public boolean isOverWrite() {
        return overWrite;
    }
    public void setOverWrite(boolean overWrite) {
        this.overWrite = overWrite;
    }
}
