/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package renaming;

import RenameWandLib.OnErrorAction;

/**
 *
 * @author nicolas
 */
public class RenameParams {

    private String sourcePattern;    
    private String destinationPattern;
    private boolean recurseIntoSubdirectories = false;
    private boolean copy = false;
    private OnErrorAction onErrorAction;
    private boolean renameDirectories = false;
    private boolean matchLowerCase = false;

    public String getDestinationPattern() {
        return destinationPattern;
    }
    public void setDestinationPattern(String destinationPattern) {
        this.destinationPattern = destinationPattern;
    }
    public String getSourcePattern() {
        return sourcePattern;
    }
    public void setSourcePattern(String sourcePattern) {
        this.sourcePattern = sourcePattern;
    }
    public boolean isRecurseIntoSubdirectories() {
        return recurseIntoSubdirectories;
    }
    public void setRecurseIntoSubdirectories(boolean recurseIntoSubdirectories) {
        this.recurseIntoSubdirectories = recurseIntoSubdirectories;
    }
    public boolean isCopy() {
        return copy;
    }
    public void setCopy(boolean copy) {
        this.copy = copy;
    }
    public OnErrorAction getOnErrorAction() {
        return onErrorAction;
    }
    public void setOnErrorAction(OnErrorAction onErrorAction) {
        this.onErrorAction = onErrorAction;
    }
    public boolean isRenameDirectories() {
        return renameDirectories;
    }
    public void setRenameDirectories(boolean renameDirectories) {
        this.renameDirectories = renameDirectories;
    }
    public boolean isMatchLowerCase() {
        return matchLowerCase;
    }
    public void setMatchLowerCase(boolean matchLowerCase) {
        this.matchLowerCase = matchLowerCase;
    }    
}
