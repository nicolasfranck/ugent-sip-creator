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
    private ErrorAction onErrorAction;
    private boolean ignoreCase = false;
    private boolean overWrite = false;
    private boolean simulateOnly = false;

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
        this.source = source;
    }
    
}
