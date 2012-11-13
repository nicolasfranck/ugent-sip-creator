package ugent.bagger.params;

import ugent.rename.ErrorAction;

/**
 *
 * @author nicolas
 */
public class RenameParams {
    private String source;    
    private String destination;
    private boolean copy = false;
    private boolean regex = false;
    private ErrorAction onErrorAction = ErrorAction.undoAll;
    private boolean ignoreCase = false;
    private boolean overWrite = false;
    private boolean simulateOnly = false;

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