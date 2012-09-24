package ugent.bagger.params;

import ugent.rename.ErrorAction;
import ugent.rename.PaddingChar;
import ugent.rename.PreSort;
import ugent.rename.StartPosRelative;
import ugent.rename.StartPosType;

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
    private StartPosType startPosType = StartPosType.RELATIVE;
    private StartPosRelative startPosRelative = StartPosRelative.BEFORE_EXTENSION;
    private String separatorBefore = "";
    private String separatorAfter = "";
    private PreSort preSort = PreSort.NONE;

    public PreSort getPreSort() {
        return preSort;
    }
    public void setPreSort(PreSort preSort) {
        this.preSort = preSort;
    }   
    public String getSeparatorBefore() {
        return separatorBefore;
    }
    public void setSeparatorBefore(String separatorBefore) {
        if(separatorBefore == null){
            separatorBefore = "";
        }
        this.separatorBefore = separatorBefore;
    }
    public String getSeparatorAfter() {
        return separatorAfter;
    }
    public void setSeparatorAfter(String separatorAfter) {
        if(separatorAfter == null){
            separatorAfter = "";
        }
        this.separatorAfter = separatorAfter;
    }
    public StartPosType getStartPosType() {
        return startPosType;
    }
    public void setStartPosType(StartPosType startPosType) {
        this.startPosType = startPosType;
    }
    public StartPosRelative getStartPosRelative() {
        return startPosRelative;
    }
    public void setStartPosRelative(StartPosRelative startPosRelative) {
        this.startPosRelative = startPosRelative;
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