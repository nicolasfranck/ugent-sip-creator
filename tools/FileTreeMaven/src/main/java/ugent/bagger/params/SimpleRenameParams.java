/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package ugent.bagger.params;

import RenameWandLib.ErrorAction;





/**
 *
 * @author nicolas
 */
public class SimpleRenameParams {
    
    private String prefix;
    private String suffix;
    private String name;       
    private int numPadding = 0;
    private int startNumber = 0;
    private int jumpNumber = 1;
    private ErrorAction onErrorAction = ErrorAction.ignore;
    private boolean overWrite = false;
    private boolean number = true;
    private boolean preserveExtension = true;

    public boolean isPreserveExtension() {
        return preserveExtension;
    }
    public void setPreserveExtension(boolean preserveExtension) {
        this.preserveExtension = preserveExtension;
    }
    public boolean isNumber() {
        return number;
    }
    public void setNumber(boolean number) {
        this.number = number;
    }
    public int getJumpNumber() {
        return jumpNumber;
    }

    public void setJumpNumber(int jumpNumber) {
        this.jumpNumber = jumpNumber;
    }

    public int getStartNumber() {
        return startNumber;
    }

    public void setStartNumber(int startNumber) {
        this.startNumber = startNumber;
    }
    
    public ErrorAction getOnErrorAction() {
        return onErrorAction;
    }
    public void setOnErrorAction(ErrorAction onErrorAction) {
        this.onErrorAction = onErrorAction;
    }        
    public boolean isOverWrite() {
        return overWrite;
    }
    public void setOverWrite(boolean overWrite) {
        this.overWrite = overWrite;
    }
    public int getNumPadding() {
        return numPadding;
    }

    public void setNumPadding(int paddingNum) {
        this.numPadding = paddingNum;
    }
    
    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
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
