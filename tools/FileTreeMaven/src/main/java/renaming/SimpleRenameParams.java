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
public class SimpleRenameParams {
    
    private String prefix;
    private String suffix;
    private String name;
    private String extension;    
    private boolean renumber;
    private int paddingNum;    

    private boolean recurseIntoSubdirectories = false;
    private boolean copy = false;
    private OnErrorAction onErrorAction;    
    private boolean matchLowerCase = false;
    private boolean overWrite = false;
    
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

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public int getPaddingNum() {
        return paddingNum;
    }

    public void setPaddingNum(int paddingNum) {
        this.paddingNum = paddingNum;
    }
    
    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public boolean isRenumber() {
        return renumber;
    }

    public void setRenumber(boolean renumber) {
        this.renumber = renumber;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}
