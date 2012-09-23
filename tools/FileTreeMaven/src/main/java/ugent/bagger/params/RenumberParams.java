package ugent.bagger.params;

import ugent.rename.ErrorAction;
import ugent.rename.PaddingChar;

/**
 *
 * @author nicolas
 */
public class RenumberParams {
    private int start = 0;
    private int end = 0;
    private int step = 1;
    private int startPos = 0;
    private int padding = 1;
    private PaddingChar paddingChar = PaddingChar.valueOf("NULL");
    private boolean overWrite = false;
    private boolean simulateOnly = false;
    private ErrorAction onErrorAction = ErrorAction.skip;

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
    public boolean isSimulateOnly() {
        return simulateOnly;
    }
    public void setSimulateOnly(boolean simulateOnly) {
        this.simulateOnly = simulateOnly;
    }
    public int getEnd() {
        return end;
    }
    public void setEnd(int end) {
        this.end = end;
    }

    public int getStep() {
        return step;
    }

    public void setStep(int step) {
        this.step = step;
    }
   
    public int getPadding() {
        return padding;
    }
    public void setPadding(int padding) {
        this.padding = padding;
    }
    public PaddingChar getPaddingChar() {
        return paddingChar;
    }
    public void setPaddingChar(PaddingChar paddingChar) {
        this.paddingChar = paddingChar;
    }    
    public int getStart() {
        return start;
    }
    public void setStart(int start) {
        this.start = start;
    }
    public int getStartPos() {
        return startPos;
    }
    public void setStartPos(int startPos) {
        this.startPos = startPos;
    }     
}