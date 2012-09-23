package ugent.rename;

import java.io.File;
import java.util.ArrayList;

/**
 *
 * @author nicolas
 */
public class Renumber extends AbstractRenamer{    
    private int start = 0;
    private int end = 0;
    private int step = 1;
    private StartPosType startPosType = StartPosType.ABSOLUTE;
    private int startPos = 0;
    private StartPosRelative startPosRelative = StartPosRelative.BEFORE_EXTENSION;
    private int padding = 1;
    private String separator = "";
    private PaddingChar paddingChar = PaddingChar.NULL;

    public StartPosRelative getStartPosRelative() {
        return startPosRelative;
    }
    public void setStartPosRelative(StartPosRelative startPosRelative) {
        this.startPosRelative = startPosRelative;
    }
    public StartPosType getStartPosType() {
        return startPosType;
    }
    public void setStartPosType(StartPosType startPosType) {
        this.startPosType = startPosType;
    }
    public String getSeparator() {
        return separator;
    }
    public void setSeparator(String separator) {
        this.separator = separator;
    }
    public int getStart() {
        return start;
    }
    public void setStart(int start) {
        this.start = start;
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
    public int getStartPos() {
        return startPos;
    }
    public void setStartPos(int startPos) {
        if(startPos < 0){
            return;
        }
        this.startPos = startPos;
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
    private String getFormatString(){
        String formatString = "%";
        if(padding > 0){
            formatString += paddingChar+""+padding;
        }
        formatString += "d";
        return formatString;
    }
    @Override
    protected ArrayList<RenameFilePair> getFilePairs() {
        ArrayList<RenameFilePair>pairs = new ArrayList<RenameFilePair>();
        
        int i = start;
        for(File inputFile:getInputFiles()){
            if(step >= 0){                
                if(i > end){
                    break;                
                }
            }else if(i < end){
                break;               
            }            
            String nameInputFile = inputFile.getName();
            
            int index = 0;           
            
            if(startPosType == StartPosType.ABSOLUTE){            
                if((nameInputFile.length() - 1) < startPos){
                    //start positie voorbij laatste character
                    continue;
                }            
                index = startPos;
            }else{
                if(startPosRelative == StartPosRelative.BEGIN){
                    index = 0;
                }else if(startPosRelative == StartPosRelative.END){
                    index = nameInputFile.length() - 1;
                }else{
                    int posDot = nameInputFile.lastIndexOf('.');
                    index = posDot >= 0 ? posDot:nameInputFile.length() - 1;
                }
            }
            String left = index > 0 ? nameInputFile.substring(0,index):"";
            String right = nameInputFile.substring(index);
            String format = getFormatString();
            String formattedNumber = String.format(format,i);            
            String nameOutputFile = left+separator+formattedNumber+separator+right;            
            System.out.println(nameInputFile+" => "+nameOutputFile);
        
            pairs.add(new RenameFilePair(
                inputFile,
                new File(inputFile.getParentFile(),nameOutputFile)
            ));
            i+=step;
        }       
        System.out.println("filePairs.size = "+pairs.size());
        return pairs;
    } 
    public static void main(String [] args){
        Renumber renumber = new Renumber();
        ArrayList<File>inputFiles = FUtils.listFiles("/home/nicolas/c++");
        System.out.println("num files: "+inputFiles.size());
        renumber.setInputFiles(inputFiles);
        renumber.setStartPosType(StartPosType.RELATIVE);
        renumber.setStartPosRelative(StartPosRelative.BEFORE_EXTENSION);
        renumber.setStart(0);
        renumber.setStep(1);
        renumber.setPadding(4);
        renumber.setEnd(inputFiles.size() - 1);
        renumber.rename();
    }
}