/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package renaming;

import java.util.HashMap;

/**
 *
 * @author nicolas
 */
public class CleanParams {
    private HashMap substitutes;
    private boolean cleanDirectories = false;
    private boolean copy = false;

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
}
