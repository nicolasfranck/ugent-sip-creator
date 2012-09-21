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
    private int incr = 1;
    private int startPos = 0;
    private int padding = 1;
    private char paddingChar = '0';
    

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
    public int getIncr() {
        return incr;
    }
    public void setIncr(int incr) {       
        this.incr = incr;
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
    public char getPaddingChar() {
        return paddingChar;
    }
    public void setPaddingChar(char paddingChar) {
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
            if(incr >= 0){
                if(i > end){
                    break;
                }
            }else{
                if(i < end){
                    break;
                }
            }
            
            String nameInputFile = inputFile.getName();            
            
            if((nameInputFile.length() - 1) < startPos){
                //start positie voorbij laatste character
                continue;
            }
            
            String left = startPos > 0 ? nameInputFile.substring(0,startPos):"";
            String right = nameInputFile.substring(startPos);
            String format = getFormatString();
            System.out.println("format: "+format);
            String formattedNumber = String.format(format,i);
            
            String nameOutputFile = left+formattedNumber+right;
            
            System.out.println(nameInputFile+" => "+nameOutputFile);
        
            pairs.add(new RenameFilePair(
                inputFile,
                new File(inputFile.getParentFile(),nameOutputFile)
            ));
            i+=incr;
        }
        
        return pairs;
    }
    public static void main(String [] args){
        Renumber renumber = new Renumber();
        ArrayList<File>files = FUtils.listFiles(new File("/home/nicolas/xml"));
        renumber.setInputFiles(files);
        System.out.println("inputFiles: "+files.size());
      
        renumber.setSimulateOnly(true);   
        ArrayList<RenameFilePair> pairs = renumber.getFilePairs();
        
        
    }
}