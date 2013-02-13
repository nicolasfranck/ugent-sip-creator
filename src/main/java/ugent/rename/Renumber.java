package ugent.rename;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

/**
 *
 * @author nicolas
 */
public class Renumber extends AbstractRenamer{
    int start = 0;
    int end = Integer.MAX_VALUE;
    int step = 1;
    StartPosType startPosType = StartPosType.ABSOLUTE;
    int startPos = 0;    
    StartPosRelative startPosRelative = StartPosRelative.END;
    int padding = 1;
    String separatorBefore = "";
    String separatorAfter = "";
    PaddingChar paddingChar = PaddingChar.NULL;
    PreSort preSort = PreSort.NO_SORT;
    Sequence sequence = new DecimalSequence();
    boolean renameExtension = false;

    public boolean isRenameExtension() {
        return renameExtension;
    }
    public void setRenameExtension(boolean renameExtension) {
        this.renameExtension = renameExtension;
    }    
    public Sequence getSequence() {        
        return sequence;
    }
    public void setSequence(Sequence sequence) {
        this.sequence = sequence;
    }   

    public PreSort getPreSort() {
        return preSort;
    }
    public void setPreSort(PreSort preSort) {
        this.preSort = preSort;
    }
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

    public String getSeparatorBefore() {
        return separatorBefore;
    }

    public void setSeparatorBefore(String separatorBefore) {
        this.separatorBefore = separatorBefore;
    }

    public String getSeparatorAfter() {
        return separatorAfter;
    }

    public void setSeparatorAfter(String separatorAfter) {
        this.separatorAfter = separatorAfter;
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
    String getFormatString(){
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
        
        ArrayList<File>files = getInputFiles();
        
        FileSorter.sort(files,preSort);
        
        int i = start;
        sequence.setCounter(start);
        sequence.setStep(step);
        sequence.setPadding(padding);        
        
        for(File inputFile:files){
            if(step >= 0){                
                if(i > end){
                    break;                
                }
            }else if(i < end){
                break;               
            }            
            String nameInputFile = inputFile.getName();
            String originalNameInputFile = nameInputFile;
            String extension = null;
            
            
            //extensie ook mee betrekken in rename?
            if(inputFile.isFile() && !isRenameExtension()){
                boolean isDoubleExtension = false;
                //controleer speciale gevallen
                for(String n:doubleFileExtension){
                    if(nameInputFile.toLowerCase().endsWith("."+n)){
                        //en niet 'extension = n', want de test wordt case-insensitive uitgevoerd..
                        extension = nameInputFile.substring(nameInputFile.length() - n.length());
                        isDoubleExtension = true;
                        nameInputFile = nameInputFile.substring(0,nameInputFile.length() - n.length() - 1);
                        break;
                    }
                }
                if(!isDoubleExtension){
                    int pos = nameInputFile.lastIndexOf('.');
                    extension = pos >= 0 ? nameInputFile.substring(pos + 1):"";
                    nameInputFile = pos >= 0 ? nameInputFile.substring(0,pos) : nameInputFile;                    
                }                
            }
            log.debug("source: "+inputFile+" ,nameInputFile: "+nameInputFile+", extension:"+extension);
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
                    index = nameInputFile.length();
                }
            }
            String left = index > 0 ? nameInputFile.substring(0,index):"";
            String right = nameInputFile.substring(index);            
            
            String formattedNumber = sequence.next();
            String nameOutputFile = left+separatorBefore+formattedNumber+separatorAfter+right;            
            
            //extensie ook mee betrekken in rename => extension er terug aan kleven
            if(inputFile.isFile() && !isRenameExtension()){
                nameOutputFile += (extension != null ? "."+extension : "");
            }
        
            pairs.add(new RenameFilePair(
                inputFile,
                new File(inputFile.getParentFile(),nameOutputFile)
            ));
            
            i+=step;             
        }       

        return pairs;
    } 
    
    public static void main(String [] args){
        /*Renumber renumber = new Renumber();
        ArrayList<File>inputFiles = FUtils.listFiles("/home/njfranck/test");
        log.debug("num files: "+inputFiles.size());
        renumber.setSimulateOnly(true);
        renumber.setInputFiles(inputFiles);
        renumber.setStartPosType(StartPosType.RELATIVE);
        renumber.setStartPosRelative(StartPosRelative.BEGIN);
        renumber.setRenameExtension(true);
        
        renumber.setSeparatorAfter("-");
        renumber.setPadding(10);        
        renumber.setRenameListener(new RenameListenerAdapter() {
            @Override
            public void onRenameSuccess(RenameFilePair pair, int index) {
                log.debug(pair.getSource()+" => "+pair.getTarget());
            }            
        });
        renumber.rename();*/
        ArrayList<File>files = new ArrayList<File>(Arrays.asList(new File("/home/njfranck/TESTDATA_TIM/2.Hernummer/5.sorteer").listFiles()));
        for(File file:files){
            
            log.debug(file+" => "+new Date(file.lastModified()).toString()+"("+file.lastModified()+")");
        }
        FileSorter.sort(files,PreSort.FILE_DATE_MODIFIED_DESC);
        log.debug("after sorting:");
        
        for(File file:files){            
            log.debug(file+" => "+new Date(file.lastModified()).toString()+"("+file.lastModified()+")");
        }
    }
}