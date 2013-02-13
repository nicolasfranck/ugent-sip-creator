package ugent.bagger.params;

import ugent.rename.ErrorAction;
import ugent.rename.PaddingChar;
import ugent.rename.PreSort;
import ugent.rename.Radix;
import ugent.rename.StartPosRelative;
import ugent.rename.StartPosType;

/**
 *
 * @author nicolas
 */
public class RenumberParams {
    int start = 0;
    int end = Integer.MAX_VALUE;
    int step = 1;
    int startPos = 0;
    int padding = 1;
    PaddingChar paddingChar = PaddingChar.valueOf("NULL");
    boolean overWrite = false;
    boolean simulateOnly = false;
    ErrorAction onErrorAction = ErrorAction.undoAll;
    StartPosType startPosType = StartPosType.RELATIVE;    
    StartPosRelative startPosRelative = StartPosRelative.END;
    String separatorBefore = "-";
    String separatorAfter = "";
    PreSort preSort = PreSort.NO_SORT;
    Radix radix = Radix.DECIMAL;

    public Radix getRadix() {
        return radix;
    }
    public void setRadix(Radix radix) {
        this.radix = radix;
    }    
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