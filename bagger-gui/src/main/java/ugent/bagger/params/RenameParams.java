package ugent.bagger.params;

import ugent.rename.ErrorAction;

/**
 *
 * @author nicolas
 */
public class RenameParams {
    String source;    
    String destination;
    boolean copy = false;
    boolean regex = false;
    ErrorAction onErrorAction = ErrorAction.undoAll;
    boolean ignoreCase = false;
    boolean overWrite = false;
    boolean simulateOnly = false;
    boolean renameExtension = false;
    String prefix;
    String postfix;

    public boolean isRenameExtension() {
        return renameExtension;
    }

    public void setRenameExtension(boolean renameExtension) {
        this.renameExtension = renameExtension;
    }
    public String getPrefix() {
        return prefix;
    }
    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }
    public String getPostfix() {
        return postfix;
    }
    public void setPostfix(String postfix) {
        this.postfix = postfix;
    }
    public boolean isRegex() {
        return regex;
    }
    public void setRegex(boolean regex) {
        this.regex = regex;
    }
    public boolean isSimulateOnly() {
        return simulateOnly;
    }
    public void setSimulateOnly(boolean simulateOnly) {
        this.simulateOnly = simulateOnly;
    }    
    public boolean isIgnoreCase() {
        return ignoreCase;
    }
    public void setIgnoreCase(boolean ignoreCase) {
        this.ignoreCase = ignoreCase;
    }
    public boolean isCopy() {
        return copy;
    }
    public void setCopy(boolean copy) {
        this.copy = copy;
    }
    public String getDestination() {
        return destination;
    }
    public void setDestination(String destination) {
        if(destination == null){
            destination = "";
        }
        this.destination = destination;
    }    
    public void setDestinationPattern(String destination) {
        this.destination = destination;
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
    public String getSource() {
        return source;
    }
    public void setSource(String source) {
        if(source == null){
            source = "";
        }
        this.source = source;
    }    
}