/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Params;

import RenameWandLib.ErrorAction;

/**
 *
 * @author nicolas
 */
public class AdvancedRenameParams {

    private String sourcePattern;    
    private String destinationPattern;
    private boolean recurseIntoSubdirectories = true;
    private boolean renameDirectories = false;
    private boolean copy = false;
    private ErrorAction onErrorAction;
    private boolean matchLowerCase = false;
    private boolean overWrite = false;

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
    public ErrorAction getOnErrorAction() {
        return onErrorAction;
    }
    public void setOnErrorAction(ErrorAction onErrorAction) {
        this.onErrorAction = onErrorAction;
    }    
    public boolean isMatchLowerCase() {
        return matchLowerCase;
    }
    public void setMatchLowerCase(boolean matchLowerCase) {
        this.matchLowerCase = matchLowerCase;
    }
    public boolean isOverWrite() {
        return overWrite;
    }
    public void setOverWrite(boolean overWrite) {
        this.overWrite = overWrite;
    }

    public boolean isRenameDirectories() {
        return renameDirectories;
    }

    public void setRenameDirectories(boolean renameDirectories) {
        this.renameDirectories = renameDirectories;
    }    

}
